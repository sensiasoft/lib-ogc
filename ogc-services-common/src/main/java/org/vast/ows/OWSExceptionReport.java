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

package org.vast.ows;

import java.util.ArrayList;
import java.util.List;


/**
 * <p>
 * This class encapsulates a list of OWS exceptions and can be
 * used to generate an XML report.
 * It itself derives from OWSException can be thrown as such. 
 * </p>
 *
 * @author Alex Robin
 * @since 23 oct. 07
 * */
public class OWSExceptionReport extends OWSException
{
	static final long serialVersionUID = 0x2E03E7BD4483B097L;
	protected ArrayList<OWSException> exceptionList;
	
	
	public OWSExceptionReport()
	{
		super("");
		this.version = "1.0";
		this.exceptionList = new ArrayList<OWSException>();
	}
	
	
	public OWSExceptionReport(String version)
	{
		this();
		this.version = version;
	}
	
	
	public OWSExceptionReport(OWSException e)
	{
		this();
		version = e.version;
		soapVersion = e.soapVersion;
		exceptionList.add(e);
	}
	
	
	/**
	 * Helper method to add an exception to the report
	 * @param e
	 */
	public void add(OWSException e)
	{
		exceptionList.add(e);
	}


	public List<OWSException> getExceptionList()
	{
		return exceptionList;
	}

	
	@Override
	public String getMessage()
	{
		StringBuilder buf = new StringBuilder();
		
		for (int i=0; i<exceptionList.size(); i++)
		{
			OWSException e = exceptionList.get(i);
			buf.append(e.getMessage()).append('\n');
		}
		
		buf.setLength(buf.length()-1);
		return buf.toString();
	}
	
	
	public void process() throws OWSException
	{
		if (!exceptionList.isEmpty())
			throw this;
	}
}
