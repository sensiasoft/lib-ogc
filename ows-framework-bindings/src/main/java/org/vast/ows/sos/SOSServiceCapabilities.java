/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License Version
1.1 (the "License"); you may not use this file except in compliance with
the License. You may obtain a copy of the License at
http://www.mozilla.org/MPL/MPL-1.1.html

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.

The Original Code is the "OGC Service Framework".

The Initial Developer of the Original Code is Spotimage S.A.
Portions created by the Initial Developer are Copyright (C) 2007
the Initial Developer. All Rights Reserved.

Contributor(s): 
   Alexandre Robin <alexandre.robin@spotimage.fr>

******************************* END LICENSE BLOCK ***************************/

package org.vast.ows.sos;

import org.vast.ows.OWSServiceCapabilities;


/**
 * <p><b>Description:</b><br/>
 * Representation of SOS service capabilities document.
 * </p>
 *
 * <p>Copyright (c) 2013</p>
 * @author Alexandre Robin <alex.robin@sensiasoftware.com>
 * @date Sep 15, 2013
 */
public class SOSServiceCapabilities extends OWSServiceCapabilities
{
    public static String PROFILE_RESULT_RETRIEVAL = "http://www.opengis.net/spec/SOS/2.0/conf/resultRetrieval";
    public static String PROFILE_OBS_INSERTION = "http://www.opengis.net/spec/SOS/2.0/conf/obsInsertion";
    public static String PROFILE_RESULT_INSERTION = "http://www.opengis.net/spec/SOS/2.0/conf/resultInsertion";  
    
    SOSInsertionCapabilities insertionCapabilities;
	
	
	public SOSServiceCapabilities()
	{
	    this.service = "SOS";
	}


    public SOSInsertionCapabilities getInsertionCapabilities()
    {
        return insertionCapabilities;
    }


    public void setInsertionCapabilities(SOSInsertionCapabilities insertionCapabilities)
    {
        this.insertionCapabilities = insertionCapabilities;
    }
	
}
