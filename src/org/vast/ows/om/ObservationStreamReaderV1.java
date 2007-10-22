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

package org.vast.ows.om;

import java.io.*;
import org.vast.cdm.common.CDMException;
import org.vast.ows.OWSException;
import org.vast.ows.OWSExceptionReader;
import org.vast.sweCommon.SWEFilter;
import org.vast.sweCommon.SWEReader;
import org.vast.sweCommon.URIStreamHandler;
import org.vast.xml.*;
import org.w3c.dom.*;


public class ObservationStreamReaderV1 extends SWEReader
{
	protected String resultUri;
	protected SWEFilter streamFilter;
	protected AbstractObservation observation;
    protected OMUtils omUtils = new OMUtils();
    
    
    public ObservationStreamReaderV1()
    {
    }
    
    
    public void parse(InputStream inputStream) throws CDMException
	{
		try
		{
			streamFilter = new SWEFilter(inputStream);
			streamFilter.setDataElementName("result");
			
			// parse xml header using DOMReader
			DOMHelper dom = new DOMHelper(streamFilter, false);
			OWSExceptionReader.checkException(dom);
			
			// find first observation element
			Element rootElement = dom.getRootElement();
			NodeList elts = rootElement.getOwnerDocument().getElementsByTagNameNS("http://www.opengis.net/om", "Observation");
			Element obsElt = (Element)elts.item(0);			
			if (obsElt == null)
				throw new CDMException("XML Response doesn't contain any Observation");
			
            // create observation reader
            observation = omUtils.readObservation(dom, obsElt);
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


    public AbstractObservation getObservation()
    {
        return observation;
    }
}
