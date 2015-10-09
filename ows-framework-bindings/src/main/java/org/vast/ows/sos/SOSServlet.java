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
import net.opengis.swe.v20.BinaryEncoding;
import net.opengis.swe.v20.DataBlock;
import net.opengis.swe.v20.DataComponent;
import net.opengis.swe.v20.DataEncoding;
import net.opengis.swe.v20.JSONEncoding;
import net.opengis.swe.v20.TextEncoding;
import net.opengis.swe.v20.XMLEncoding;
import org.vast.cdm.common.DataStreamWriter;
import org.vast.ogc.OGCRegistry;
import org.vast.ogc.gml.GMLUtils;
import org.vast.ogc.om.IObservation;
import org.vast.ogc.om.OMUtils;
import org.vast.ows.GetCapabilitiesRequest;
import org.vast.ows.OWSExceptionReport;
import org.vast.ows.OWSRequest;
import org.vast.ows.OWSUtils;
import org.vast.ows.server.OWSServlet;
import org.vast.ows.swe.DeleteSensorRequest;
import org.vast.ows.swe.DescribeSensorRequest;
import org.vast.ows.swe.UpdateSensorRequest;
import org.vast.swe.AbstractDataWriter;
import org.vast.swe.FilteredWriter;
import org.vast.swe.SWEHelper;
import org.vast.util.TimeExtent;
import org.vast.xml.DOMHelper;
import org.vast.xml.IXMLWriterDOM;
import org.vast.xml.QName;
import org.vast.xml.XMLImplFinder;
import org.vast.xml.XMLReaderException;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;


/**
 * <p>
 * Base abstract class for implementing SOS servlets
 * </p>
 *
 * @author Alex Robin <alex.robin@sensiasoftware.com>
 * @date Nov 24, 2012
 * */
@SuppressWarnings("serial")
public abstract class SOSServlet extends OWSServlet
{
    protected static final String TEXT_MIME_TYPE = "text/plain";
    protected static final String BINARY_MIME_TYPE = "application/octet-stream";
    protected static final String UNSUPPORTED_MSG = " operation is not supported on this server";
    protected static final QName EXT_REPLAY = new QName("replayspeed"); // kvp params are always lower case
    
    // Table of SOS data providers: 1 for each observation offering
	protected Map<String, ISOSDataProviderFactory> dataProviders = new LinkedHashMap<String, ISOSDataProviderFactory>();
	
	// Table of <ProcedureId, SensorMLUrl> - 1 per procedure 
	protected Hashtable<String, String> sensorMLUrls = new Hashtable<String, String>();
	    
	
	@Override
    public void handleRequest(OWSRequest request) throws Exception
    {
	    // core operations
	    if (request instanceof GetCapabilitiesRequest)
            handleRequest((GetCapabilitiesRequest)request);
        else if (request instanceof DescribeSensorRequest)
            handleRequest((DescribeSensorRequest)request);
        else if (request instanceof GetFeatureOfInterestRequest)
            handleRequest((GetFeatureOfInterestRequest)request);
	    else if (request instanceof GetObservationRequest)
            handleRequest((GetObservationRequest)request);
	    
	    // result retrieval
        else if (request instanceof GetResultRequest)
            handleRequest((GetResultRequest)request);
        else if (request instanceof GetResultTemplateRequest)
            handleRequest((GetResultTemplateRequest)request);
	    
	    // transactional methods
        else if (request instanceof InsertSensorRequest)
            handleRequest((InsertSensorRequest)request);
        else if (request instanceof UpdateSensorRequest)
            handleRequest((UpdateSensorRequest)request);
        else if (request instanceof DeleteSensorRequest)
            handleRequest((DeleteSensorRequest)request);
        else if (request instanceof InsertObservationRequest)
            handleRequest((InsertObservationRequest)request);
        else if (request instanceof InsertResultRequest)
            handleRequest((InsertResultRequest)request);
        else if (request instanceof InsertResultTemplateRequest)
            handleRequest((InsertResultTemplateRequest)request);
    }


    protected void handleRequest(GetCapabilitiesRequest query) throws Exception
    {
	    sendCapabilities("ALL", query.getResponseStream());
    }
	    
	
    /**
     * Checks DescribeSensorRequest validity and call the right handler
     * @param request
     */
	protected void handleRequest(DescribeSensorRequest request) throws Exception
	{
		String sensorId = request.getProcedureID();
		if (sensorId == null)
			throw new SOSException("A DescribeSensor request must specify a sensorId argument");
		
		//  Check for SensorId in SensorMLUrl hashtable
		String smlUrl = sensorMLUrls.get(sensorId);
		if(smlUrl == null)
			throw new SOSException("SensorML description for " + sensorId + " not available");
		
		DOMHelper dom = new DOMHelper(smlUrl, false);
        dom.serialize(dom.getBaseElement(), request.getResponseStream() , true);
	}
	
	
	/**
	 * Checks GetResultTemplateRequest validity and call the right handler
	 * @param request
	 */
	protected void handleRequest(GetResultTemplateRequest request) throws Exception
	{
	    ISOSDataProvider dataProvider = null;
        
        try
        {
            // check query parameters        
    	    OWSExceptionReport report = new OWSExceptionReport();
            checkQueryObservables(request.getOffering(), request.getObservables(), report);
            report.process();
            
    	    // setup data provider
    	    SOSDataFilter filter = new SOSDataFilter(request.getObservables().get(0));
    	    dataProvider = getDataProvider(request.getOffering(), filter);
    	    
    	    // build filtered component tree
    	    DataComponent filteredStruct = dataProvider.getResultStructure().copy();
    	    filteredStruct.accept(new DataStructFilter(request.getObservables()));
    	    
            // build and send response 
    	    GetResultTemplateResponse resp = new GetResultTemplateResponse();
    	    resp.setResultStructure(filteredStruct);
    	    resp.setResultEncoding(dataProvider.getDefaultResultEncoding());
    	    sendResponse(request, resp);    	    
        }
        finally
        {
            if (dataProvider != null)
                dataProvider.close();
        }
	}
	
	
	/**
     * Checks GetResultRequest validity and call the right handler
     * @param request
     */
    protected void handleRequest(GetResultRequest request) throws Exception
    {
        ISOSDataProvider dataProvider = null;
                
        try
        {
            // check query parameters
            OWSExceptionReport report = new OWSExceptionReport();
            checkQueryObservables(request.getOffering(), request.getObservables(), report);
            checkQueryProcedures(request.getOffering(), request.getProcedures(), report);
            checkQueryTime(request.getOffering(), request.getTime(), report);
            report.process();
            
            // setup data filter (including extensions)
            SOSDataFilter filter = new SOSDataFilter(request.getFoiIDs(), request.getObservables(), request.getTime());
            if (request.getExtensions().containsKey(EXT_REPLAY))
            {
                String replaySpeed = (String)request.getExtensions().get(EXT_REPLAY);
                filter.setReplaySpeedFactor(Double.parseDouble(replaySpeed));
            }
            
            // setup data provider
            dataProvider = getDataProvider(request.getOffering(), filter);
            DataComponent resultStructure = dataProvider.getResultStructure();
            DataEncoding resultEncoding = dataProvider.getDefaultResultEncoding();
            
            // write response with SWE common data stream
            OutputStream os = new BufferedOutputStream(request.getResponseStream());
            
            // write small xml wrapper if requested
            if (((GetResultRequest) request).isXmlWrapper())
            {
                String nsUri = OGCRegistry.getNamespaceURI(SOSUtils.SOS, request.getVersion());
                os.write(new String("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n").getBytes());
                os.write(new String("<GetResultResponse xmlns=\"" + nsUri + "\">\n<resultValues>\n").getBytes());
            }
            
            // set response headers in case of HTTP response
            else if (request.getHttpResponse() != null)
            {
                if (resultEncoding instanceof TextEncoding)
                    request.getHttpResponse().setContentType(TEXT_MIME_TYPE);
                else if (resultEncoding instanceof JSONEncoding)
                    request.getHttpResponse().setContentType(OWSUtils.JSON_MIME_TYPE);
                else if (resultEncoding instanceof XMLEncoding)
                    request.getHttpResponse().setContentType(OWSUtils.XML_MIME_TYPE);
                else if (resultEncoding instanceof BinaryEncoding)
                    request.getHttpResponse().setContentType(BINARY_MIME_TYPE);
                else
                    throw new RuntimeException("Unsupported encoding: " + resultEncoding.getClass().getCanonicalName());
            }
            
            // use specific format handler if available
            boolean dataWritten = false;
            if (resultEncoding instanceof BinaryEncoding)
                dataWritten = writeCustomFormatStream(request, dataProvider, os);
            
            // otherwise use default
            if (!dataWritten)
            {
                // prepare writer for selected encoding
                DataStreamWriter writer = SWEHelper.createDataWriter(resultEncoding);
                
                // we also do filtering here in case data provider hasn't modified the datablocks
                writer = new FilteredWriter((AbstractDataWriter)writer, request.getObservables());
                writer.setDataComponents(resultStructure);
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
        }
        finally
        {
            if (dataProvider != null)
                dataProvider.close();
        }
    }
    
    
    /**
     * This method inits the data stream by setting correct MIME type and writing necessary header info
     * Sub-classes can override this method to use specific wrapper formats (e.g. video containers such as MP4)
     * @param request
     * @param resultEncoding
     * @param os
     */
    protected boolean writeCustomFormatStream(GetResultRequest request, ISOSDataProvider dataProvider, OutputStream os) throws Exception
    {
        return false;
    }
	
	
	/**
	 * Checks GetObservationRequest validity and call the right handler
	 * @param request
	 */
	protected void handleRequest(GetObservationRequest request) throws Exception
	{
	    ISOSDataProvider dataProvider = null;
	    
	    try
        {
            if (request.getOffering() == null)
            	throw new SOSException("A GetObservation SOS request must specify an offeringId argument");
            
            if (request.getObservables().isEmpty())
            	throw new SOSException("An SOS request must contain at least one observable");
            
            // set default format
            if (request.getFormat() == null)
                request.setFormat(GetObservationRequest.DEFAULT_FORMAT);
            
            // check query parameters
            OWSExceptionReport report = new OWSExceptionReport();
            checkQueryObservables(request.getOffering(), request.getObservables(), report);
            checkQueryProcedures(request.getOffering(), request.getProcedures(), report);
            checkQueryFormat(request.getOffering(), request.getFormat(), report);
            checkQueryTime(request.getOffering(), request.getTime(), report);
            report.process();
            
            // setup data provider
            SOSDataFilter filter = new SOSDataFilter(request.getFoiIDs(), request.getObservables(), request.getTime());
            dataProvider = getDataProvider(request.getOffering(), filter);
            
            // prepare obs writer for requested O&M version
            String format = request.getFormat();
            String omVersion = format.substring(format.lastIndexOf('/') + 1);
            IXMLWriterDOM<IObservation> obsWriter = (IXMLWriterDOM<IObservation>)OGCRegistry.createWriter(OMUtils.OM, OMUtils.OBSERVATION, omVersion);
            String sosNsUri = OGCRegistry.getNamespaceURI(SOSUtils.SOS, "2.0");
            String sosPrefix = "sos";
            
            // init xml document writing
            OutputStream os = new BufferedOutputStream(request.getResponseStream());
            XMLEventFactory xmlFactory = XMLEventFactory.newInstance();
            XMLEventWriter xmlWriter = XMLOutputFactory.newInstance().createXMLEventWriter(os, "UTF-8");
            xmlWriter.add(xmlFactory.createStartDocument());
            xmlWriter.add(xmlFactory.createStartElement(sosPrefix, sosNsUri, "GetObservationResponse"));
            xmlWriter.add(xmlFactory.createNamespace(sosPrefix, sosNsUri));
            
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
                
                xmlWriter.add(xmlFactory.createStartElement(sosPrefix, sosNsUri, "observationData"));
                
                XMLInputFactory factory = XMLImplFinder.getStaxInputFactory();
                XMLEventReader domReader = factory.createXMLEventReader(new DOMSource(obsElt));
                while (domReader.hasNext())
                {
                    XMLEvent event = domReader.nextEvent();
                    if (!event.isStartDocument() && !event.isEndDocument())
                        xmlWriter.add(event);
                }
                
                xmlWriter.add(xmlFactory.createEndElement(sosPrefix, sosNsUri, "observationData"));
                xmlWriter.flush();
                os.write('\n');
            }
            
            xmlWriter.add(xmlFactory.createEndDocument());
            xmlWriter.close();
        }
        finally
        {
            if (dataProvider != null)
                dataProvider.close();
        }
	}
	
	
	protected void handleRequest(GetFeatureOfInterestRequest request) throws Exception
	{
	    throw new UnsupportedOperationException(request.getOperation() + UNSUPPORTED_MSG);
	}
	
	
	protected void handleRequest(InsertSensorRequest request) throws Exception
    {
	    throw new UnsupportedOperationException(request.getOperation() + UNSUPPORTED_MSG);
    }
	
	
	protected void handleRequest(UpdateSensorRequest request) throws Exception
    {
        throw new UnsupportedOperationException(request.getOperation() + UNSUPPORTED_MSG);
    }
	
	
	protected void handleRequest(DeleteSensorRequest request) throws Exception
    {
        throw new UnsupportedOperationException(request.getOperation() + UNSUPPORTED_MSG);
    }
	
	
	protected void handleRequest(InsertObservationRequest request) throws Exception
    {
	    throw new UnsupportedOperationException(request.getOperation() + UNSUPPORTED_MSG);
    }
	
	
	protected void handleRequest(InsertResultTemplateRequest request) throws Exception
    {
	    throw new UnsupportedOperationException(request.getOperation() + UNSUPPORTED_MSG);
    }
	
	
	protected void handleRequest(InsertResultRequest request) throws Exception
    {
	    throw new UnsupportedOperationException(request.getOperation() + UNSUPPORTED_MSG);
    }
	
	
	/**
	 * Checks if all selected observables are available in this offering
	 * @param offeringID
	 * @param observables
	 * @param report Exception report to append OWS exceptions to
	 * @throws SOSException
	 */
	protected void checkQueryObservables(String offeringID, List<String> observables, OWSExceptionReport report) throws SOSException
	{
		Element offeringElt = getOffering(offeringID);		
		NodeList observableElts = capsHelper.getElements(offeringElt, "observableProperty");
		boolean ok;
		
		for (String obsProp: observables)
		{
			ok = false;
			
			for (int i=0; i<observableElts.getLength(); i++)
			{
				String idString =  capsHelper.getAttributeValue((Element)observableElts.item(i), "href");
				if(idString == null)
                    idString = capsHelper.getElementValue((Element)observableElts.item(i));
				
				if (idString.equals(obsProp))
				{
					ok = true;
					break;
				}
			}
			
			if (!ok)
			    report.add(new SOSException(SOSException.invalid_param_code, "observedProperty", obsProp, "Observed property " + obsProp + " is not available for offering " + offeringID));
		}
	}
    
    
    /**
     * Checks if all selected procedures are available in this offering
     * @param offeringID
     * @param procedures
     * @param report Exception report to append OWS exceptions to
     * @throws SOSException
     */
    protected void checkQueryProcedures(String offeringID, List<String> procedures, OWSExceptionReport report) throws SOSException
    {
        Element offeringElt = getOffering(offeringID);
        NodeList procedureElts = capsHelper.getElements(offeringElt, "procedure");
        boolean ok;
        
        for (String procID: procedures)
        {
            ok = false;
            
            for (int i=0; i<procedureElts.getLength(); i++)
            {
                String idString =  capsHelper.getAttributeValue((Element)procedureElts.item(i), "href");
                if(idString == null)
                    idString = capsHelper.getElementValue((Element)procedureElts.item(i));
                
                if (idString.equals(procID))
                {
                    ok = true;
                    break;
                }
            }
            
            if (!ok)
                report.add(new SOSException(SOSException.invalid_param_code, "procedure", procID, "Procedure " + procID + " is not available for offering " + offeringID));
        }
    }
	
	
	/**
	 * Checks if selected format is available in this offering
	 * @param offeringID
	 * @param format
	 * @param report Exception report to append OWS exceptions to
	 * @throws SOSException
	 */
	protected void checkQueryFormat(String offeringID, String format, OWSExceptionReport report) throws SOSException
	{
		Element offeringElt = getOffering(offeringID);
        
        // get format lists from either resultFormat or responseFormat elts
		NodeList formatElts = capsHelper.getElements(offeringElt, "resultFormat");
    	if (formatElts.getLength() == 0)
    	    formatElts = capsHelper.getElements(offeringElt, "responseFormat");
    
    	// if no formats are specified in caps, check if default is requested
    	if (format.equals(GetObservationRequest.DEFAULT_FORMAT))
    	    return;
        
        // check query format vs. each supported format in the capabilities
        int listSize = formatElts.getLength();
		for (int i=0; i<listSize; i++)
		{
			String formatString = capsHelper.getElementValue((Element)formatElts.item(i), "");
			
			if (formatString.equals(format))
				return;
		}
		
		report.add(new SOSException(SOSException.invalid_param_code, "format", format, "Format " + format + " is not available for offering " + offeringID));
	}
	
	
	/**
	 * Checks if requested time is available in this offering
	 * @param offeringID
	 * @param requestTime
	 * @param report Exception report to append OWS exceptions to
	 * @throws SOSException
	 * TODO  these check*() methods need to be moved to account for differing SOS versions
	 */
	protected void checkQueryTime(String offeringID, TimeExtent requestTime, OWSExceptionReport report) throws SOSException
	{
        // make sure startTime <= stopTime
        if (requestTime.getStartTime() > requestTime.getStopTime())
            report.add(new SOSException("The requested period must begin before the it ends"));
        
        // loads capabilities advertised time
        Element offeringElt = getOffering(offeringID);
        Element timeElt = capsHelper.getElement(offeringElt, "eventTime/*");
        if(timeElt == null)
        	timeElt = capsHelper.getElement(offeringElt, "phenomenonTime/*");
        if(timeElt == null)
            throw new SOSException("Internal Error: No Time Present in Capabilities Document");        
        GMLUtils gmlUtils = new GMLUtils(GMLUtils.V3_2);
        ArrayList<TimeExtent> timeList = new ArrayList<TimeExtent>();
        
        try
        {
            // case of time aggregate
            if (capsHelper.existElement(timeElt, "member"))
            {
                NodeList timeElts = capsHelper.getElements(timeElt, "member/*");                
                for(int i = 0; i < timeElts.getLength(); i++)
                {
                    Element timeMemberElt = (Element)timeElts.item(i);
                    TimeExtent timeExtent = gmlUtils.readTimePrimitiveAsTimeExtent(capsHelper, timeMemberElt);
                    timeList.add(timeExtent);
                }
            }            
            // case of single instant/period/grid
            else
            {
                TimeExtent timeExtent = gmlUtils.readTimePrimitiveAsTimeExtent(capsHelper, timeElt);
                timeList.add(timeExtent);
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
            
            report.add(new SOSException(SOSException.invalid_param_code, "phenomenonTime", requestTime.getIsoString(0), buf.toString()));
        }            
	}
	
	
    /**
     * Lookup offering element in caps using ID
     * @param offeringID
     * @return
     * @throws SOSException if no offering with requested ID can be found
     */
	protected Element getOffering(String offeringID) throws SOSException
	{
		Element offElt = null;
		
		NodeList offElts = capsHelper.getElements("contents/Contents/offering/*");
        for (int i = 0; i < offElts.getLength(); i++)
        {
            offElt = (Element)offElts.item(i);
            if (capsHelper.getElementValue(offElt, "identifier").equals(offeringID))
                return offElt;
        }
        
        throw new SOSException("No offering with ID " + offeringID + " is available on this server");
	}
	
	
	protected ISOSDataProvider getDataProvider(String offering, SOSDataFilter filter) throws Exception
	{
	    ISOSDataProviderFactory factory = dataProviders.get(offering);
        if (factory == null)
            throw new IllegalStateException("No valid data provider factory found for offering " + offering);
        return factory.getNewDataProvider(filter);
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
	
	
    @Override
    protected String getServiceType()
    {
        return SOSUtils.SOS;
    }
}
