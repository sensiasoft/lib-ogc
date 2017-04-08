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

import org.vast.ogc.gml.GMLStaxBindings;
import net.opengis.fes.v20.Factory;
import net.opengis.fes.v20.impl.FESFactory;
import net.opengis.fes.v20.bind.XMLStreamBindings;


/**
 * <p>
 * Helper wrapping the auto-generated FES StAX bindings
 * </p>
 *
 * @author Alex Robin
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
        nsContext.registerNamespace("ows", net.opengis.fes.v20.bind.XMLStreamBindings.OWS_NS_URI);
    }
    
    
    public Factory getFactory()
    {
        return factory;
    }
    
}
