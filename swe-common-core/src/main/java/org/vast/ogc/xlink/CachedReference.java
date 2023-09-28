/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are subject to the Mozilla Public License Version
 1.1 (the "License"); you may not use this file except in compliance with
 the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/MPL-1.1.html
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.
 
 The Initial Developer of the Original Code is SENSIA SOFTWARE LLC.
 Portions created by the Initial Developer are Copyright (C) 2012
 the Initial Developer. All Rights Reserved.

 Please Contact Alexandre Robin for more
 information.
 
 Contributor(s): 
    Alexandre Robin
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ogc.xlink;

import java.io.IOException;
import java.util.Objects;
import org.vast.util.ResolveException;


/**
 * <p>
 * Implementation of Xlink Reference that keeps a cached version of the target.
 * Object is cached on the first call to getTarget().
 * Reloading the target object can be enforced by calling refresh().
 * </p>
 * 
 * @param <T> Type of the link target object
 *
 * @author Alex Robin
 * @since Sep 28, 2012
 *
 */
public class CachedReference<T> implements IXlinkReference<T>
{
    protected String href;
    protected String role;
    protected String arcRole;
    protected String title;
    protected T value;
    protected transient IReferenceResolver<? extends T> resolver;


    public CachedReference()
    {        
    }
    
    
    public CachedReference(String href)
    {
        setHref(href);
    }
    
    
    public CachedReference(IReferenceResolver<? extends T> resolver)
    {
        setResolver(resolver);
    }
    
    
    public CachedReference(String href, IReferenceResolver<T> resolver)
    {
        setHref(href);
        setResolver(resolver);
    }
    
    
    @Override
    public String getHref()
    {
        return href;
    }


    @Override
    public void setHref(String href)
    {
        this.href = href;
    }


    @Override
    public String getRole()
    {
        return role;
    }


    @Override
    public void setRole(String role)
    {
        this.role = role;
    }


    @Override
    public String getArcRole()
    {
        return arcRole;
    }


    @Override
    public void setArcRole(String arcRole)
    {
        this.arcRole = arcRole;
    }


    @Override
    public String getTitle()
    {
        return title;
    }


    @Override
    public void setTitle(String title)
    {
        this.title = title;
    }


    @Override
    public T getTarget()
    {
        try
        {
            if (value == null)
                refresh();
        }
        catch (IOException e)
        {
            throw new ResolveException("Error while fetching linked content", e);
        }
        
        return value;
    }
    
    
    public void setResolver(IReferenceResolver<? extends T> resolver)
    {
        this.resolver = resolver;
    }
    
    
    public void refresh() throws IOException
    {
        if (resolver == null)
            throw new IllegalStateException("No resolver has been set for " + href);
        
        value = fetchTarget(href);
        
        // also try to fetch using role URI as unique ID of the object
        if (value == null && role != null)
            value = fetchTarget(role);       
    }
    
    
    protected T fetchTarget(String href) throws IOException
    {
        if (href != null)
            return resolver.fetchTarget(href);
        else
            return null;
    }
    
    
    @Override
    public boolean equals(Object o)
    {
        if (!(o instanceof CachedReference))
            return false;
        
        var ref = (CachedReference<?>)o; 
        return Objects.equals(href, ref.href) &&
               Objects.equals(role, ref.role) &&
               Objects.equals(arcRole, ref.arcRole);
    }
    
    
    @Override
    public int hashCode()
    {
        return Objects.hash(href, role, arcRole);
    }
}
