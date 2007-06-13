/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are subject to the Mozilla Public License Version
 1.1 (the "License"); you may not use this file except in compliance with
 the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/MPL-1.1.html
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.
 
 The Original Code is the "SensorML DataProcessing Engine".
 
 The Initial Developer of the Original Code is the
 University of Alabama in Huntsville (UAH).
 Portions created by the Initial Developer are Copyright (C) 2006
 the Initial Developer. All Rights Reserved.
 
 Contributor(s): 
    Alexandre Robin <robin@nsstc.uah.edu>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ogc;

import java.util.Hashtable;
import org.vast.util.ExceptionSystem;
import org.vast.xml.DOMHelper;
import org.vast.xml.DOMHelperException;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;


/**
 * <p><b>Title:</b>
 * OGC Registry
 * </p>
 *
 * <p><b>Description:</b><br/>
 * This class allows to keep track of what classes to use
 * to read/write different versions of service requests as well
 * as other (mostly xml) messages and documents. This class
 * obtains the default mappings from the OGCRegistry.xml file. 
 * </p>
 *
 * <p>Copyright (c) 2007</p>
 * @author Alexandre Robin
 * @date Jan 16, 2007
 * @version 1.0
 */
public class OGCRegistry
{
    public final static String XLINK_NS = "http://www.w3.org/1999/xlink";
    public final static String OGC_NS = "http://www.opengis.net/ogc";
    public final static String OWS_NS = "http://www.opengis.net/ows";
    public final static String GML_NS = "http://www.opengis.net/gml";
    public final static String OM_NS = "http://www.opengis.net/om";
    public final static String SWE_NS = "http://www.opengis.net/swe";
    public final static String SML_NS = "http://www.opengis.net/sensorML";
    public final static String SOS_NS = "http://www.opengis.net/sos";
    public final static String SAS_NS = "http://www.opengis.net/sas";
    public final static String SPS_NS = "http://www.opengis.net/sps";
    
    protected static Hashtable<String, Class> readerClasses;
    protected static Hashtable<String, Class> writerClasses;
        
    
    static
    {
        readerClasses = new Hashtable<String, Class>();
        writerClasses = new Hashtable<String, Class>();
        String mapFileUrl = OGCRegistry.class.getResource("OGCRegistry.xml").toString();
        loadMaps(mapFileUrl, false);
    }
    
    
    /**
     * Instantiates a reader object for the specified content type, subtype and version
     * @param type
     * @param subType
     * @param version
     * @return
     * @throws IllegalStateException
     */
    public static Object createReader(String type, String subType, String version) throws IllegalStateException
    {
        return createObject(readerClasses, type, subType, version);
    }
    
    
    /**
     * Instantiates a reader object for the specified content type and version
     * @param type
     * @param version
     * @return
     * @throws IllegalStateException
     */
    public static Object createReader(String type, String version) throws IllegalStateException
    {
        return createObject(readerClasses, type, null, version);
    }
    
    
    /**
     * Instantiates a writer object for the specified content type, subtype and version
     * @param type
     * @param subType
     * @param version
     * @return
     * @throws IllegalStateException
     */
    public static Object createWriter(String type, String subType, String version) throws IllegalStateException
    {
        return createObject(writerClasses, type, subType, version);
    }
    
    
    /**
     * Instantiates a writer object for the specified content type and version
     * @param type
     * @param version
     * @return
     * @throws IllegalStateException
     */
    public static Object createWriter(String type, String version) throws IllegalStateException
    {
        return createObject(writerClasses, type, null, version);
    }
    
    
    /**
     * Registers a reader class for given content type and version
     * @param type
     * @param subType
     * @param version
     * @param className
     * @throws IllegalStateException
     */
    public static void addReaderClass(String type, String subType, String version, String className) throws IllegalStateException
    {
        addClass(readerClasses, type, subType, version, className);
    }
    
    
    /**
     * Registers a writer class for given content type and version
     * @param type
     * @param subType
     * @param version
     * @param className
     * @throws IllegalStateException
     */
    public static void addWriterClass(String type, String subType, String version, String className) throws IllegalStateException
    {
        addClass(writerClasses, type, subType, version, className);
    }
    
    
    /**
     * Retrieves a registered reader class
     * @param type
     * @param subType
     * @param version
     * @return null if none found
     */
    public static Class getReaderClass(String type, String subType, String version)
    {
        return getClass(readerClasses, type, subType, version);
    }
    
    
    /**
     * Retrieves a registered writer class
     * @param type
     * @param subType
     * @param version
     * @return null if none found
     */
    public static Class getWriterClass(String type, String subType, String version)
    {
        return getClass(writerClasses, type, subType, version);
    }    
    
    
    /**
     * Handles registration of reader/writer classes into the tables
     * @param table
     * @param type
     * @param subType
     * @param version
     * @param className
     * @throws IllegalStateException
     */
    private static void addClass(Hashtable<String, Class> table, String type, String subType, String version, String className) throws IllegalStateException
    {
        type = normalizeTypeString(type);
        subType = normalizeSubtypeString(subType);
        version = normalizeVersionString(version);
        StringBuffer key = new StringBuffer(type);
        
        if (subType != null)
        {
            key.append('_');
            key.append(subType);
        }
        
        if (version != null)
        {
            key.append('_');
            key.append(version);
        }
        
        // try to instantiate corresponding class
        try
        {
            Class ioClass = Class.forName(className);
            table.put(key.toString(), ioClass);
        }
        catch (ClassNotFoundException e)
        {
            // problem if class is not found in class path
            throw new IllegalStateException("Error while registering reader/writer Class " + className, e);
        }
    }
    
    
    /**
     * Handles the retrieval of class objects from tables
     * @param table
     * @param type
     * @param subType
     * @param version
     * @return
     * @throws IllegalStateException
     */
    private static Class getClass(Hashtable<String, Class> table, String type, String subType, String version) throws IllegalStateException
    {
        Class ioClass;        
        StringBuffer key;
        type = normalizeTypeString(type);
        subType = normalizeSubtypeString(subType);
        version = normalizeVersionString(version);
        
        // first try to retrieve exactly what's asked
        key = new StringBuffer(type);        
        if (subType != null)
        {
            key.append('_');
            key.append(subType);
        }        
        if (version != null)
        {
            key.append('_');
            key.append(version);
        }
        ioClass = table.get(key.toString());
        if (ioClass != null)
            return ioClass;
        
        // if not found, try to find one without subType
        key = new StringBuffer(type);     
        if (version != null)
        {
            key.append('_');
            key.append(version);
        }
        ioClass = table.get(key.toString());
        if (ioClass != null)
            return ioClass;
        
        // if not found, try to find one without version
        key = new StringBuffer(type);     
        if (subType != null)
        {
            key.append('_');
            key.append(subType);
        }
        ioClass = table.get(key.toString());
        if (ioClass != null)
            return ioClass;
        
        // if not found, try to find one without version nor subType
        ioClass = table.get(type);
        if (ioClass != null)
            return ioClass;        
        
        throw new IllegalStateException("No reader/writer found for " + type + "/" + subType + " v" + version);
    }
        
    
    /**
     * Handles the instantiation of reader/writer classes
     * @param table
     * @param type
     * @param subType
     * @param version
     * @return
     */
    private static Object createObject(Hashtable<String, Class> table, String type, String subType, String version) throws IllegalStateException
    {
        Class objClass = getClass(table, type, subType, version);                                           
        
        // instantiate the reader class using reflection
        try
        {
            Object obj = objClass.newInstance();
            return obj;
        }
        catch (Exception e)
        {
            throw new IllegalStateException("Error while instantiating new reader/writer", e);
        }
    }
    
    
    /**
     * Loads an xml file containing mappings from types of readers/writers to class
     * @param xmlFileUrl
     * @param replace
     */
    public static void loadMaps(String xmlFileUrl, boolean replace)
    {
        try
        {
            // open mappings file
            DOMHelper dom = new DOMHelper(xmlFileUrl, false);
            
            // clear hashtables if requested
            if (replace)
            {
                readerClasses.clear();
                writerClasses.clear();
            }
            
            // add reader hashtable entries
            NodeList readerElts = dom.getElements("Reader");            
            for (int i=0; i<readerElts.getLength(); i++)
            {
                Element readerElt = (Element)readerElts.item(i);
                String type = dom.getAttributeValue(readerElt, "type");
                String subType = dom.getAttributeValue(readerElt, "subType");
                String version = dom.getAttributeValue(readerElt, "version");
                String className = dom.getAttributeValue(readerElt, "class");
                
                try
                {
                    addClass(readerClasses, type, subType, version, className);
                }
                catch (IllegalStateException e)
                {
                    ExceptionSystem.display(e);
                }                
            }
            
            // add writer hashtable entries
            NodeList writerElts = dom.getElements("Writer");            
            for (int i=0; i<writerElts.getLength(); i++)
            {
                Element writerElt = (Element)writerElts.item(i);
                String type = dom.getAttributeValue(writerElt, "type");
                String subType = dom.getAttributeValue(writerElt, "subType");
                String version = dom.getAttributeValue(writerElt, "version");
                String className = dom.getAttributeValue(writerElt, "class");
                
                try
                {
                    addClass(writerClasses, type, subType, version, className);
                }
                catch (IllegalStateException e)
                {
                    ExceptionSystem.display(e);
                }                
            }
        }
        catch (DOMHelperException e)
        {
            throw new IllegalStateException("Invalid OWSRegistry mapping file");
        }
    }
    
    
    private static String normalizeTypeString(String type)
    {
        if (type != null && type.length() > 0 && !type.equalsIgnoreCase("*"))
            return type.toUpperCase();
        else
            return null;
    }
    
    
    private static String normalizeSubtypeString(String subType)
    {
        if (subType != null && subType.length() > 0 && !subType.equalsIgnoreCase("*"))
            return subType.toUpperCase();
        else
            return null;
    }
    
    
    private static String normalizeVersionString(String version)
    {
        if (version != null && version.length() > 0 && !version.equalsIgnoreCase("*"))
        {
            int lastPeriod = version.length();
            
            // remove trailing zeros
            // ex: 1.0.0 => 1 and 1.0 => 1 so that they map to the same version
            while (lastPeriod > 1 && version.charAt(lastPeriod-1) == '0')
                lastPeriod = version.lastIndexOf('.', lastPeriod-1);
            
            return version.substring(0, lastPeriod);
        }
        else
            return null;
    }
    
    
    /**
     * Provides direct access to the readerClasses hashtable
     * @return
     */
    public static Hashtable<String, Class> getReaderClasses()
    {
        return readerClasses;
    }
    
    
    /**
     * Provides direct access to the writerClasses hashtable
     * @return
     */
    public static Hashtable<String, Class> getWriterClasses()
    {
        return writerClasses;
    }
}