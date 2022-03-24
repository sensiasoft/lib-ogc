/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.
 
Copyright (C) 2022 Sensia Software LLC. All Rights Reserved.
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.data;

import net.opengis.OgcProperty;
import net.opengis.OgcPropertyImpl;
import net.opengis.swe.v20.Count;
import net.opengis.swe.v20.DataComponent;
import net.opengis.swe.v20.DataEncoding;
import net.opengis.swe.v20.DataStream;
import net.opengis.swe.v20.EncodedValues;


/**
 * <p>
 * DataStream object implementation
 * </p>
 *
 * @author Alex Robin
 * @since Mar 24, 2022
 */
public class DataStreamImpl extends AbstractSWEIdentifiableImpl implements DataStream
{
    private static final long serialVersionUID = 2640330595178102027L;
    protected OgcPropertyImpl<Count> elementCount;
    protected OgcPropertyImpl<DataComponent> elementType = new OgcPropertyImpl<DataComponent>();
    protected DataEncoding encoding;
    protected OgcPropertyImpl<EncodedValues> values;


    @Override
    public boolean isSetElementCount()
    {
        return elementCount != null && elementCount.hasValue();
    }


    @Override
    public Count getElementCount()
    {
        return elementCount.getValue();
    }


    @Override
    public OgcProperty<Count> getElementCountProperty()
    {
        return elementCount;
    }


    @Override
    public void setElementCount(Count elementCount)
    {
        if (this.elementCount == null)
            this.elementCount = new OgcPropertyImpl<Count>();
        this.elementCount.setValue(elementCount);
    }


    @Override
    public DataComponent getElementType()
    {
        return this.elementType.getValue();
    }


    @Override
    public OgcProperty<DataComponent> getElementTypeProperty()
    {
        return this.elementType;
    }


    @Override
    public void setElementType(String name, DataComponent component)
    {
        elementType.setName(name);
        elementType.setValue(component);
    }


    @Override
    public DataEncoding getEncoding()
    {
        return encoding;
    }


    @Override
    public boolean isSetEncoding()
    {
        return encoding != null;
    }


    @Override
    public void setEncoding(DataEncoding encoding)
    {
        this.encoding = encoding;
    }


    @Override
    public EncodedValues getValues()
    {
        return this.values.getValue();
    }


    @Override
    public boolean isSetValues()
    {
        return this.values != null && this.values.hasValue();
    }


    @Override
    public void setValues(EncodedValues values)
    {
        if (this.values == null)
            this.values = new OgcPropertyImpl<EncodedValues>();
        this.values.setValue(values);
    }


    @Override
    public DataStream copy()
    {
        DataStreamImpl newDs = new DataStreamImpl();
        this.copyTo(newDs);
        if (elementCount != null)
            newDs.elementCount = elementCount.copy();
        if (elementType != null)
            newDs.elementType = elementType.copy();
        if (encoding != null)
            newDs.encoding = encoding.copy();
        if (values != null)
            newDs.values = values.copy();
        return newDs;
    }

}
