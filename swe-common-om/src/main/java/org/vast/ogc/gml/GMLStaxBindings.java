/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are Copyright (C) 2014 Sensia Software LLC.
 All Rights Reserved.
 
 Contributor(s): 
    Alexandre Robin
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ogc.gml;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import org.vast.ogc.xlink.IXlinkReference;
import org.vast.util.TimeExtent;
import net.opengis.gml.v32.AbstractFeature;
import net.opengis.gml.v32.AbstractGML;
import net.opengis.gml.v32.AbstractGeometry;
import net.opengis.gml.v32.AbstractTimeGeometricPrimitive;
import net.opengis.gml.v32.AbstractTimePrimitive;
import net.opengis.gml.v32.Measure;
import net.opengis.gml.v32.bind.XMLStreamBindings;
import net.opengis.gml.v32.impl.GMLFactory;


/**
 * <p>
 * Helper wrapping the auto-generated GML StAX bindings
 * </p>
 *
 * @author Alex Robin
 * @since Sep 25, 2014
 */
public class GMLStaxBindings extends XMLStreamBindings
{
    public final static String NS_PREFIX_GML = "gml";
    public final static String NS_PREFIX_XLINK = "xlink";

    protected GmlIdGenerator<AbstractGML> geomIds = new SequentialIdGenerator<>("G", true);
    protected GmlIdGenerator<AbstractGML> timeIds = new SequentialIdGenerator<>("T", true);
    protected GmlIdGenerator<AbstractGML> featureIds = new SequentialIdGenerator<>("F", true);
    protected StringBuilder sb = new StringBuilder();
    protected Map<QName, IFeatureStaxBindings<AbstractFeature>> featureTypesBindings;
    protected DecimalFormat formatter = new DecimalFormat(GMLFactory.COORDINATE_FORMAT);
    
    
    public GMLStaxBindings()
    {
        this(false);
    }
    
    
    public GMLStaxBindings(boolean useJTS)
    {
        this(new GMLFactory(useJTS));
    }
    
    
    public GMLStaxBindings(net.opengis.gml.v32.Factory fac)
    {
        super(fac);
        featureTypesBindings = new HashMap<>();
        nsContext.registerNamespace(NS_PREFIX_GML, net.opengis.gml.v32.bind.XMLStreamBindings.NS_URI);
        nsContext.registerNamespace(NS_PREFIX_XLINK, net.opengis.swe.v20.bind.XMLStreamBindings.XLINK_NS_URI);
    }
    
    
    public GMLFactory getFactory()
    {
        return (GMLFactory)factory;
    }
    
    
    public void registerFeatureBinding(IFeatureStaxBindings<AbstractFeature> binding)
    {
        for (QName fType: binding.getSupportedFeatureTypes())
            featureTypesBindings.put(fType, binding);
    }
    
    
    public GenericFeature readGenericFeature(XMLStreamReader reader) throws XMLStreamException
    {
        QName featureType = reader.getName();
        GenericFeature newFeature = new GenericFeatureImpl(featureType);
                
        Map<String, String> attrMap = collectAttributes(reader);
        this.readAbstractFeatureTypeAttributes(attrMap, newFeature);
        
        reader.nextTag();
        this.readAbstractFeatureTypeElements(reader, newFeature);
                
        // also read all other properties in a generic manner
        while (reader.getEventType() != XMLStreamConstants.END_ELEMENT)
        {
            QName propName = reader.getName();
            
            // skip until next non-whitespace event
            do {reader.next();}            
            while (reader.isWhiteSpace());
                        
            int eventType = reader.getEventType();            
            switch (eventType)
            {
                case XMLStreamReader.CHARACTERS:
                    String text = reader.getText();
                    if (text != null)
                        newFeature.setProperty(propName, text.trim());
                    reader.nextTag();
                    break;
                    
                case XMLStreamReader.START_ELEMENT:
                    String objName = reader.getLocalName();
                    if ("TimeInstant".equals(objName) ||
                        "TimePeriod".equals(objName))
                    {
                        newFeature.setProperty(propName, readAbstractTimeGeometricPrimitive(reader));
                        reader.nextTag();
                    }
                    else if ("Point".equals(objName) ||
                             "LineString".equals(objName) ||
                             "Polygon".equals(objName))
                    {
                        newFeature.setProperty(propName, readAbstractGeometry(reader));
                        reader.nextTag();
                    }
                    else
                        skipElementAndAllChildren(reader);
                    break;
                    
                default:
                    
            }
            
            reader.nextTag();
        }        
        
        return newFeature;
    }
    
    
    public void writeGenericFeature(XMLStreamWriter writer, IFeature bean) throws XMLStreamException
    {
        QName featureType = getFeatureQName(bean);
        String newPrefix = ensurePrefix(writer, featureType);
        
        // element name
        writer.writeStartElement(featureType.getNamespaceURI(), featureType.getLocalPart());
        if (newPrefix != null)
            writer.writeNamespace(newPrefix, featureType.getNamespaceURI());
        
        // write property namespaces if needed
        for (Entry<QName, Object> prop: bean.getProperties().entrySet())
            ensureNamespaceDecl(writer, prop.getKey());
        
        // common attributes and elements from AbstractFeature
        if (bean instanceof AbstractFeature)
        {
            writeAbstractFeatureTypeAttributes(writer, (AbstractFeature)bean);
            writeAbstractFeatureTypeElements(writer, (AbstractFeature)bean);
        }
        else
        {
            writeCommonFeatureProperties(writer, bean);
            writeCustomFeatureProperties(writer, bean);
        }
        
        // write all other properties
        for (Entry<QName, Object> prop: bean.getProperties().entrySet())
        {
            // prop name
            QName propName = prop.getKey();
            writer.writeStartElement(propName.getNamespaceURI(), propName.getLocalPart());
            
            // prop value
            Object val = prop.getValue();
            
            if (val instanceof IXlinkReference<?>)
            {
                String href = ((IXlinkReference<?>) val).getHref();
                if (href != null)
                    writer.writeAttribute(XLINK_NS_URI, "href", href);
            }
            else if (val instanceof AbstractGeometry)
            {
                writeAbstractGeometry(writer, (AbstractGeometry)val);
            }
            else if (val instanceof AbstractTimeGeometricPrimitive)
            {
                writeAbstractTimeGeometricPrimitive(writer, (AbstractTimeGeometricPrimitive)val);
            }
            else if (val instanceof Measure)
            {
                writer.writeAttribute("uom", ((Measure) val).getUom());
                writer.writeCharacters(Double.toString(((Measure) val).getValue()));
            }
            else
                writer.writeCharacters(val.toString());
                
            writer.writeEndElement();
        }
        
        writer.writeEndElement();
    }
    
    
    protected void writeCommonFeatureProperties(XMLStreamWriter writer, IFeature bean) throws XMLStreamException
    {
        if (bean.getDescription() != null)
        {
            writer.writeStartElement(NS_URI, "description");
            writer.writeCharacters(bean.getDescription());
            writer.writeEndElement();
        }
        
        if (bean.getName() != null)
        {
            writer.writeStartElement(NS_URI, "name");
            writer.writeCharacters(bean.getName());
            writer.writeEndElement();
        }
        
        // geometry
        if (bean instanceof IGeoFeature && ((IGeoFeature)bean).getGeometry() != null)
        {
            writer.writeStartElement(NS_URI, "location");
            this.writeAbstractGeometry(writer, ((IGeoFeature)bean).getGeometry());
            writer.writeEndElement();
        }
        
        // validTime
        if (bean instanceof ITemporalFeature && ((ITemporalFeature)bean).getValidTime() != null)
        {
            writer.writeStartElement(NS_URI, "validTime");
            TimeExtent validTime = ((ITemporalFeature)bean).getValidTime();
            this.writeAbstractTimeGeometricPrimitive(writer, GMLUtils.timeExtentToTimePrimitive(validTime, false));
            writer.writeEndElement();
        }
    }
    
    
    protected void writeCustomFeatureProperties(XMLStreamWriter writer, IFeature bean) throws XMLStreamException
    {
        String nsUri = getFeatureQName(bean).getNamespaceURI();
        
        // write all custom properties with supported data type
        for (Entry<QName, Object> prop: bean.getProperties().entrySet())
        {
            QName propName = prop.getKey();
            Object val = prop.getValue();
            String propNsUri = propName.getNamespaceURI() != null ? propName.getNamespaceURI() : nsUri;
            
            if (val instanceof Boolean)
            {
                writer.writeStartElement(propNsUri, propName.getLocalPart());
                writer.writeCharacters(val.toString());
                writer.writeEndElement();
            }
            else if (val instanceof Number)
            {
                writer.writeStartElement(propNsUri, propName.getLocalPart());
                writer.writeCharacters(val.toString());
                writer.writeEndElement();
            }
            else if (val instanceof String)
            {
                writer.writeStartElement(propNsUri, propName.getLocalPart());
                writer.writeCharacters(val.toString());
                writer.writeEndElement();
            }
            else if (val instanceof IXlinkReference<?>)
            {
                String href = ((IXlinkReference<?>) val).getHref();
                if (href != null) 
                {
                    writer.writeStartElement(propNsUri, propName.getLocalPart());
                    writer.writeAttribute(NS_PREFIX_XLINK, "href", href);
                    writer.writeEndElement();
                }
            }
            else if (val instanceof Measure)
            {
                writer.writeStartElement(propNsUri, propName.getLocalPart());
                writer.writeAttribute("uom", ((Measure) val).getUom());
                writer.writeCharacters(Double.toString(((Measure) val).getValue()));
                writer.writeEndElement();
            }
            else if (val instanceof AbstractGeometry &&
                (bean instanceof IGeoFeature && val != ((IGeoFeature)bean).getGeometry()) )
            {
                writer.writeStartElement(propNsUri, propName.getLocalPart());
                writeAbstractGeometry(writer, (AbstractGeometry)val);
                writer.writeEndElement();
            }
            else if (val instanceof AbstractTimeGeometricPrimitive &&
                !GenericTemporalFeatureImpl.PROP_VALID_TIME.equals(propName))
            {
                writer.writeStartElement(propNsUri, propName.getLocalPart());
                writeAbstractTimeGeometricPrimitive(writer, (AbstractTimeGeometricPrimitive)val);
                writer.writeEndElement();
            }
        }
    }
    
    
    @Override
    public AbstractFeature readAbstractFeature(XMLStreamReader reader) throws XMLStreamException
    {
        QName featureType = reader.getName();
        IFeatureStaxBindings<AbstractFeature> customBindings = featureTypesBindings.get(featureType);
        
        if (customBindings != null)
            return (AbstractFeature)customBindings.readFeature(reader, featureType);
        else if (featureType.getNamespaceURI().equals(NS_URI))
            return super.readAbstractFeature(reader);
        else
            return readGenericFeature(reader);
    }


    @Override
    public void writeAbstractFeature(XMLStreamWriter writer, AbstractFeature bean) throws XMLStreamException
    {
        QName featureType = bean.getQName();
        IFeatureStaxBindings<AbstractFeature> customBindings = featureTypesBindings.get(featureType);
        
        if (customBindings != null)
            customBindings.writeFeature(writer, bean);
        else
            this.writeGenericFeature(writer, bean);
    }


    @Override
    public void writeAbstractGMLTypeAttributes(XMLStreamWriter writer, AbstractGML bean) throws XMLStreamException
    {        
        String gmlID;
        
        // automatically generate gml:id if not set
        if (bean instanceof AbstractGeometry)
            gmlID = geomIds.nextId(bean);
        else if (bean instanceof AbstractTimePrimitive)
            gmlID = timeIds.nextId(bean);
        else if (bean instanceof AbstractFeature)
            gmlID = featureIds.nextId(bean);
        else
            gmlID = getStringValue(bean.getId());
        
        writer.writeAttribute(nsContext.getPrefix(NS_URI), NS_URI, "id", gmlID);
    }
    
    
    @Override
    protected String getCoordinateStringValue(double[] coords)
    {
        sb.setLength(0);
        
        for (double val: coords) {
            sb.append(formatter.format(val));
            sb.append(' ');
        }
        
        sb.setLength(sb.length()-1);
        return sb.toString();
    }
    
    
    public static QName getFeatureQName(IFeature f)
    {
        if (f instanceof AbstractFeature)
            return ((AbstractFeature) f).getQName();
        else
            return new QName(f.getClass().getPackageName(), f.getClass().getSimpleName());
    }
}
