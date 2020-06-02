/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.
 
Copyright (C) 2012-2015 Sensia Software LLC. All Rights Reserved.
 
******************************* END LICENSE BLOCK ***************************/

package net.opengis.fes.v20.impl;

import java.util.Objects;
import javax.xml.namespace.QName;
import net.opengis.fes.v20.Literal;


/**
 * POJO class for XML type LiteralType(@http://www.opengis.net/fes/2.0).
 *
 * This is a complex type.
 */
public class LiteralImpl implements Literal
{
    protected QName type;
    protected String value;
    
    
    public LiteralImpl()
    {
    }
    
    
    /**
     * Gets the type property
     */
    @Override
    public QName getType()
    {
        return type;
    }
    
    
    /**
     * Checks if type is set
     */
    @Override
    public boolean isSetType()
    {
        return (type != null);
    }
    
    
    /**
     * Sets the type property
     */
    @Override
    public void setType(QName type)
    {
        this.type = type;
    }


    @Override
    public String getValue()
    {
        return value;
    }


    @Override
    public boolean isSetValue()
    {
        return (value != null);
    }


    @Override
    public void setValue(String value)
    {
        this.value = value;
    }
    
    
    @Override
    public String toString()
    {
        return getValue();
    }
    
    
    @Override
    public boolean equals(Object obj)
    {
        return obj instanceof Literal &&
               Objects.equals(type, ((Literal)obj).getType()) &&
               Objects.equals(value, ((Literal)obj).getValue());
    }
    
    
    @Override
    public int hashCode()
    {
        return Objects.hash(type,
                            value);
    }
}
