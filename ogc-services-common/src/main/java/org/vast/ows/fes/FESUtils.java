/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.
 
Copyright (C) 2012-2015 Sensia Software LLC. All Rights Reserved.
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ows.fes;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import net.opengis.fes.v20.BinarySpatialOp;
import net.opengis.fes.v20.BinaryTemporalOp;
import net.opengis.fes.v20.FilterCapabilities;
import net.opengis.fes.v20.ValueReference;
import org.vast.ows.OWSCommonUtils;
import org.vast.ows.OWSException;
import org.vast.util.Bbox;
import org.vast.util.TimeExtent;
import org.vast.xml.DOMHelper;
import org.vast.xml.XMLImplFinder;
import org.vast.xml.XMLReaderException;
import org.vast.xml.XMLWriterException;
import org.w3c.dom.Element;


public class FESUtils extends OWSCommonUtils
{
    public final static String FES = "FES";
    public final static String V2_0 = "2.0";
    
    protected static Pattern NS_DECL_PATTERN = Pattern.compile("xmlns\\(.*\\)(,xmlns\\(.*\\))*");
    protected static Pattern NS_DECL_SPLIT = Pattern.compile("(\\),)?xmlns\\(");
    
    FESStaxBindings filterBindings;
    
    
    public FESUtils(String version)
    {
        // TODO load correct bindings for desired version
        filterBindings = new FESStaxBindings(true);
    }
    
    
    /**
     * Reads the KVP argument encoding the temporal filter
     * @param arg arg argument value extracted from query string
     * @param namespaceMap
     * @return temporal operator
     * @throws OWSException
     */
    public BinaryTemporalOp readKVPTemporalFilter(String arg, Map<String, String> namespaceMap) throws OWSException
    {
        try
        {
            int firstComma = arg.indexOf(',');
            String valueRef = arg.substring(0, firstComma);
            String isoTime = arg.substring(firstComma + 1);
            
            // parse iso time string
            TimeExtent time = parseTimeArg(isoTime);
            BinaryTemporalOp timeOp = FESRequestUtils.timeExtentToFilter(time);
            
            // set value reference
            // TODO use namespace map
            ((ValueReference)timeOp.getOperand1()).setValue(valueRef);
            
            return timeOp;
        }
        catch (Exception e)
        {
            throw new OWSException("Invalid temporal filter: " + arg, e);
        }
    }
    
    
    /**
     * Reads the KVP argument encoding the spatial filter
     * @param arg argument value extracted from query string
     * @param namespaceMap
     * @return spatial operator
     * @throws OWSException
     */
    public BinarySpatialOp readKVPSpatialFilter(String arg, Map<String, String> namespaceMap) throws OWSException
    {
        try
        {
            int firstComma = arg.indexOf(',');
            String valueRef = arg.substring(0, firstComma);
            String bboxString = arg.substring(firstComma + 1);
            
            // parse bbox string
            Bbox bbox = parseBboxArg(bboxString);
            BinarySpatialOp spatialOp = FESRequestUtils.bboxToFilter(bbox);
            
            // set value reference
            // TODO use namespace map
            ((ValueReference)spatialOp.getOperand1()).setValue(valueRef);
            
            return spatialOp;
        }
        catch (Exception e)
        {
            throw new OWSException("Invalid spatial filter: " + arg, e);
        }
    }
    
    
    /**
     * Reads the KVP argument containing namespace prefix declarations
     * @param argValue KVP argument value
     * @return namespace map
     * @throws OWSException 
     */
    public Map<String, String> readKVPNamespaces(String argValue) throws OWSException
    {
        //if (!NS_DECL_PATTERN.matcher(argValue).matches())
        //    throw new SOSException(SOSException.invalid_param_code, "namespaces", null, null);
        
        Map<String, String> namespaceMap = new HashMap<String, String>();
        String[] nsList = NS_DECL_SPLIT.split(argValue);
        for (String ns: nsList)
        {
            if (ns.length() > 0)
            {
                String[] nsElts = ns.split(",");
                namespaceMap.put(nsElts[0], nsElts[1]);
            }
        }
        
        return namespaceMap;
    }
    
    
    /**
     * Reads temporal filter encoded according to OGC filter schema v2.0
     * @param timeOpElt
     * @return temporal operator
     * @throws XMLReaderException
     */
    public BinaryTemporalOp readXMLTemporalFilter(Element timeOpElt) throws XMLReaderException
    {
        try
        {
            DOMSource domSrc = new DOMSource(timeOpElt);
            XMLStreamReader reader = XMLImplFinder.getStaxInputFactory().createXMLStreamReader(domSrc);
            reader.nextTag();
            return (BinaryTemporalOp)filterBindings.readTemporalOps(reader);
        }
        catch (Exception e)
        {
            throw new XMLReaderException("Error while reading temporal filter", timeOpElt, e);
        }
    }
    
    
    /**
     * Writes a temporal filter according to OGC filter schema v2.0
     * @param dom
     * @param temporalOp
     * @return DOM element containing the filter XML 
     * @throws XMLWriterException
     */
    public Element writeTemporalFilter(DOMHelper dom, BinaryTemporalOp temporalOp) throws XMLWriterException
    {
        try
        {
            DOMResult result = new DOMResult(dom.createElement("fragment"));
            XMLStreamWriter writer = XMLImplFinder.getStaxOutputFactory().createXMLStreamWriter(result);
            filterBindings.setNamespacePrefixes(writer);
            filterBindings.declareNamespacesOnRootElement();
            filterBindings.writeTemporalOps(writer, temporalOp);
            return (Element)result.getNode().getFirstChild();            
        }
        catch (Exception e)
        {
            throw new XMLWriterException("Error while writing temporal filter", e);
        }
    }
    
    
    /**
     * Reads spatial filter encoded according to OGC filter XML schema
     * @param spatialOpElt
     * @return spatial operator
     * @throws XMLReaderException 
     */
    public BinarySpatialOp readXMLSpatialFilter(Element spatialOpElt) throws XMLReaderException
    {
        try
        {
            DOMSource domSrc = new DOMSource(spatialOpElt);
            XMLStreamReader reader = XMLImplFinder.getStaxInputFactory().createXMLStreamReader(domSrc);
            reader.nextTag();
            return (BinarySpatialOp)filterBindings.readSpatialOps(reader);
        }
        catch (Exception e)
        {
            throw new XMLReaderException("Error while reading spatial filter", spatialOpElt, e);
        }
    }
    
    
    /**
     * Writes a spatial filter according to OGC filter schema v2.0
     * @param dom
     * @param spatialOp
     * @return DOM element containing the filter XML 
     * @throws XMLWriterException
     */
    public Element writeSpatialFilter(DOMHelper dom, BinarySpatialOp spatialOp) throws XMLWriterException
    {
        try
        {
            DOMResult result = new DOMResult(dom.createElement("fragment"));
            XMLStreamWriter writer = XMLImplFinder.getStaxOutputFactory().createXMLStreamWriter(result);
            filterBindings.setNamespacePrefixes(writer);
            filterBindings.declareNamespacesOnRootElement();
            filterBindings.writeSpatialOps(writer, spatialOp);
            return (Element)result.getNode().getFirstChild();
        }
        catch (Exception e)
        {
            throw new XMLWriterException("Error while writing temporal filter", e);
        }
    }
    
    
    /**
     * Reads filter capabilities
     * @param filterCapsElt
     * @return filter capabilities
     * @throws XMLReaderException 
     */
    public FilterCapabilities readFilterCapabilities(Element filterCapsElt) throws XMLReaderException
    {
        try
        {
            DOMSource domSrc = new DOMSource(filterCapsElt);
            XMLStreamReader reader = XMLImplFinder.getStaxInputFactory().createXMLStreamReader(domSrc);
            reader.nextTag();
            return (FilterCapabilities)filterBindings.readFilterCapabilities(reader);
        }
        catch (Exception e)
        {
            throw new XMLReaderException("Error while reading filter capabilities", filterCapsElt, e);
        }
    }
    
    
    /**
     * Writes filter capabilities according to OGC filter schema v2.0
     * @param dom
     * @param filterCaps
     * @return DOM element containing the filter capabilities XML 
     * @throws XMLWriterException
     */
    public Element writeFilterCapabilities(DOMHelper dom, FilterCapabilities filterCaps) throws XMLWriterException
    {
        try
        {
            DOMResult result = new DOMResult(dom.createElement("fragment"));
            XMLStreamWriter writer = XMLImplFinder.getStaxOutputFactory().createXMLStreamWriter(result);
            filterBindings.setNamespacePrefixes(writer);
            filterBindings.declareNamespacesOnRootElement();
            filterBindings.writeFilterCapabilities(writer, filterCaps);
            return (Element)result.getNode().getFirstChild();
        }
        catch (Exception e)
        {
            throw new XMLWriterException("Error while writing filter capabilities", e);
        }
    }
    
    
    public void resetIdCounters()
    {
        
    }
}
