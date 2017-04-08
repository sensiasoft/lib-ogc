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

package org.vast.ows.sld;


/**
 * <p>
 * SLD enumeration constants
 * </p>
 *
 * @author Alex Robin <alex.robin@sensiasoftware.com>
 * @date Nov 11, 2005
 * */
public interface SLD
{
	// Line Join Types
	public enum JoinType
	{
		JOIN_MITRE,
		JOIN_ROUND,
		JOIN_BEVEL,
	};
	
	// Line Cap Types
	public enum CapType
	{
		CAP_BUTT,
		CAP_SQUARE,
		CAP_ROUND
	};
	
	// Mark Shapes
	public enum MarkShape
	{
		MARK_SQUARE,
		MARK_CIRCLE,
		MARK_TRIANGLE, 
		MARK_STAR,
		MARK_CROSS,
		MARK_X
	};
	
	// Font Styles
	public enum Font
	{
		OBLIQUE,
		NORMAL,
		ITALIC,
		BOLD
	};
}
