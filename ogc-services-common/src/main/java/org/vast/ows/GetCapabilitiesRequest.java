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
    Tony Cook <tcook@nsstc.uah.edu>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ows;

import java.util.HashSet;
import java.util.Set;


/**
 * <p>
 * Container for GetCapabilities query parameters
 * </p>
 *
 * @author Alex Robin
 * @since Sep 21, 2007
 * */
public class GetCapabilitiesRequest extends OWSRequest
{
    protected String section;
    protected Set<String> acceptedVersions;
	
    
    public GetCapabilitiesRequest()
    {
        service = "OWS";
        operation = "GetCapabilities";
        acceptedVersions = new HashSet<String>();
    }


	public String getSection()
	{
		return section;
	}


	public void setSection(String section)
	{
		this.section = section;
	}


    public Set<String> getAcceptedVersions()
    {
        return acceptedVersions;
    }
}
