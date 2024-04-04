/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.
 
Copyright (C) 2024 Sensia Software LLC. All Rights Reserved.
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.data;

import org.vast.swe.SWEConstants;
import org.vast.swe.helper.GeoPosHelper;
import net.opengis.OgcProperty;
import net.opengis.OgcPropertyImpl;
import net.opengis.swe.v20.AllowedGeoms;
import net.opengis.swe.v20.Count;
import net.opengis.swe.v20.DataArray;
import net.opengis.swe.v20.DataBlock;
import net.opengis.swe.v20.DataComponentVisitor;
import net.opengis.swe.v20.DataType;
import net.opengis.swe.v20.GeometryData;


public class GeometryDataImpl extends DataChoiceImpl implements GeometryData
{
    private static final long serialVersionUID = 1327491468723187801L;
    protected String referenceFrame = SWEConstants.REF_FRAME_CRS84h;
    protected OgcProperty<AllowedGeoms> constraint;
    protected int numDims = 3;
    protected DataArray coordArray1, coordArray2, coordArray3;
    
    
    public GeometryDataImpl()
    {
        var helper = new GeoPosHelper();
        
        // point
        coordArray1 = helper.createArray()
            .withFixedSize(numDims)
            .withElement("val", helper.createQuantity())
            .build();
        addItem(GeomType.Point.toString(), coordArray1);
        
        // linestring
        Count lineSizeComp;
        var line = helper.createRecord()
            .addField("numPoints", lineSizeComp = helper.createCount()
                .id("NUM_POINTS")
                .definition(SWEConstants.DEF_NUM_POINTS)
                .build())
            .addField("coordinates", helper.createArray()
                .withSizeComponent(lineSizeComp)
                .withElement("point", coordArray2 = helper.createArray()
                    .withFixedSize(numDims)
                    .withElement("val", helper.createQuantity())
                    .build()))
            .build();
        addItem(GeomType.LineString.toString(), line);
        
        // polygon
        Count numRingComp;
        Count numPointsComp;
        var poly = helper.createRecord()
            .addField("numRings", numRingComp = helper.createCount()
                .id("NUM_RINGS")
                .definition(SWEConstants.DEF_NUM_ROWS)
                .build())
            .addField("rings", helper.createArray()
                .withSizeComponent(numRingComp)
                .withElement("ring", helper.createRecord()
                    .addField("numPoints", numPointsComp = helper.createCount()
                        .id("NUM_POINTS")
                        .definition(SWEConstants.DEF_NUM_POINTS)
                        .build())
                    .addField("points", helper.createArray()
                        .withSizeComponent(numPointsComp)
                        .withElement("point", coordArray3 = helper.createArray()
                            .withFixedSize(numDims)
                            .withElement("val", helper.createQuantity())
                            .build()))
                    )
                )
            .build();
        addItem(GeomType.Polygon.toString(), poly);
    }
    
    
    @Override
    public GeometryDataImpl copy()
    {
        GeometryDataImpl newObj = new GeometryDataImpl();
        super.copyTo(newObj);
        
        newObj.selected = selected;
        newObj.referenceFrame = referenceFrame;
        newObj.numDims = numDims;
        
        if (constraint != null)
            newObj.constraint = constraint.copy();
        else
            newObj.constraint = null;
        
        return newObj;
    }
    
    
    @Override
    public void setGeomType(GeomType geomType)
    {
        super.setSelectedItem(geomType.ordinal());
    }


    @Override
    public GeomType getGeomType()
    {
        var selectedIdx = getSelected();
        return GeomType.values()[selectedIdx];
    }


    @Override
    public AllowedGeoms getConstraint()
    {
        if (constraint == null)
            return null;
        return constraint.getValue();
    }


    @Override
    public OgcProperty<AllowedGeoms> getConstraintProperty()
    {
        if (constraint == null)
            constraint = new OgcPropertyImpl<>();
        return constraint;
    }


    @Override
    public boolean isSetConstraint()
    {
        return (constraint != null && (constraint.hasValue() || constraint.hasHref()));
    }


    @Override
    public void setConstraint(AllowedGeoms constraint)
    {
        if (this.constraint == null)
            this.constraint = new OgcPropertyImpl<>();
        this.constraint.setValue(constraint);
    }


    @Override
    public String getReferenceFrame()
    {
        return referenceFrame;
    }
    
    
    @Override
    public boolean isSetReferenceFrame()
    {
        return (referenceFrame != null);
    }
    
    
    @Override
    public void setReferenceFrame(String referenceFrame)
    {
        this.referenceFrame = referenceFrame;
        
        // compute num dims
        if (SWEConstants.REF_FRAME_CRS84.equals(referenceFrame))
            setNumDims(2);
        else if (SWEConstants.REF_FRAME_CRS84h.equals(referenceFrame))
            setNumDims(3);
        else if (SWEConstants.REF_FRAME_4326.equals(referenceFrame))
            setNumDims(2);
        else if (SWEConstants.REF_FRAME_4979.equals(referenceFrame))
            setNumDims(3);
    }


    @Override
    public int getNumDims()
    {
        return numDims;
    }


    @Override
    public void setNumDims(int numDims)
    {
        this.numDims = numDims;
        
        // also set on nested arrays
        ((DataArrayImpl)coordArray1).setFixedSize(numDims);
        ((DataArrayImpl)coordArray2).setFixedSize(numDims);
        ((DataArrayImpl)coordArray3).setFixedSize(numDims);
    }
    
    
    @Override
    public void setDataType(DataType dataType)
    {
        
    }
    
    
    @Override
    public void setData(DataBlock dataBlock)
    {
        super.setData(dataBlock);
        
        // try to infer num dims from data
        // not a good idea since it can be inconsistent with CRS
        /*numDims = computeNumDims(dataBlock);
        ((DataArrayImpl)coordArray1).updateSizeComponent(numDims);
        ((DataArrayImpl)coordArray2).updateSizeComponent(numDims);
        ((DataArrayImpl)coordArray3).updateSizeComponent(numDims);*/
        
    }
    
    
    public static int computeNumDims(DataBlock dataBlock)
    {
        int selected = dataBlock.getIntValue(0);
        int numDims = 0;
        
        if (selected == 0) // point
        {
            numDims = dataBlock.getAtomCount() - 1;
        }
        else if (selected == 1) // linestring
        {
            int numPoints = dataBlock.getIntValue(1);
            if (numPoints != 0)
                numDims = (dataBlock.getAtomCount() - 2) / numPoints;
        }
        else if (selected == 2) // polygon
        {
            var polyData = ((DataBlockMixed)dataBlock).getUnderlyingObject()[1];
            var ringListData = ((DataBlockMixed)polyData).getUnderlyingObject()[1];
            if (((DataBlockList)ringListData).getListSize() > 0)
            {
                var firstRingData = ((DataBlockList)ringListData).get(0);
                int numPoints = firstRingData.getIntValue(0);
                if (numPoints != 0)
                    numDims = (firstRingData.getAtomCount() - 1) / numPoints;
            }
        }
        
        return numDims;
    }


    @Override
    public void accept(DataComponentVisitor visitor)
    {
        visitor.visit((GeometryData)this);
    }


    @Override
    public String getLocalFrame()
    {
        return null;
    }


    @Override
    public boolean isSetLocalFrame()
    {
        return false;
    }


    @Override
    public void setLocalFrame(String localFrame)
    {
        throw new UnsupportedOperationException();
    }
}
