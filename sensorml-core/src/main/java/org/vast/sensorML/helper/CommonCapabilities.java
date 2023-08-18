/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.
 
Copyright (C) 2020 Sensia Software LLC. All Rights Reserved.
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.sensorML.helper;

import org.vast.sensorML.SMLHelper;
import org.vast.sensorML.SMLMetadataBuilders.CapabilityListBuilder;
import org.vast.swe.SWEBuilders.QuantityBuilder;
import org.vast.swe.SWEBuilders.QuantityRangeBuilder;
import org.vast.swe.SWEHelper;
import net.opengis.swe.v20.DataArray;
import net.opengis.swe.v20.Quantity;


/**
 * <p>
 * Helper methods to generate common system capabilities
 * </p>
 *
 * @author Alex Robin
 * @date Jul 16, 2020
 */
public class CommonCapabilities extends SMLPropertiesHelper
{
    public final static String SSN_SYSTEMS_URL_PREFIX = "http://www.w3.org/ns/ssn/systems/";
    
    public final static String SYSTEM_CAPS_DEF = SSN_SYSTEMS_URL_PREFIX + "SystemCapability";
    public final static String OPERATING_RANGE_DEF = SSN_SYSTEMS_URL_PREFIX + "OperatingRange";
    public final static String SURVIVAL_RANGE_DEF = SSN_SYSTEMS_URL_PREFIX + "SurvivalRange";
    
    public static final String MEAS_RANGE_DEF = SSN_SYSTEMS_URL_PREFIX + "MeasurementRange";
    public static final String MEAS_RANGE_LABEL = "Measurement Range";
    public static final String DYNAMIC_RANGE_DEF = SWEHelper.getPropertyUri("DynamicRange");
    public static final String DYNAMIC_RANGE_LABEL = "Dynamic Range";
    public static final String ACTUATION_RANGE_DEF = SSN_SYSTEMS_URL_PREFIX + "ActuationRange";
    public static final String ACTUATION_RANGE_LABEL = "Actuation Range";
    public static final String SAMPLING_FREQ_DEF = SWEHelper.getPropertyUri("SamplingFrequency");
    public static final String SAMPLING_FREQ_LABEL = "Sampling Frequency";
    public static final String REPORTING_FREQ_DEF = SWEHelper.getPropertyUri("ReportingFrequency");
    public static final String REPORTING_FREQ_LABEL = "Reporting Frequency";
    public static final String INTEGRATION_TIME_DEF = SWEHelper.getPropertyUri("IntegrationTime");
    public static final String INTEGRATION_TIME_LABEL = "Integration Time";
    public static final String RESPONSE_TIME_DEF = SSN_SYSTEMS_URL_PREFIX + "ResponseTime";
    public static final String RESPONSE_TIME_LABEL = "Response Time";
    public static final String LATENCY_DEF = SSN_SYSTEMS_URL_PREFIX + "Latency";
    public static final String LATENCY_LABEL = "Latency";
    public static final String SENSITIVITY_DEF = SSN_SYSTEMS_URL_PREFIX + "Sensitivity";
    public static final String SENSITIVITY_LABEL = "Sensitivity";
    public static final String RESOLUTION_DEF = SSN_SYSTEMS_URL_PREFIX + "Resolution";
    public static final String RESOLUTION_LABEL = "Resolution";
    public static final String DETECTION_LIMIT_DEF = SSN_SYSTEMS_URL_PREFIX + "DetectionLimit";
    public static final String DETECTION_LIMIT_LABEL = "Detection Limit";
    public static final String DRIFT_DEF = SSN_SYSTEMS_URL_PREFIX + "Drift";
    public static final String DRIFT_LABEL = "Drift";
    public static final String ACCURACY_DEF = SSN_SYSTEMS_URL_PREFIX + "Accuracy";
    public static final String ACCURACY_LABEL = "Accuracy";
    public static final String ABSOLUTE_ACCURACY_DEF = SWEHelper.getPropertyUri("AbsoluteAccuracy");
    public static final String ABSOLUTE_ACCURACY_LABEL = "Absolute Accuracy";
    public static final String RELATIVE_ACCURACY_DEF = SWEHelper.getPropertyUri("RelativeAccuracy");
    public static final String RELATIVE_ACCURACY_LABEL = "Relative Accuracy";
    public static final String PRECISION_DEF = SSN_SYSTEMS_URL_PREFIX + "Precision";
    public static final String PRECISION_LABEL = "Precision";
    public static final String SNR_DEF = SWEHelper.getPropertyUri("SNR");
    public static final String SNR_LABEL = "Signal-to-Noise Ratio";
    public static final String FOV_DEF = SWEHelper.getPropertyUri("FieldOfView");
    public static final String FOV_LABEL = "Field of View";
    public static final String FOCAL_LENGTH_DEF = SWEHelper.getPropertyUri("FocalLength");
    public static final String FOCAL_LENGTH_LABEL = "Focal Length";
    public static final String POINTING_ANGLE_DEF = SWEHelper.getPropertyUri("PointingRange");
    public static final String POINTING_ANGLE_LABEL = "Pointing Range";
    
    
    public CommonCapabilities(SMLHelper sml)
    {
        super(sml);
    }
    
    
    public CapabilityListBuilder operatingProperties()
    {
        return sml.createCapabilityList()
            .definition(OPERATING_RANGE_DEF)
            .label("Operating Range");
    }
    
    
    public CapabilityListBuilder survivableProperties()
    {
        return sml.createCapabilityList()
            .definition(SURVIVAL_RANGE_DEF)
            .label("Survival Range");
    }
    
    
    /**
     * Creates a capability list describing the system capabilities,
     * using W3C SSN semantics
     * @return The pre-configured builder for chaining
     */
    public CapabilityListBuilder systemCapabilities()
    {
        return sml.createCapabilityList()
            .definition(SYSTEM_CAPS_DEF)
            .label("System Capabilities");
    }
    
    public QuantityRangeBuilder measurementRange(double min, double max, String uom)
    {
        return sml.createQuantityRange()
            .definition(MEAS_RANGE_DEF)
            .label(MEAS_RANGE_LABEL)
            .uomCode(uom)
            .value(min, max);
    }
    
    public QuantityRangeBuilder actuationRange(double min, double max, String uom)
    {
        return sml.createQuantityRange()
            .definition(ACTUATION_RANGE_DEF)
            .label(ACTUATION_RANGE_LABEL)
            .uomCode(uom)
            .value(min, max);
    }
    
    public QuantityRangeBuilder dynamicRange(double min, double max, String uom)
    {
        return sml.createQuantityRange()
            .definition(DYNAMIC_RANGE_DEF)
            .label(DYNAMIC_RANGE_LABEL)
            .uomCode(uom)
            .value(min, max);
    }
    
    public QuantityBuilder samplingFrequency(double freq)
    {
        return sml.createQuantity()
            .definition(SAMPLING_FREQ_DEF)
            .label(SAMPLING_FREQ_LABEL)
            .uomCode("Hz")
            .value(freq);
    }
    
    public QuantityBuilder reportingFrequency(double freq)
    {
        return sml.createQuantity()
            .definition(REPORTING_FREQ_DEF)
            .label(REPORTING_FREQ_LABEL)
            .uomCode("Hz")
            .value(freq);
    }
    
    public QuantityBuilder integrationTime(double value, String uom)
    {
        checkUom(uom, SWEHelper.TIME_UNIT);
        
        return sml.createQuantity()
            .definition(INTEGRATION_TIME_DEF)
            .label(INTEGRATION_TIME_LABEL)
            .uomCode(uom)
            .value(value);
    }
    
    public QuantityRangeBuilder integrationTimeRange(double min, double max, String uom)
    {
        checkUom(uom, SWEHelper.TIME_UNIT);
        
        return sml.createQuantityRange()
            .definition(INTEGRATION_TIME_DEF)
            .label(INTEGRATION_TIME_LABEL)
            .uomCode(uom)
            .value(min, max);
    }
    
    public QuantityBuilder responseTime(double value, String uom)
    {
        checkUom(uom, SWEHelper.TIME_UNIT);
        
        return sml.createQuantity()
            .definition(RESPONSE_TIME_DEF)
            .label(RESPONSE_TIME_LABEL)
            .uomCode("ms")
            .value(value);
    }
    
    public QuantityBuilder latency(double value, String uom)
    {
        checkUom(uom, SWEHelper.TIME_UNIT);
        
        return sml.createQuantity()
            .definition(LATENCY_DEF)
            .label(LATENCY_LABEL)
            .uomCode(uom)
            .value(value);
    }
    
    public QuantityBuilder resolution(double value, String uom)
    {
        return sml.createQuantity()
            .definition(RESOLUTION_DEF)
            .label(RESOLUTION_LABEL)
            .uomCode(uom)
            .value(value);
    }
    
    public QuantityBuilder detectionLimit(double value, String uom)
    {
        return sml.createQuantity()
            .definition(DETECTION_LIMIT_DEF)
            .label(DETECTION_LIMIT_LABEL)
            .uomCode(uom)
            .value(value);
    }
    
    public QuantityBuilder absoluteAccuracy(double value, String uom)
    {
        return sml.createQuantity()
            .definition(ABSOLUTE_ACCURACY_DEF)
            .label(ABSOLUTE_ACCURACY_LABEL)
            .uomCode(uom)
            .value(value);
    }
    
    public QuantityBuilder relativeAccuracy(double value)
    {
        return sml.createQuantity()
            .definition(RELATIVE_ACCURACY_DEF)
            .label(RELATIVE_ACCURACY_LABEL)
            .uomCode("%")
            .value(value);
    }
    
    public QuantityBuilder drift(double value, String uom)
    {
        return sml.createQuantity()
            .definition(DRIFT_DEF)
            .label(DRIFT_LABEL)
            .uomCode(uom)
            .value(value);
    }
    
    public QuantityBuilder precision(double value, String uom)
    {
        return sml.createQuantity()
            .definition(PRECISION_DEF)
            .label(PRECISION_LABEL)
            .uomCode(uom)
            .value(value);
    }
    
    public QuantityBuilder snr(double value)
    {
        return sml.createQuantity()
            .definition(SNR_DEF)
            .label(SNR_LABEL)
            .uomCode("dB")
            .value(value);
    }
    
    public QuantityBuilder sensitivity(double value, String uom)
    {
        return sml.createQuantity()
            .definition(SENSITIVITY_DEF)
            .label(SENSITIVITY_LABEL)
            .uomCode(uom)
            .value(value);
    }
    
    public Quantity mtbf(double val, String uom)
    {
        checkUom(uom, SWEHelper.TIME_UNIT);
        
        return sml.createQuantity()
            .definition(SWEHelper.getDBpediaUri("Mean_time_between_failures"))
            .label("MTBF")
            .uomCode(uom)
            .value(val)
            .build();
    }
    
    public DataArray characteristicCurve(String label, Quantity input, Quantity output)
    {
        DataArray array = sml.createArray()
            .definition(SWEHelper.getPropertyUri("CharacteristicCurve"))
            .label(label != null ? label : "Characteristic Curve")
            .withFixedSize(1)
            .withElement("point", sml.createRecord()
                .addField(input.getName(), input)
                .addField(output.getName(), output)
                .build())
            .build();
        
        array.assignNewDataBlock();
        array.setEncoding(sml.newTextEncoding(",", " "));
        array.setValues(sml.newEncodedValuesProperty());
        return array;
    }
    
    public DataArray spectralResponse(String freqUnit)
    {
        DataArray array = sml.createArray()
            .definition(SWEHelper.getPropertyUri("SpectralResponse"))
            .label("Frequency Response")
            .withFixedSize(1)
            .withElement("point", sml.createRecord()
                .addField("freq", sml.createQuantity()
                    .definition(SWEHelper.getQudtUri("Frequency"))
                    .label("Frequency")
                    .uomCode(freqUnit)
                    .build())
                .addField("power", sml.createQuantity()
                    .definition(SWEHelper.getQudtUri("SoundPowerLevel"))
                    .label("Output Level")
                    .uomCode("dB")
                    .build())
                .build())
            .build();
        
        array.assignNewDataBlock();
        array.setEncoding(sml.newTextEncoding(",", " "));
        array.setValues(sml.newEncodedValuesProperty());
        return array;
    }
    
    
    /* geometric properties */
    
    public QuantityBuilder fieldOfView(double value, String uom)
    {
        checkUom(uom, SWEHelper.ANGLE_UNIT);
        
        return sml.createQuantity()
            .definition(FOV_DEF)
            .label(FOV_LABEL)
            .uomCode(uom)
            .value(value);
    }
    
    public QuantityRangeBuilder fovRange(double min, double max, String uom)
    {
        checkUom(uom, SWEHelper.ANGLE_UNIT);
        
        return sml.createQuantityRange()
            .definition(FOV_DEF)
            .label(FOV_LABEL)
            .uomCode(uom)
            .value(min, max);
    }
    
    public QuantityBuilder focalLength(double value, String uom)
    {
        checkUom(uom, SWEHelper.DISTANCE_UNIT);
        
        return sml.createQuantity()
            .definition(FOCAL_LENGTH_DEF)
            .label(FOCAL_LENGTH_LABEL)
            .uomCode(uom)
            .value(value);
    }
    
    public QuantityRangeBuilder focalLengthRange(double min, double max, String uom)
    {
        checkUom(uom, SWEHelper.DISTANCE_UNIT);
        
        return sml.createQuantityRange()
            .definition(FOCAL_LENGTH_DEF)
            .label(FOCAL_LENGTH_LABEL)
            .uomCode(uom)
            .value(min, max);
    }
    
    
    public QuantityRangeBuilder pointingRange(double min, double max, String uom)
    {
        checkUom(uom, SWEHelper.ANGLE_UNIT);
        
        return sml.createQuantityRange()
            .definition(POINTING_ANGLE_DEF)
            .label(POINTING_ANGLE_LABEL)
            .uomCode(uom)
            .value(min, max);
    }
    
    
    public DataArray directionalResponse()
    {
        DataArray array = sml.createArray()
            .definition(SWEHelper.getPropertyUri("DirectionalResponse"))
            .label("Directional Response")
            .withFixedSize(1)
            .withElement("point", sml.createRecord()
                .addField("az", sml.createQuantity()
                    .definition(SWEHelper.getQudtUri("AzimuthAngle"))
                    .label("Azimuth Angle")
                    .uomCode("deg")
                    .build())
                .addField("power", sml.createQuantity()
                    .definition(SWEHelper.getQudtUri("SoundPowerLevel"))
                    .label("Output Level")
                    .uomCode("dB")
                    .build())
                .build())
            .build();
        
        array.assignNewDataBlock();
        array.setEncoding(sml.newTextEncoding(",", " "));
        array.setValues(sml.newEncodedValuesProperty());
        return array;
    }
}
