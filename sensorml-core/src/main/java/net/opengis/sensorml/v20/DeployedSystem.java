/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.
 
Copyright (C) 2012-2015 Sensia Software LLC. All Rights Reserved.
 
******************************* END LICENSE BLOCK ***************************/

package net.opengis.sensorml.v20;

import java.io.Serializable;
import javax.xml.namespace.QName;
import org.vast.ogc.geopose.Pose;
import net.opengis.gml.v32.AbstractGML;
import net.opengis.gml.v32.Point;
import net.opengis.gml.v32.Reference;
import net.opengis.swe.v20.Text;
import net.opengis.swe.v20.Vector;


/**
 * POJO class for type DeployedSystem
 *
 * This is a complex type.
 */
@SuppressWarnings("javadoc")
public interface DeployedSystem extends AbstractGML
{
    public final static QName DEFAULT_QNAME = new QName(DescribedObject.DEFAULT_NS_URI, DeployedSystem.class.getSimpleName());
    
    
    /**
     * Gets the reference to the system
     */
    public Reference getSystemRef();
    
    
    /**
     * Sets the reference to the system
     */
    public void setSystemRef(Reference systemRef);
    
    
    /**
     * Gets the position property
     */
    public Serializable getPosition();
    
    
    /**
     * Checks if position is set
     */
    public boolean isSetPosition();
    
    
    /**
     * Adds a new positionAsText property
     */
    public void setPositionAsText(Text position);
    
    
    /**
     * Adds a new positionAsPoint property
     */
    public void setPositionAsPoint(Point position);
    
    
    /**
     * Adds a new positionAsVector property
     */
    public void setPositionAsVector(Vector position);
    
    
    /**
     * Adds a new positionAsPose property
     */
    public void setPositionAsPose(Pose pose);
    
    
    /**
     * Gets the configuration property
     */
    public Settings getConfiguration();
    
    
    /**
     * Checks if configuration is set
     */
    public boolean isSetConfiguration();
    
    
    /**
     * Sets the configuration property
     */
    public void setConfiguration(Settings configuration);
    
}
