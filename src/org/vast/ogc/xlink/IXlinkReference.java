/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are subject to the Mozilla Public License Version
 1.1 (the "License"); you may not use this file except in compliance with
 the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/MPL-1.1.html
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.
 
 The Initial Developer of the Original Code is SENSIA SOFTWARE LLC.
 Portions created by the Initial Developer are Copyright (C) 2012
 the Initial Developer. All Rights Reserved.

 Please Contact Alexandre Robin <alex.robin@sensiasoftware.com> for more
 information.
 
 Contributor(s): 
    Alexandre Robin <alex.robin@sensiasoftware.com>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ogc.xlink;


/**
 * <p><b>Title:</b>
 * IXlinkReference
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Interface for an xlink reference
 * </p>
 *
 * <p>Copyright (c) 2012</p>
 * @author Alexandre Robin
 * @date Sep 28, 2012
 * @version 1.0
 */
public interface IXlinkReference<TargetType>
{
    public String getHref();
    
    public void setHref(String href);
    
    public String getRole();
    
    public void setRole(String role);
    
    public String getArcRole();
    
    public void setArcRole(String arcRole);
    
    public TargetType getTarget();
}
