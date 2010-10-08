/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are subject to the Mozilla Public License Version
 1.1 (the "License"); you may not use this file except in compliance with
 the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/MPL-1.1.html
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.
 
 The Original Code is the "OGC Service Framework".
 
 The Initial Developer of the Original Code is the VAST team at the
 University of Alabama in Huntsville (UAH). <http://vast.uah.edu>
 Portions created by the Initial Developer are Copyright (C) 2007
 the Initial Developer. All Rights Reserved.
 
 Contributor(s): 
    Alexandre Robin <alexandre.robin@spotimage.fr>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ows.sps;

import org.vast.ogc.OGCRegistry;
import org.vast.ows.OWSReference;
import org.vast.xml.DOMHelper;
import org.w3c.dom.Element;


/**
 * <p><b>Title:</b><br/>
 * DescribeResultAccessReference
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Subclass of OWSReference specific for SPS DescribeResultAccessResponse.
 * It adds the ability to append an SPSMetadata object to specify the type
 * of data access operation or service to be used to retrieve task data.
 * </p>
 *
 * <p>Copyright (c) 2007</p>
 * @author Alexandre Robin <alexandre.robin@spotimage.fr>
 * @date 8 oct. 2010
 * @version 1.0
 */
public class DescribeResultAccessReference extends OWSReference
{
	
	public void setDataAccessType(String dataAccessType)
	{
		Element spsMetaElt = findSPSMetadata();
		
		// remove it if already set
		if (spsMetaElt == null)
			this.getMetadata().remove(spsMetaElt);
			
		// create SPSMetadata
		DOMHelper dom = new DOMHelper();
		dom.addUserPrefix("sps", OGCRegistry.getNamespaceURI(SPSUtils.SPS, "2.0"));
		spsMetaElt = dom.createElement("sps:SPSMetadata");
		dom.setElementValue(spsMetaElt, "sps:dataAccessType", dataAccessType);
		this.getMetadata().add(spsMetaElt);
	}
	
	
	public String getDataAccessType()
	{
		Element spsMetaElt = findSPSMetadata();
		
		if (spsMetaElt == null)
			return null;
		else
		{
			DOMHelper dom = new DOMHelper();
			return dom.getElementValue(spsMetaElt, "dataAccessType");
		}
	}
	
	
	protected Element findSPSMetadata()
	{
		for (Object obj: this.getMetadata())
			if (obj instanceof Element && ((Element)obj).getLocalName().equals("SPSMetadata"))
				return (Element)obj;
		
		return null;
	}
}
