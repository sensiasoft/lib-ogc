/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.
 
Copyright (C) 2012-2015 Sensia Software LLC. All Rights Reserved.
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ogc.om;

import javax.xml.namespace.QName;
import net.opengis.OgcPropertyImpl;
import net.opengis.gml.v32.AbstractGeometry;
import net.opengis.gml.v32.LineString;
import net.opengis.gml.v32.Point;
import net.opengis.gml.v32.Polygon;
import org.vast.ogc.gml.ExtensibleFeatureImpl;
import org.vast.ogc.gml.FeatureRef;
import org.vast.ogc.gml.GMLUtils;
import org.vast.ogc.xlink.CachedReference;
import org.vast.ogc.xlink.IXlinkReference;
import org.vast.swe.SWEConstants;
import org.vast.util.Asserts;
import com.google.common.collect.ImmutableMap;


public class SamplingFeature<GeomType extends AbstractGeometry> extends ExtensibleFeatureImpl
{
    private static final long serialVersionUID = 6351566323396110876L;
    
    public static final String SAMS_NS_PREFIX = "sams";
    public static final String SAMS_NS_URI = "http://www.opengis.net/samplingSpatial/2.0";
    public static final String SF_NS_PREFIX = "sf";
    public static final String SF_NS_URI = "http://www.opengis.net/sampling/2.0";
    
    public static final QName QNAME = new QName(SAMS_NS_URI, "SF_SpatialSamplingFeature", SAMS_NS_PREFIX);
    public static final String FTYPE = GMLUtils.qNameToUri(QNAME);
    public static final QName PROP_TYPE = new QName(SF_NS_URI, "type", SF_NS_PREFIX);
    public static final QName PROP_SAMPLED_FEATURE = new QName(SF_NS_URI, "sampledFeature", SF_NS_PREFIX);
    public static final QName PROP_HOSTED_PROCEDURE = new QName(SAMS_NS_URI, "hostedProcedure", SAMS_NS_PREFIX);
    public static final QName PROP_SHAPE = new QName(SAMS_NS_URI, "shape", SAMS_NS_PREFIX);
    
    protected IXlinkReference<Void> type;
    protected IXlinkReference<?> sampledFeature;
    protected IXlinkReference<?> hostedProcedure;
    
    
    public SamplingFeature()
    {
    }
    
    
    public SamplingFeature(String type)
    {
        this();
        setSfType(type);
    }
    
    
    public QName getQName()
    {
        return QNAME;
    }
    
    
    public String getType()
    {
        return FTYPE;
    }
    
    
    public String getSfType()
    {
        if (type == null)
            return null;
        return type.getHref();
    }


    public void setSfType(String type)
    {
        properties = null; // reset cached properties map
        this.type = new CachedReference<Void>(type);
    }
    
    
    public void setSampledFeature(IXlinkReference<?> ref)
    {
        this.sampledFeature = Asserts.checkNotNull(ref, IXlinkReference.class);
    }
    
    
    public void setSampledFeature(String title, String href)
    {
        properties = null; // reset cached properties map
        sampledFeature = new OgcPropertyImpl<>();
        sampledFeature.setHref(Asserts.checkNotNullOrBlank(href, "href"));
        sampledFeature.setTitle(title);
    }
    
    
    public IXlinkReference<?> getSampledFeature()
    {
        return sampledFeature;
    }
    
    
    public void setHostedProcedureUID(String procUID)
    {
        properties = null; // reset cached properties map
        this.hostedProcedure = procUID != null ? new ProcedureRef(procUID) : null;
    }
    
    
    public String getHostedProcedureUID()
    {
        return hostedProcedure != null ? hostedProcedure.getHref() : null;
    }
    
    
    public void setShape(GeomType geom)
    {
        properties = null; // reset cached properties map
        setGeometry(geom);
    }
    
    
    @SuppressWarnings("unchecked")
    public GeomType getShape()
    {
        return (GeomType)getGeometry();
    }
    
    
    protected void appendProperties(ImmutableMap.Builder<QName, Object> builder)
    {
        builder.put(PROP_TYPE, type);
        
        // sampled feature is a mandatory property
        if (sampledFeature != null)
            builder.put(PROP_SAMPLED_FEATURE, sampledFeature != null ? sampledFeature : new FeatureRef<>(SWEConstants.NIL_UNKNOWN));
        
        if (hostedProcedure != null)
            builder.put(PROP_HOSTED_PROCEDURE, hostedProcedure);
        
        if (location != null && location.hasValue())
            builder.put(PROP_SHAPE, getGeometry());
    }
    
    
    public SamplingFeature<? extends AbstractGeometry> getAsSpecializedType()
    {
        String type = getSfType();
        SamplingFeature<? extends AbstractGeometry> sf = null;
        
        if (SamplingPoint.TYPE.equals(type))
        {
            sf = new SamplingPoint();
            ((SamplingPoint)sf).setShape((Point)getShape());
        }
        else if (SamplingSurface.TYPE.equals(type))
        {
            sf = new SamplingSurface();
            ((SamplingSurface)sf).setShape((Polygon)getShape());
        }
        else if (SamplingCurve.TYPE.equals(type))
        {
            sf = new SamplingCurve();
            ((SamplingCurve)sf).setShape((LineString)getShape());
        }
        else
            throw new UnsupportedOperationException("Unsupported sampling feature type " + type);
        
        sf.setId(getId());
        sf.setIdentifier(getIdentifier());
        sf.setDescription(getDescription());
        sf.setName(getName());
        sf.setBoundedByAsEnvelope(getBoundedBy());
        if (sf.isSetGeometry())
            sf.setGeometry(getGeometry());
        sf.setSampledFeature(getSampledFeature());
        sf.setHostedProcedureUID(getHostedProcedureUID());
        
        return sf;
    }


    @Override
    public boolean hasCustomGeomProperty()
    {
        return true;
    }

}