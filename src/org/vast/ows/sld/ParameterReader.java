/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are subject to the Mozilla Public License Version
 1.1 (the "License"); you may not use this file except in compliance with
 the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/MPL-1.1.html
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.
 
 The Original Code is the "SensorML DataProcessing Engine".
 
 The Initial Developer of the Original Code is the
 University of Alabama in Huntsville (UAH).
 Portions created by the Initial Developer are Copyright (C) 2006
 the Initial Developer. All Rights Reserved.
 
 Contributor(s): 
    Alexandre Robin <robin@nsstc.uah.edu>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ows.sld;

import org.vast.xml.DOMHelper;
import org.vast.ows.sld.functions.LookUpTable1D;
import org.w3c.dom.Element;


/**
 * <p><b>Title:</b><br/>
 * CSS Parameter Reader
 * </p>
 *
 * <p><b>Description:</b><br/>
 * This class parses a CssParameter element and create corresponding object.
 * This supports STT extension for including a mapping function between the
 * property value and the actual renderable parameter.
 * </p>
 *
 * <p>Copyright (c) 2005</p>
 * @author Alexandre Robin
 * @date Nov 11, 2005
 * @version 1.0
 */
public class ParameterReader
{
    private FunctionReader functionReader;
    
    
    public ParameterReader()
    {
        functionReader = new FunctionReader();
    }
    
    
    public ScalarParameter readPropertyName(DOMHelper dom, Element paramElt)
    {
        if (paramElt == null)
            return null;
        
        ScalarParameter param = new ScalarParameter();
        String pName = dom.getElementValue(paramElt, "");
        param.setPropertyName(pName);
        
        return param;
    }
    
    
    public ScalarParameter readCssParameter(DOMHelper dom, Element paramElt)
	{
		ScalarParameter param;
        
		if (paramElt == null)
			return null;
		
		// case of simple property name for mapping
		if (dom.existElement(paramElt, "PropertyName"))
		{
            Element propNameElt = dom.getElement(paramElt, "PropertyName");
            param = readPropertyName(dom, propNameElt);
        }
        
        // case of mapping through a mapping function
        else if (dom.existElement(paramElt, "*"))
        {
            param = readParamWithMappingFunction(dom, paramElt);
        }
        
		// otherwise case of a constant
		else
        {
    		param = readCssParameterValue(dom, paramElt);
        }
		
		return param;
	}
    
    
    public ScalarParameter readCssParameterValue(DOMHelper dom, Element paramElt)
    {
        String val = dom.getElementValue(paramElt, "");
        ScalarParameter param = new ScalarParameter();
        
        try
        {
            // case of numeric value
            float numVal = Float.parseFloat(val);
            param.setConstantValue(new Float(numVal));
        }
        catch (NumberFormatException e)
        {
            // case of string value
            param.setConstantValue(val);
        }
        
        return param;
    }
    
    
    public ScalarParameter readParamWithMappingFunction(DOMHelper dom, Element paramElt)
    {
        ScalarParameter param = new ScalarParameter();
        
        // first read property name
        Element funcElt = dom.getFirstChildElement(paramElt);
        String pName = dom.getElementValue(funcElt, "PropertyName");
        param.setPropertyName(pName);
        
        // then read the function
        MappingFunction function = functionReader.readFunction(dom, funcElt);
        param.setMappingFunction(function);
        
        return param;
    }
    
    
    public MappingFunction readLUT(DOMHelper dom, Element lutElt)
    {
        // parse values
        String values = dom.getElementValue(lutElt, "TableValues");
        String[] valueTable = values.split(" ");
        int tupleCount = valueTable.length/2;
        double[][] tableData = new double[2][tupleCount];
        
        for (int i = 0; i < tupleCount; i++)
        {
            tableData[0][i] = Double.parseDouble(valueTable[2*i]);
            tableData[1][i] = Double.parseDouble(valueTable[2*i + 1]);
        }
        
        MappingFunction function = new LookUpTable1D(tableData);
        return function;
    }
}
