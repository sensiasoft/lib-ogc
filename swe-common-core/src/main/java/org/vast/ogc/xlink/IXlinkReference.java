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

 Please Contact Alexandre Robin for more
 information.
 
 Contributor(s): 
    Alexandre Robin
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ogc.xlink;


/**
 * <p>
 * Interface for an xlink reference. It is used to implement all types of links
 * (e.g. web linking in JSON)
 * @param <T> Type of the link target object
 * </p>
 *
 * @author Alex Robin
 * @since Sep 28, 2012
 * */
public interface IXlinkReference<T>
{
    public String getHref();
    
    public void setHref(String href);
    
    public String getRole();
    
    public void setRole(String role);
    
    public String getArcRole();
    
    public void setArcRole(String arcRole);
    
    public String getTitle();

    public void setTitle(String title);
    
    public String getMediaType();

    public void setMediaType(String mediaType);
    
    public String getTargetUID();

    public void setTargetUID(String uid);
    
    public String getTargetInterface();

    public void setTargetInterface(String type);
    
    public T getTarget();
}
