/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.
 
Copyright (C) 2023 Sensia Software LLC. All Rights Reserved.
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.json;

import java.io.IOException;
import java.io.StringReader;
import java.util.AbstractMap.SimpleEntry;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Map.Entry;
import org.vast.util.Asserts;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;


/**
 * <p>
 * Implementation of JsonReader to re-stream JSON data previously buffered
 * while waiting for a specific member name/key.
 * </p>
 *
 * @author Alex Robin
 * @since Sep 29, 2023
 */
public class JsonReaderWithBuffer extends JsonReader
{
    final JsonReader delegate;
    final Deque<Entry<String, JsonElement>> bufferedMembers;
    JsonReader tempReader;
    boolean replaying = false;
    
    
    public JsonReaderWithBuffer(JsonReader delegate)
    {
        super(new StringReader(""));
        this.delegate = Asserts.checkNotNull(delegate, JsonReader.class);
        this.bufferedMembers = new LinkedList<>();
    }
    
    
    /**
     * Buffer the next object value so it can be replayed later
     * @param name member name
     */
    public void buffer(String name)
    {
        replaying = false;
        
        var entry = new SimpleEntry<>(name, JsonParser.parseReader(delegate));
        bufferedMembers.addLast(entry);
    }
    
    
    public void startReplay()
    {
        replaying = true;
    }


    @Override
    public String nextName() throws IOException
    {
        maybeCloseTempReader();
        
        if (tempReader != null)
        {
            return tempReader.nextName();
        }
        else if (replaying && !bufferedMembers.isEmpty())
        {
            var nextMember = bufferedMembers.pollFirst();
            
            var memberVal = nextMember.getValue().toString();
            tempReader = new JsonReader(new StringReader(memberVal));
            
            return nextMember.getKey();
        }
        else
            return delegate.nextName();
    }


    @Override
    public boolean hasNext() throws IOException
    {
        maybeCloseTempReader();
        
        if (tempReader != null)
            return tempReader.hasNext();
        else if (replaying && !bufferedMembers.isEmpty())
            return true;
        else
            return delegate.hasNext();
    }
    
    
    protected void maybeCloseTempReader() throws IOException
    {
        if (tempReader != null && tempReader.peek() == JsonToken.END_DOCUMENT)
            tempReader = null;
    }


    @Override
    public void endObject() throws IOException
    {
        if (tempReader != null)
            tempReader.endObject();
        else
            delegate.endObject();
    }


    @Override
    public JsonToken peek() throws IOException
    {
        maybeCloseTempReader();
        
        if (tempReader != null)
            return tempReader.peek();
        else if (replaying && !bufferedMembers.isEmpty())
            return JsonToken.NAME;
        else
            return delegate.peek();
    }
    
    
    @Override
    public void beginArray() throws IOException
    {
        if (tempReader != null)
            tempReader.beginArray();
        else
            delegate.beginArray();
    }


    @Override
    public void endArray() throws IOException
    {
        if (tempReader != null)
            tempReader.endArray();
        else
            delegate.endArray();
    }


    @Override
    public void beginObject() throws IOException
    {
        if (tempReader != null)
            tempReader.beginObject();
        else
            delegate.beginObject();
    }


    @Override
    public String nextString() throws IOException
    {
        if (tempReader != null)
            return tempReader.nextString();
        else
            return delegate.nextString();
    }


    @Override
    public boolean nextBoolean() throws IOException
    {
        if (tempReader != null)
            return tempReader.nextBoolean();
        else
            return delegate.nextBoolean();
    }


    @Override
    public void nextNull() throws IOException
    {
        if (tempReader != null)
            tempReader.nextNull();
        else
            delegate.nextNull();
    }


    @Override
    public double nextDouble() throws IOException
    {
        if (tempReader != null)
            return tempReader.nextDouble();
        else
            return delegate.nextDouble();
    }


    @Override
    public long nextLong() throws IOException
    {
        if (tempReader != null)
            return tempReader.nextLong();
        else
            return delegate.nextLong();
    }


    @Override
    public int nextInt() throws IOException
    {
        if (tempReader != null)
            return tempReader.nextInt();
        else
            return delegate.nextInt();
    }


    @Override
    public void skipValue() throws IOException
    {
        if (tempReader != null)
            tempReader.skipValue();
        else
            delegate.skipValue();
    }


    @Override
    public String getPath()
    {
        if (tempReader != null)
            return tempReader.getPath();
        else
            return delegate.getPath();
    }

}
