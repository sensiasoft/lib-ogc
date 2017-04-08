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
    Alexandre Robin <alexandre.robin@spotimage.fr>
 
 ******************************* END LICENSE BLOCK ***************************/

package org.vast.ows;

import java.util.ArrayList;


/**
 * <p>
 * Container for common OWS Identification info
 * </p>
 *
 * @author Alex Robin
 * @since Oct 11, 2007
 * */
public class OWSIdentification
{
	protected String title;
	protected String description;
	protected String identifier;
	protected ArrayList<String> keywords;
	protected ArrayList<Object> metadata;


	public OWSIdentification()
	{
		keywords = new ArrayList<String>(5);
		metadata = new ArrayList<Object>(1);
	}
	
	
	public String getTitle()
	{
		return title;
	}


	public void setTitle(String title)
	{
		this.title = title;
	}


	public String getDescription()
	{
		return description;
	}


	public void setDescription(String description)
	{
		this.description = description;
	}


	public String getIdentifier()
	{
		return identifier;
	}


	public void setIdentifier(String identifier)
	{
		this.identifier = identifier;
	}


	public ArrayList<String> getKeywords()
	{
		return keywords;
	}


	public ArrayList<Object> getMetadata()
	{
		return metadata;
	}
}
