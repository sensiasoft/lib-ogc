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

package org.vast.ows.wcs;

import org.vast.ows.OWSReference;
import org.vast.ows.OWSReferenceGroup;


public class CoverageRefGroup extends OWSReferenceGroup
{
	public final static String ROLE_COV_PIXELS = "urn:ogc:def:role:WCS:1.1:Pixels";
	public final static String ROLE_COV_METADATA = "urn:ogc:def:role:WCS:1.1:CoverageMetadata";	
	public final static String ROLE_COV_DESCRIPTION = "urn:ogc:def:role:WCS:1.1:CoverageDescription";
	public final static String ROLE_COV_TRANSFORMATION = "urn:ogc:def:role:WCS:1.1:GeoreferencingTransformation";


	public OWSReference getReference(String role)
	{
		for (int i=0; i<referenceList.size(); i++)
		{
			OWSReference nextReference = referenceList.get(i);
			if (nextReference.getRole().equals(role))
				return nextReference;
		}
		
		return null;
	}
}
