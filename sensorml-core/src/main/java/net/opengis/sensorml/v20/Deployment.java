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

import javax.xml.namespace.QName;
import net.opengis.OgcPropertyList;


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
     * Gets the definition property
     */
    public String getDefinition();
    
    
    /**
     * Checks if definition is set
     */
    public boolean isSetDefinition();
    
    
    /**
     * Sets the definition property
     */
    public void setDefinition(String definition);
    
    
    /**
     * Gets the localReferenceFrame property
     */
    public SpatialFrame getLocalReferenceFrame();
    
    
    /**
     * Checks if localReferenceFrame is set
     */
    public boolean isSetLocalReferenceFrame();
    
    
    /**
     * Sets the localReferenceFrame property
     */
    public void setLocalReferenceFrame(SpatialFrame localReferenceFrame);
    
    
    /**
     * Gets the localTimeFrame property
     */
    public TemporalFrame getLocalTimeFrame();
    
    
    /**
     * Checks if localTimeFrame is set
     */
    public boolean isSetLocalTimeFrame();
    
    
    /**
     * Sets the localTimeFrame property
     */
    public void setLocalTimeFrame(TemporalFrame localTimeFrame);
    
    
    /**
     * Gets the reference to the platform
     */
    public DeployedSystem getPlatform();
    
    
    /**
     * Checks if platform is set
     */
    public boolean isSetPlatform();
    
    
    /**
     * Sets the platform
     */
    public void setPlatform(DeployedSystem platform);
    
    
    /**
     * Gets the list of deployed systems
     */
    public OgcPropertyList<DeployedSystem> getDeployedSystemList();
    
    
    /**
     * Returns number of deployed systems
     */
    public int getNumDeployedSystems();
    
    
    /**
     * Gets the deployed system with the given name
     */
    public DeployedSystem getDeployedSystem(String name);
    
    
    /**
     * Adds a new deployed system
     */
    public void addDeployedSystem(String name, DeployedSystem sys);
    
}
