/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are subject to the Mozilla Public License Version
 1.1 (the "License"); you may not use this file except in compliance with
 the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/MPL-1.1.html
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.
 
 The Original Code is the "OGC Service Framework".
 
 The Initial Developer of the Original Code is the VAST team at the University of Alabama in Huntsville (UAH). <http://vast.uah.edu> Portions created by the Initial Developer are Copyright (C) 2007 the Initial Developer. All Rights Reserved. Please Contact Mike Botts <mike.botts@uah.edu> for more information.
 
 Contributor(s): 
    Alexandre Robin <alexandre.robin@spotimage.fr>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ows.sps;

import java.util.Map;
import java.util.Set;

import net.opengis.sps.x10.InputDescriptorDocument.InputDescriptor;
import net.opengis.sps.x10.InputParameterType;
import net.opengis.sps.x10.NotificationTargetType;
import net.opengis.sps.x10.ParametersType;
import net.opengis.sps.x10.SubmitDocument;
import net.opengis.sps.x10.SubmitRequestType;
import net.opengis.sps.x10.SubmitRequestType.SensorParam;

import org.apache.xmlbeans.XmlObject;


/**
 * <p><b>Title:</b><br/>
 * SPSSubmitRequest
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Class for creating SPS Submit requests. Based upon given notification
 * information and data description, a Submit request will be created by this
 * class.
 * </p>
 * 
 * <p><b>Limitations:</b><br/>
 * Time 
 *
 * <p>Copyright (c) 2007</p>
 * @author Gregoire Berthiau, Johannes Echterhoff
 * @date Nov 30, 2007
 * @version 1.0
 */
public class SPSSubmitRequest
{
	
	
	private SubmitDocument request;
	
//	private static final String DEFAULT_WNS_ID = "NULL";
//	private static final String DEFAULT_WNS_URL = "http://localhost/WNS/wns";
	
	public SPSSubmitRequest(String wnsId, String wnsUrl, String sensorID, Map<String, InputDescriptor> descriptorMap, Set<String> inputParamIds) {
	   
//	   String wnsId_;
//	   String wnsUrl_;
//	   
//	   if(wnsId != null) {
//	      wnsId_ = wnsId;
//	   } else {
//	      wnsId_ = DEFAULT_WNS_ID;
//	   }
//	   
//	   if(wnsUrl != null) {
//	      wnsUrl_ = wnsUrl;
//	   } else {
//	      wnsUrl_ = DEFAULT_WNS_URL;
//	   }
	   
	   SubmitDocument sd = SubmitDocument.Factory.newInstance();
      SubmitRequestType srt = sd.addNewSubmit();
      
      srt.setVersion("1.0.0");
      srt.setService("SPS");
      
      NotificationTargetType ntt = srt.addNewNotificationTarget();
      ntt.setNotificationID(wnsId);
      ntt.setNotificationURL(wnsUrl);
      
      SensorParam tmp = srt.addNewSensorParam();
		tmp.addNewSensorID().setStringValue(sensorID);
		ParametersType parameters = tmp.addNewParameters();
		
		for(String inputParamId : inputParamIds) {
		   
		   InputDescriptor descriptor = descriptorMap.get(inputParamId);
		   
		   InputParameterType param = parameters.addNewInputParameter();
		   param.setParameterID(descriptor.getParameterID());
		   param.addNewValue().set((XmlObject)descriptor.getDefinition().getCommonData().getDomNode());
		}
	}
	
	public SubmitDocument getSubmitRequest() {	   
	   return this.request;
	}
	
}
