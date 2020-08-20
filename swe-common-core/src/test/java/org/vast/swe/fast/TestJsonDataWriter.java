/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.
 
Copyright (C) 2012-2020 Sensia Software LLC. All Rights Reserved.
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.swe.fast;

import static org.junit.Assert.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.junit.Test;
import org.vast.data.DataBlockString;
import org.vast.swe.fast.JsonDataWriter.StringWriter;


public class TestJsonDataWriter
{

    private byte[] writeToBuffer(String... text) throws IOException
    {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        
        JsonDataWriter writer = new JsonDataWriter();
        writer.setOutput(os);
        StringWriter sw = writer.new StringWriter(null);        
        DataBlockString data = new DataBlockString(1);
        for (String t: text) {
            data.setStringValue(t);
            sw.writeValue(data, 0);
        }
        writer.flush();
        
        return os.toByteArray();
    }
    
    
    @Test
    public void testWriteStringEscape() throws IOException
    {
        String text = "bla bla \"quoted\" \\backslash\\ \n \n \t \t";
        String expected = "\"" + text
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\t", "\\t")
                .replace("\n", "\\n") + "\"";
        
        byte[] output = writeToBuffer(text);
        System.out.write(output);
        System.out.println();
        assertEquals(expected, new String(output));        
    }
    
    
    @Test
    public void testWriteStringNoEscape() throws IOException
    {
        String text = "bla bla no need to escape, no characters invalid in JSON .!?/^()&é{}";
        String expected = "\"" + text + "\"";
        
        byte[] output = writeToBuffer(text);
        System.out.write(output);
        System.out.println();
        assertEquals(expected, new String(output));
    }
    
    
    @Test
    public void testWriteSeveralStrings() throws IOException
    {
        String[] text = new String[] {
            "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/", // 64 chars
            "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+\\", // 64 chars w/ 1 escape
            "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/*", // 65 chars
            "ABCDEFGHIJKLMNOPQRSTUVWXYZ\\"
        };
        
        String expected = "";
        for (String t: text)
            expected += "\"" + t.replace("\\", "\\\\") + "\"";
        
        byte[] output = writeToBuffer(text);
        System.out.write(output);
        System.out.println();
        assertEquals(expected, new String(output));
    }

}
