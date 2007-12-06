/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are subject to the Mozilla Public License Version
 1.1 (the "License"); you may not use this file except in compliance with
 the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/MPL-1.1.html
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.
 
 The Original Code is the "OGC Service Framework".
 
 The Initial Developer of the Original Code is the VAST team at the
 University of Alabama in Huntsville (UAH). <http://vast.uah.edu>
 Portions created by the Initial Developer are Copyright (C) 2007
 the Initial Developer. All Rights Reserved.
 Please Contact Mike Botts <mike.botts@uah.edu> for more information.
 
 Contributor(s): 
    Alexandre Robin <alexandre.robin@spotimage.fr>
 
 ******************************* END LICENSE BLOCK ***************************/

package org.vast.ows.sps;

import java.util.Map;
import java.util.Set;

import net.opengis.sps.x10.InputParameterType;
import net.opengis.sps.x10.NotificationTargetType;
import net.opengis.sps.x10.ParametersType;
import net.opengis.sps.x10.SubmitDocument;
import net.opengis.sps.x10.SubmitRequestType;
import net.opengis.sps.x10.InputDescriptorDocument.InputDescriptor;
import net.opengis.sps.x10.InputDescriptorType.Definition.CommonData;
import net.opengis.sps.x10.InputParameterType.Value;
import net.opengis.sps.x10.SubmitRequestType.SensorParam;
import net.opengis.swe.x10.BooleanDocument;
import net.opengis.swe.x10.CategoryDocument;
import net.opengis.swe.x10.ConditionalDataDocument;
import net.opengis.swe.x10.ConditionalDataType;
import net.opengis.swe.x10.ConditionalValueDocument;
import net.opengis.swe.x10.ConditionalValueType;
import net.opengis.swe.x10.CountDocument;
import net.opengis.swe.x10.CountRangeDocument;
import net.opengis.swe.x10.DataRecordDocument;
import net.opengis.swe.x10.DataRecordType;
import net.opengis.swe.x10.EnvelopeDocument;
import net.opengis.swe.x10.EnvelopeType;
import net.opengis.swe.x10.GeoLocationAreaDocument;
import net.opengis.swe.x10.PositionDocument;
import net.opengis.swe.x10.PositionType;
import net.opengis.swe.x10.QuantityDocument;
import net.opengis.swe.x10.QuantityRangeDocument;
import net.opengis.swe.x10.SimpleDataRecordDocument;
import net.opengis.swe.x10.SimpleDataRecordType;
import net.opengis.swe.x10.TextDocument;
import net.opengis.swe.x10.TimeDocument;
import net.opengis.swe.x10.TimeRangeDocument;
import net.opengis.swe.x10.VectorDocument;
import net.opengis.swe.x10.VectorType;
import net.opengis.swe.x10.GeoLocationAreaDocument.GeoLocationArea;

import org.apache.log4j.Logger;

/**
 * <p>
 * <b>Title:</b><br/> SPSSubmitRequest
 * </p>
 * 
 * <p>
 * <b>Description:</b><br/> Class for creating SPS Submit requests. Based upon
 * given notification information and data description, a Submit request will be
 * created by this class.
 * </p>
 * 
 * <p>
 * <b>Limitations:</b><br/> Time
 * 
 * <p>
 * Copyright (c) 2007
 * </p>
 * 
 * @author Johannes Echterhoff, Gregoire Berthiau
 * @date Nov 30, 2007
 * @version 1.0
 */
public class SPSSubmitRequest {

   private static Logger log = Logger.getLogger(SPSSubmitRequest.class);

   private SubmitDocument request;

   public SPSSubmitRequest(String wnsId, String wnsUrl, String sensorID,
         Map<String, InputDescriptor> descriptorMap, Set<String> inputParamIds) {

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

      for (String inputParamId : inputParamIds) {

         InputDescriptor descriptor = descriptorMap.get(inputParamId);

         InputParameterType param = parameters.addNewInputParameter();
         param.setParameterID(descriptor.getParameterID());

         Value value = param.addNewValue();

         CommonData data = descriptor.getDefinition().getCommonData();

         if (data.isSetBoolean()) {
            BooleanDocument bd = BooleanDocument.Factory.newInstance();
            bd.addNewBoolean().set(data.getBoolean());
            value.set(bd);
         } else if (data.isSetText()) {
            TextDocument td = TextDocument.Factory.newInstance();
            td.addNewText().set(data.getText());
            value.set(td);
         } else if (data.isSetTime()) {
            TimeDocument td = TimeDocument.Factory.newInstance();
            td.addNewTime().set(data.getTime());
            value.set(td);
         } else if (data.isSetCount()) {
            CountDocument cd = CountDocument.Factory.newInstance();
            cd.addNewCount().set(data.getCount());
            value.set(cd);
         } else if (data.isSetQuantity()) {
            QuantityDocument qd = QuantityDocument.Factory.newInstance();
            qd.addNewQuantity().set(data.getQuantity());
            value.set(qd);
         } else if (data.isSetCategory()) {
            CategoryDocument cd = CategoryDocument.Factory.newInstance();
            cd.addNewCategory().set(data.getCategory());
            value.set(cd);
         } else if (data.isSetCountRange()) {
            CountRangeDocument crd = CountRangeDocument.Factory.newInstance();
            crd.addNewCountRange().set(data.getCountRange());
            value.set(crd);
         } else if (data.isSetQuantityRange()) {
            QuantityRangeDocument qrd = QuantityRangeDocument.Factory.newInstance();
            qrd.addNewQuantityRange().set(data.getQuantityRange());
            value.set(qrd);
         } else if (data.isSetTimeRange()) {
            TimeRangeDocument trd = TimeRangeDocument.Factory.newInstance();
            trd.addNewTimeRange().set(data.getTimeRange());
            value.set(trd);
         } else if (data.isSetAbstractDataRecord()) {

            if (data.getAbstractDataRecord().schemaType().equals(
                  PositionType.type)) {
               PositionDocument pd = PositionDocument.Factory.newInstance();
               pd.addNewPosition().set(
                     (PositionType) data.getAbstractDataRecord());
               value.set(pd);
            } else if (data.getAbstractDataRecord().schemaType().equals(
                  VectorType.type)) {
               VectorDocument vd = VectorDocument.Factory.newInstance();
               vd.addNewVector().set((VectorType) data.getAbstractDataRecord());
               value.set(vd);
            } else if (data.getAbstractDataRecord().schemaType().equals(
                  EnvelopeType.type)) {
               EnvelopeDocument ed = EnvelopeDocument.Factory.newInstance();
               ed.addNewEnvelope().set(
                     (EnvelopeType) data.getAbstractDataRecord());
               value.set(ed);
            } else if (data.getAbstractDataRecord().schemaType().equals(
                  SimpleDataRecordType.type)) {
               SimpleDataRecordDocument sdrd = SimpleDataRecordDocument.Factory.newInstance();
               sdrd.addNewSimpleDataRecord().set(
                     (SimpleDataRecordType) data.getAbstractDataRecord());
               value.set(sdrd);
            } else if (data.getAbstractDataRecord().schemaType().equals(
                  DataRecordType.type)) {
               DataRecordDocument drd = DataRecordDocument.Factory.newInstance();
               drd.addNewDataRecord().set(
                     (DataRecordType) data.getAbstractDataRecord());
               value.set(drd);
            } else if (data.getAbstractDataRecord().schemaType().equals(
                  ConditionalValueType.type)) {
               ConditionalValueDocument cvd = ConditionalValueDocument.Factory.newInstance();
               cvd.addNewConditionalValue().set(
                     (ConditionalValueType) data.getAbstractDataRecord());
               value.set(cvd);
            } else if (data.getAbstractDataRecord().schemaType().equals(
                  ConditionalDataType.type)) {
               ConditionalDataDocument cdd = ConditionalDataDocument.Factory.newInstance();
               cdd.addNewConditionalData().set(
                     (ConditionalDataType) data.getAbstractDataRecord());
               value.set(cdd);
            } else if (data.getAbstractDataRecord().schemaType().equals(
                  GeoLocationArea.type)) {
               GeoLocationAreaDocument glad = GeoLocationAreaDocument.Factory.newInstance();
               glad.addNewGeoLocationArea().set(
                     (GeoLocationArea) data.getAbstractDataRecord());
               value.set(glad);
            } else {
               log.debug("Unknown type for AbstractDataRecord encountered: "
                     + data.getAbstractDataRecord().schemaType());
            }

         } else if (data.isSetAbstractDataArray1()) {
            log.debug("AbstractDataArray encountered which is not supported by this implementation.");
         } else {
            log.debug("Unknown descriptor data encountered when trying to"
                  + " populate InputParameter: " + data.xmlText());
         }
      }

      this.request = sd;
   }

   public SubmitDocument getSubmitRequest() {

      return this.request;
   }

}
