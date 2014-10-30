/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are subject to the Mozilla Public License Version
 1.1 (the "License"); you may not use this file except in compliance with
 the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/MPL-1.1.html
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.
 
 The Original Code is the "SensorML DataProcessing Engine".
 
 The Initial Developer of the Original Code is the VAST team at the University of Alabama in Huntsville (UAH). <http://vast.uah.edu> Portions created by the Initial Developer are Copyright (C) 2007 the Initial Developer. All Rights Reserved. Please Contact Mike Botts <mike.botts@uah.edu> for more information.
 
 Contributor(s): 
    Alexandre Robin <robin@nsstc.uah.edu>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.cdm.common;

import org.vast.xml.DOMHelper;
import org.vast.xml.XMLWriterException;
import org.w3c.dom.*;


/**
 * <p>
 * Concrete implementations of this interface are responsible for
 * creating an XML element containing a data components structure
 * using info specified in the DataComponent object.  
 * </p>
 *
 * <p>Copyright (c) 2007</p>
 * @author Alexandre Robin
 * @since Feb 03, 2006
 * @version 1.0
 */
public interface DataComponentWriter
{
	
    /**
     * Creates a DOM Element from a DataComponent object
     * @param dom
     * @param dataComponents
     * @return new element
     * @throws XMLWriterException
	 */
    public Element writeComponent(DOMHelper dom, DataComponent dataComponents) throws XMLWriterException;
    
    
    /**
     * Creates a DOM Element from a DataComponent object
     * with the option of writing inline data in all components of the tree
     * @param dom
     * @param dataComponents
     * @param writeInlineData
     * @return new element
     * @throws XMLWriterException
     */
    public Element writeComponent(DOMHelper dom, DataComponent dataComponents, boolean writeInlineData) throws XMLWriterException;
}
