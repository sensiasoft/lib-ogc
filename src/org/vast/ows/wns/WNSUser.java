/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are subject to the Mozilla Public License Version
 1.1 (the "License"); you may not use this file except in compliance with
 the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/MPL-1.1.html
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.
 
 The Original Code part of the "OGC Service Framework".
 
 The Initial Developer of the Original Code is Spotimage S.A.
 Portions created by the Initial Developer are Copyright (C) 2007
 the Initial Developer. All Rights Reserved.
 
 Contributor(s): 
    Alexandre Robin <alexandre.robin@spotimage.fr>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ows.wns;


/**
 * <p><b>Title:</b><br/>
 * WNS User
 * </p>
 *
 * <p><b>Description:</b><br/>
 * 
 * </p>
 *
 * <p>Copyright (c) 2007</p>
 * @author Alexandre Robin <alexandre.robin@spotimage.fr>
 * @date Jan 16, 2008
 * @version 1.0
 */
public abstract class WNSUser
{
	public final static String XMPP = "XMPP";
    public final static String HTTP = "HTTP";
    public final static String EMAIL = "Email";
    public final static String PHONE = "Phone";
    public final static String FAX = "Fax";
    public final static String SMS = "SMS";
    
	protected String id;
	protected String name;


	public String getId()
	{
		return id;
	}


	public void setId(String id)
	{
		this.id = id;
	}


	public String getName()
	{
		return name;
	}


	public void setName(String name)
	{
		this.name = name;
	}
}
