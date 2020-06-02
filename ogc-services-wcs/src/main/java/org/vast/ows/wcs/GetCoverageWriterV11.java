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
import org.vast.ows.*;
import org.w3c.dom.*;
import org.vast.xml.DOMHelper;


/**
 * <p>
 * Provides methods to generate a KVP or XML GetCoverage request based
 * on values contained in a GetCoverageRequest object for version 1.1.1
 * </p>
 *
 * @author Alex Robin <alex.robin@sensiasoftware.com>
 * @date Sep 21, 2007
 * */
public class GetCoverageWriterV11 extends AbstractRequestWriter<GetCoverageRequest>
{
	protected WCSCommonWriterV11 wcsWriter = new WCSCommonWriterV11();
	protected OWSCommonWriterV11 owsWriter = new OWSCommonWriterV11();
	
	
	@Override
	public String buildURLQuery(GetCoverageRequest request) throws OWSException
	{
	    StringBuilder urlBuff = new StringBuilder(request.getGetServer());
        addCommonArgs(urlBuff, request);
        
        // COVERAGE
        urlBuff.append("&IDENTIFIER=" + request.getCoverage());
        
        // TIME
        if (request.getTime() != null)
        {
            urlBuff.append("&TIME=");
            this.writeTimeArgument(urlBuff, request.getTime());
        }
        
        // BBOX and CRS if specified
        if (request.getBbox() != null)
        {
        	urlBuff.append("&CRS=" + request.getBbox().getCrs());
        	urlBuff.append("&BBOX=");
	        this.writeBboxArgument(urlBuff, request.getBbox());
	        
	        // add either RESX/RESY or WIDTH/HEIGHT
	        if (request.isUseResolution())
	        {	        
	        	urlBuff.append("&RESX=" + request.getResX());
	        	urlBuff.append("&RESY=" + request.getResY());
	        	if (request.getResZ() > 0)
	        		urlBuff.append("&RESZ=" + request.getResZ());
	        }
	        else
	        {
	        	urlBuff.append("&WIDTH=" + request.getWidth());
	        	urlBuff.append("&HEIGHT=" + request.getHeight());
	        	if (request.getDepth() > 0)
	        		urlBuff.append("&DEPTH=" + request.getDepth());
	        }
        }
        
        // TODO range subset
        
        // RESPONSE_CRS
        if (request.getGridCrs() != null)
        	urlBuff.append("&RESPONSE_CRS=" + request.getGridCrs().getBaseCrs());
        
        // FORMAT
        urlBuff.append("&FORMAT=" + request.getFormat());
        
        // EXCEPTIONS
        if (request.getExceptionType() != null)
        	urlBuff.append("&EXCEPTIONS=" + request.getExceptionType());
        
        // vendor parameters
        writeKVPExtensions(urlBuff, request);
        
        // replace spaces
        String url = urlBuff.toString();
        url = url.replaceAll(" ","%20");
		return url;
	}
	
	
	@Override
	public Element buildXMLQuery(DOMHelper dom, GetCoverageRequest request) throws OWSException
	{
		dom.addUserPrefix(DOMHelper.DEFAULT_PREFIX, OGCRegistry.getNamespaceURI(OWSUtils.WCS, request.getVersion()));
		dom.addUserPrefix("ows", OGCRegistry.getNamespaceURI(OWSUtils.OWS, "1.1"));
		
		// root element
		Element rootElt = dom.createElement("GetCoverage");
		addCommonXML(dom, rootElt, request);
		
		// source coverage
		dom.setElementValue(rootElt, "ows:Identifier", request.getCoverage());
		
		////// domain subset //////
		Element domainElt = dom.addElement(rootElt, "DomainSubset");
		
		// bounding box
		Element bboxElt = owsWriter.buildBbox(dom, request.getBbox());
		domainElt.appendChild(bboxElt);
		
		// TODO temporal subset
				
		////// range subset //////
		for (int i=0; i<request.getFieldSubsets().size(); i++)
        {
			Element fieldElt = dom.addElement(rootElt, "RangeSubset/+FieldSubset");
			FieldSubset field = request.getFieldSubsets().get(i);			
			dom.setElementValue(fieldElt, "ows:Identifier", field.getIdentifier());
			
			// all axis subsets
			for (int j=0; j<request.getFieldSubsets().size(); j++)
	        {
		        AxisSubset axis = field.getAxisSubsets().get(j);
				Element axisElt = dom.addElement(fieldElt, "+AxisSubset");
				dom.setElementValue(axisElt, "Identifier", axis.getIdentifier());
				
				// add all single values
				for (int k=0; k<axis.getKeys().size(); k++)
					dom.setElementValue(axisElt, "+Key", axis.getKeys().get(k));
			}
				
			////// interpolation method //////
			if (field.getInterpolationMethod() != null)
				dom.setElementValue(fieldElt, "interpolationType", field.getInterpolationMethod());
        }
		
		////// output parameters //////
		Element outputElt = dom.addElement(rootElt, "Output");
		
		if (request.getGridCrs() != null)
		{
			String id = "REQUEST_GRID_CRS";
			Element gridCrsElt = wcsWriter.buildGridCRS(dom, request.getGridCrs(), id);
			outputElt.appendChild(gridCrsElt);
		}
		
		// format
		String format = request.getFormat();
		if (format != null)
			dom.setAttributeValue(outputElt, "format", request.getFormat());
		
		// store
		boolean store = request.getStore();
		if (store != false)
			dom.setAttributeValue(outputElt, "store", Boolean.toString(store));
		
		return rootElt;
	}
}