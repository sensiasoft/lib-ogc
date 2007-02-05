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

package org.vast.ows.om;

import java.io.*;
import org.vast.cdm.common.CDMException;
import org.vast.math.Vector3d;
import org.vast.ows.OWSException;
import org.vast.ows.OWSExceptionReader;
import org.vast.ows.gml.GMLGeometryReader;
import org.vast.sweCommon.CDMFilter;
import org.vast.sweCommon.CDMReader;
import org.vast.sweCommon.SWECommonUtils;
import org.vast.sweCommon.URIStreamHandler;
import org.vast.xml.*;
import org.w3c.dom.*;


public class ObservationReader extends CDMReader
{
	protected String resultUri;
	protected CDMFilter streamFilter;
	protected Vector3d foiLocation;
    protected String procedure;
    protected String observationName;
       
    
    public ObservationReader()
    {
        foiLocation = new Vector3d();
    }
    
	
	public void parse(InputStream inputStream) throws CDMException
	{
		try
		{
			streamFilter = new CDMFilter(inputStream);
			streamFilter.setDataElementName("result");
			
//			int val;
//			try
//			{
//				do
//				{
//					val = streamFilter.read();
//					System.out.print((char)val);				
//				}
//				while (val != -1);
//			}
//			catch (IOException e){}
//			System.exit(0);
			
			// parse xml header using DOMReader
			DOMHelper dom = new DOMHelper(streamFilter, false);			
			OWSExceptionReader.checkException(dom);
			
			// find first observation element
			Element rootElement = dom.getRootElement();
			NodeList elts = rootElement.getOwnerDocument().getElementsByTagNameNS("http://www.opengis.net/om", "CommonObservation");
			Element obsElt = (Element)elts.item(0);			
			if (obsElt == null)
				throw new CDMException("XML Response doesn't contain any Observation");
			
            // read FOI location
            GMLGeometryReader geometryReader = new GMLGeometryReader();
            Element pointElt = dom.getElement(obsElt, "featureOfInterest/*/location/Point");
            if (pointElt != null)
                foiLocation = geometryReader.readPoint(dom, pointElt);
            
            // read procedure ID and observation name
            procedure = dom.getAttributeValue(obsElt, "procedure/@href");
            observationName = dom.getElementValue(obsElt, "featureOfInterest/*/name");
            
            // read resultDefinition
			Element defElt = dom.getElement(obsElt, "resultDefinition/DataDefinition");
			Element dataElt = dom.getElement(defElt, "dataComponents");
			Element encElt = dom.getElement(defElt, "encoding");		
            SWECommonUtils utils = new SWECommonUtils();
            this.dataComponents = utils.readComponentProperty(dom, dataElt);
            this.dataEncoding = utils.readEncodingProperty(dom, encElt);
			
			// read external link if present
			resultUri = dom.getAttributeValue(obsElt, "result/externalLink");
		}
        catch (IllegalStateException e)
        {
            throw new CDMException("No reader found for SWECommon", e);
        }
		catch (DOMHelperException e)
		{
			throw new CDMException("Error while parsing Observation XML", e);
		}
		catch (OWSException e)
		{
			throw new CDMException(e.getMessage());
		}
	}
	
	
	public InputStream getDataStream() throws CDMException
	{
		if (resultUri != null)
		{
			try
			{
				streamFilter.startReadingData();
				streamFilter.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			
			return URIStreamHandler.openStream(resultUri);
		}
		else
		{
			streamFilter.startReadingData();
			return streamFilter;
		}
	}


    public Vector3d getFoiLocation()
    {
        return foiLocation;
    }


    public String getObservationName()
    {
        return observationName;
    }


    public String getProcedure()
    {
        return procedure;
    }
}
