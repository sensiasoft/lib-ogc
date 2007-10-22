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

package org.vast.ows.wcs;

import org.vast.ows.AbstractCapabilitiesReader;
import org.vast.ows.OWSException;


/**
 * <p><b>Title:</b><br/>
 * WCS Capabilities Reader
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Abstract WCS Capabilities Reader containing common code
 * for all versions
 * </p>
 *
 * <p>Copyright (c) 2006</p>
 * @author Alexandre Robin
 * @date Dec 20, 2006
 * @version 1.0
 */
public abstract class WCSCapabilitiesReader extends AbstractCapabilitiesReader
{

    @Override
    protected String buildQuery() throws OWSException
    {
        String query = null;
        query = this.server + "SERVICE=WCS&VERSION=" + version + "&REQUEST=GetCapabilities"; 
        return query;
    }
}
