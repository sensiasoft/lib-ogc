/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.
 
Copyright (C) 2012-2017 Sensia Software LLC. All Rights Reserved.
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.sensorML;

import java.util.Arrays;
import org.vast.process.IProcessExec;
import org.vast.sensorML.SMLBuilders.AbstractProcessBuilder;
import org.vast.sensorML.SMLBuilders.AggregateProcessBuilder;
import org.vast.sensorML.SMLBuilders.PhysicalComponentBuilder;
import org.vast.sensorML.SMLBuilders.PhysicalSystemBuilder;
import org.vast.sensorML.SMLBuilders.SimpleProcessBuilder;
import org.vast.swe.SWEHelper;
import org.vast.util.Asserts;
import net.opengis.OgcPropertyList;
import net.opengis.sensorml.v20.AbstractProcess;
import net.opengis.sensorml.v20.AggregateProcess;
import net.opengis.sensorml.v20.DataInterface;
import net.opengis.sensorml.v20.IdentifierList;
import net.opengis.sensorml.v20.PhysicalComponent;
import net.opengis.sensorml.v20.PhysicalSystem;
import net.opengis.sensorml.v20.SimpleProcess;
import net.opengis.sensorml.v20.Term;
import net.opengis.swe.v20.AbstractSWEIdentifiable;
import net.opengis.swe.v20.DataComponent;
import net.opengis.swe.v20.DataEncoding;
import net.opengis.swe.v20.DataStream;


/**
 * <p>
 * Helper class for creating SensorML process and system components and 
 * dealing with process input/output structures.
 * </p>
 *
 * @author Alex Robin
 * @since May 16, 2017
 */
public class SMLHelper
{
    public static final SMLFactory DEFAULT_SML_FACTORY = new SMLFactory();
    protected SMLFactory fac = DEFAULT_SML_FACTORY;
    
    
    static class LinkTarget
    {
        public AbstractProcess process;
        public DataComponent component;
        
        public LinkTarget(AbstractProcess parent, DataComponent component)
        {
            this.process = parent;
            this.component = component;
        }
    }
    
    
    /**
     * Create a SML helper with the default factory
     */
    public SMLHelper()
    {        
    }
    
    
    /**
     * Create a SML helper with the provided factory
     * @param fac SML process object factory
     */
    public SMLHelper(SMLFactory fac)
    {
        this.fac = fac;
    }
    
    
    /**
     * Helper method to edit a SensorML process in-place using a builder
     * @param <B> Type of expected builder
     * @param process process instance to start from
     * @return A builder corresponding to the provided provided process type
     */
    @SuppressWarnings("unchecked")
    public <B extends AbstractProcessBuilder<?,?>> B edit(AbstractProcess process)
    {
        Asserts.checkNotNull(process, AbstractProcess.class);

        if (process instanceof PhysicalSystem)
            return (B)createPhysicalSystem();
        else if (process instanceof PhysicalComponent)
            return (B)createPhysicalComponent();
        else if (process instanceof AggregateProcess)
            return (B)createAggregateProcess();
        else if (process instanceof SimpleProcess)
            return (B)createSimpleProcess();
        else
            throw new IllegalArgumentException("Unsupported process type: " + process.getClass().getCanonicalName());
    }


    /**
     * @return A builder to create a new SimpleProcess object
     */
    public SimpleProcessBuilder createSimpleProcess()
    {
        return new SimpleProcessBuilder(fac);
    }


    /**
     * @return A builder to create a new AggregateProcess object
     */
    public AggregateProcessBuilder createAggregateProcess()
    {
        return new AggregateProcessBuilder(fac);
    }


    /**
     * @return A builder to create a new PhysicalComponent object
     */
    public PhysicalComponentBuilder createPhysicalComponent()
    {
        return new PhysicalComponentBuilder(fac);
    }


    /**
     * @return A builder to create a new PhysicalSystem object
     */
    public PhysicalSystemBuilder createPhysicalSystem()
    {
        return new PhysicalSystemBuilder(fac);
    }
    
    
    /*
      Static helper methods for finding data components within the process hierarchy
    */    
    
    /**
     * Helper to get the input/output/parameter component description whether
     * the IO is wrapped in a DataInterface or DataStream or given as a raw
     * DataComponent.
     * @param ioDef
     * @return DataComponent
     */
    public static DataComponent getIOComponent(AbstractSWEIdentifiable ioDef)
    {
        if (ioDef instanceof DataInterface)
            return ((DataInterface)ioDef).getData().getElementType();
        else if (ioDef instanceof DataStream)
            return ((DataStream)ioDef).getElementType();
        else
            return (DataComponent)ioDef;
    }
    
    
    /**
     * Helper to get the input/output/parameter encoding description whether
     * the IO is wrapped in a DataInterface or DataStream.<br/>
     * This method returns null if the IO is described as a raw DataComponent.
     * @param ioDef
     * @return DataComponent
     */
    public static DataEncoding getIOEncoding(AbstractSWEIdentifiable ioDef)
    {
        if (ioDef instanceof DataInterface)
            return ((DataInterface)ioDef).getData().getEncoding();
        else if (ioDef instanceof DataStream)
            return ((DataStream)ioDef).getEncoding();
        else 
            return null;
    }
    
    
    /**
     * Finds a component in a process/component tree using a path.<br/>
     * Link path format is either '[components/{component_name}/...][inputs|outputs|parameters]/{name}/{name}/... 
     * @param parent process from which to start the search
     * @param linkString desired path as a String composed of process name, port type and data component names separated by {@value SWEHelper#PATH_SEPARATOR} characters
     * @return the process/component pair targeted by the given path or null if target process is not resolved
     * @throws SMLException if the specified path is incorrect
     */
    public static LinkTarget findComponentByPath(AbstractProcess parent, String linkString) throws SMLException
    {
        String processName = null;
        String portType = null;
        String portName = null;
        String[] dataPath = null;
        int portTypeIndex = 0;
        
        // parse link path
        String[] linkPath = linkString.split(SWEHelper.PATH_SEPARATOR);

        // if we're linking to a component IO
        // support only links to components for now 
        if ("components".equals(linkPath[0]))
        {
            if (!(parent instanceof AggregateProcess))
                throw new SMLException("Cannot refer to sub-components in a non-aggregate process");
            
            if (linkPath.length < 2)
                throw new SMLException("The name of a sub-component must be specified");
            
            processName = linkPath[1];
            portTypeIndex = 2;
        }

        // now extract port type, name and component path
        if (linkPath.length < portTypeIndex+1)
            throw new SMLException("At least a port type and name must be specified");
        portType = linkPath[portTypeIndex];
            
        if (linkPath.length > portTypeIndex+1)
            portName = linkPath[portTypeIndex+1];
        
        if (linkPath.length > portTypeIndex+2)
            dataPath = Arrays.copyOfRange(linkPath, portTypeIndex+2, linkPath.length);
        
        // find process
        AbstractProcess process;
        if (processName != null)
        {
            if (!((AggregateProcess)parent).getComponentList().hasProperty(processName))
                throw new SMLException(String.format("Cannot find process '%s'", processName));
            
            process = ((AggregateProcess)parent).getComponent(processName);
            
            // process can still be null if link to process was not resolved
            if (process == null)
                throw new SMLException(String.format("Process '%s' exists in the chain but hasn't been resolved yet", processName));
        }
        else
            process = parent;
        
        // find port
        DataComponent port;
        if ("inputs".equals(portType))
            port = process.getInputList().getComponent(portName);
        else if ("outputs".equals(portType))
            port = process.getOutputList().getComponent(portName);
        else if ("parameters".equals(portType))
            port = process.getParameterList().getComponent(portName);
        else
            throw new IllegalArgumentException(String.format("Invalid port type '%s'. Must be one of 'inputs', 'outputs' or 'parameters'", portType));

        // find sub component in port structure
        try
        {
            DataComponent component = (dataPath != null) ? SWEHelper.findComponentByPath(port, dataPath) : port;
            return new LinkTarget(process, component);
        }
        catch (Exception e)
        {
            StringBuilder path = new StringBuilder();
            for(String s : dataPath)
                path.append(s).append(SWEHelper.PATH_SEPARATOR);
            throw new SMLException(String.format("Cannot find data component '%s'", path.substring(0,path.length()-1)), e);
        }
    }
    
    
    /**
     * Constructs the path to link to the specified component
     * @param process the process where the data component resides
     * @param component the component to link to
     * @return Full path to input or output component
     */
    public static String getComponentPath(IProcessExec process, DataComponent component)
    {
        StringBuilder linkString = new StringBuilder();
        
        // find parent port
        DataComponent parentPort = SWEHelper.getRootComponent(component);
        
        // append port type
        if (process.getInputList().contains(parentPort))
            linkString.append("inputs/");
        else if (process.getOutputList().contains(parentPort))
            linkString.append("outputs/");
        else if (process.getParameterList().contains(parentPort))
            linkString.append("parameters/");
        else
            throw new IllegalArgumentException("Cannot find data component in the specified process");
        
        // append path within port structure
        linkString.append(SWEHelper.getComponentPath(component));
        return linkString.toString();
    }
    
    
    /* Deprecated methods */ 
    
    /**
     * @deprecated Use {@link createPhysicalSystem()}
     */
    @Deprecated
    @SuppressWarnings("javadoc")
    public PhysicalSystem newPhysicalSystem()
    {
        return fac.newPhysicalSystem();
    }
    
        
    /* Deprecated way of editing a SensorML process description */
    
    @Deprecated
    AbstractProcess process;
    
    /**
     * @deprecated Use {@link #edit(AbstractProcess)} that returns a builder
     */
    @Deprecated
    @SuppressWarnings("javadoc") 
    public SMLHelper(AbstractProcess process)
    {
        this.process = process;
    }    
    
    @Deprecated
    public void addIdentifier(String label, String def, String value)
    {
        Asserts.checkNotNull(process);
        
        // ensure we have an identification section
        OgcPropertyList<IdentifierList> sectionList = process.getIdentificationList();
        IdentifierList idList;
        if (sectionList.isEmpty())
        {
            idList = fac.newIdentifierList();
            sectionList.add(idList);
        }
        else
            idList = sectionList.get(0);
        
        Term term = fac.newTerm();
        term.setDefinition(def);
        term.setLabel(label);
        term.setValue(value);
        idList.addIdentifier(term);
    }    
    
    public void addSerialNumber(String value)
    {
        addIdentifier(SMLBuilders.SERIAL_NUMBER_LABEL, SMLBuilders.SERIAL_NUMBER_DEF, value);
    }
}
