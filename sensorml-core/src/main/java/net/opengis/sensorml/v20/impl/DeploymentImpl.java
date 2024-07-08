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

import javax.xml.namespace.QName;
import net.opengis.OgcPropertyList;
import net.opengis.sensorml.v20.DeployedSystem;
import net.opengis.sensorml.v20.Deployment;
import net.opengis.sensorml.v20.SpatialFrame;
import net.opengis.sensorml.v20.TemporalFrame;


/**
 * POJO class for XML type DeploymentType(@http://www.opengis.net/sensorml/2.0).
 *
 * This is a complex type.
 */
public class DeploymentImpl extends DescribedObjectImpl implements Deployment
{
    private static final long serialVersionUID = 6200618857495858650L;

    protected String definition;
    protected SpatialFrame localReferenceFrame;
    protected TemporalFrame localTimeFrame;
    protected DeployedSystem platform;
    protected OgcPropertyList<DeployedSystem> deployedSystems = new OgcPropertyList<DeployedSystem>();
    
    
    @Override
    public QName getQName()
    {
        return Deployment.DEFAULT_QNAME;
    }
    
    
    /**
     * Gets the definition property
     */
    @Override
    public String getDefinition()
    {
        return definition;
    }
    
    
    /**
     * Checks if definition is set
     */
    @Override
    public boolean isSetDefinition()
    {
        return (definition != null);
    }
    
    
    /**
     * Sets the definition property
     */
    @Override
    public void setDefinition(String definition)
    {
        this.definition = definition;
    }
    
    
    /**
     * Gets the localReferenceFrame property
     */
    @Override
    public SpatialFrame getLocalReferenceFrame()
    {
        return localReferenceFrame;
    }
    
    
    /**
     * Checks if localReferenceFrame is set
     */
    @Override
    public boolean isSetLocalReferenceFrame()
    {
        return (localReferenceFrame != null);
    }
    
    
    /**
     * Sets the localReferenceFrame property
     */
    @Override
    public void setLocalReferenceFrame(SpatialFrame localReferenceFrame)
    {
        this.localReferenceFrame = localReferenceFrame;
    }
    
    
    /**
     * Gets the localTimeFrame property
     */
    @Override
    public TemporalFrame getLocalTimeFrame()
    {
        return localTimeFrame;
    }
    
    
    /**
     * Checks if localTimeFrame is set
     */
    @Override
    public boolean isSetLocalTimeFrame()
    {
        return (localReferenceFrame != null);
    }
    
    
    /**
     * Sets the localTimeFrame property
     */
    @Override
    public void setLocalTimeFrame(TemporalFrame localTimeFrame)
    {
        this.localTimeFrame = localTimeFrame;
    }
    
    
    /**
     * Gets the platform property
     */
    @Override
    public DeployedSystem getPlatform()
    {
        return platform;
    }
    
    
    /**
     * Checks if platform is set
     */
    @Override
    public boolean isSetPlatform()
    {
        return (platform != null);
    }
    
    
    /**
     * Sets the platform property
     */
    @Override
    public void setPlatform(DeployedSystem platform)
    {
        this.platform = platform;
    }
    
    
    /**
     * Gets the list of deployed systems
     */
    @Override
    public OgcPropertyList<DeployedSystem> getDeployedSystemList()
    {
        return deployedSystems;
    }
    
    
    /**
     * Returns number of deployed systems
     */
    @Override
    public int getNumDeployedSystems()
    {
        return deployedSystems.size();
    }
    
    
    /**
     * Gets the deployed system with the given name
     */
    @Override
    public DeployedSystem getDeployedSystem(String name)
    {
        return deployedSystems.get(name);
    }
    
    
    /**
     * Adds a new deployed system
     */
    @Override
    public void addDeployedSystem(String name, DeployedSystem sys)
    {
        deployedSystems.add(name, sys);
    }
}
