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
    Tony Cook <tcook@nsstc.uah.edu>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ows.wcs;

import java.io.IOException;

import org.vast.ows.SweDataWriter;
import org.vast.ows.SweResponseSerializer;
import org.w3c.dom.Element;


/**
 * <p>Title: WCSResponseWriter</p>
 *
 * <p>Description:
 * Write SWE XML response in a WCS
 * </p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 * @author Alexandre Robin
 * @since Aug 9, 2005
 * @version 1.0
 */
public class WCSResponseSerializer extends SweResponseSerializer
{
	SweDataWriter resultWriter;	
	int bytesPerPoint;  //  used only by BinaryBlock encoding?
	
	public WCSResponseSerializer()
	{		
	}
	
	public void setBytesPerPoint(int bpp){
		//  set Encoding Dimension
		bytesPerPoint = bpp;
	}

	public void setGridDimension(int xdim, int ydim){
		this.setGridDimension(xdim, ydim, 1);
	}
	
	//  Maybe this should only be in derived classes, as it is already being a pain to generalize
	//  between Goes and SRTM
	//  Leaving it now.  SRTMHandler is the only class still calling this.  TC 6/13/06
	public void setGridDimension(int xdim, int ydim, int zdim){
		// set numRows
		Element elt = respXML.getElement("result/Data/definition/DataDefinition/dataComponents/DataArray");
		elt.setAttribute("arraySize", "" + ydim);
		// set numCols
		Element ncElt = respXML.getElement("result/Data/definition/DataDefinition/dataComponents/DataArray/" + 
											"component/DataArray");		
		ncElt.setAttribute("arraySize", "" + xdim);
		
		//  TODO:  support zdim
		int dim = xdim*ydim*zdim;
		Element blElt = respXML.getElement("result/Data/definition/DataDefinition/encoding/BinaryBlock"); 
		if(blElt != null)
			blElt.setAttribute("byteLength", "" + (dim*bytesPerPoint));
	}
	
	
	public void setDataWriter(SweDataWriter dataWriter)
	{
		this.resultWriter = dataWriter;
	}
	
	
	protected void writeResultDefinition(Element elt)
	{
		
	}
	
	
	protected void writeResultEncoding(Element elt)
	{
		
	}
	
	
	/**
	 * Called every time an XML element is serialized
	 * Override to set up new hooks
	 */
	protected void serializeElement(Element elt) throws IOException
	{
		if (elt.getLocalName().equals("value"))
		{
			this._format.setIndenting(false);
			this._printer.printText("\n<swe:value>");
			this._printer.flush();
			
            resultWriter.write();
			
			this._printer.printText("\n</swe:value>");
			this._printer.flush();
			this._format.setIndenting(true);
		}
		else
		{
			super.serializeElement(elt);
		}
	}	
}