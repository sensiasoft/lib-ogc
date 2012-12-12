package org.vast.ows.sas;


import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import org.vast.cdm.common.CDMException;
import org.vast.cdm.common.DataHandler;
import org.vast.ogc.OGCException;
import org.vast.ogc.OGCExceptionReader;
import org.vast.sweCommon.SWEFilter;
import org.vast.sweCommon.SWEReader;
import org.vast.util.DateTimeFormat;
import org.vast.util.ReaderException;
import org.vast.xml.DOMHelper;
import org.vast.xml.DOMHelperException;
import org.vast.xml.XMLReaderException;
import org.w3c.dom.Element;


/**
 * <p><b>Title:</b>
 *  SASAlertReader
 * </p>
 *
 * <p><b>Description:</b><br/>
 * </p>
 *
 * <p>Copyright (c) 2007</p>
 * @author Tony Cook
 * @date Dec 6, 2007
 * @version 1.0
 */

public class SASAlertReader extends SWEReader
{
	protected SWEFilter streamFilter;
	protected double timestamp;       
    
    public SASAlertReader()
    {
    }
    
    
    @Override
    public void parse(InputStream inputStream, DataHandler handler) throws IOException
    {
		try
		{
		    dataHandler = handler;
		    streamFilter = new SWEFilter(inputStream);
			streamFilter.setDataElementName("AlertData");
			
			// parse xml header using DOMReader
			DOMHelper dom = new DOMHelper(streamFilter, false);
			OGCExceptionReader.checkException(dom);
			
			// find first Observation element
			Element rootElement = dom.getRootElement();

            // read timestamp
            Element timestampElt = dom.getElement(rootElement, "Timestamp");
            String timestampISO = dom.getElementValue(timestampElt);
            timestamp = DateTimeFormat.parseIso(timestampISO);        

            // create dataParser
            this.dataParser = createDataParser();
		}
        catch (IllegalStateException e)
        {
            throw new ReaderException("No reader found for SWECommon", e);
        }
		catch (DOMHelperException e)
		{
			throw new XMLReaderException("Error while parsing Observation XML", e);
		}		
		catch (OGCException e)
		{
			throw new XMLReaderException(e.getMessage());
		} 
		catch (ParseException e) 
		{
			throw new XMLReaderException("Error while parsing the ISO-formatted timestamp", e);
		}
	}


	@Override
	public InputStream getDataStream() throws IOException 
	{
		streamFilter.startReadingData();
		return streamFilter;
	}

	public double getTimestamp() throws CDMException 
	{
		return timestamp;
	}
	
}

