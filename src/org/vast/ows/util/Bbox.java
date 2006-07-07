/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are subject to the Mozilla Public License Version
 1.1 (the "License"); you may not use this file except in compliance with
 the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/MPL-1.1.html
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.
 
 The Original Code is the "SensorML DataProcessing Engine".
 
 The Initial Developer of the Original Code is the
 University of Alabama in Huntsville (UAH).
 Portions created by the Initial Developer are Copyright (C) 2006
 the Initial Developer. All Rights Reserved.
 
 Contributor(s): 
    Tony Cook <tcook@nsstc.uah.edu>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ows.util;

/**
* <p>Title: Bbox</p>
*
* <p>Description:
* Simple structure for OGC-style bbox info.  This avoids the need to import 
* stt.Data.Geographically) BoundedRegion (and stt.jar)
* </p>
*
* <p>Copyright: Copyright (c) 2005</p>
* @author Tony Cook
* @since Aug 16, 2005
* @version 1.0
*/
public class Bbox {
	protected double minX;
	protected double maxX;
	protected double minY;
	protected double maxY;

	public double getMaxX() {
		return maxX;
	}
	public void setMaxX(double maxX) {
		this.maxX = maxX;
	}
	public double getMaxY() {
		return maxY;
	}
	public void setMaxY(double maxY) {
		this.maxY = maxY;
	}
	public double getMinX() {
		return minX;
	}
	public void setMinX(double minX) {
		this.minX = minX;
	}
	public double getMinY() {
		return minY;
	}
	public void setMinY(double minY) {
		this.minY = minY;
	}
	
	public Bbox copy()
	{
		Bbox bbox = new Bbox();
		bbox.setMinX(this.minX);
		bbox.setMinY(this.minY);
		bbox.setMaxX(this.maxX);
		bbox.setMaxY(this.maxY);
		return bbox;
	}
}
