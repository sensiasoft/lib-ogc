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

package org.vast.ows.wcst;

import org.vast.xml.DOMHelper;
import org.vast.xml.QName;
import org.w3c.dom.*;
import org.vast.ogc.OGCRegistry;
import org.vast.ows.*;
import org.vast.ows.wcs.WCSException;


/**
 * <p><b>Title:</b><br/>
 * WCS Transaction Writer V1.1.1
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Writer to generate an WCS-T Transaction for version 1.1.1 based
 * on values contained in a WCSTransactionRequest object. 
 * </p>
 *
 * <p>Copyright (c) 2007</p>
 * @author Alexandre Robin <alexandre.robin@spotimage.fr>
 * @date 22 nov. 07
 * @version 1.0
 */
public class WCSTransactionWriterV11 extends AbstractRequestWriter<WCSTransactionRequest>
{
	protected OWSReferenceWriterV11 owsWriter = new OWSReferenceWriterV11();
	
	
	@Override
	public String buildURLQuery(WCSTransactionRequest request) throws OWSException
	{
		throw new WCSException(noKVP + "WCS 1.1 Transaction");
	}


	@Override
	public Element buildXMLQuery(DOMHelper dom, WCSTransactionRequest request) throws OWSException
	{
		dom.addUserPrefix(QName.DEFAULT_PREFIX, OGCRegistry.getNamespaceURI(OGCRegistry.WCS, request.getVersion()));
		
		// root element
		Element rootElt = dom.createElement("Transaction");
		addCommonXML(dom, rootElt, request);
		Element inputElt = dom.addElement(rootElt, "InputCoverages");
		
		// add all coverage briefs
		for (int i=0; i<request.getInputCoverages().size(); i++)
		{
			Element coverageElt = dom.addElement(inputElt, "+Coverage");
			OWSReferenceGroup coverageInfo = request.getInputCoverages().get(i);
			owsWriter.buildRefGroupXML(dom, coverageElt, coverageInfo);
		}
		
		return rootElt;
	}
}