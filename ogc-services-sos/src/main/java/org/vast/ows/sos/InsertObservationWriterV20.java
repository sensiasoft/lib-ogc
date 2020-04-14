/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.
 
Copyright (C) 2012-2015 Sensia Software LLC. All Rights Reserved.
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ows.sos;

import java.io.IOException;
import org.vast.xml.DOMHelper;
import org.vast.ogc.OGCRegistry;
import org.vast.ogc.om.IObservation;
import org.vast.ogc.om.ObservationWriterV20;
import org.vast.ows.OWSException;
import org.vast.ows.OWSUtils;
import org.vast.ows.swe.SWERequestWriter;
import org.w3c.dom.Element;


/**
 * <p>
 * Provides methods to generate a KVP or XML SOS InsertObservation
 * request as defined in version 2.0 of the SOS standard using
 * values contained in a InsertObservationRequest object 
 * </p>
 *
 * @author Alex Robin
 * @date Feb 2, 2014
 * */
public class InsertObservationWriterV20 extends SWERequestWriter<InsertObservationRequest>
{
    private ObservationWriterV20 obsWriter = new ObservationWriterV20();
    
    
	public InsertObservationWriterV20()
	{
	}
	
	
	@Override
	public Element buildXMLQuery(DOMHelper dom, InsertObservationRequest request) throws OWSException
	{
		dom.addUserPrefix("sos", OGCRegistry.getNamespaceURI(OWSUtils.SOS, request.getVersion()));
		
		// root element
		Element rootElt = dom.createElement("sos:" + request.getOperation());
		addCommonXML(dom, rootElt, request);
		
		// offering
		dom.setElementValue(rootElt, "+sos:offering", request.getOffering());
		
		// observation
		try
        {
		    for (IObservation obs: request.getObservations())
		    {
    		    Element propElt = dom.addElement(rootElt, "+sos:observation");
    		    Element obsElt = obsWriter.write(dom, obs);
    		    propElt.appendChild(obsElt);
		    }
        }
        catch (IOException e)
        {
            throw new SOSException("Cannot write observation", e);
        }   
        
		return rootElt;
	}
}