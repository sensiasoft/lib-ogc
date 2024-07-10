/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are subject to the Mozilla Public License Version
 1.1 (the "License"); you may not use this file except in compliance with
 the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/MPL-1.1.html
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.
 
 The Initial Developer of the Original Code is SENSIA SOFTWARE LLC.
 Portions created by the Initial Developer are Copyright (C) 2012
 the Initial Developer. All Rights Reserved.

 Please Contact Alexandre Robin for more
 information.
 
 Contributor(s): 
    Alexandre Robin
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ogc.xlink;

import java.io.IOException;
import org.vast.ogc.OGCRegistry;
import org.vast.xml.DOMHelper;
import org.w3c.dom.Element;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import net.opengis.OgcProperty;


public class XlinkUtils
{
    private final static String NS_URI = OGCRegistry.getNamespaceURI(OGCRegistry.XLINK);
        
        
    public static void readXlinkAttributes(DOMHelper dom, Element propertyElt, IXlinkReference<?> refObj)
    {
        String href = dom.getAttributeValue(propertyElt, "href");
        refObj.setHref(href);
        
        String role = dom.getAttributeValue(propertyElt, "role");
        refObj.setRole(role);
        
        String arcRole = dom.getAttributeValue(propertyElt, "arcrole");
        refObj.setArcRole(arcRole);
    }
    
    
    public static void writeXlinkAttributes(DOMHelper dom, Element propertyElt, IXlinkReference<?> refObj)
    {
        dom.addUserPrefix("xlink", NS_URI);
        
        if (refObj.getHref() != null && !refObj.getHref().isEmpty())
            dom.setAttributeValue(propertyElt, "xlink:href", refObj.getHref());
        
        if (refObj.getRole() != null && !refObj.getRole().isEmpty())
            dom.setAttributeValue(propertyElt, "xlink:role", refObj.getRole());
        
        if (refObj.getArcRole() != null && !refObj.getArcRole().isEmpty())
            dom.setAttributeValue(propertyElt, "xlink:arcrole", refObj.getArcRole());
    }
    
    
    /**
     * Write link to JSON using 
     * @param writer
     * @param link
     * @throws IOException
     */
    public static void writeLink(JsonWriter writer, IXlinkReference<?> link) throws IOException
    {
        if (link.getHref() != null)
        {
            writer.beginObject();

            if (link instanceof OgcProperty)
            {
                var name = ((OgcProperty<?>)link).getName();
                if (name != null)
                    writer.name("name").value(name);
            }

            if (link.getHref() != null)
                writer.name("href").value(link.getHref());
            
            if (link.getArcRole() != null)
                writer.name("rel").value(link.getArcRole());
            
            if (link.getTitle() != null)
                writer.name("title").value(link.getTitle());
            
            if (link.getTargetUID() != null)
                writer.name("uid").value(link.getTargetUID());
            
            if (link.getMediaType() != null)
                writer.name("type").value(link.getMediaType());
            
            if (link.getRole() != null)
                writer.name("rt").value(link.getRole());
            
            if (link.getTargetInterface() != null)
                writer.name("if").value(link.getTargetInterface());
            
            writer.endObject();
        }
    }
    
    
    public static <R extends IXlinkReference<?>> R readLink(JsonReader reader, R link) throws IOException
    {
        if (reader.peek() == JsonToken.BEGIN_OBJECT)
            reader.beginObject();
        
        while (reader.hasNext())
        {
            String name = reader.nextName();
            
            if ("name".equals(name) && link instanceof OgcProperty)
                ((OgcProperty<?>)link).setName(reader.nextString());
            else if ("href".equals(name))
                link.setHref(reader.nextString());
            else if ("rel".equals(name))
                link.setArcRole(reader.nextString());
            else if ("title".equals(name))
                link.setTitle(reader.nextString());
            else if ("uid".equals(name))
                link.setTargetUID(reader.nextString());
            else if ("type".equals(name))
                link.setMediaType(reader.nextString());
            else if ("rt".equals(name))
                link.setRole(reader.nextString());
            else if ("if".equals(name))
                link.setTargetInterface(reader.nextString());
            else
                reader.skipValue();
        }
        reader.endObject();
        
        return link;
    }
    
    
    public static <R extends IXlinkReference<?>> R readLink(JsonObject obj, R link)
    {
        if (obj.has("href"))
            link.setHref(obj.get("href").getAsString());
        
        if (obj.has("rel"))
            link.setArcRole(obj.get("rel").getAsString());
        
        if (obj.has("title"))
            link.setTitle(obj.get("title").getAsString());
        
        if (obj.has("uid"))
            link.setTargetUID(obj.get("uid").getAsString());
        
        if (obj.has("type"))
            link.setMediaType(obj.get("type").getAsString());
            
        if (obj.has("rt"))
            link.setRole(obj.get("rt").getAsString());
        
        if (obj.has("if"))
            link.setTargetInterface(obj.get("if").getAsString());
        
        return link;
    }
}
