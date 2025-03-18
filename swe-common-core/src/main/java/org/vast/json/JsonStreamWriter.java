/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.
 
Copyright (C) 2012-2016 Sensia Software LLC. All Rights Reserved.
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.json;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.Iterator;
import javax.xml.namespace.NamespaceContext;
import javax.xml.stream.XMLStreamWriter;
import org.vast.util.NumberUtils;
import com.google.common.base.Strings;
import com.google.gson.Strictness;
import com.google.gson.stream.JsonWriter;


/**
 * <p>
 * Stream writer implementation for serializing SWE metadata as JSON
 * </p>
 *
 * @author Alex Robin
 * @since Jun 4, 2016
 */
public class JsonStreamWriter implements XMLStreamWriter, JsonConstants
{
    final static String INDENT1 = "  ";

    protected JsonWriter writer;
    protected JsonContext currentContext = new JsonContext();
    protected boolean indent;
    protected int indentSize = 0;
    protected boolean markAttributes = false;

    protected static class JsonContext
    {
        public JsonContext parent;
        public String eltName;
        public boolean firstChild = true;
        public boolean skipParent = false;
        public boolean isArray = false;
        public boolean emptyElement = true;
    }


    public JsonStreamWriter(OutputStream os, Charset charset)
    {
        this.writer = new JsonWriter(new OutputStreamWriter(os, charset));
        this.writer.setStrictness(Strictness.LENIENT);
        this.writer.setSerializeNulls(true);
        this.writer.setIndent(INDENT1);
    }
    
    
    public JsonStreamWriter(JsonWriter writer)
    {
        this.writer = writer;
    }
    
    
    public void resetContext()
    {
        currentContext.firstChild = true;
    }
    
    
    public void beginArray() throws IOException
    {
        writer.beginArray();
    }
    
    
    public void endArray() throws IOException
    {
        writer.endArray();
        writer.flush();
    }


    protected boolean isArray(String namespaceURI, String localName)
    {
        return false;
    }
    
    
    protected String getPluralName(String localName)
    {
        if (localName.endsWith("s"))
            return localName;
        else
            return localName + "s";
    }


    protected boolean isObjectElement(String namespaceURI, String localName)
    {
        return false;
    }


    protected boolean isValueArray(String namespaceURI, String localName)
    {
        return false;
    }
    
    
    protected boolean isNumericValue(String value)
    {
        return NumberUtils.isNumericExp(value);
    }


    protected void pushContext(String eltName)
    {
        JsonContext prevContext = currentContext;
        currentContext = new JsonContext();
        currentContext.parent = prevContext;
        currentContext.eltName = eltName;
    }


    protected void popContext()
    {
        currentContext = currentContext.parent;
    }


    @Override
    public void writeStartDocument() throws JsonStreamException
    {
    }


    @Override
    public void writeStartDocument(String version) throws JsonStreamException
    {
        writeStartDocument();
    }


    @Override
    public void writeStartDocument(String encoding, String version) throws JsonStreamException
    {
        writeStartDocument();
    }


    @Override
    public void writeEndDocument() throws JsonStreamException
    {
    }


    protected void prepareAppendToObject() throws IOException
    {
        if (currentContext.firstChild)
        {
            writer.beginObject();
            currentContext.firstChild = false;
        }
    }


    protected void closeArray() throws IOException
    {
        writer.endArray();
        writer.setIndent(INDENT1); // in case we were writing array inline
        popContext();
    }


    @Override
    public void writeStartElement(String prefix, String localName, String namespaceURI) throws JsonStreamException
    {
        try
        {
            // need to close current array if element name changes
            if (currentContext.isArray)
            {
                if (!localName.equals(currentContext.eltName))
                    closeArray();
            }

            prepareAppendToObject();
            currentContext.emptyElement = false;

            // special case for object elements: use 'type' in JSON
            if (isObjectElement(namespaceURI, localName))
            {
                writer.name(OBJECT_TYPE_PROPERTY);
                writer.value(localName);
                boolean skipParent = currentContext.parent != null;
                pushContext(localName);
                currentContext.firstChild = false;
                currentContext.skipParent = skipParent;
            }
            else
            {
                if (!currentContext.isArray)
                {
                    // start JSON array if element has multiplicity > 1
                    if (isArray(namespaceURI, localName))
                    {
                        writer.name(getPluralName(localName));
                        pushContext(localName);
                        writer.beginArray();
                        currentContext.firstChild = false;
                        currentContext.isArray = true;
                    }
                    else
                    {
                        writer.name(localName);
                    }
                }
                
                pushContext(localName);
            }
        }
        catch (IOException e)
        {
            throw new JsonStreamException("Error starting JSON object " + localName, e);
        }
    }


    @Override
    public void writeStartElement(String namespaceURI, String localName) throws JsonStreamException
    {
        writeStartElement(null, localName, namespaceURI);
    }


    @Override
    public void writeStartElement(String localName) throws JsonStreamException
    {
        writeStartElement(null, localName, null);
    }


    @Override
    public void writeEmptyElement(String localName) throws JsonStreamException
    {
        try
        {
            writer.name(localName);
            writer.nullValue();
        }
        catch (IOException e)
        {
            throw new JsonStreamException("Error writing empty JSON object " + localName, e);
        }
    }


    @Override
    public void writeEmptyElement(String namespaceURI, String localName) throws JsonStreamException
    {
        // no namespace support -> use only local name
        writeEmptyElement(localName);
    }


    @Override
    public void writeEmptyElement(String prefix, String localName, String namespaceURI) throws JsonStreamException
    {
        // no namespace support -> use only local name
        writeEmptyElement(localName);
    }


    @Override
    public void writeEndElement() throws JsonStreamException
    {
        try
        {
            // close current array if end of surrounding object
            if (currentContext.isArray)
                closeArray();
            
            if (!currentContext.skipParent)
            {
                if (!currentContext.firstChild)
                    writer.endObject();
                else if (currentContext.emptyElement)
                    writer.nullValue();
            }
            
            writer.flush();
            popContext();
        }
        catch (IOException e)
        {
            throw new JsonStreamException("Error closing JSON object", e);
        }
    }


    protected void writeValue(String value) throws IOException
    {
        writeValue(value, true);
    }
    
    
    protected void writeValue(String value, boolean inline) throws IOException
    {
        if (isValueArray(null, currentContext.eltName))
        {
            String[] tokens = value.split(" ");
            if (tokens.length > 0)
            {
                writer.beginArray();
                writer.setIndent("");
                for (String val: tokens)
                {
                    if (isNumericValue(val))
                        writer.jsonValue(val);
                    else
                        writer.value(val);
                }
                writer.endArray();
                writer.setIndent(INDENT1);
            }
            else
                writer.nullValue();
        }
        else
        {
            // write simple array values inline
            if (inline && currentContext.parent.isArray)
                writer.setIndent("");
            
            if (isNumericValue(value))
                writer.jsonValue(value);
            else
                writer.value(value);
        }
    }


    @Override
    public void writeAttribute(String localName, String value) throws JsonStreamException
    {
        try
        {
            if (markAttributes)
                localName = ATT_PREFIX + localName;
            
            prepareAppendToObject();
            currentContext.emptyElement = false;
            
            writer.name(localName);
            writeValue(value, false);
        }
        catch (IOException e)
        {
            throw new JsonStreamException("Error writing attribute " + localName, e);
        }
    }


    @Override
    public void writeAttribute(String namespaceURI, String localName, String value) throws JsonStreamException
    {
        // no namespace support -> use only local name
        writeAttribute(localName, value);
    }


    @Override
    public void writeAttribute(String prefix, String namespaceURI, String localName, String value) throws JsonStreamException
    {
        // no namespace support -> use only local name
        writeAttribute(localName, value);
    }


    @Override
    public void writeCharacters(String text) throws JsonStreamException
    {
        try
        {
            if (Strings.isNullOrEmpty(text))
                return;
            
            if (!currentContext.firstChild)
            {
                writer.name("value");
                writeValue(text, false);
            }
            else
                writeValue(text);
            
            currentContext.emptyElement = false;
        }
        catch (IOException e)
        {
            throw new JsonStreamException("Error writing JSON value", e);
        }
    }


    @Override
    public void writeCharacters(char[] text, int start, int len) throws JsonStreamException
    {
        writeCharacters(new String(text, start, len));
    }


    @Override
    public NamespaceContext getNamespaceContext()
    {
        // no namespace support
        return new NamespaceContext() {
            @Override
            public String getNamespaceURI(String arg0)
            {
                return null;
            }

            @Override
            public String getPrefix(String arg0)
            {
                return null;
            }

            @Override
            public Iterator<String> getPrefixes(String arg0)
            {
                return null;
            }
        };
    }


    @Override
    public String getPrefix(String uri) throws JsonStreamException
    {
        // no namespace support
        return null;
    }


    @Override
    public void setDefaultNamespace(String uri) throws JsonStreamException
    {
        // no namespace support
    }


    @Override
    public void setNamespaceContext(NamespaceContext context) throws JsonStreamException
    {
        // no namespace support
    }


    @Override
    public void setPrefix(String prefix, String uri) throws JsonStreamException
    {
        // no namespace support
    }


    @Override
    public void writeCData(String data) throws JsonStreamException
    {
        // CData is not supported in JSON
        // ignore silently
    }


    @Override
    public void writeComment(String data) throws JsonStreamException
    {
        // comments are not supported in JSON
        // ignore silently
    }


    @Override
    public void writeDTD(String dtd) throws JsonStreamException
    {
        // no DTD support
        // ignore silently
    }


    @Override
    public void writeEntityRef(String name) throws JsonStreamException
    {
        // entities not supported in JSON
        // ignore silently
    }


    @Override
    public void writeDefaultNamespace(String namespaceURI) throws JsonStreamException
    {
        // no namespace support
        // ignore silently
    }


    @Override
    public void writeNamespace(String prefix, String namespaceURI) throws JsonStreamException
    {
        // no namespace support
        // ignore silently
    }


    @Override
    public void writeProcessingInstruction(String target) throws JsonStreamException
    {
        // processing instructions not supported in JSON
        // ignore silently
    }


    @Override
    public void writeProcessingInstruction(String target, String data) throws JsonStreamException
    {
        // processing instructions not supported in JSON
        // ignore silently
    }


    @Override
    public Object getProperty(String name) throws IllegalArgumentException
    {
        return null;
    }


    @Override
    public void flush() throws JsonStreamException
    {
        try
        {
            writer.flush();
        }
        catch (IOException e)
        {
            throw new JsonStreamException("Error when flushing JSON writer", e);
        }
    }


    @Override
    public void close() throws JsonStreamException
    {
        try
        {
            writer.close();
        }
        catch (IOException e)
        {
            throw new JsonStreamException("Error when closing JSON writer", e);
        }
    }
}
