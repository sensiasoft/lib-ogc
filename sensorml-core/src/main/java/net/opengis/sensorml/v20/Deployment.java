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
import java.util.List;
import javax.xml.namespace.QName;
import net.opengis.OgcPropertyList;
import net.opengis.gml.v32.Point;
import net.opengis.swe.v20.DataRecord;
import net.opengis.swe.v20.Text;
import net.opengis.swe.v20.Time;
import net.opengis.swe.v20.Vector;


/**
 * POJO class for XML type DeploymentType(@http://www.opengis.net/sensorml/2.0).
 *
 * This is a complex type.
 */
@SuppressWarnings("javadoc")
public interface Deployment extends DescribedObject
{
    public final static QName DEFAULT_QNAME = new QName(DEFAULT_NS_URI, Deployment.class.getSimpleName());
    
    
    /**
     * Gets the list of localReferenceFrame properties
     */
    public List<SpatialFrame> getLocalReferenceFrameList();
    
    
    /**
     * Returns number of localReferenceFrame properties
     */
    public int getNumLocalReferenceFrames();
    
    
    /**
     * Adds a new localReferenceFrame property
     */
    public void addLocalReferenceFrame(SpatialFrame localReferenceFrame);
    
    
    /**
     * Gets the list of localTimeFrame properties
     */
    public List<TemporalFrame> getLocalTimeFrameList();
    
    
    /**
     * Returns number of localTimeFrame properties
     */
    public int getNumLocalTimeFrames();
    
    
    /**
     * Adds a new localTimeFrame property
     */
    public void addLocalTimeFrame(TemporalFrame localTimeFrame);
    
    
    /**
     * Gets the list of position properties
     */
    public OgcPropertyList<Serializable> getPositionList();
    
    
    /**
     * Returns number of position properties
     */
    public int getNumPositions();
    
    
    /**
     * Adds a new positionAsText property
     */
    public void addPositionAsText(Text position);
    
    
    /**
     * Adds a new positionAsPoint property
     */
    public void addPositionAsPoint(Point position);
    
    
    /**
     * Adds a new positionAsVector property
     */
    public void addPositionAsVector(Vector position);
    
    
    /**
     * Adds a new positionAsDataRecord property
     */
    public void addPositionAsDataRecord(DataRecord position);
    
    
    /**
     * Gets the list of timePosition properties
     */
    public OgcPropertyList<Time> getTimePositionList();
    
    
    /**
     * Returns number of timePosition properties
     */
    public int getNumTimePositions();
    
    
    /**
     * Adds a new timePosition property
     */
    public void addTimePosition(Time timePosition);
    
    
    /**
     * Gets the list of components
     */
    public OgcPropertyList<AbstractProcess> getComponentList();
    
    
    /**
     * Returns number of components
     */
    public int getNumComponents();
    
    
    /**
     * Gets the component with the given name
     */
    public AbstractProcess getComponent(String name);
    
    
    /**
     * Adds a new component
     */
    public void addComponent(String name, AbstractProcess component);
    
}
