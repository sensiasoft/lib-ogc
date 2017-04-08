package org.vast.ows.sas;


import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import org.vast.cdm.common.CDMException;
import org.vast.cdm.common.DataHandler;
import org.vast.ogc.OGCException;
import org.vast.ogc.OGCExceptionReader;
import org.vast.swe.SWEFilter;
import org.vast.swe.SWEReader;
import org.vast.util.DateTimeFormat;
import org.vast.util.ReaderException;
import org.vast.xml.DOMHelper;
import org.vast.xml.DOMHelperException;
import org.vast.xml.XMLReaderException;
import org.w3c.dom.Element;


/**
 * <p>
 * </p>
 *
 * @author Tony Cook
 * @date Dec 6, 2007
 * */

public class SASAlertReader extends SWEReader
{
	protected SWEFilter streamFilter;
	protected double timestamp;
	protected DateTimeFormat timeFormat = new DateTimeFormat();
	
    
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
            timestamp = timeFormat.parseIso(timestampISO);        

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

