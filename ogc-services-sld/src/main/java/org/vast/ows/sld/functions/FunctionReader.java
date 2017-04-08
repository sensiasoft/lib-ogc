/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are subject to the Mozilla Public License Version
 1.1 (the "License"); you may not use this file except in compliance with
 the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/MPL-1.1.html
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.
 
 The Original Code is the "OGC Service Framework".
 
 The Initial Developer of the Original Code is the VAST team at the University of Alabama in Huntsville (UAH). <http://vast.uah.edu> Portions created by the Initial Developer are Copyright (C) 2007 the Initial Developer. All Rights Reserved. Please Contact Mike Botts <mike.botts@uah.edu> for more information.
 
 Contributor(s): 
    Alexandre Robin <robin@nsstc.uah.edu>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ows.sld.functions;

import org.vast.xml.DOMHelper;
import org.vast.ows.sld.MappingFunction;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;


/**
 * <p>
 * This class parses one of the MappingFunction elements allowed within
 * a CssParameter definition. It will create the appropriate MappingFunction
 * object which will be use to dynamically preprocess the data before it is 
 * displayed.
 * </p>
 *
 * @author Alex Robin <alex.robin@sensiasoftware.com>
 * @date Nov 11, 2005
 * */
public class FunctionReader
{
	
    
    public MappingFunction readFunction(DOMHelper dom, Element functionElt, StringIdProvider stringIdProvider)
	{
		String functionType = functionElt.getLocalName();
        MappingFunction func = null;
        
        if (functionType.equalsIgnoreCase("LinearAdjustment"))
        {
            func = readLinearAdjustment(dom, functionElt);
        }
        else if (functionType.equalsIgnoreCase("DirectLookUpTable"))
        {
            func = readDirectTable(dom, functionElt, stringIdProvider);
        }
        else if (functionType.equalsIgnoreCase("CategoryLookUpTable"))
        {
            func = readCategoryTable(dom, functionElt, stringIdProvider);
        }
        else if (functionType.equalsIgnoreCase("LookUpTable0D"))
        {
            func = readLUT0D(dom, functionElt, stringIdProvider);
        }
        else if (functionType.equalsIgnoreCase("LookUpTable1D"))
        {
            func = readLUT1D(dom, functionElt);
        }
        
        return func;
	}
    
    
    /**
     * Create a LinearAdjustment function object
     * @param dom
     * @param lutElt
     * @return
     */
    public MappingFunction readLinearAdjustment(DOMHelper dom, Element lutElt)
    {
        // default gain and offset values
        double gain = 1;
        double offset = 0;
        
        // attempt to parse gain value
        String gainVal = dom.getElementValue(lutElt, "Gain");
        if (gainVal != null)
            gain = Double.parseDouble(gainVal);
        
        // attempt to parse offset value
        String offVal = dom.getElementValue(lutElt, "Offset");
        if (offVal != null)
            offset = Double.parseDouble(offVal);
        
        MappingFunction function = new LinearAdjustment(gain, offset);
        return function;
    }
    
    
    /**
     * Create a DirectLookUpTable object
     * @param dom
     * @param lutElt
     * @return
     */
    public MappingFunction readDirectTable(DOMHelper dom, Element lutElt, StringIdProvider stringIdProvider)
    {
        // parse values
        String[] tableValues = dom.getElementValue(lutElt, "TableValues").split(" ");
        double[] tableData = new double[tableValues.length];
        
        if (stringIdProvider == null)
            for (int i=0; i<tableValues.length; i++)
                tableData[i] = Double.parseDouble(tableValues[i]);
        else
            for (int i=0; i<tableValues.length; i++)
                tableData[i] = stringIdProvider.getStringId(tableValues[i]);
        
        MappingFunction function = new DirectLookUpTable(tableData);
        return function;
    }
    
    
    /**
     * Create a CategoryLookUpTable object
     * @param dom
     * @param lutElt
     * @return
     */
    public MappingFunction readCategoryTable(DOMHelper dom, Element tableElt, StringIdProvider stringIdProvider)
    {
        CategoryLookUpTable function = new CategoryLookUpTable();
        
        // parse default value
        String defValue = dom.getElementValue(tableElt, "Default");
        if (stringIdProvider != null)
            function.setDefaultValue(stringIdProvider.getStringId(defValue));
        else
            function.setDefaultValue(Double.parseDouble(defValue));
        
        // parse mapping values
        NodeList mapElts = dom.getElements(tableElt, "Mapping");
        for (int i=0; i<mapElts.getLength(); i++)
        {
            Element nextMapping = (Element)mapElts.item(i);
            String key = dom.getAttributeValue(nextMapping, "@key");
            String val = dom.getElementValue(nextMapping);
            
            if (stringIdProvider != null)
                function.addMapping(key, stringIdProvider.getStringId(val));
            else
                function.addMapping(key, Double.parseDouble(val));
        }
        
        return function;
    }
    
    
    /**
     * Create a LookUpTable0D object
     * @param dom
     * @param lutElt
     * @return
     */
    public MappingFunction readLUT0D(DOMHelper dom, Element lutElt, StringIdProvider stringIdProvider)
    {
        // parse values
        Element valuesElt = dom.getElement(lutElt, "TableValues");
        double[][] tableData = parseTableValues(dom, valuesElt, stringIdProvider);
        
        MappingFunction function = new LookUpTable0D(tableData);
        return function;
    }
    
    
    /**
     * Create a LookUpTable1D object
     * @param dom
     * @param lutElt
     * @return
     */
    public MappingFunction readLUT1D(DOMHelper dom, Element lutElt)
    {
        // parse values
        Element valuesElt = dom.getElement(lutElt, "TableValues");
        double[][] tableData = parseTableValues(dom, valuesElt, null);
        
        MappingFunction function = new LookUpTable1D(tableData);
        return function;
    }
    
    
    /**
     * Parse doublets of values to be used for table lookup 
     * @param dom
     * @param valuesElt
     * @return
     */
    private double[][] parseTableValues(DOMHelper dom, Element valuesElt, StringIdProvider stringIdProvider)
    {
        String values = dom.getElementValue(valuesElt);
        String[] valueTable = values.split(" ");
        int tupleCount = valueTable.length/2;
        double[][] tableData = new double[2][tupleCount];
        
        for (int i = 0; i < tupleCount; i++)
        {
            String srcVal = valueTable[2*i];
            tableData[0][i] = Double.parseDouble(srcVal);
            
            String destVal = valueTable[2*i + 1];
            if (stringIdProvider != null)
                tableData[1][i] = stringIdProvider.getStringId(destVal);
            else
                tableData[1][i] = Double.parseDouble(destVal);
        }
        
        return tableData;
    }
}
