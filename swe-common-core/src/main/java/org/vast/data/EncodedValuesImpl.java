/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.
 
Copyright (C) 2012-2015 Sensia Software LLC. All Rights Reserved.
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.data;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.vast.cdm.common.DataStreamParser;
import org.vast.cdm.common.DataStreamWriter;
import org.vast.swe.SWEHelper;
import net.opengis.swe.v20.DataEncoding;
import net.opengis.OgcPropertyImpl;
import net.opengis.swe.v20.BinaryEncoding;
import net.opengis.swe.v20.BlockComponent;
import net.opengis.swe.v20.ByteEncoding;
import net.opengis.swe.v20.DataArray;
import net.opengis.swe.v20.DataBlock;
import net.opengis.swe.v20.DataStream;
import net.opengis.swe.v20.EncodedValues;


/**
 * <p>
 * Full implementation of EncodedValues wrapping SWE Common data stream 
 * parsers/writers allowing decoding/encoding of any encoded array or stream
 * </p>
 *
 * @author Alex Robin
 * @since Oct 3, 2014
 */
public class EncodedValuesImpl extends OgcPropertyImpl<byte[]> implements EncodedValues
{
    private static final long serialVersionUID = 5065676107640449321L;

    private String text;
    private DataBlockList data;
    
    
    @Override
    public boolean resolveHref()
    {
        // TODO Add support for remote data
        return false;
    }


    @Override
    public void setAsText(DataArray array, DataEncoding encoding, String text)
    {
        this.text = text;
        if (array != null)
            decode(array, encoding);
    }
    
    
    public void decode(BlockComponent array, DataEncoding encoding)
    {
        try
        {
            DataStreamParser parser = SWEHelper.createDataParser(encoding);
            parser.setParentArray(array);
            InputStream is = new ByteArrayInputStream(text.getBytes());
            parser.parse(is);
            this.text = null; // clear after successful decoding
        }
        catch (IOException e)
        {
            throw new IllegalStateException("Cannot parse encoded values", e);
        }
    }


    @Override
    public String getAsText(DataArray array, DataEncoding encoding)
    {
        return encode(array, encoding);
    }


    public String encode(BlockComponent array, DataEncoding encoding)
    {
        // force base64 if byte encoding is raw
        if (encoding instanceof BinaryEncoding)
            ((BinaryEncoding) encoding).setByteEncoding(ByteEncoding.BASE_64);
        
        // write values with proper encoding to byte array
        ByteArrayOutputStream os = new ByteArrayOutputStream(array.getData().getAtomCount()*10);
        try
        {
            DataStreamWriter writer = SWEHelper.createDataWriter(encoding);
            writer.setParentArray(array);
            writer.write(os);
            writer.flush();
        }
        catch (IOException e)
        {
            throw new IllegalStateException("Cannot write encoded values", e);
        }
        
        // convert byte array to string
        return os.toString();
    }


    @Override
    public void setAsText(DataStream dataStream, DataEncoding encoding, String text)
    {
        this.text = text;
        
        try
        {
            DataStreamParser parser = SWEHelper.createDataParser(encoding);
            parser.setDataComponents(dataStream.getElementType());
            parser.setInput(new ByteArrayInputStream(text.getBytes()));
            DataBlock dataBlk;
            data = new DataBlockList(false);
            while ((dataBlk = parser.parseNextBlock()) != null)
                data.add(dataBlk);
            this.text = null; // clear after successful decoding
        }
        catch (IOException e)
        {
            throw new IllegalStateException("Cannot parse encoded values", e);
        }
    }


    @Override
    public String getAsText(DataStream dataStream, DataEncoding encoding)
    {
        return encode(dataStream, encoding);
    }


    String encode(DataStream dataStream, DataEncoding encoding)
    {
        // force base64 if byte encoding is raw
        if (encoding instanceof BinaryEncoding)
            ((BinaryEncoding) encoding).setByteEncoding(ByteEncoding.BASE_64);
        
        // write values with proper encoding to byte array
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try
        {
            DataStreamWriter writer = SWEHelper.createDataWriter(encoding);
            writer.setDataComponents(dataStream.getElementType());
            writer.setOutput(os);
            for (DataBlock dataBlk: data.blockList)
                writer.write(dataBlk);
            writer.flush();
        }
        catch (IOException e)
        {
            throw new IllegalStateException("Cannot write encoded values", e);
        }
        
        // convert byte array to string
        return os.toString();
    }

}
