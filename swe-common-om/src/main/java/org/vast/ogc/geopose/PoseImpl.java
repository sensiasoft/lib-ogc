/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.
 
Copyright (C) 2023 Sensia Software LLC. All Rights Reserved.
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ogc.geopose;

import org.vast.swe.SWEConstants;
import org.vast.util.Asserts;
import org.vast.util.BaseBuilder;


public class PoseImpl implements Pose
{
    private static final long serialVersionUID = 2054521303340034670L;
    protected String refFrame;
    protected String ltpRefFrame;
    protected String localFrame;
    protected double[] position;
    protected double[] orientation;
    
    
    @Override
    public String getReferenceFrame()
    {
        return refFrame;
    }


    @Override
    public void setReferenceFrame(String uri)
    {
        this.refFrame = uri;
    }


    @Override
    public String getLTPReferenceFrame()
    {
        return ltpRefFrame;
    }


    @Override
    public void setLTPReferenceFrame(String uri)
    {
        this.ltpRefFrame = uri;
    }
    
    
    @Override
    public String getLocalFrame()
    {
        return localFrame;
    }


    @Override
    public void setLocalFrame(String uri)
    {
        this.localFrame = uri;
    }


    @Override
    public double[] getPosition()
    {
        return position;
    }


    @Override
    public void setPosition(double[] coords)
    {
        Asserts.checkArgument(coords.length == 2 || coords.length == 3, "Position must be 2D or 3D");
        this.position = coords;
    }


    @Override
    public double[] getOrientation()
    {
        return orientation;
    }


    @Override
    public void setOrientation(double[] coords)
    {
        Asserts.checkArgument(coords.length == 3 || coords.length == 4, "Orientation must be 3D Euler angles or 4-components quaternion");
        this.orientation = coords;
    }
    
    
    public static class PoseBuilder extends BaseBuilder<Pose>
    {
        PoseBuilder()
        {
            this.instance = new PoseImpl();
        }
        
        
        public PoseBuilder referenceFrame(String uri)
        {
            this.instance.setReferenceFrame(uri);
            return this;
        }
        
        
        public PoseBuilder ltpReferenceFrame(String uri)
        {
            this.instance.setLTPReferenceFrame(uri);
            return this;
        }
        
        
        public PoseBuilder localFrame(String uri)
        {
            this.instance.setLocalFrame(uri);
            return this;
        }
        
        
        public PoseBuilder position(double[] coords)
        {
            this.instance.setPosition(coords);
            return this;
        }
        
        
        public PoseBuilder llaPos(double lat, double lon, double h)
        {
            this.instance.setReferenceFrame(SWEConstants.REF_FRAME_4979);
            this.instance.setPosition(new double[] {lat, lon , h});
            return this;
        }
        
        
        public PoseBuilder xyzPos(double x, double y, double z)
        {
            this.instance.setPosition(new double[] {x, y, z});
            return this;
        }
        
        
        public PoseBuilder orientation(double[] coords)
        {
            this.instance.setOrientation(coords);
            return this;
        }
        
        
        public PoseBuilder eulerAngles(double yaw, double pitch, double roll)
        {
            this.instance.setOrientation(new double[] {yaw, pitch, roll});
            return this;
        }
        
        
        public PoseBuilder quaternion(double x, double y, double z, double w)
        {
            this.instance.setOrientation(new double[] {x, y, z, w});
            return this;
        }
    }

}
