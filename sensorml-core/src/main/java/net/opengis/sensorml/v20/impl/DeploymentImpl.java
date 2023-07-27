/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.
 
Copyright (C) 2023 Sensia Software LLC. All Rights Reserved.
 
******************************* END LICENSE BLOCK ***************************/

package net.opengis.sensorml.v20.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.namespace.QName;
import net.opengis.OgcPropertyList;
import net.opengis.gml.v32.Point;
import net.opengis.sensorml.v20.AbstractProcess;
import net.opengis.sensorml.v20.Deployment;
import net.opengis.sensorml.v20.SpatialFrame;
import net.opengis.sensorml.v20.TemporalFrame;
import net.opengis.swe.v20.DataRecord;
import net.opengis.swe.v20.Text;
import net.opengis.swe.v20.Time;
import net.opengis.swe.v20.Vector;


/**
 * POJO class for XML type DeploymentType(@http://www.opengis.net/sensorml/2.0).
 *
 * This is a complex type.
 */
public class DeploymentImpl extends DescribedObjectImpl implements Deployment
{
    private static final long serialVersionUID = 6200618857495858650L;
    
    protected ArrayList<SpatialFrame> localReferenceFrameList = new ArrayList<SpatialFrame>();
    protected ArrayList<TemporalFrame> localTimeFrameList = new ArrayList<TemporalFrame>();
    protected OgcPropertyList<Serializable> positionList = new OgcPropertyList<Serializable>();
    protected OgcPropertyList<Time> timePositionList = new OgcPropertyList<Time>();
    protected OgcPropertyList<AbstractProcess> components = new OgcPropertyList<>(5);
    
    
    @Override
    public QName getQName()
    {
        return Deployment.DEFAULT_QNAME;
    }
    
    
    /**
     * Gets the list of localReferenceFrame properties
     */
    @Override
    public List<SpatialFrame> getLocalReferenceFrameList()
    {
        return localReferenceFrameList;
    }
    
    
    /**
     * Returns number of localReferenceFrame properties
     */
    @Override
    public int getNumLocalReferenceFrames()
    {
        if (localReferenceFrameList == null)
            return 0;
        return localReferenceFrameList.size();
    }
    
    
    /**
     * Adds a new localReferenceFrame property
     */
    @Override
    public void addLocalReferenceFrame(SpatialFrame localReferenceFrame)
    {
        this.localReferenceFrameList.add(localReferenceFrame);
    }
    
    
    /**
     * Gets the list of localTimeFrame properties
     */
    @Override
    public List<TemporalFrame> getLocalTimeFrameList()
    {
        return localTimeFrameList;
    }
    
    
    /**
     * Returns number of localTimeFrame properties
     */
    @Override
    public int getNumLocalTimeFrames()
    {
        if (localTimeFrameList == null)
            return 0;
        return localTimeFrameList.size();
    }
    
    
    /**
     * Adds a new localTimeFrame property
     */
    @Override
    public void addLocalTimeFrame(TemporalFrame localTimeFrame)
    {
        this.localTimeFrameList.add(localTimeFrame);
    }
    
    
    /**
     * Gets the list of position properties
     */
    @Override
    public OgcPropertyList<Serializable> getPositionList()
    {
        return positionList;
    }
    
    
    /**
     * Returns number of position properties
     */
    @Override
    public int getNumPositions()
    {
        if (positionList == null)
            return 0;
        return positionList.size();
    }
    
    
    /**
     * Adds a new positionAsText property
     */
    @Override
    public void addPositionAsText(Text position)
    {
        this.positionList.add(position);
    }
    
    
    /**
     * Adds a new positionAsPoint property
     */
    @Override
    public void addPositionAsPoint(Point position)
    {
        this.positionList.add(position);
    }
    
    
    /**
     * Adds a new positionAsVector property
     */
    @Override
    public void addPositionAsVector(Vector position)
    {
        this.positionList.add(position);
    }
    
    
    /**
     * Adds a new positionAsDataRecord property
     */
    @Override
    public void addPositionAsDataRecord(DataRecord position)
    {
        this.positionList.add(position);
    }
    
    
    /**
     * Gets the list of timePosition properties
     */
    @Override
    public OgcPropertyList<Time> getTimePositionList()
    {
        return timePositionList;
    }
    
    
    /**
     * Returns number of timePosition properties
     */
    @Override
    public int getNumTimePositions()
    {
        if (timePositionList == null)
            return 0;
        return timePositionList.size();
    }
    
    
    /**
     * Adds a new timePosition property
     */
    @Override
    public void addTimePosition(Time timePosition)
    {
        this.timePositionList.add(timePosition);
    }
    
    
    /**
     * Gets the list of components
     */
    @Override
    public OgcPropertyList<AbstractProcess> getComponentList()
    {
        return components;
    }
    
    
    /**
     * Returns number of components
     */
    @Override
    public int getNumComponents()
    {
        return components.size();
    }
    
    
    /**
     * Gets the component with the given name
     */
    @Override
    public AbstractProcess getComponent(String name)
    {
        return components.get(name);
    }
    
    
    /**
     * Adds a new component
     */
    @Override
    public void addComponent(String name, AbstractProcess component)
    {
        components.add(name, component);
    }
}
