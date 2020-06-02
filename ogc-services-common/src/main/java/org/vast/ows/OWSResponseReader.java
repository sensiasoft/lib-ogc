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

import org.vast.xml.DOMHelper;
import org.w3c.dom.Element;


/**
 * <p>
 * Base interface for all OWS response readers
 * </p>
 *
 * @author Alex Robin
 * @since 21 nov. 07
 * * @param <ResponseType> Type of response object supported by this reader
 */
public interface OWSResponseReader<ResponseType extends OWSResponse>
{

	/**
     * Reads XML response from the given element and using the given DOMHelper.
     * @param dom DOMHelper instance used to read the XML content
     * @param responseElt DOM element corresponding to the root of the request
     * @return response object deserialized from XML
     * @throws OWSException
     */
    public ResponseType readXMLResponse(DOMHelper dom, Element responseElt) throws OWSException;
    
}