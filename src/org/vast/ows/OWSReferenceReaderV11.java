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

package org.vast.ows;

import org.vast.ows.OWSReference;
import org.w3c.dom.*;
import org.vast.xml.DOMHelper;

/**
 * <p><b>Title:</b><br/>
 * OWS Reference Reader V11
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Utility methods to read OWS common References and Reference Groups
 * </p>
 *
 * <p>Copyright (c) 2007</p>
 * @author Alexandre Robin <alexandre.robin@spotimage.fr>
 * @date 22 nov. 07
 * @version 1.0
 */
public class OWSReferenceReaderV11
{

	public OWSReference readXML(DOMHelper dom, Element refElt) throws OWSException
	{
		OWSReference ref = new OWSReference();		
		
		// role
		String role = dom.getAttributeValue(refElt, "@role");
		ref.setRole(role);
		
		// href
		String endpoint = dom.getAttributeValue(refElt, "@href");
		ref.setHref(endpoint);
		
		// format
		String format = dom.getElementValue(refElt, "Format");
		ref.setFormat(format);
		
		return ref;
	}
	
	
	public void readRefGroupInfo(DOMHelper dom, Element refGroupElt, OWSReferenceGroup refGroup)
	{
		// title = transaction type (add, update, delete)
		String title = dom.getElementValue(refGroupElt, "Title");
		refGroup.setTitle(title);
		
		// description
		String description = dom.getElementValue(refGroupElt, "Abstract");
		refGroup.setDescription(description);
		
		// identifier
		String identifier = dom.getElementValue(refGroupElt, "Identifier");
		refGroup.setIdentifier(identifier);
	}
}