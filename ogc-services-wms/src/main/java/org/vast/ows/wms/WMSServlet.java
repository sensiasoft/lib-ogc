/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License Version
1.1 (the "License"); you may not use this file except in compliance with
the License. You may obtain a copy of the License at
http://www.mozilla.org/MPL/MPL-1.1.html

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.

The Original Code is the "OGC Service Framework".

The Initial Developer of the Original Code is the VAST team at the University
of Alabama in Huntsville (UAH). <http://vast.uah.edu> Portions created by the
Initial Developer are Copyright (C) 2007 the Initial Developer. All Rights Reserved.
Please Contact Mike Botts <mike.botts@uah.edu> for more information.

Contributor(s):
Mathieu Dhainaut <mathieu.dhainaut@ext.spotimage.fr>
Alexandre Robin <alexandre.robin@spotimage.fr>

 ******************************* END LICENSE BLOCK ***************************/

package org.vast.ows.wms;

import org.vast.ows.OWSException;
import org.vast.ows.OWSRequest;
import org.vast.ows.OWSUtils;
import org.vast.ows.server.OWSServlet;
import org.vast.ows.wms.GetMapRequest;


/**
 * <p>Title: WMSServlet</p>
 *
 * <p>Description:
 * Base abstract class for implementing WMS servlets
 * </p>
 *
 * @author Mathieu Dhainaut
 * */
public abstract class WMSServlet extends OWSServlet
{
	private static final long serialVersionUID = 1265879257871196681L;
	protected OWSUtils owsUtils = new OWSUtils();
	
	
	@Override
    public void handleRequest(OWSRequest request) throws OWSException
    {
        if (request instanceof GetMapRequest)
        {
            processQuery((GetMapRequest) request);
        }
        else
            throw new OWSException("Unsupported operation " + request.getOperation());
    }


    public abstract void processQuery(GetMapRequest request) throws OWSException;
}
