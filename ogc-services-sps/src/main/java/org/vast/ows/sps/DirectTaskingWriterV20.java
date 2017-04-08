/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.
 
Copyright (C) 2016-2017 Sensia Software LLC. All Rights Reserved.
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ows.sps;

import org.vast.xml.DOMHelper;
import org.vast.xml.XMLWriterException;
import org.vast.ogc.OGCRegistry;
import org.vast.ows.OWSException;
import org.vast.ows.OWSUtils;
import org.vast.ows.swe.SWERequestWriter;
import org.vast.swe.SWEUtils;
import org.vast.util.DateTime;
import org.w3c.dom.Element;


/**
 * <p>
 * Provides methods to generate an XML SPS DirectTasking request 
 * </p>
 *
 * @author Alex Robin
 * @date Jan 24, 2017
 * */
public class DirectTaskingWriterV20 extends SWERequestWriter<DirectTaskingRequest>
{	
    protected SWEUtils sweUtils = new SWEUtils(SWEUtils.V2_0);
    
    
	public DirectTaskingWriterV20()
	{
	}
	
	
	@Override
	public Element buildXMLQuery(DOMHelper dom, DirectTaskingRequest request) throws OWSException
	{
		dom.addUserPrefix("sps", OGCRegistry.getNamespaceURI(OWSUtils.SPS, request.getVersion()));
		dom.addUserPrefix("swe", OGCRegistry.getNamespaceURI(SWEUtils.SWE, "2.0"));
		
		// root element
		Element rootElt = dom.createElement("sps:" + request.getOperation());
		addCommonXML(dom, rootElt, request);
		
		// procedure
		dom.setElementValue(rootElt, "+sps:procedure", request.getProcedureID());
		
		// time slot
		try
		{
    		StringBuilder buf = new StringBuilder();
    		writeTimeArgument(buf, request.getTimeSlot());
    		dom.setElementValue(rootElt, "+sps:timeSlot", buf.toString());
		}
        catch (Exception e)
        {
            throw new OWSException("Error while writing time slot", e);
        }
		
		// encoding
        try
        {
            Element encodingPropertyElt = dom.addElement(rootElt, "sps:encoding");        
            Element encodingElt = sweUtils.writeEncoding(dom, request.getEncoding());
            encodingPropertyElt.appendChild(encodingElt); 
    	}
        catch (XMLWriterException e)
        {
            throw new OWSException("Error while writing SWE Common encoding", e);
        }
        
        // latest response time
        DateTime latestResponseTime = request.getLatestResponseTime();
        if (latestResponseTime != null)
        {
            String isoTime = timeFormat.formatIso(latestResponseTime.getJulianTime(), 0);
            dom.setElementValue(rootElt, "sps:latestResponseTime", isoTime);
        }
        
		return rootElt;
	}
}