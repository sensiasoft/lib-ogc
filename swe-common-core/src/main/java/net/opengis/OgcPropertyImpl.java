/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.
 
Copyright (C) 2012-2015 Sensia Software LLC. All Rights Reserved.
 
******************************* END LICENSE BLOCK ***************************/

package net.opengis;

import java.io.IOException;
import java.io.Serializable;
import org.vast.ogc.xlink.SimpleLink;
import org.vast.util.Asserts;


public class OgcPropertyImpl<T extends Serializable> extends SimpleLink<T> implements OgcProperty<T>
{
    private static final long serialVersionUID = -4533173517189205779L;
    
    protected String name;
    protected String nilReason;
    protected T value;
    protected transient HrefResolver hrefResolver;
    
    
    public OgcPropertyImpl()
    {
    }
    
    
    public OgcPropertyImpl(T value)
    {
        this.value = value;
    }
    
    
    public OgcPropertyImpl(String name, T value)
    {
        this(value);
        this.name = name;
    }
    
    
    @Override
    public OgcPropertyImpl<T> copy()
    {
        OgcPropertyImpl<T> newProp = new OgcPropertyImpl<>();
        copyTo(newProp);
        return newProp;
    }
    
    
    public void copyTo(OgcPropertyImpl<T> other)
    {        
        other.name = this.name;
        other.href = this.href;
        other.role = this.role;
        other.arcRole = this.arcRole;
        other.nilReason = this.nilReason;
        other.hrefResolver = this.hrefResolver;
        
        if (this.value != null && this.value instanceof HasCopy)
            other.setValue((T)((HasCopy)this.value).copy());
        else
            other.value = this.value;
    }
    
    
    @Override
    public String getName()
    {
        return name;
    }


    @Override
    public void setName(String name)
    {
        this.name = name;
    }
    
    
    @Override
    public boolean hasHref()
    {
        return href != null;
    }
    
    
    @Override
    public String getNilReason()
    {
        return nilReason;
    }


    @Override
    public void setNilReason(String nilReason)
    {
        this.nilReason = nilReason;
    }
    
    
    @Override
    public boolean hasValue()
    {
        return value != null;
    }


    @Override
    public T getValue()
    {
        return value;
    }
    
    
    @Override
    public void setValue(T value)
    {
        this.value = value;
    }
    
    
    public void setHrefResolver(HrefResolver hrefResolver)
    {
        this.hrefResolver = hrefResolver;
    }


    public boolean resolveHref() throws IOException
    {
        if (hasValue())
            return true;
        
        Asserts.checkState(hasHref(), "Property must have an href");
        Asserts.checkState(hrefResolver != null, "No href resolver is configured");
        
        // call resolver and discard it so it can be GC
        boolean ret = hrefResolver.resolveHref(this);
        hrefResolver = null;
        
        return ret;
    }


    @Override
    public String getArcRole()
    {
        return null;
    }


    @Override
    public String getTargetUID()
    {
        // use arcrole property to store UID since it's not used in this context
        return arcRole;
    }


    @Override
    public void setTargetUID(String targetUID)
    {
        // use arcrole property to store UID since it's not used in this context
        setArcRole(targetUID);
    }
    
    
    @Override
    public T getTarget()
    {
        return getValue();
    }
}
