/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are subject to the Mozilla Public License Version
 1.1 (the "License"); you may not use this file except in compliance with
 the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/MPL-1.1.html

 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.

 The Original Code is the "SensorML DataProcessing Engine".

 The Initial Developer of the Original Code is the VAST team at the University of Alabama in Huntsville (UAH). <http://vast.uah.edu> Portions created by the Initial Developer are Copyright (C) 2007 the Initial Developer. All Rights Reserved. Please Contact Mike Botts <mike.botts@uah.edu> for more information.

 Contributor(s):
    Alexandre Robin <robin@nsstc.uah.edu>

******************************* END LICENSE BLOCK ***************************/

package org.vast.swe;

import net.opengis.OgcProperty;
import net.opengis.OgcPropertyImpl;
import net.opengis.swe.v20.Boolean;
import net.opengis.swe.v20.AllowedTimes;
import net.opengis.swe.v20.AllowedTokens;
import net.opengis.swe.v20.AllowedValues;
import net.opengis.swe.v20.BinaryBlock;
import net.opengis.swe.v20.BinaryComponent;
import net.opengis.swe.v20.BinaryMember;
import net.opengis.swe.v20.ByteEncoding;
import net.opengis.swe.v20.ByteOrder;
import net.opengis.swe.v20.Category;
import net.opengis.swe.v20.CategoryRange;
import net.opengis.swe.v20.Count;
import net.opengis.swe.v20.CountRange;
import net.opengis.swe.v20.DataArray;
import net.opengis.swe.v20.DataChoice;
import net.opengis.swe.v20.DataComponent;
import net.opengis.swe.v20.DataEncoding;
import net.opengis.swe.v20.BinaryEncoding;
import net.opengis.swe.v20.DataRecord;
import net.opengis.swe.v20.DataStream;
import net.opengis.swe.v20.DataType;
import net.opengis.swe.v20.EncodedValues;
import net.opengis.swe.v20.JSONEncoding;
import net.opengis.swe.v20.Matrix;
import net.opengis.swe.v20.NilValue;
import net.opengis.swe.v20.NilValues;
import net.opengis.swe.v20.Quantity;
import net.opengis.swe.v20.QuantityRange;
import net.opengis.swe.v20.ScalarComponent;
import net.opengis.swe.v20.Text;
import net.opengis.swe.v20.TextEncoding;
import net.opengis.swe.v20.Time;
import net.opengis.swe.v20.TimeRange;
import net.opengis.swe.v20.UnitReference;
import net.opengis.swe.v20.Vector;
import net.opengis.swe.v20.XMLEncoding;
import java.io.Serializable;
import org.vast.cdm.common.CDMException;
import org.vast.cdm.common.DataStreamParser;
import org.vast.cdm.common.DataStreamWriter;
import org.vast.data.AbstractDataComponentImpl;
import org.vast.data.AbstractSimpleComponentImpl;
import org.vast.data.BinaryComponentImpl;
import org.vast.data.BinaryEncodingImpl;
import org.vast.data.CountImpl;
import org.vast.data.DataArrayImpl;
import org.vast.data.DataIterator;
import org.vast.data.DataValue;
import org.vast.data.SWEFactory;
import org.vast.data.ScalarIterator;
import org.vast.data.TextEncodingImpl;
import org.vast.swe.SWEBuilders.BooleanBuilder;
import org.vast.swe.SWEBuilders.CategoryBuilder;
import org.vast.swe.SWEBuilders.CategoryRangeBuilder;
import org.vast.swe.SWEBuilders.CountBuilder;
import org.vast.swe.SWEBuilders.CountRangeBuilder;
import org.vast.swe.SWEBuilders.DataArrayBuilder;
import org.vast.swe.SWEBuilders.DataChoiceBuilder;
import org.vast.swe.SWEBuilders.DataRecordBuilder;
import org.vast.swe.SWEBuilders.MatrixBuilder;
import org.vast.swe.SWEBuilders.QuantityBuilder;
import org.vast.swe.SWEBuilders.QuantityRangeBuilder;
import org.vast.swe.SWEBuilders.TextBuilder;
import org.vast.swe.SWEBuilders.TimeBuilder;
import org.vast.swe.SWEBuilders.TimeRangeBuilder;
import org.vast.swe.SWEBuilders.VectorBuilder;
import org.vast.swe.fast.JsonDataWriter;
import org.vast.swe.fast.XmlDataWriter;
import org.vast.swe.helper.RasterHelper;
import org.vast.util.Asserts;


/**
 * <p>
 * Helper class for creating common data structures and encodings and browsing
 * a data component tree.
 * </p>
 *
 * @author Alex Robin>
 * @since Feb 26, 2015
 */
public class SWEHelper
{
    public static final SWEFactory DEFAULT_SWE_FACTORY = new SWEFactory();
    public static final String PATH_SEPARATOR = "/";
    protected SWEFactory fac = DEFAULT_SWE_FACTORY;
    
    
    /**
     * Create a SWE helper with the default factory
     */
    public SWEHelper()
    {        
    }
    
    
    /**
     * Create a SWE helper with the provided factory
     * @param fac SWE component factory
     */
    public SWEHelper(SWEFactory fac)
    {
        this.fac = fac;
    }


    /**
     * @param epsgCode
     * @return the CRS URI for the given EPSG integer code
     */
    public static String getEpsgUri(int epsgCode)
    {
        return SWEConstants.EPSG_URI_PREFIX + epsgCode;
    }


    public static String getPropertyUri(String propName)
    {
    	return SWEConstants.SWE_PROP_URI_PREFIX + propName;
    }


    public static String getQudtUri(String propName)
    {
        return SWEConstants.QUDT_URI_PREFIX + propName;
    }


    public static String getDBpediaUri(String propName)
    {
    	return SWEConstants.DBPEDIA_URI_PREFIX + propName;
    }


    public static OgcProperty<Serializable> newLinkProperty(String href)
    {
        return newLinkProperty(null, href, null);
    }


    public static OgcProperty<Serializable> newLinkProperty(String name, String href)
    {
        return newLinkProperty(name, href, null);
    }


    public static OgcProperty<Serializable> newLinkProperty(String name, String href, String role)
    {
        OgcPropertyImpl<Serializable> prop = new OgcPropertyImpl<>();
        prop.setName(name);
        prop.setHref(href);
        prop.setRole(role);
        return prop;
    }


    /**
     * @return A builder to create a new Boolean component
     */
    public BooleanBuilder createBoolean()
    {
        return new BooleanBuilder(fac);
    }


    /**
     * @return A builder to create a new Category component
     */
    public CategoryBuilder createCategory()
    {
        return new CategoryBuilder(fac);
    }


    /**
     * @return A builder to create a new CategoryRange component
     */
    public CategoryRangeBuilder createCategoryRange()
    {
        return new CategoryRangeBuilder(fac);
    }


    /**
     * @return A builder to create a new Count component
     */
    public CountBuilder createCount()
    {
        return new CountBuilder(fac);
    }


    /**
     * @return A builder to create a new CountRange component
     */
    public CountRangeBuilder createCountRange()
    {
        return new CountRangeBuilder(fac);
    }


    /**
     * @return A builder to create a new Quantity component
     */
    public QuantityBuilder createQuantity()
    {
        return new QuantityBuilder(fac);
    }


    /**
     * @return A builder to create a new QuantityRange component
     */
    public QuantityRangeBuilder createQuantityRange()
    {
        return new QuantityRangeBuilder(fac);
    }


    /**
     * @return A builder to create a new Time component
     */
    public TimeBuilder createTime()
    {
        return new TimeBuilder(fac);
    }


    /**
     * @return A builder to create a new TimeRange component
     */
    public TimeRangeBuilder createTimeRange()
    {
        return new TimeRangeBuilder(fac);
    }


    /**
     * @return A builder to create a new Boolean component
     */
    public TextBuilder createText()
    {
        return new TextBuilder(fac);
    }


    /**
     * @return A builder to create a new DataRecord component
     */
    public DataRecordBuilder createDataRecord()
    {
        return new DataRecordBuilder(fac);
    }


    /**
     * @return A builder to create a new DataRecord component
     */
    public DataRecordBuilder createRecord()
    {
        return createDataRecord();
    }


    /**
     * @param fac Factory to use to create the component objects
     * @return A builder to create a new Vector component
     */
    public VectorBuilder createVector()
    {
        return new VectorBuilder(fac);
    }


    /**
     * @return A builder to create a new DataChoice component
     */
    public DataChoiceBuilder createDataChoice()
    {
        return new DataChoiceBuilder(fac);
    }


    /**
     * @return A builder to create a new DataChoice component
     */
    public DataChoiceBuilder createChoice()
    {
        return createDataChoice();
    }


    /**
     * @return A builder to create a new DataArray component
     */
    public DataArrayBuilder createDataArray()
    {
        return new DataArrayBuilder(fac);
    }


    /**
     * @return A builder to create a new DataArray component
     */
    public DataArrayBuilder createArray()
    {
        return createDataArray();
    }


    /**
     * @return A builder to create a new Matrix component
     */
    public MatrixBuilder createMatrix()
    {
        return new MatrixBuilder(fac);
    }


    //////////////////////////////////////
    // Encoding and parser/writer stuff //
    //////////////////////////////////////

    /**
     * Creates a text encoding with the specified separators.<br/>
     * Since no escaping is supported, it is up to the programmer to make sure that
     * separator characters are not present within the encoded data.
     * @param tokenSep separator used between tokens
     * @param blockSep separator used to delimit complete tuples
     * @return the configured TextEncoding instance
     */
    public TextEncoding newTextEncoding(String tokenSep, String blockSep)
    {
        return new TextEncodingImpl(tokenSep, blockSep);
    }


    /**
     * Creates a binary encoding with the specified options
     * @param byteOrder byte ordering (endianness) of the byte stream
     * @param byteEncoding byte encoding used (raw or base64)
     * @return the BinaryEncoding instance (not that it is not fully configured
     * since data types of all fields have to be specified)
     */
    public BinaryEncoding newBinaryEncoding(ByteOrder byteOrder, ByteEncoding byteEncoding)
    {
        BinaryEncodingImpl encoding = new BinaryEncodingImpl();
        encoding.setByteOrder(byteOrder);
        encoding.setByteEncoding(byteEncoding);
        return encoding;
    }


    /**
     * Helper method to instantiate the proper parser for the given encoding
     * @param encoding
     * @return instance of parser capable of handling the given encoding
     */
    public static DataStreamParser createDataParser(DataEncoding encoding)
    {
        DataStreamParser parser = null;

        if (encoding instanceof TextEncoding)
            parser = new AsciiDataParser();
        else if (encoding instanceof BinaryEncoding)
            parser = new BinaryDataParser();
        else if (encoding instanceof XMLEncoding)
            parser = new XmlDataParser();

        parser.setDataEncoding(encoding);
        return parser;
    }


    /**
     * Helper method to instantiate the proper writer for the given encoding
     * @param encoding
     * @return instance of writer capable of handling the given encoding
     */
    public static DataStreamWriter createDataWriter(DataEncoding encoding)
    {
        DataStreamWriter writer = null;

        if (encoding instanceof TextEncoding)
            writer = new AsciiDataWriter();
        else if (encoding instanceof BinaryEncoding)
            writer = new BinaryDataWriter();
        else if (encoding instanceof XMLEncoding)
            writer = new XmlDataWriter();
        else if (encoding instanceof JSONEncoding)
            writer = new JsonDataWriter();

        writer.setDataEncoding(encoding);
        return writer;
    }


    /**
     * Gets the default encoding for the given data structure.<br/>
     * This uses BinaryEncoding if data structure contains a large array and TextEncoding
     * otherwise.
     * @param dataComponents
     * @return an appropriately configured encoding
     */
    public static DataEncoding getDefaultEncoding(DataComponent dataComponents)
    {
        // check if one of the children is a large array
        for (DataComponent c: new DataIterator(dataComponents))
        {
            if (c instanceof DataArray)
            {
                DataArrayImpl array = (DataArrayImpl)c;
                if (array.isVariableSize() || (array.getElementCount() != null && array.getElementCount().getValue() > 10))
                    return getDefaultBinaryEncoding(dataComponents);
            }
        }

        // otherwise return default text encoding
        return new TextEncodingImpl();
    }


    /**
     * Get default binary encoding for the given component tree.<br/>
     * Data types used will be ones specified in each scalar component.
     * @param dataComponents component whose children will be mapped to encoding options
     * @return binary encoding instance pre-configured for the component
     */
    public static BinaryEncoding getDefaultBinaryEncoding(DataComponent dataComponents)
    {
        BinaryEncodingImpl encoding = new BinaryEncodingImpl();
        encoding.setByteEncoding(ByteEncoding.RAW);
        encoding.setByteOrder(ByteOrder.BIG_ENDIAN);

        // use default encoding info for each data value
        ScalarIterator it = new ScalarIterator(dataComponents);
        while (it.hasNext())
        {
            DataComponent[] nextPath = it.nextPath();
            DataValue nextScalar = (DataValue)nextPath[nextPath.length-1];

            // build path (just use / for root)
            StringBuilder pathString = new StringBuilder();
            pathString.append(PATH_SEPARATOR);
            for (int i = 0; i < nextPath.length; i++)
            {
                pathString.append(nextPath[i].getName());
                pathString.append(PATH_SEPARATOR);
            }

            BinaryComponentImpl binaryOpts = new BinaryComponentImpl();
            binaryOpts.setCdmDataType(nextScalar.getDataType());
            binaryOpts.setRef(pathString.substring(0, pathString.length()-1));

            encoding.addMemberAsComponent(binaryOpts);
            nextScalar.setEncodingInfo(binaryOpts);
        }

        return encoding;
    }


    /**
     * Ajusts encoding settings to ensures that data produced with the encoding
     * can be embedded in XML (e.g. for binary encoding this enforces base64)
     * @param encoding
     * @return copy of encoding, reconfigured appropriately
     */
    public static DataEncoding ensureXmlCompatible(DataEncoding encoding)
    {
        // if binary data, ensure encoding is set to base64
        if (encoding instanceof BinaryEncoding)
        {
            encoding = encoding.copy();
            ((BinaryEncoding) encoding).setByteEncoding(ByteEncoding.BASE_64);
        }

        return encoding;
    }


    /**
     * Assigns binary components and blocks definitions to the actual data component.
     * This sets the encodingInfo attribute of the component so it can be used to generate specialized datablocks.
     * For scalars, it also sets the default data type so it is the same as in the encoded stream.
     * @param dataComponents
     * @param encoding
     * @throws CDMException
     */
    public static void assignBinaryEncoding(DataComponent dataComponents, BinaryEncoding encoding) throws CDMException
    {
        for (BinaryMember binaryOpts: encoding.getMemberList())
        {
            DataComponent comp = findComponentByPath(dataComponents, binaryOpts.getRef());
            ((AbstractDataComponentImpl)comp).setEncodingInfo(binaryOpts);

            // for scalars, also set default data type
            if (binaryOpts instanceof BinaryComponent)
                ((AbstractSimpleComponentImpl)comp).setDataType(((BinaryComponentImpl)binaryOpts).getCdmDataType());
        }

        // set default data type for implicit array size components if not already set
        DataIterator it = new DataIterator(dataComponents);
        while (it.hasNext())
        {
            DataComponent comp = it.next();
            if (comp instanceof DataArrayImpl)
            {
                CountImpl count = (CountImpl)((DataArrayImpl)comp).getArraySizeComponent();
                if (count != null && count.getEncodingInfo() == null)
                {
                    BinaryComponent binaryOpts = new BinaryComponentImpl();
                    binaryOpts.setCdmDataType(count.getDataType());
                    count.setEncodingInfo(binaryOpts);
                }
            }
        }
    }


    //////////////////////////////////////////////
    // Methods for manipulating component trees //
    //////////////////////////////////////////////

    /**
     * Finds the first component in the tree matching the given filter
     * @param parent component from which to start the search
     * @param filter component filter instance (must not be null)
     * @return the first component matching the filter
     */
    public static DataComponent findComponent(DataComponent parent, IComponentFilter filter)
    {
        if (parent instanceof DataArrayImpl)
            parent = ((DataArrayImpl)parent).getElementType();

        int childCount = parent.getComponentCount();
        for (int i=0; i<childCount; i++)
        {
            DataComponent child = parent.getComponent(i);

            if (filter.accept(child))
                return child;

            // try to find it recursively!
            DataComponent desiredParam = findComponent(child, filter);
            if (desiredParam != null)
                return desiredParam;
        }

        return null;
    }


    /**
     * Finds a component in the component tree using its name (property name)
     * @param parent component from which to start the search
     * @param name component name to look for
     * @return the first component with the specified name
     */
    public static DataComponent findComponentByName(final DataComponent parent, final String name)
    {
        return findComponent(parent, new IComponentFilter() {
            @Override
            public boolean accept(DataComponent comp)
            {
                String childName = comp.getName();
                if (childName != null && childName.equals(name))
                    return true;
                return false;
            }
        });
    }


    /**
     * Finds a component in the component tree using its definition
     * @param parent component from which to start the search
     * @param defUri definition URI to look for
     * @return the first component with the specified definition
     */
    public static DataComponent findComponentByDefinition(final DataComponent parent, final String defUri)
    {
        return findComponent(parent, new IComponentFilter() {
            @Override
            public boolean accept(DataComponent comp)
            {
                String childDef = comp.getDefinition();
                if (childDef != null && childDef.equals(defUri))
                    return true;
                return false;
            }
        });
    }


    /**
     * Finds a component in a component tree using a path
     * @param parent component from which to start the search
     * @param path desired path as a String composed of component names separated by {@value #PATH_SEPARATOR} characters
     * @return the component with the given path
     * @throws CDMException if the specified path is incorrect
     */
    public static DataComponent findComponentByPath(DataComponent parent, String path) throws CDMException
    {
        try
        {
            return findComponentByPath(parent, path.split(PATH_SEPARATOR));
        }
        catch (CDMException e)
        {
            throw new CDMException("Unknown component " + path, e);
        }
    }


    /**
     * Finds a component in a component tree using a path
     * @param parent component from which to start the search
     * @param path desired path as a String array containing a sequence of component names
     * @return the component with the given path
     * @throws CDMException if the specified path is incorrect
     */
    public static DataComponent findComponentByPath(DataComponent parent, String[] path) throws CDMException
    {
        DataComponent comp = parent;

        for (int i=0; i<path.length; i++)
        {
            String pathElt = path[i];
            if (pathElt.length() == 0) // a leading '/' create an empty array element
                continue;

            comp = comp.getComponent(pathElt);
            if (comp == null)
                throw new CDMException("Unknown component " + pathElt);
        }

        return comp;
    }


    /**
     * Computes the path of the component from the root of the data structure
     * @param component
     * @return A '/' separated path of component names
     */
    public static String getComponentPath(DataComponent component)
    {
        Asserts.checkNotNull(component, DataComponent.class);
        Asserts.checkNotNull(component.getName(), "Component must have a name");

        StringBuilder path = new StringBuilder(component.getName());
        DataComponent parent = component.getParent();

        while (parent != null && parent.getName() != null)
        {
            path.insert(0, parent.getName() + PATH_SEPARATOR);
            parent = parent.getParent();
        }

        return path.toString();
    }


    /**
     * Find the root component of the data structure that this component belongs to
     * @param component
     * @return the root component
     */
    public static DataComponent getRootComponent(DataComponent component)
    {
        DataComponent parentPort = component;
        while (parentPort.getParent() != null)
            parentPort = parentPort.getParent();
        return parentPort;
    }


    /**
     * Retrieves an indexer for the first time stamp component found in the parent structure
     * @param parent
     * @return indexer instance for the time stamp component or null if no time stamp could be found
     */
    public static ScalarIndexer getTimeStampIndexer(DataComponent parent)
    {
        ScalarComponent timeStamp = (ScalarComponent)findComponentByDefinition(parent, SWEConstants.DEF_SAMPLING_TIME);
        if (timeStamp == null)
            timeStamp = (ScalarComponent)findComponentByDefinition(parent, SWEConstants.DEF_PHENOMENON_TIME);
        if (timeStamp == null)
            return null;
        return new ScalarIndexer(parent, timeStamp);
    }



    /* Deprecated methods, replaced by builders */

    /**
     * @deprecated Use {@link #createBoolean()}
     */
    @Deprecated
    @SuppressWarnings("javadoc")
    public Boolean newBoolean(String definition, String label, String description)
    {
        Boolean b = fac.newBoolean();
        b.setDefinition(definition);
        b.setLabel(label);
        b.setDescription(description);
        return b;
    }


    /**
     * @deprecated Use {@link #createText()}
     */
    @Deprecated
    @SuppressWarnings("javadoc")
    public Text newText(String definition, String label, String description)
    {
        Text tx = fac.newText();
        tx.setDefinition(definition);
        tx.setLabel(label);
        tx.setDescription(description);
        return tx;
    }


    /**
     * @deprecated Use {@link #createCount()}
     */
    @Deprecated
    @SuppressWarnings("javadoc")
    public Count newCount(String definition, String label, String description, DataType dataType)
    {
        Count c = fac.newCount(dataType == null ? DataType.INT : dataType);
        c.setDefinition(definition);
        c.setLabel(label);
        c.setDescription(description);
        return c;
    }


    /**
     * @deprecated Use {@link #createCount()}
     */
    @Deprecated
    @SuppressWarnings("javadoc")
    public Count newCount(String definition, String label, String description)
    {
        return newCount(definition, label, description, DataType.INT);
    }


    /**
     * @deprecated Use {@link #createCategory()}
     */
    @Deprecated
    @SuppressWarnings("javadoc")
    public Category newCategory(String definition, String label, String description, String codeSpace)
    {
        Category c = fac.newCategory();
        c.setDefinition(definition);
        c.setLabel(label);
        c.setDescription(description);
        c.setCodeSpace(codeSpace);
        return c;
    }


    /**
     * @deprecated Use {@link #createQuantity()}
     */
    @Deprecated
    @SuppressWarnings("javadoc")
    public Quantity newQuantity(String definition, String label, String description, String uom, DataType dataType)
    {
        Quantity q = fac.newQuantity(dataType == null ? DataType.DOUBLE : dataType);
        q.setDefinition(definition);
        q.setLabel(label);
        q.setDescription(description);

        if (uom.startsWith(SWEConstants.URN_PREFIX) || uom.startsWith(SWEConstants.HTTP_PREFIX))
            q.getUom().setHref(uom);
        else
            q.getUom().setCode(uom);

        return q;
    }


    /**
     * @deprecated Use {@link #createQuantity()}
     */
    @Deprecated
    @SuppressWarnings("javadoc")
    public Quantity newQuantity(String definition, String label, String description, String uom)
    {
        return newQuantity(definition, label, description, uom, DataType.DOUBLE);
    }


    /**
     * @deprecated Use {@link #createTime()}
     */
    @Deprecated
    @SuppressWarnings("javadoc")
    public Time newTime(String definition, String label, String description, String uom, String timeRef, DataType dataType)
    {
        Time t = fac.newTime(dataType == null ? DataType.DOUBLE : dataType);
        t.setDefinition(definition);
        t.setLabel(label);
        t.setDescription(description);
        t.setReferenceFrame(timeRef);

        if (uom.startsWith(SWEConstants.URN_PREFIX) || uom.startsWith(SWEConstants.HTTP_PREFIX))
            t.getUom().setHref(uom);
        else
            t.getUom().setCode(uom);

        return t;
    }


    /**
     * @deprecated Use {@link #createTime()}
     */
    @Deprecated
    @SuppressWarnings("javadoc")
    public Time newTime(String definition, String label, String description, String uom, String timeRef)
    {
        return newTime(definition, label, description, uom, timeRef, DataType.DOUBLE);
    }


    /**
     * @deprecated Use {@link #createTime()}
     */
    @Deprecated
    @SuppressWarnings("javadoc")
    public Time newTimeIsoUTC(String definition, String label, String description)
    {
        return newTime(definition, label, description, Time.ISO_TIME_UNIT, SWEConstants.TIME_REF_UTC);
    }


    /**
     * @deprecated Use {@link #createTime()} and {@link TimeBuilder#asSamplingTimeIsoUTC()}
     */
    @Deprecated
    @SuppressWarnings("javadoc")
    public Time newTimeStampIsoUTC()
    {
        return newTime(SWEConstants.DEF_SAMPLING_TIME, "Sampling Time", null, Time.ISO_TIME_UNIT, SWEConstants.TIME_REF_UTC);
    }


    /**
     * @deprecated Use {@link #createTime()} and {@link TimeBuilder#asSamplingTimeIsoGPS()}
     */
    @Deprecated
    @SuppressWarnings("javadoc")
    public Time newTimeStampIsoGPS()
    {
        return newTime(SWEConstants.DEF_SAMPLING_TIME, "Sampling Time", null, Time.ISO_TIME_UNIT, SWEConstants.TIME_REF_GPS);
    }


    /**
     * @deprecated Use {@link #createTime()} and {@link TimeBuilder#asPhenomenonTimeIsoUTC()}
     */
    @Deprecated
    @SuppressWarnings("javadoc")
    public Time newPhenomenonTimeIsoUTC(String label, String description)
    {
        return newTime(SWEConstants.DEF_PHENOMENON_TIME, label, description, Time.ISO_TIME_UNIT, SWEConstants.TIME_REF_UTC);
    }


    /**
     * @deprecated Use {@link #createTime()}
     */
    @Deprecated
    @SuppressWarnings("javadoc")
    public Time newTimeStampOnBoardClock(String uomCode, String timeRef)
    {
        return newTime(SWEConstants.DEF_SAMPLING_TIME, "Sampling Time", null, uomCode, timeRef);
    }


    /**
     * @deprecated Use {@link #createDataRecord()} and {@link #createTime()}
     */
    @Deprecated
    @SuppressWarnings("javadoc")
    public DataRecord wrapWithTimeStamp(Time timeStamp, DataComponent... subComponents)
    {
        DataRecord rec = fac.newDataRecord(subComponents.length + 1);
        rec.addComponent("time", timeStamp);

        for (DataComponent childComp: subComponents)
            rec.addComponent(childComp.getName(), childComp);

        return rec;
    }


    /**
     * @deprecated Use {@link #createDataRecord()} and {@link #createTime()}
     */
    @Deprecated
    @SuppressWarnings("javadoc")
    public DataRecord wrapWithTimeStampUTC(DataComponent... subComponents)
    {
        Time timeStamp = newTimeStampIsoUTC();
        return wrapWithTimeStamp(timeStamp, subComponents);
    }


    /**
     * Creates a component for carrying system ID (e.g. station ID, sensor ID, device ID, etc...)
     * @return new Text instance
     * @deprecated use {@link SWEBuilders} class
     */
    @Deprecated
    @SuppressWarnings("javadoc")
    public Text newSystemIdComponent()
    {
        Text t = fac.newText();
        t.setDefinition(SWEConstants.DEF_SYSTEM_ID);
        return t;
    }


    /**
     * @deprecated Use {@link #createVector()}
     */
    @Deprecated
    @SuppressWarnings("javadoc")
    public Vector newVector(String def, String crs, String[] names, String[] labels, String[] uoms, String[] axes)
    {
        Vector loc = fac.newVector();
        loc.setDefinition(def);
        loc.setReferenceFrame(crs == null ? SWEConstants.NIL_UNKNOWN : crs);

        Quantity c;
        for (int i = 0; i < names.length; i++)
        {
            c = fac.newQuantity(DataType.DOUBLE);
            if (labels != null)
                c.setLabel(labels[i]);
            if (uoms != null)
                c.getUom().setCode(uoms[i]);
            if (axes != null)
                c.setAxisID(axes[i]);
            loc.addComponent(names[i], c);
        }

        return loc;
    }


    /**
     * @deprecated Use {@link #createDataArray()}
     */
    @Deprecated
    @SuppressWarnings("javadoc")
    public DataArray newDataArray(Count sizeComponent, String eltName, DataComponent elementType)
    {
        DataArray array = fac.newDataArray();
        ((DataArrayImpl)array).setElementCount(sizeComponent);
        array.setElementType(eltName, elementType);
        return array;
    }


    /**
     * @deprecated Use {@link RasterHelper#newRgbImage(int, int, DataType)}
     */
    @Deprecated
    @SuppressWarnings("javadoc")
    public DataArray newRgbImage(int width, int height, DataType dataType)
    {
        return new RasterHelper().newRgbImage(width, height, dataType);
    }


    /**
     * Creates a data stream description with given description and encoding
     * @param dataDescription description of each stream element
     * @param dataEncoding data encoding spec
     * @return the new DataStream object
     * @deprecated use {@link SWEBuilders} class
     */
    @Deprecated
    @SuppressWarnings("javadoc")
    public DataStream newDataStream(DataComponent dataDescription, DataEncoding dataEncoding)
    {
        DataStream ds = fac.newDataStream();
        ds.setElementType(dataDescription.getName(), dataDescription);
        ds.setEncoding(dataEncoding);
        return ds;
    }

    /**
     * @deprecated Use {@link #createDataRecord()}
     */
    @Deprecated
    @SuppressWarnings("javadoc")
    public DataRecord newDataRecord()
    {
        return fac.newDataRecord();
    }


    /**
     * @deprecated Use {@link #createDataRecord()}
     */
    @Deprecated
    @SuppressWarnings("javadoc")
    public DataRecord newDataRecord(int recordSize)
    {
        return fac.newDataRecord(recordSize);
    }


    /**
     * @deprecated Use {@link #createVector()}
     */
    @Deprecated
    @SuppressWarnings("javadoc")
    public Vector newVector()
    {
        return fac.newVector();
    }


    /**
     * @deprecated Use {@link #createDataArray()}
     */
    @Deprecated
    @SuppressWarnings("javadoc")
    public DataArray newDataArray()
    {
        return fac.newDataArray();
    }


    /**
     * @deprecated Use {@link #createDataArray()}
     */
    @Deprecated
    @SuppressWarnings("javadoc")
    public DataArray newDataArray(int arraySize)
    {
        return fac.newDataArray(arraySize);
    }


    /**
     * @deprecated Use {@link #createMatrix()}
     */
    @Deprecated
    @SuppressWarnings("javadoc")
    public Matrix newMatrix()
    {
        return fac.newMatrix();
    }


    /**
     * @deprecated Use {@link #createMatrix()}
     */
    @Deprecated
    @SuppressWarnings("javadoc")
    public Matrix newMatrix(int arraySize)
    {
        return fac.newMatrix(arraySize);
    }


    /**
     * @deprecated Use {@link #createDataChoice()}
     */
    @Deprecated
    @SuppressWarnings("javadoc")
    public DataChoice newDataChoice()
    {
        return fac.newDataChoice();
    }


    /**
     * @deprecated Use {@link #createDataStream()}
     */
    @Deprecated
    @SuppressWarnings("javadoc")
    public DataStream newDataStream()
    {
        return fac.newDataStream();
    }


    /**
     * @deprecated Use {@link #createBoolean()}
     */
    @Deprecated
    @SuppressWarnings("javadoc")
    public Boolean newBoolean()
    {
        return fac.newBoolean();
    }


    /**
     * @deprecated Use {@link #createText()}
     */
    @Deprecated
    @SuppressWarnings("javadoc")
    public Text newText()
    {
        return fac.newText();
    }


    /**
     * @deprecated Use {@link #createCount()}
     */
    @Deprecated
    @SuppressWarnings("javadoc")
    public Count newCount()
    {
        return fac.newCount();
    }


    /**
     * @deprecated Use {@link #createCount()}
     */
    @Deprecated
    @SuppressWarnings("javadoc")
    public Count newCount(DataType dataType)
    {
        return fac.newCount(dataType);
    }


    /**
     * @deprecated Use {@link #createCountRange()}
     */
    @Deprecated
    @SuppressWarnings("javadoc")
    public CountRange newCountRange()
    {
        return fac.newCountRange();
    }


    /**
     * @deprecated Use {@link #createCountRange()}
     */
    @Deprecated
    @SuppressWarnings("javadoc")
    public CountRange newCountRange(DataType dataType)
    {
        return fac.newCountRange(dataType);
    }


    /**
     * @deprecated Use {@link #createCategory()}
     */
    @Deprecated
    @SuppressWarnings("javadoc")
    public Category newCategory()
    {
        return fac.newCategory();
    }


    /**
     * @deprecated Use {@link #createCategoryRange()}
     */
    @Deprecated
    @SuppressWarnings("javadoc")
    public CategoryRange newCategoryRange()
    {
        return fac.newCategoryRange();
    }


    /**
     * @deprecated Use {@link #createQuantity()}
     */
    @Deprecated
    @SuppressWarnings("javadoc")
    public Quantity newQuantity()
    {
        return fac.newQuantity();
    }


    /**
     * @deprecated Use {@link #createQuantity()}
     */
    @Deprecated
    @SuppressWarnings("javadoc")
    public Quantity newQuantity(DataType dataType)
    {
        return fac.newQuantity(dataType);
    }


    /**
     * @deprecated Use {@link #createQuantityRange()}
     */
    @Deprecated
    @SuppressWarnings("javadoc")
    public QuantityRange newQuantityRange()
    {
        return fac.newQuantityRange();
    }


    /**
     * @deprecated Use {@link #createQuantityRange()}
     */
    @Deprecated
    @SuppressWarnings("javadoc")
    public QuantityRange newQuantityRange(DataType dataType)
    {
        return fac.newQuantityRange(dataType);
    }


    /**
     * @deprecated Use {@link #createTime()}
     */
    @Deprecated
    @SuppressWarnings("javadoc")
    public Time newTime()
    {
        return fac.newTime();
    }


    /**
     * @deprecated Use {@link #createTime()}
     */
    @Deprecated
    @SuppressWarnings("javadoc")
    public Time newTime(DataType dataType)
    {
        return fac.newTime(dataType);
    }


    /**
     * @deprecated Use {@link #createTimeRange()}
     */
    @Deprecated
    @SuppressWarnings("javadoc")
    public TimeRange newTimeRange()
    {
        return fac.newTimeRange();
    }


    /**
     * @deprecated Use {@link #createTimeRange()}
     */
    @Deprecated
    @SuppressWarnings("javadoc")
    public TimeRange newTimeRange(DataType dataType)
    {
        return fac.newTimeRange(dataType);
    }


    @Deprecated
    public NilValues newNilValues()
    {
        return fac.newNilValues();
    }


    @Deprecated
    public AllowedTokens newAllowedTokens()
    {
        return fac.newAllowedTokens();
    }


    @Deprecated
    public AllowedValues newAllowedValues()
    {
        return fac.newAllowedValues();
    }


    @Deprecated
    public AllowedTimes newAllowedTimes()
    {
        return fac.newAllowedTimes();
    }


    @Deprecated
    public UnitReference newUnitReference()
    {
        return fac.newUnitReference();
    }


    @Deprecated
    public NilValue newNilValue()
    {
        return fac.newNilValue();
    }


    public XMLEncoding newXMLEncoding()
    {
        return fac.newXMLEncoding();
    }


    public TextEncoding newTextEncoding()
    {
        return fac.newTextEncoding();
    }


    @Deprecated
    public BinaryBlock newBinaryBlock()
    {
        return fac.newBinaryBlock();
    }


    @Deprecated
    public BinaryEncoding newBinaryEncoding()
    {
        return fac.newBinaryEncoding();
    }


    @Deprecated
    public BinaryComponent newBinaryComponent()
    {
        return fac.newBinaryComponent();
    }


    @Deprecated
    public EncodedValues newEncodedValuesProperty()
    {
        return fac.newEncodedValuesProperty();
    }
}
