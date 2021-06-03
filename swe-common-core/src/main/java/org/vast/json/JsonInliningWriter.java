/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.
 
Copyright (C) 2021 Sensia Software LLC. All Rights Reserved.
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.json;

import java.io.FilterWriter;
import java.io.IOException;
import java.io.Writer;
import com.google.gson.stream.JsonWriter;


/**
 * <p>
 * Extension of JsonWriter allowing inlining of certain objects and arrays,
 * often used when object or array contains only scalar values.
 * </p>
 *
 * @author Alex Robin
 * @since Jan 27, 2021
 */
public class JsonInliningWriter extends JsonWriter
{
    InliningFilterWriter inliningWriter;
    
    
    public static class InliningFilterWriter extends FilterWriter
    {
        boolean writeInline;
        
        protected InliningFilterWriter(Writer out)
        {
            super(out);
        }
        
        @Override
        public void write(int c) throws IOException
        {
            if (writeInline && c == '\n')
                super.write(' ');
            else
                super.write(c);
        }

        @Override
        public void write(String str) throws IOException
        {
            if (writeInline)
            {
                if (str.length() == 1 && str.equals("\n"))
                    super.write(" ");
                else if (str.isBlank())
                    return;
                else
                    super.write(str);
            }
            else
                super.write(str);
        }
        
        public void writeInline(boolean writeInline)
        {
            this.writeInline = writeInline;
        }
    }
    
    
    public JsonInliningWriter(Writer out)
    {
        this(new InliningFilterWriter(out));
    }
    
    
    protected JsonInliningWriter(InliningFilterWriter out)
    {
        super(out);
        this.inliningWriter = out;
    }
    
    
    public void writeInline(boolean writeInline)
    {
        inliningWriter.writeInline(writeInline);
    }    

}
