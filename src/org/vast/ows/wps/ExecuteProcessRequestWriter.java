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
    Alexandre Robin <alexandre.robin@spotimage.fr>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ows.wps;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.vast.cdm.common.CDMException;
import org.vast.ows.OWSException;
import org.vast.sweCommon.AbstractDataWriter;
import org.vast.sweCommon.SWEFactory;


/**
 * <p><b>Title:</b><br/>
 * Execute Process Request Writer
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Writer to write the Execute Process data into the request stream
 * </p>
 *
 * <p>Copyright (c) 2008</p>
 * @author Gregoire Berthiau
 * @date Dec 15, 2008
 * @version 1.0
 */
public class ExecuteProcessRequestWriter
{

    public final static String invalidEndpoint = "No Endpoint URL specified in request object";
    public final static String ioError = "IO Error while sending request:";
	
    
	public HttpURLConnection sendRequest(ExecuteProcessRequest request) throws OWSException, CDMException
    {
	    try
        {
        	URL url;
        	String endpoint = request.getPostServer();
        	
        	if (endpoint == null)
        		endpoint = request.getGetServer();
        	
        	if (endpoint == null)
        		throw new OWSException(invalidEndpoint);
        	
            // remove ? at the end of Endpoint URL
            if (endpoint.endsWith("?"))
                url = new URL(endpoint.substring(0, endpoint.length()-1));
            else
                url = new URL(endpoint);
           
            // initiatlize HTTP connection
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            PrintStream out = new PrintStream(connection.getOutputStream());
            
            String encodingType = request.inputDataEncoding.getEncodingType().toString();
            
            if(encodingType.equalsIgnoreCase("ascii"))
            	connection.setRequestProperty("Content-type", "text/plain"); 
            else if(encodingType.equalsIgnoreCase("xml"))
            	connection.setRequestProperty("Content-type", "text/xml");
            else if(encodingType.equalsIgnoreCase("binary"))
            	connection.setRequestProperty("Content-type", "application/binary");            	
            else throw new IOException(ioError + "\n" + "this content-type is not handled. " +
            		"The content type can only be ascii (text/plain), text/xml or application/binary");
            
            AbstractDataWriter writer = (AbstractDataWriter)SWEFactory.createDataWriter(request.getInputDataEncoding());
    		writer.setDataEncoding(request.getInputDataEncoding());
    		writer.setDataComponents(request.getInputDataComponent());
    		writer.write(out);
            
            // send post data                       
            out.flush();
            connection.connect();
            out.close();
            
            // return server response stream
            return connection;
        }
        catch (IOException e)
        {
        	ByteArrayOutputStream buf = new ByteArrayOutputStream();
        	throw new OWSException(ioError + "\n" + buf, e);
        }
    }

}