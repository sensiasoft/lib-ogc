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

import java.util.ArrayList;
import java.util.List;


/**
 * <p><b>Description:</b><br/>
 * Representation of SOS insertion capabilities section.
 * </p>
 *
 * <p>Copyright (c) 2013</p>
 * @author Alexandre Robin <alex.robin@sensiasoftware.com>
 * @date Sep 15, 2013
 */
public class SOSInsertionCapabilities
{
    protected List<String> procedureFormats;
    protected List<String> observationTypes;
    protected List<String> foiTypes;
    protected List<String> supportedEncodings;
	
	
	public SOSInsertionCapabilities()
	{
	    procedureFormats = new ArrayList<String>(1);
        observationTypes = new ArrayList<String>(2);
        foiTypes = new ArrayList<String>(2);
        supportedEncodings = new ArrayList<String>(2);
	}
	
	
	public List<String> getProcedureFormats()
    {
        return procedureFormats;
    }


    public void setProcedureFormats(List<String> procedureFormats)
    {
        this.procedureFormats = procedureFormats;
    }
    
    
    public List<String> getObservationTypes()
    {
        return observationTypes;
    }


    public void setObservationTypes(List<String> observationTypes)
    {
        this.observationTypes = observationTypes;
    }
    
    
    public List<String> getFoiTypes()
    {
        return foiTypes;
    }


    public void setFoiTypes(List<String> foiTypes)
    {
        this.foiTypes = foiTypes;
    }
    
    
    public List<String> getSupportedEncodings()
    {
        return supportedEncodings;
    }


    public void setSupportedEncodings(List<String> supportedEncodings)
    {
        this.supportedEncodings = supportedEncodings;
    }
}
