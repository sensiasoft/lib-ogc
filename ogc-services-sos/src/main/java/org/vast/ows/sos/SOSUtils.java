/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are subject to the Mozilla Public License Version
 1.1 (the "License"); you may not use this file except in compliance with
 the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/MPL-1.1.html
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.
 
 The Original Code is the "OGC Service Framework".
 
 The Initial Developer of the Original Code is the VAST team at the
 University of Alabama in Huntsville (UAH). <http://vast.uah.edu>
 Portions created by the Initial Developer are Copyright (C) 2007
 the Initial Developer. All Rights Reserved.

 Please Contact Mike Botts <mike.botts@uah.edu>
 or Alexandre Robin <alex.robin@sensiasoftware.com> for more information.
 
 Contributor(s): 
    Alexandre Robin <alex.robin@sensiasoftware.com>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ows.sos;

import net.opengis.swe.v20.DataEncoding;
import net.opengis.swe.v20.DataComponent;
import org.vast.ogc.OGCRegistry;
import org.vast.ows.AbstractRequestReader;
import org.vast.ows.OWSBindingProvider;
import org.vast.ows.OWSException;
import org.vast.ows.OWSExceptionReport;
import org.vast.ows.OWSRequest;
import org.vast.ows.OWSRequestReader;
import org.vast.ows.OWSUtils;
import org.vast.ows.SweEncodedMessageProcessor;
import org.vast.xml.DOMHelper;
import org.w3c.dom.Element;


public class SOSUtils extends OWSUtils implements OWSBindingProvider
{
    
    @Override
    public void loadBindings()
    {
        String mapFileUrl = getClass().getResource("SOSRegistry.xml").toString();
        OGCRegistry.loadMaps(mapFileUrl, false);
    }
    
    
    public OWSRequest readSweEncodedRequest(DOMHelper dom, Element requestElt, DataComponent structure, DataEncoding encoding) throws OWSException
    {
        // read common params and check that they're present
        OWSRequest request = new OWSRequest();        
        AbstractRequestReader.readCommonXML(dom, requestElt, request);
        OWSExceptionReport report = new OWSExceptionReport();
        AbstractRequestReader.checkParameters(request, report, SOSUtils.SOS);
        report.process();
        
        // parse request with appropriate reader
        try
        {
            OWSRequestReader<?> reader = (OWSRequestReader<?>)OGCRegistry.createReader(SOSUtils.SOS, request.getOperation(), request.getVersion());
            ((SweEncodedMessageProcessor)reader).setSweCommonStructure(structure, encoding);
            return reader.readXMLQuery(dom, requestElt);
        }
        catch (IllegalStateException e)
        {
            String spec = SOSUtils.SOS + " " + request.getOperation() + " v" + request.getVersion();
            throw new OWSException(UNSUPPORTED_SPEC_MSG + spec, e);
        }
    }
    
}
