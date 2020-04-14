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
 
 Contributor(s): 
    Alexandre Robin <robin@nsstc.uah.edu>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ows.wps;

import org.vast.ows.wps.ExecuteProcessRequest;
import org.vast.ows.wps.DescribeProcessRequest;



/**
 * <p>
 * Interface for an WPS dataset (process offering) handler
 * </p>
 *
 * @author Gregoire Berthiau
 * @since Dec 3, 2008
 * */
public interface WPSHandler
{
	public abstract void executeProcess(ExecuteProcessRequest request) throws Exception;
	public abstract void describeProcess(DescribeProcessRequest request) throws Exception;
}