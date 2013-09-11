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

package org.vast.ows.sos;

import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.events.XMLEvent;
import javax.xml.transform.dom.DOMSource;
import org.vast.cdm.common.DataBlock;
import org.vast.cdm.common.DataStreamWriter;
import org.vast.ogc.OGCRegistry;
import org.vast.ogc.gml.GMLTimeReader;
import org.vast.ogc.om.IObservation;
import org.vast.ogc.om.OMUtils;
import org.vast.ows.GetCapabilitiesRequest;
import org.vast.ows.OWSRequest;
import org.vast.ows.server.OWSServlet;
import org.vast.ows.server.SOSDataFilter;
import org.vast.ows.swe.DescribeSensorRequest;
import org.vast.sweCommon.SWEFactory;
import org.vast.util.TimeExtent;
import org.vast.xml.DOMHelper;
import org.vast.xml.IXMLWriterDOM;
import org.vast.xml.XMLReaderException;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;


/**
 * <p><b>Title:</b>
 * SOSServlet2
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Base abstract class for implementing SOS servlets
 * </p>
 *
 * <p>Copyright (c) 2012</p>
 * @author Alexandre Robin
 * @date Nov 24, 2012
 * @version 1.0
 */
@SuppressWarnings("serial")
public abstract class SOSServlet extends OWSServlet
{
    
    // Table of SOS data providers: 1 for each observation offering
	protected Map<String, ISOSDataProviderFactory> dataProviders = new LinkedHashMap<String, ISOSDataProviderFactory>();
	
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
        else if (request instanceof GetResultTemplateRequest)
            handleRequest((GetResultTemplateRequest)request);
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
        dom.serialize(dom.getBaseElement(), query.getResponseStream() , null);		
	}
	
	
	/**
	 * Decode the query, check validity and call the right handler
	 * @param query
	 */
	protected void handleRequest(GetResultTemplateRequest request) throws Exception
	{
	    // check query parameters
        checkQueryObservables(request.getOffering(), request.getObservables(), capsHelper);
        
	    // setup data provider
	    SOSDataFilter filter = new SOSDataFilter(request.getObservables().get(0));
	    ISOSDataProvider dataProvider = getDataProvider(request.getOffering(), filter);
        
        // build and 
	    GetResultTemplateResponse resp = new GetResultTemplateResponse();
	    resp.setResultStructure(dataProvider.getResultStructure());
	    resp.setResultEncoding(dataProvider.getDefaultResultEncoding());

	    // write response to response stream
	    OutputStream os = new BufferedOutputStream(request.getResponseStream());
	    owsUtils.writeXMLResponse(os, resp, request.getVersion());
	    os.flush();
	}
	
	
	/**
     * Decode the query, check validity and call the right handler
     * @param query
     */
    protected void handleRequest(GetResultRequest request) throws Exception
    {
        // check query parameters
        checkQueryObservables(request.getOffering(), request.getObservables(), capsHelper);
        checkQueryProcedures(request.getOffering(), request.getProcedures(), capsHelper);
        checkQueryTime(request.getOffering(), request.getTime(), capsHelper);
        
        // setup data provider
        SOSDataFilter filter = new SOSDataFilter(request.getFoiIDs(), request.getObservables(), request.getTime());
        ISOSDataProvider dataProvider = getDataProvider(request.getOffering(), filter);
        
        // write response with SWE common data stream
        OutputStream os = new BufferedOutputStream(request.getResponseStream());
        
        // write small xml wrapper if requested
        if (((GetResultRequest) request).isXmlWrapper())
        {
            String nsUri = OGCRegistry.getNamespaceURI(SOSUtils.SOS, request.getVersion());
            os.write(new String("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n").getBytes());
            os.write(new String("<GetResultResponse xmlns=\"" + nsUri + "\">\n<resultValues>\n").getBytes());
        }
        else
            request.getHttpResponse().setContentType("text/plain");
        
        // prepare writer for selected encoding
        DataStreamWriter writer = SWEFactory.createDataWriter(dataProvider.getDefaultResultEncoding());
        writer.setDataComponents(dataProvider.getResultStructure());
        writer.setOutput(os);
        
        // write each record in output stream
        DataBlock nextRecord;
        while ((nextRecord = dataProvider.getNextResultRecord()) != null)
        {
            writer.write(nextRecord);
            writer.flush();
        }
        
        // close xml wrapper
        if (((GetResultRequest) request).isXmlWrapper())
            os.write(new String("\n</resultValues>\n</GetResultResponse>").getBytes());            
                
        os.flush();
    }
	
	
	/**
	 * Decode the query, check validity and call the right handler
	 * @param query
	 */
	protected void handleRequest(GetObservationRequest request) throws Exception
	{
		if (request.getOffering() == null)
			throw new SOSException("A GetObservation SOS request must specify an offeringId argument");
		
		if (request.getObservables().isEmpty())
			throw new SOSException("An SOS request must contain at least one observable");
		
		// check query parameters
		checkQueryObservables(request.getOffering(), request.getObservables(), capsHelper);
        checkQueryProcedures(request.getOffering(), request.getProcedures(), capsHelper);
		checkQueryFormat(request.getOffering(), request.getFormat(), capsHelper);
		checkQueryTime(request.getOffering(), request.getTime(), capsHelper);
		String format = request.getFormat();
		
		// setup data provider
        SOSDataFilter filter = new SOSDataFilter(request.getFoiIDs(), request.getObservables(), request.getTime());
        ISOSDataProvider dataProvider = getDataProvider(request.getOffering(), filter);
		
		// prepare obs writer for requested O&M version
		String nsUri = format;
        String omVersion = nsUri.substring(format.lastIndexOf('/') + 1);
        IXMLWriterDOM<IObservation> obsWriter = (IXMLWriterDOM<IObservation>)OGCRegistry.createWriter(OMUtils.OM, OMUtils.OBSERVATION, omVersion);
        
		// init xml document writing
		OutputStream os = new BufferedOutputStream(request.getResponseStream());
		XMLEventFactory xmlFactory = XMLEventFactory.newInstance();
		XMLEventWriter xmlWriter = XMLOutputFactory.newInstance().createXMLEventWriter(os);
		xmlWriter.add(xmlFactory.createStartDocument());
		xmlWriter.add(xmlFactory.createStartElement("om", nsUri, "GetObservationResponse"));
		
		// write each observation in stream
		boolean firstObs = true;
		IObservation obs;
		while ((obs = dataProvider.getNextObservation()) != null)
		{
		    DOMHelper dom = new DOMHelper();
		    Element obsElt = obsWriter.write(dom, obs);
		    
		    // add namespaces
		    if (firstObs)
		    {
		        for (Entry<String, String> nsDef: dom.getXmlDocument().getNSTable().entrySet())
		            xmlWriter.add(xmlFactory.createNamespace(nsDef.getKey(), nsDef.getValue()));

		        firstObs = false;
		    }
		    
		    xmlWriter.add(xmlFactory.createStartElement("om", nsUri, "observationData"));
		    
		    XMLEventReader domReader = XMLInputFactory.newInstance().createXMLEventReader(new DOMSource(obsElt));
		    while (domReader.hasNext())
		    {
		        XMLEvent event = domReader.nextEvent();
		        if (!event.isStartDocument() && !event.isEndDocument())
		            xmlWriter.add(event);
		    }
		    
		    xmlWriter.add(xmlFactory.createEndElement("om", nsUri, "observationData"));
		}
		
		xmlWriter.add(xmlFactory.createEndDocument());
		xmlWriter.close();
	}
	
	
	/**
	 * Checks if all selected observables are available in this offering
	 * @param offering
	 * @param observables
	 * @param capsReader
	 * @throws SOSException
	 */
	protected void checkQueryObservables(String offering, List<String> observables, DOMHelper capsReader) throws SOSException
	{
		Element offeringElt = getOffering(offering, capsReader);		
		NodeList observableElts = capsReader.getElements(offeringElt, "observableProperty");
		boolean ok;
		
		for (int j=0; j<observables.size(); j++)
		{
			ok = false;
			
			for (int i=0; i<observableElts.getLength(); i++)
			{
				String idString =  capsReader.getAttributeValue((Element)observableElts.item(i), "href");
				if(idString == null)
                    idString = capsReader.getElementValue((Element)observableElts.item(i));
				
				if (idString.equals(observables.get(j)))
				{
					ok = true;
					break;
				}
			}
			
			if (!ok)
				throw new SOSException("Observed property " + observables.get(j) + " is not available for offering " + offering);
		}
	}
    
    
    /**
     * Checks if all selected procedures are available in this offering
     * @param offering
     * @param procedures
     * @param capsReader
     * @throws SOSException
     */
    protected void checkQueryProcedures(String offering, List<String> procedures, DOMHelper capsReader) throws SOSException
    {
        Element offeringElt = getOffering(offering, capsReader);
        NodeList procedureElts = capsReader.getElements(offeringElt, "procedure");
        boolean ok;
        
        for (int j=0; j<procedures.size(); j++)
        {
            ok = false;
            
            for (int i=0; i<procedureElts.getLength(); i++)
            {
                String idString =  capsReader.getAttributeValue((Element)procedureElts.item(i), "href");
                if(idString == null)
                    idString = capsReader.getElementValue((Element)procedureElts.item(i));
                
                if (idString.equals(procedures.get(j)))
                {
                    ok = true;
                    break;
                }
            }
            
            if (!ok)
                throw new SOSException("Procedure " + procedures.get(j) + " is not available for offering " + offering);
        }
    }
	
	
	/**
	 * Checks if selected format is available in this offering
	 * @param offering
	 * @param format
	 * @param capsReader
	 * @throws SOSException
	 */
	protected void checkQueryFormat(String offering, String format, DOMHelper capsReader) throws SOSException
	{
		Element offeringElt = getOffering(offering, capsReader);
        
        // get format lists from either resultFormat or responseFormat elts
		NodeList formatElts = capsReader.getElements(offeringElt, "resultFormat");
    	if (formatElts.getLength() == 0)
    	    formatElts = capsReader.getElements(offeringElt, "responseFormat");
    
    	// if no formats are specified in caps, check if default is requested
    	if (format.equals(GetObservationRequest.DEFAULT_FORMAT))
    	    return;
        
        // check query format vs. each supported format in the capabilities
        int listSize = formatElts.getLength();
		for (int i=0; i<listSize; i++)
		{
			String formatString = capsReader.getElementValue((Element)formatElts.item(i), "");
			
			if (formatString.equals(format))
				return;
		}
		
		throw new SOSException("Format " + format + " is not available for offering " + offering);
	}
	
	
	/**
	 * Checks if requested time is available in this offering
	 * @param offering
	 * @param requestTime
	 * @param capsReader
	 * @throws SOSException
	 * 	  TODO  these check*() methods need to be moved to account for differing SOS versions

	 */
	protected void checkQueryTime(String offering, TimeExtent requestTime, DOMHelper dom) throws SOSException
	{
        // make sure startTime <= stopTime
        if (requestTime.getStartTime() > requestTime.getStopTime())
            throw new SOSException("The requested period must begin before the it ends");
        
        // loads capabilities advertised time
        Element offeringElt = getOffering(offering, dom);
        Element timeElt = dom.getElement(offeringElt, "eventTime/*");
        if(timeElt == null)
        	timeElt = dom.getElement(offeringElt, "phenomenonTime/*");
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
            if (timeList.get(i).contains(requestTime))
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
     * @param offeringID
     * @param capsReader
     * @return
     * @throws SOSException
     */
	protected Element getOffering(String offeringID, DOMHelper capsReader) throws SOSException
	{
		Element offElt = null;
		
		NodeList offElts = capsReader.getElements("contents/Contents/offering/*");
        for (int i = 0; i < offElts.getLength(); i++)
        {
            offElt = (Element)offElts.item(i);
            if (capsReader.getElementValue(offElt, "identifier").equals(offeringID))
                return offElt;
        }
        
        throw new SOSException("No offering with ID " + offeringID + " is available on this server");
	}
	
	
	protected ISOSDataProvider getDataProvider(String offering, SOSDataFilter filter) throws Exception
	{
	    ISOSDataProviderFactory dataProvider = dataProviders.get(offering);
        if (dataProvider == null)
            throw new IllegalStateException("No valid data provider found for offering " + offering);     
        return dataProvider.getNewProvider(filter);
	}

	
	/**
	 * Load the Caps doc, and build a hashtable of SensorID<=>SensorMLUrl mappings
	 * NOTE : Any subclasses that override this method should call super.updateCaps() first,
	 *        or the SensorML hashtable won't be built. TC
	 * @param capFile - the Capabilites file
	 */
	@Override
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

	
	public void addDataProvider(String offeringID, ISOSDataProviderFactory dataProvider)
	{
	    dataProviders.put(offeringID, dataProvider);
	}


	public void removeDataProvider(String offeringID)
	{
	    dataProviders.remove(offeringID);
	}
}
