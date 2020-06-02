/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are subject to the Mozilla Public License Version
 1.1 (the "License"); you may not use this file except in compliance with
 the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/MPL-1.1.html
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.
 
 The Original Code is the "OGC Service Framework".
 
 The Initial Developer of the Original Code is Spotimage S.A.
 Portions created by the Initial Developer are Copyright (C) 2007
 the Initial Developer. All Rights Reserved.
 
 Contributor(s): 
    Alexandre Robin <alexandre.robin@spotimage.fr>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ows.wcs;

import org.vast.ogc.OGCRegistry;
import org.vast.ogc.gml.GMLUtils;
import org.vast.ows.OWSException;
import org.vast.xml.DOMHelper;
import org.w3c.dom.Element;


/**
 * <p>
 * Util method to write XML common to several WCS documents
 * </p>
 *
 * @author Alex Robin <alex.robin@sensiasoftware.com>
 * @date Dec 4, 2007
 * */
public class WCSCommonWriterV11
{
	
	public Element buildGridCRS(DOMHelper dom, WCSRectifiedGridCrs gridCrs, String id) throws OWSException
    {
		dom.addUserPrefix("gml", OGCRegistry.getNamespaceURI(GMLUtils.GML));
		Element gridElt = dom.createElement("GridCRS");
		
		dom.setElementValue(gridElt, "gml:srsName", id);
		dom.setElementValue(gridElt, "GridBaseCRS", gridCrs.getBaseCrs());
		dom.setElementValue(gridElt, "GridType", gridCrs.getGridType());
		
		// origin coordinates
		double[] origin = gridCrs.getGridOrigin();
		String originVal = origin[0] + " " + origin[1];
		dom.setElementValue(gridElt, "GridOrigin", originVal);
		
		// offsets coordinates
		double[][] offsets = gridCrs.getGridOffsets();
		String offsetText = getGridOffsets(offsets);
		dom.setElementValue(gridElt, "GridOffsets", offsetText);
		
		// cs type
		dom.setElementValue(gridElt, "GridCS", gridCrs.getGridCs());
		
		return gridElt;
    }
	
	
	/**
     * Utility method to write a list of vectors as space separated values
     * @param gridOffsets
     * @return
     */
    protected String getGridOffsets(double[][] offsets) throws OWSException
    {
    	StringBuffer offsetBuf = new StringBuffer();
    	
		for (int u=0; u<offsets.length; u++)
			for (int v=0; v<offsets[0].length; v++)
				offsetBuf.append(offsets[u][v] + " ");
		
		String offsetVal = offsetBuf.toString().trim();
		return offsetVal;
    }
}
