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

import org.vast.xml.DOMHelper;
import org.vast.ogc.OGCRegistry;
import org.vast.ows.OWSException;
import org.vast.ows.OWSUtils;
import org.vast.sensorML.SMLUtils;
import org.w3c.dom.Element;


/**
 * <p>
 * Provides methods to generate a KVP or XML SOS InsertSensor
 * request as defined in version 2.0 of the SOS standard using
 * values contained in a InsertSensorRequest object 
 * </p>
 *
 * @author Alex Robin
 * @date Feb 2, 2014
 * */
public class InsertSensorWriterV20 extends org.vast.ows.swe.InsertSensorWriterV20<InsertSensorRequest>
{
    SMLUtils smlUtils = new SMLUtils(SMLUtils.V2_0);
    
    
	public InsertSensorWriterV20()
	{
	}
	
	
	@Override
	public Element buildXMLQuery(DOMHelper dom, InsertSensorRequest request) throws OWSException
	{
		Element rootElt = super.buildXMLQuery(dom, request);
        
        // sos specific metadata
		dom.addUserPrefix("sos", OGCRegistry.getNamespaceURI(OWSUtils.SOS, request.getVersion()));
		Element insertMetaElt = dom.addElement(rootElt, "swes:metadata/sos:SosInsertionMetadata");
        for (String obsType: request.getObservationTypes())
            dom.setElementValue(insertMetaElt, "+sos:observationType", obsType);
        for (String foiType: request.getFoiTypes())
            dom.setElementValue(insertMetaElt, "+sos:featureOfInterestType", foiType);
        
		return rootElt;
	}
}