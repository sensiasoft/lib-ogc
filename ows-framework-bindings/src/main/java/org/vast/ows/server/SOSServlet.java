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

package org.vast.ows.server;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Hashtable;
import org.vast.ogc.gml.GMLTimeReader;
import org.vast.ows.GetCapabilitiesRequest;
import org.vast.ows.OWSRequest;
import org.vast.ows.OWSUtils;
import org.vast.ows.sos.GetObservationRequest;
import org.vast.ows.sos.GetResultRequest;
import org.vast.ows.sos.SOSException;
import org.vast.ows.sos.SOSUtils;
import org.vast.ows.swe.DescribeSensorRequest;
import org.vast.util.TimeExtent;
import org.vast.xml.DOMHelper;
import org.vast.xml.XMLReaderException;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;


/**
 * <p>
 * Base abstract class for implementing SOS servlets
 * @deprecated use org.vast.ows.sos.SOSServlet
 * </p>
 *
 * @author Alex Robin <alex.robin@sensiasoftware.com>
 * */
public abstract class SOSServlet extends OWSServlet
{
    private static final long serialVersionUID = 6940984824581209178L;
    protected OWSUtils owsUtils = new OWSUtils();
    
	// Table of SOS handlers: 1 for each observation offering
	protected Hashtable<String, SOSHandler> dataSetHandlers = new Hashtable<String, SOSHandler>();
	
	// Table of <ProcedureId, SensorMLUrl> - 1 per procedure 
	protected Hashtable<String, String> sensorMLUrls = new Hashtable<String, String>();
	    
	
	@Override
    public void handleRequest(OWSRequest request) throws Exception
    {
	    if (request instanceof GetCapabilitiesRequest)
            handleRequest((GetCapabilitiesRequest)request);
        else if (request instanceof DescribeSensorRequest)
            handleRequest((DescribeSensorRequest)request);
        else if (request instanceof GetObservationRequest)
            handleRequest((GetObservationRequest)request);
        else if (request instanceof GetResultRequest)
            handleRequest((GetResultRequest)request);
    }


    protected void handleRequest(GetCapabilitiesRequest query) throws Exception
    {
	    sendCapabilities("ALL", query.getResponseStream());
    }
	    
	
	protected void handleRequest(DescribeSensorRequest query) throws Exception
	{
		String sensorId = query.getProcedureID();
		if (sensorId == null)
			throw new SOSException("A DescribeSensor request must specify a sensorId argument");
		
		//  Check for SensorId in SensorMLUrl hashtable
		String smlUrl = sensorMLUrls.get(sensorId);
		if(smlUrl == null)
			throw new SOSException("SensorML description for " + sensorId + " not available");
		
		DOMHelper dom = new DOMHelper(smlUrl, false);				
        dom.serialize(dom.getBaseElement(), query.getResponseStream() , true);		
	}
	
	/**
	 * Decode the query, check validity and call the right handler
	 * @param query
	 */
	protected void handleRequest(GetResultRequest query) throws Exception
	{
		if (query.getOffering() == null)
			throw new SOSException("A GetResult SOS request must specify an offeringId argument");
		
		if (query.getObservables().isEmpty())
			throw new SOSException("An SOS request must contain at least one observable ID");
		
		if (query.getFormat() == null)
			throw new SOSException("A GetResult SOS request must specify a format argument");
		
					
		SOSHandler handler = dataSetHandlers.get(query.getOffering());

		if (handler != null)
		{
			// then retrieve observation data
            handler.getResult(query);				
		}
		else
			throw new SOSException("Data for observation " + query.getOffering() + " is unavailable on this server");
	}
	
	/**
	 * Decode the query, check validity and call the right handler
	 * @param query
	 */
	protected void handleRequest(GetObservationRequest query) throws Exception
	{
		if (query.getOffering() == null)
			throw new SOSException("A GetObservation SOS request must specify an offeringId argument");
		
		if (query.getObservables().isEmpty())
			throw new SOSException("An SOS request must contain at least one observable ID");
		
		if (query.getFormat() == null)
			throw new SOSException("A GetObservation SOS request must specify a format argument");
		
					
		SOSHandler handler = dataSetHandlers.get(query.getOffering());

		if (handler != null)
		{
			// check query parameters
			checkQueryObservables(query, capsHelper);
            checkQueryProcedures(query, capsHelper);
			checkQueryFormat(query, capsHelper);
			checkQueryTime(query, capsHelper);
			
			// then retrieve observation data
            handler.getObservation(query);				
		}
		else
			throw new SOSException("Data for observation " + query.getOffering() + " is unavailable on this server");
	}
	
	
	/**
	 * Checks if all selected observables are available in this offering
	 * @param query
	 * @param capsReader
	 * @throws SOSException
	 */
	protected void checkQueryObservables(GetObservationRequest query, DOMHelper capsReader) throws SOSException
	{
		Element offeringElt = getOffering(query.getOffering(), capsReader);		
		NodeList observableElts = capsReader.getElements(offeringElt, "observedProperty");
		boolean ok;
		
		for (int j=0; j<query.getObservables().size(); j++)
		{
			ok = false;
			
			for (int i=0; i<observableElts.getLength(); i++)
			{
				String idString =  capsReader.getAttributeValue((Element)observableElts.item(i), "href");
				if(idString == null)
                    idString = capsReader.getAttributeValue((Element)observableElts.item(i), "*/id");
				
				if (idString.equals(query.getObservables().get(j)))
				{
					ok = true;
					break;
				}
			}
			
			if (!ok)
				throw new SOSException("Observed Property - " + query.getObservables().get(j) + " - is not available for offering " + query.getOffering());
		}
	}
    
    
    /**
     * Checks if all selected procedures are available in this offering
     * @param query
     * @param capsReader
     * @throws SOSException
     */
    protected void checkQueryProcedures(GetObservationRequest query, DOMHelper capsReader) throws SOSException
    {
        Element offeringElt = getOffering(query.getOffering(), capsReader);     
        NodeList procedureElts = capsReader.getElements(offeringElt, "procedure");
        boolean ok;
        
        for (int j=0; j<query.getProcedures().size(); j++)
        {
            ok = false;
            
            for (int i=0; i<procedureElts.getLength(); i++)
            {
                String idString =  capsReader.getAttributeValue((Element)procedureElts.item(i), "href");
                if(idString == null)
                    idString = capsReader.getAttributeValue((Element)procedureElts.item(i), "*/id");
                
                if (idString.equals(query.getProcedures().get(j)))
                {
                    ok = true;
                    break;
                }
            }
            
            if (!ok)
                throw new SOSException("Procedure - " + query.getProcedures().get(j) + " - is not available for offering " + query.getOffering());
        }
    }
	
	
	/**
	 * Checks if selected format is available in this offering
	 * @param query
	 * @param capsReader
	 * @throws SOSException
	 */
	protected void checkQueryFormat(GetObservationRequest query, DOMHelper capsReader) throws SOSException
	{
		Element offeringElt = getOffering(query.getOffering(), capsReader);
        
        // get format lists from either resultFormat or responseFormat elts
		NodeList formatElts = capsReader.getElements(offeringElt, "resultFormat");
    	if(formatElts.getLength() == 0)
        {
    		formatElts =  capsReader.getElements(offeringElt, "responseFormat");
    	}
        
        // check query format vs. each supported format in the capabilities
        int listSize = formatElts.getLength();
		for (int i=0; i<listSize; i++)
		{
			String formatString = capsReader.getElementValue((Element)formatElts.item(i), "");
			
			if (formatString.equals(query.getFormat()))
				return;
		}
		
		throw new SOSException("Format - " + query.getFormat() + " - is not available for offering " + query.getOffering());
	}
	
	
	/**
	 * Checks if requested time is available in this offering
	 * @param query
	 * @param capsReader
	 * @throws SOSException
	 * 	  TODO  these check*() methods need to be moved to account for differing SOS versions

	 */
	protected void checkQueryTime(GetObservationRequest query, DOMHelper dom) throws SOSException
	{
        // make sure startTime <= stopTime
        if (query.getTime().getStartTime() > query.getTime().getStopTime())
            throw new SOSException("The requested period must begin before the it ends");
        
        // loads capabilities advertised time
        Element offeringElt = getOffering(query.getOffering(), dom);
        Element timeElt = dom.getElement(offeringElt, "eventTime/*");
        if(timeElt == null)
        	timeElt = dom.getElement(offeringElt, "time/*");
        if(timeElt == null)
            throw new SOSException("Internal Error: No Time Present in Capabilities Document");        
        GMLTimeReader timeReader = new GMLTimeReader();
        ArrayList<TimeExtent> timeList = new ArrayList<TimeExtent>();
        
        try
        {
            // case of time aggregate
            if (dom.existElement(timeElt, "member"))
            {
                NodeList timeElts = dom.getElements(timeElt, "member/*");                
                for(int i = 0; i < timeElts.getLength(); i++)
                {
                    Element timeMemberElt = (Element)timeElts.item(i);
                    TimeExtent time = timeReader.readTimePrimitive(dom, timeMemberElt);
                    timeList.add(time);
                }
            }            
            // case of single instant/period/grid
            else
            {
                TimeExtent time = timeReader.readTimePrimitive(dom, timeElt);
                timeList.add(time);
            }
        }
        catch (XMLReaderException e)
        {
            throw new SOSException("Internal Error: Invalid Time in Capabilities Document", e);
        }
        
        // check that request time is within one of the advertised periods
        boolean ok = false;
        for (int i=0; i<timeList.size(); i++)
        {
            if (timeList.get(i).contains(query.getTime()))
            {
                ok = true;
                break;
            }
        }
        
        // send error if request time is not contained in any of the advertised periods 
        if (!ok)
        {
            // construct error message
            StringBuffer buf = new StringBuffer("The requested period must be contained in ");
            if (timeList.size() > 1)
                buf.append("one of the following periods:\n");
            
            for (int i=0; i<timeList.size(); i++)
            {
                buf.append(timeList.get(i).getIsoString(0));
                if (i<timeList.size()-1)
                    buf.append(',');
            }
            
            throw new SOSException(buf.toString());
        }            
	}
	
	
    /**
     * Lookup offering element in caps using ID
     * @param obsID
     * @param capsReader
     * @return
     * @throws SOSException
     */
	protected Element getOffering(String obsID, DOMHelper capsReader) throws SOSException
	{
		Element obsElt = null;
		
		try
		{
			obsElt = capsReader.getXmlDocument().getElementByID(obsID);
		}
		catch (Exception e)
		{
			throw new SOSException("No offering with ID " + obsID + " available on this server");
		}
		
		return obsElt;
	}
	
	
	

	@Override
	/**
	 * Load the Caps doc, and build a hashtable of SensorID<=>SensorMLUrl mappings
	 * NOTE : Any subclasses that override this method should call super.updateCaps() first,
	 *        or the SensorML hashtable won't be built. TC
	 * @param capFile - the Capabilites file
	 */
	public synchronized void updateCapabilities(InputStream capFile)
	{
		super.updateCapabilities(capFile);
		//  Populate the sensorMLUrls hashtable
		NodeList procedureElts = 
			capsHelper.getElements("Contents/ObservationOfferingList/ObservationOffering/procedure");
		if(procedureElts == null)
			return;
		//  Clear the hashtable, just in case this gets called multiple times 
		sensorMLUrls.clear();
		for (int i=0; i<procedureElts.getLength(); i++) {
			Element procElt = (Element)procedureElts.item(i);
            String idString =  capsHelper.getAttributeValue(procElt, "href");
            if(idString == null)  // skip this proc if no href
            	continue;
            if(!procElt.hasChildNodes())  // skip if no child elements
            	continue;
            //  I put the child check in b/c the following statement was generating a NPE
            //  in the guts of the XML code when procElt had no children- TC
            Element infoElt = capsHelper.getElement(procElt, "internalInfo");
            if(infoElt == null)
            	continue;
            String docUrl = capsHelper.getAttributeValue(infoElt, "Parameters/sensorMLUrl/href");
            if(docUrl == null)
            	continue;
            sensorMLUrls.put(idString, docUrl);
        }
	}

	
	/**
	 * Adds a new handler or the given observation ID
	 * @param obsID
	 * @param handlerClass
	 */
	public void addDataSetHandler(String obsID, SOSHandler handlerClass)
	{
		dataSetHandlers.put(obsID, handlerClass);
	}


	public void removeDataSetHandler(String dataSetID)
	{
		dataSetHandlers.remove(dataSetID);
	}
	
	
	@Override
	protected String getServiceType()
	{
	    return SOSUtils.SOS;
	}
}
