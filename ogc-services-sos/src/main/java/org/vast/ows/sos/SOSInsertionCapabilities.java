/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License Version
1.1 (the "License"); you may not use this file except in compliance with
the License. You may obtain a copy of the License at
http://www.mozilla.org/MPL/MPL-1.1.html

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.

The Original Code is the "OGC Service Framework".

The Initial Developer of the Original Code is Spotimage S.A.
Portions created by the Initial Developer are Copyright (C) 2007
the Initial Developer. All Rights Reserved.

Contributor(s): 
   Alexandre Robin <alexandre.robin@spotimage.fr>

******************************* END LICENSE BLOCK ***************************/

package org.vast.ows.sos;

import java.util.LinkedHashSet;
import java.util.Set;


/**
 * <p><b>Description:</b><br/>
 * Representation of SOS insertion capabilities section.
 * </p>
 *
 * @author Alex Robin
 * @date Sep 15, 2013
 */
public class SOSInsertionCapabilities
{
    protected Set<String> procedureFormats;
    protected Set<String> observationTypes;
    protected Set<String> foiTypes;
    protected Set<String> supportedEncodings;
	
	
	public SOSInsertionCapabilities()
	{
	    procedureFormats = new LinkedHashSet<String>(1);
        observationTypes = new LinkedHashSet<String>(2);
        foiTypes = new LinkedHashSet<String>(2);
        supportedEncodings = new LinkedHashSet<String>(2);
	}
	
	
	public Set<String> getProcedureFormats()
    {
        return procedureFormats;
    }
    
    
    public Set<String> getObservationTypes()
    {
        return observationTypes;
    }
    
    
    public Set<String> getFoiTypes()
    {
        return foiTypes;
    }
    
    
    public Set<String> getSupportedEncodings()
    {
        return supportedEncodings;
    }
}
