/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.
 
Copyright (C) 2012-2015 Sensia Software LLC. All Rights Reserved.
 
******************************* END LICENSE BLOCK ***************************/

package net.opengis.gml.v32.impl;

import java.net.URI;
import org.vast.unit.UnitParserUCUM;
import net.opengis.gml.v32.Measure;


/**
 * POJO class for XML type MeasureType(@http://www.opengis.net/gml/3.2).
 *
 * This is a list type whose items are java.lang.String.
 */
public class MeasureImpl implements Measure
{
    private static final long serialVersionUID = 5461920677762491050L;
    
    protected double value;
    protected String uom;
    
    
    public MeasureImpl()
    {
    }
    
    
    public MeasureImpl(double value, String uom)
    {
        this.value = value;
        this.uom = uom;
    }


    @Override
    public double getValue()
    {
        return value;
    }


    @Override
    public void setValue(double value)
    {
        this.value = value;
    }
    
    
    /**
     * Gets the uom property
     */
    @Override
    public String getUom()
    {
        return uom;
    }
    
    
    /**
     * Sets the uom property
     */
    @Override
    public void setUom(String uom)
    {
        // validate that it's either a resource URI or a UCUM code
        if (uom.startsWith("http") || uom.startsWith("urn"))
            URI.create(uom);
        else
            new UnitParserUCUM().getUnit(uom);
        this.uom = uom;
    }
}
