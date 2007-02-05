/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are subject to the Mozilla Public License Version
 1.1 (the "License"); you may not use this file except in compliance with
 the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/MPL-1.1.html
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.
 
 The Original Code is the "SensorML DataProcessing Engine".
 
 The Initial Developer of the Original Code is the
 University of Alabama in Huntsville (UAH).
 Portions created by the Initial Developer are Copyright (C) 2006
 the Initial Developer. All Rights Reserved.
 
 Contributor(s): 
    Alexandre Robin <robin@nsstc.uah.edu>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ows.wrs;

import java.util.List;
import org.vast.ows.OWSQuery;
import org.vast.ows.util.Bbox;


/**
 * <p><b>Title:</b><br/>
 *  WRSQuery
 * </p>
 *
 * <p><b>Description:</b><br/>
 * </p>
 *
 * <p>Copyright (c) 2005</p>
 * @author Tony Cook
 * @date Nov 26, 2006
 * @version 1.0
 */
public class WRSQuery extends OWSQuery
{
    protected final static String unsupportedVersion = "Unsupported WRS version";
    
    protected String serverUrl = "http://dev.ionicsoft.com:8082/ows4catalog/wrs/WRS";
	protected String keyword;
	protected Bbox bbox;
	protected String serviceSearchId;
	protected List<QueryType> queryTypeList;
	public enum QueryType { 
		BBOX_SOS, KEYWORD_SOS, PROVIDER_SOS, LAYER_SOS, SERVICE_SOS
	};
	
	public WRSQuery()
	{
		service = "WRS";
	}

	public Bbox getBbox() {
		return bbox;
	}

	public void setBbox(Bbox bbox) {
		this.bbox = bbox;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public List<QueryType> getQueryTypeList() {
		return queryTypeList;
	}

	public void setQueryTypeList(List<QueryType> queryType) {
		this.queryTypeList = queryType;
	}

	public String getServiceSearchId() {
		return serviceSearchId;
	}

	public void setServiceSearchId(String serviceSearchId) {
		this.serviceSearchId = serviceSearchId;
	}

}
