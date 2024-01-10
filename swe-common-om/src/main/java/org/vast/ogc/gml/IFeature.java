/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are copyright (C) 2019, Sensia Software LLC
 All Rights Reserved. This software is the property of Sensia Software LLC.
 It cannot be duplicated, used, or distributed without the express written
 consent of Sensia Software LLC.
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ogc.gml;

import java.util.Collections;
import java.util.Map;
import javax.xml.namespace.QName;
import org.vast.util.IResource;
import org.vast.util.TimeExtent;
import net.opengis.gml.v32.AbstractGeometry;


/**
 * <p>
 * Simple interface for feature objects
 * </p>
 *
 * @author Alex Robin
 * @date Sep 5, 2019
 */
public interface IFeature extends IResource
{
    static final QName DEFAULT_QNAME = new QName(GMLStaxBindings.NS_URI, "Feature");
    
    
    /**
     * @return the local/internal ID of the feature (often automatically
     * assigned by the feature repository)
     */
    public String getId();
    
    
    /**
     * @return the globally unique identifier that can be used to identify
     * the same feature across contexts (e.g. across repositories)
     */
    public String getUniqueIdentifier();
    
    
    /**
     * @return the feature type URI
     */
    public default String getType()
    {
        return null;
    }
    
    
    /**
     * @return the Qualified name of the feature type
     */
    public default QName getQName()
    {
        var type = getType();
        return type != null ? GMLUtils.uriToQName(type) : IFeature.DEFAULT_QNAME;
    }
    
    
    /**
     * @return the geometry/location (or null if feature has no geometry)
     */
    public default AbstractGeometry getGeometry()
    {
        return null;
    }
    
    
    /**
     * To be overriden by subclasses when a property other than location provides
     * the geometry. In this case, no location property is serialized
     * @return True if a custom property contains the geometry
     */
    public default boolean hasCustomGeomProperty()
    {
        return false;
    }
    
    
    /**
     * @return feature validity period (or null if always valid)
     */
    public default TimeExtent getValidTime()
    {
        return null;
    }
    
    
    /**
     * To be overriden by subclasses when a property other than validTime provides
     * the feature time. In this case, no validTime property is serialized
     * @return True if a custom property contains the feature time stamp
     */
    public default boolean hasCustomTimeProperty()
    {
        return false;
    }
    
    
    /**
     * @return the property list
     */
    public default Map<QName, Object> getProperties()
    {
        return Collections.emptyMap();
    }
}
