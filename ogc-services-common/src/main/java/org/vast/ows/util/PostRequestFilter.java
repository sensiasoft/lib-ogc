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

package org.vast.ows.util;

import java.io.*;


/**
 * <p>
 * Filter used to receive POST request from a HTML FORM.
 * This removes all characters before the first '<'
 * </p>
 *
 * @author Alex Robin
 * @since Aug 9, 2005
 * */
public class PostRequestFilter extends FilterInputStream
{
	public PostRequestFilter(InputStream in)
	{
		super(in);
	
		try
		{
			char val;			
			do
			{
				in.mark(1);
				val = (char)in.read();
			}
			while (val != '<' && val != -1);
			
			in.reset();
		}
		catch (IOException e)
		{
			throw new IllegalStateException("Error in POST filter", e);
		}
	}
}
