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

package org.vast.ows;

import java.io.OutputStream;
import org.vast.xml.DOMHelper;
import org.w3c.dom.Element;


/**
 * <p>
 * Base interface for all OWS response writers
 * </p>
 *
 * @author Alex Robin
 * @since 21 nov. 07
 * * @param <ResponseType> Type of response object supported by this writer
 */
public interface OWSResponseWriter<ResponseType extends OWSResponse>
{
		
    /**
     * Builds a DOM element containing the response XML.<br/>
     * Note that the element is not yet appended to any parent.
     * @param dom  DOMHelper instance that will own the generated element
     * @param response response object to serialize as XML
     * @return DOM element containing the XML representation of the response 
     * @throws OWSException 
     */
    public Element buildXMLResponse(DOMHelper dom, ResponseType response) throws OWSException;
    
    
    /**
     * Builds a DOM element containing the response XML for the given version.<br/>
     * Note that the element is not yet appended to any parent.
     * @param dom  DOMHelper instance that will own the generated element
     * @param response response object to serialize as XML
     * @param version version of writer to use
     * @return DOM element containing the XML representation of the response
     * @throws OWSException 
     */
    public Element buildXMLResponse(DOMHelper dom, ResponseType response, String version) throws OWSException;
    
    
    /**
     * Writes the XML response directly to the output stream
     * @param os output stream where the XML data will be written
     * @param response response object to serialize as XML
     * @throws OWSException
     */
    public void writeXMLResponse(OutputStream os, ResponseType response) throws OWSException;
    
    
    /**
     * Writes the XML response for the desired version directly to the output stream
     * @param os output stream where the XML data will be written
     * @param response response object to serialize as XML
     * @param version version of writer to use
     * @throws OWSException
     */
    public void writeXMLResponse(OutputStream os, ResponseType response, String version) throws OWSException;
}