/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.
 
Copyright (C) 2024 Sensia Software LLC. All Rights Reserved.
 
******************************* END LICENSE BLOCK ***************************/

package net.opengis.sensorml.v20.impl;

import java.io.Serializable;
import org.vast.ogc.geopose.Pose;
import net.opengis.gml.v32.Point;
import net.opengis.gml.v32.Reference;
import net.opengis.gml.v32.impl.AbstractGMLImpl;
import net.opengis.sensorml.v20.DeployedSystem;
import net.opengis.sensorml.v20.Settings;
import net.opengis.swe.v20.Text;
import net.opengis.swe.v20.Vector;


public class DeployedSystemImpl extends AbstractGMLImpl implements DeployedSystem
{
    private static final long serialVersionUID = -5479285519087297080L;

    protected Reference systemRef;
    protected Serializable position;
    protected Settings configuration;
    

    @Override
    public Reference getSystemRef()
    {
        return systemRef;
    }


    @Override
    public void setSystemRef(Reference systemRef)
    {
        this.systemRef = systemRef;
    }


    @Override
    public Serializable getPosition()
    {
        return position;
    }


    @Override
    public boolean isSetPosition()
    {
        return position != null;
    }


    @Override
    public void setPositionAsText(Text position)
    {
        this.position = position;
    }


    @Override
    public void setPositionAsPoint(Point position)
    {
        this.position = position;
    }


    @Override
    public void setPositionAsVector(Vector position)
    {
        this.position = position;
    }


    @Override
    public void setPositionAsPose(Pose pose)
    {
        this.position = pose;
    }


    @Override
    public Settings getConfiguration()
    {
        return configuration;
    }


    @Override
    public boolean isSetConfiguration()
    {
        return configuration != null;
    }


    @Override
    public void setConfiguration(Settings configuration)
    {
        this.configuration = configuration;
    }

}
