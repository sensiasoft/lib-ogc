/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.
 
Copyright (C) 2012-2015 Sensia Software LLC. All Rights Reserved.
 
******************************* END LICENSE BLOCK ***************************/

package net.opengis.fes.v20;

import javax.xml.namespace.QName;


/**
 * POJO class for XML type LiteralType(@http://www.opengis.net/fes/2.0).
 *
 * This is a complex type.
 */
public interface Literal extends Expression
{
    
    
    /**
     * Gets the type property
     */
    public QName getType();
    
    
    /**
     * Checks if type is set
     */
    public boolean isSetType();
    
    
    /**
     * Sets the type property
     */
    public void setType(QName type);
    
    
    /**
     * Gets the inline value
     */
    public String getValue();
    
    
    /**
     * Checks if inline value is set
     */
    public boolean isSetValue();
    
    
    /**
     * Sets the inline value
     */
    public void setValue(String val);
}
