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
 University of Alabama in Huntsville (UAH). <http://vast.uah.edu>
 Portions created by the Initial Developer are Copyright (C) 2007
 the Initial Developer. All Rights Reserved.

 Please Contact Mike Botts <mike.botts@uah.edu> for more information.
 
 Contributor(s): 
    Alexandre Robin <alexandre.robin@spotimage.fr>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ows.wcs;

import org.vast.ows.OWSException;


/**
 * <p><b>Title:</b><br/>
 * WCS Common Reader V1.1
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Utility method for common WCS objects 
 * </p>
 *
 * <p>Copyright (c) 2007</p>
 * @author Alexandre Robin <alexandre.robin@spotimage.fr>
 * @date 28 nov. 07
 * @version 1.0
 */
public class WCSUtils
{
	/**
     * Utility method to parse a list of vectors from flat list of comma/space separated decimal values
     * @param argValue
     * @return
     */
    protected static double[][] parseGridOffsets(String vectorText) throws OWSException
    {
    	try
        {
    		String[] elts = vectorText.trim().split("[ ,]");
    		int gridDim = (int)Math.sqrt(elts.length);
    		double[][] vec = new double[gridDim][gridDim];
	    	
    		int index = 0;
    		for (int i=0; i<gridDim; i++)
    			for (int j=0; j<gridDim; j++)
    				vec[i][j] = Double.parseDouble(elts[index++]);
    			
	    	return vec;
        }
        catch (NumberFormatException e)
        {
            throw new OWSException("Invalid Grid Offsets: " + vectorText, e);
        }
    }
}
