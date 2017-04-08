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

package org.vast.ows.swe;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import javax.xml.namespace.QName;
import org.vast.ogc.OGCRegistry;
import org.vast.ows.OWSUtils;
import org.vast.sensorML.SMLUtils;
import org.vast.util.DateTimeFormat;
import org.vast.xml.DOMHelper;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;


/**
 * <p>
 * Utility methods to read SWES common structures
 * </p>
 *
 * @author Alex Robin
 * @date 22 jun. 10
 * */
public class SWESUtils extends OWSUtils
{
    public final static String DEFAULT_PROCEDURE_FORMAT;
    
    static
    {
        SMLUtils.loadRegistry();
        DEFAULT_PROCEDURE_FORMAT = OGCRegistry.getNamespaceURI(SMLUtils.SENSORML, "2.0");
    }
    
    
	public static Map<QName, Object> readXMLExtensions(DOMHelper dom, Element parentElt)
	{
		NodeList extensions = dom.getElements(parentElt, "extension");
		Map<QName, Object> extList = new HashMap<QName, Object>();
		
		for (int i=0; i<extensions.getLength(); i++)
		{
			Element extElt = (Element)extensions.item(i);
			Element contentElt = dom.getFirstChildElement(extElt);
			contentElt = (Element)extElt.removeChild(contentElt);
			QName extName = new QName(contentElt.getNamespaceURI(), contentElt.getLocalName()); 
			extList.put(extName, contentElt);
		}
		
		return extList;
	}
	
	
	public static void writeXMLExtensions(DOMHelper dom, Element parentElt, String prefix, String nsUri, Map<QName, Object> extObjs)
	{
		if (extObjs == null)
			return;
		
		dom.addUserPrefix(prefix, nsUri);
    	for (Entry<QName, Object> extObj: extObjs.entrySet())
        {
        	Element extContent = null;
        	QName extName = extObj.getKey();
        	Object extValue = extObj.getValue();
        	
			if (extValue instanceof Element)
        	{
        		extContent = (Element)extObj;
        		dom.getDocument().adoptNode(extContent);
        	}
			else if (extValue instanceof String || extValue instanceof Number || extValue instanceof Boolean)
	        {
			    extContent = dom.getDocument().createElementNS(extName.getNamespaceURI(), extName.getLocalPart());
			    dom.setElementValue(extContent, extValue.toString());
	        }
	        else if (extValue instanceof Date)
	        {
	            extContent = dom.getDocument().createElementNS(extName.getNamespaceURI(), extName.getLocalPart());
	            dom.setElementValue(extContent, new DateTimeFormat().formatIso(((Date)extValue).getTime() / 1000.0, 0));
	        }
			
			if (extContent != null)
			{
			    Element extElt = dom.createElement(prefix + ":extension");
                extElt.appendChild(extContent);
                parentElt.appendChild(extElt);
			}
        }
	}
	
	
	public static void writeXMLExtensions(DOMHelper dom, Element parentElt, String version, Map<QName, Object> extObjs)
	{
		String swesUri = OGCRegistry.getNamespaceURI(OWSUtils.SWES, version);
		writeXMLExtensions(dom, parentElt, "swes", swesUri, extObjs);
	}
	
	
	public static Object findExtension(String nsUri, String localName, Map<QName, Object> extObjs)
	{
		QName qname = new QName(nsUri, localName);
		Object obj = extObjs.get(qname);
		return obj;
	}
	
	
	public static void addExtension(String nsUri, String localName, Object ext, Map<QName, Object> extObjs)
	{
		QName qname = new QName(nsUri, localName);
		extObjs.put(qname, ext);
	}
}