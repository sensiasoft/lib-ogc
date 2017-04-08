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
 * <p>
 * Subclass of OWSReference specific for SPS DescribeResultAccessResponse.
 * It adds the ability to append an SPSMetadata object to specify the type
 * of data access operation or service to be used to retrieve task data.
 * </p>
 *
 * @author Alex Robin
 * @date 8 oct. 2010
 * */
public class DescribeResultAccessReference extends OWSReference
{
	public static final String ROLE_RESOURCE = "http://www.opengis.net/spec/SPS/2.0/referenceType/Resource";
	public static final String ROLE_FOLDER = "http://www.opengis.net/spec/SPS/2.0/referenceType/Folder";
	public static final String ROLE_SERVICE_KVP = "http://www.opengis.net/spec/SPS/2.0/referenceType/FullURLAccess";
	public static final String ROLE_SERVICE_POST = "http://www.opengis.net/spec/SPS/2.0/referenceType/FullServiceAccess";
	public static final String ROLE_SERVICE_ENDPOINT = "http://www.opengis.net/spec/SPS/2.0/referenceType/ServiceURL";
	
	
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
