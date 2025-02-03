/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.
 
Copyright (C) 2012-2015 Sensia Software LLC. All Rights Reserved.
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.data;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.time.Instant;
import org.vast.cdm.common.CDMException;
import org.vast.swe.SWEHelper;
import net.opengis.swe.v20.DataBlock;
import net.opengis.swe.v20.DataComponent;
import net.opengis.swe.v20.Time;


/**
 * <p>
 * This class dynamically generates an accessor with Java Dynamic Proxy Classes
 * using a data component schema and an interface with annotations that maps
 * get/set methods to component paths.
 * </p><p>
 * The {@link SweComponent} annotation must be used. When omitted, the
 * generator will try to infer the name of the component from the get/set method
 * name and look for a corresponding component at the root of the record. 
 * </p><p>
 * The following data types are supported in get/set methods:
 * - All primitive types
 * - java.time.Instant
 * - java.lang.Date
 * - JTS geometries
 * - SWE vector, quaternion, matrix, etc.
 * - primitive arrays?
 * </p>
 * 
 * TODO more efficient implementation based on generated methods compiled in
 * memory with Janino or equivalent.
 *
 * @author Alex Robin
 * @since Jan 20, 2025
 */
public class DataBlockProxy implements IDataAccessor, InvocationHandler
{   
    final DataComponent recordSchema;
    
    
    private DataBlockProxy(DataComponent recordSchema)
    {
         this.recordSchema = recordSchema.copy();
    }
    
    
    @SuppressWarnings("unchecked")
    public static <T extends IDataAccessor> T generate(DataComponent recordSchema, Class<T> targetInterface)
    {
        return (T) Proxy.newProxyInstance(
            targetInterface.getClassLoader(),
            new Class[] { targetInterface }, 
            new DataBlockProxy(recordSchema)
        );
    }
    
    
    @Override
    public Object invoke(Object proxy, Method method, Object[] args)
    {
        var methodName = method.getName();
        
        if ("wrap".equals(methodName))
        {
            wrap((DataBlock)args[0]);
            return null;
        }
        else if ("toString".equals(methodName))
        {
            return toString();
        }
        else if (method.isDefault())
        {
            try { return InvocationHandler.invokeDefault(proxy, method, args); }
            catch (Throwable e) { throw new RuntimeException(e); }
        }
        
        if (recordSchema.getData() == null)
            throw new IllegalStateException("No datablock has been assigned");
        
        // get component from mapping or try to guess
        var comp = findComponentData(method);
        
        if (isGetMethod(method))
        {
            var data = comp.getData();
            var retType = method.getReturnType();
                        
            if (retType == boolean.class)
                return data.getBooleanValue();
            else if (retType == byte.class)
                return data.getByteValue();
            else if (retType == short.class)
                return data.getShortValue();
            else if (retType == int.class)
                return data.getIntValue();
            else if (retType == long.class)
                return data.getLongValue();
            else if (retType == float.class)
                return data.getFloatValue();
            else if (retType == double.class)
                return data.getDoubleValue();
            else if (retType == String.class)
                return data.getStringValue();
            else if (retType == Instant.class)
                return convertToInstant(data);
            else
                throw new IllegalStateException("Unsupported datatype: " + retType);
        }
        
        else if (isSetMethod(method))
        {
            var data = comp.getData();
            var argType = method.getParameters()[0].getType();
            var val = args[0];
                        
            if (argType == boolean.class)
                data.setBooleanValue((boolean)val);
            else if (argType == byte.class)
                data.setByteValue((byte)val);
            else if (argType == short.class)
                data.setShortValue((short)val);
            else if (argType == int.class)
                data.setIntValue((int)val);
            else if (argType == long.class)
                data.setLongValue((long)val);
            else if (argType == float.class)
                data.setFloatValue((float)val);
            else if (argType == double.class)
                data.setDoubleValue((double)val);
            else if (argType == String.class)
                data.setStringValue((String)val);
            else if (argType == Instant.class)
                setFromInstant(data, (Instant)val);
            else
                throw new IllegalStateException("Unsupported datatype: " + argType);
            return null;
        }
        
        else
            return null;
    }
    
    
    protected DataComponent findComponentData(Method m)
    {
        String path = null;
        
        var annotations = m.getAnnotationsByType(SweMapping.class);
        if (annotations.length > 0)
        {
            var sweMapping = annotations[0];
            path = sweMapping.path();
        }
        
        try
        {
            return SWEHelper.findComponentByPath(recordSchema, path);
        }
        catch (CDMException e)
        {
            throw new IllegalStateException("Invalid component path: " + path, e);
        }
    }
    
    
    protected boolean isGetMethod(Method m)
    {
        return m.getName().startsWith("get") &&
               m.getReturnType() != void.class &&
               m.getParameters().length == 0;
    }
    
    
    protected boolean isSetMethod(Method m)
    {
        return m.getName().startsWith("set") &&
               m.getReturnType() == void.class &&
               m.getParameters().length == 1;
    }
    
    
    protected Instant convertToInstant(DataBlock data)
    {
        var epochTimeAsDouble = data.getDoubleValue();
        var seconds = Math.floor(epochTimeAsDouble);
        var nanos = (int)((epochTimeAsDouble - epochTimeAsDouble) * 1e9);
        return Instant.ofEpochSecond((long)seconds, nanos);
    }
    
    
    protected void setFromInstant(DataBlock data, Instant ts)
    {
        if (ts != null)
        {
            var seconds = ts.getEpochSecond();
            var nanos = ts.getNano();
            var t = (double)seconds + nanos/1e9;
            data.setDoubleValue(t);
        }
        else
            data.setDoubleValue(Double.NaN);
    }


    @Override
    public void wrap(DataBlock db)
    {
        recordSchema.setData(db);
    }
    
    
    @Override
    public String toString()
    {
        var it = new ScalarIterator(recordSchema);
        var sb = new StringBuilder();
        while (it.hasNext())
        {            
            var c = it.next();
            var dblk = c.getData();
            
            sb.append(c.getName()).append(": ");
            
            if (c instanceof Time)
            {
                if (!Double.isNaN(dblk.getDoubleValue()))
                    sb.append(Instant.ofEpochMilli((long)(dblk.getDoubleValue()*1000)));
                else
                    sb.append("");
            }
            else
                sb.append(dblk.getStringValue());
            
            sb.append('\n');
        }
        
        return sb.toString();
    }
}
