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
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ows;


/**
 * <p><b>Title:</b><br/>
 * OWS Layer Capabilities
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Abstract base class for all objects representing OWS 
 * capabilities document. Sepcific services should derive
 * their LayerCapabilities object from this base class.
 * </p>
 *
 * <p>Copyright (c) 2007</p>
 * @author Alexandre Robin
 * @date Oct 31, 2005
 * @version 1.0
 */
public abstract class OWSLayerCapabilities
{
	protected OWSServiceCapabilities parent;
	protected String id;
	protected String name;
	protected String description;	
	    

    public OWSLayerCapabilities()
    {    	
    }
    
    
	public String getDescription()
	{
		return description;
	}


	public void setDescription(String description)
	{
		this.description = description;
	}


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


	public OWSServiceCapabilities getParent()
	{
		return parent;
	}


	public void setParent(OWSServiceCapabilities parent)
	{
		this.parent = parent;
	}
	
	
	public String toString()
    {
    	return name;
    }
}
