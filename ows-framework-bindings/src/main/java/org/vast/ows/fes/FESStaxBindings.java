/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are Copyright (C) 2014 Sensia Software LLC.
 All Rights Reserved.
 
 Contributor(s): 
    Alexandre Robin <alex.robin@sensiasoftware.com>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ows.fes;

import org.vast.ogc.gml.GMLStaxBindings;
import net.opengis.fes.v20.Factory;
import net.opengis.fes.v20.impl.FESFactory;
import net.opengis.fes.v20.bind.XMLStreamBindings;


/**
 * <p>
 * Helper wrapping the auto-generated FES StAX bindings
 * </p>
 *
 * <p>Copyright (c) 2014 Sensia Software LLC</p>
 * @author Alexandre Robin <alex.robin@sensiasoftware.com>
 * @since Sep 25, 2014
 */
public class FESStaxBindings extends XMLStreamBindings
{

    public FESStaxBindings()
    {
        this(false);
    }
    
    
    public FESStaxBindings(boolean useJTS)
    {
        super(new FESFactory(), new GMLStaxBindings(useJTS));
        nsContext = gmlBindings.getNamespaceContext();
        nsContext.registerNamespace("fes", net.opengis.fes.v20.bind.XMLStreamBindings.NS_URI);
    }
    
    
    public Factory getFactory()
    {
        return factory;
    }
    
}
