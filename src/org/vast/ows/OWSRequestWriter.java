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
 * <p><b>Title:</b>
 * OWS Request Writer
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Base interface for all OWS request writers
 * </p>
 *
 * <p>Copyright (c) 2007</p>
 * @author Alexandre Robin
 * @date Jan 16, 2007
 * @version 1.0
 */
public interface OWSRequestWriter<QueryType extends OWSRequest>
{

    /**
     * Builds a String containing the GET request URL
     * @param query
     * @return
     */
    public String buildURLQuery(QueryType query) throws OWSException;


    /**
     * Builds a DOM element containing the request XML
     * Note that the element is not yet appended to any parent.
     * @param query
     * @return
     */
    public Element buildXMLQuery(DOMHelper dom, QueryType query) throws OWSException;
    
    
    /**
     * Writes an XML query to the specified output stream
     * @param os
     * @param owsQuery
     * @throws OWSException
     */
    public void writeXMLQuery(OutputStream os, QueryType owsQuery) throws OWSException;

}