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
    public final static String SYSTEM_CAPS_DEF = "http://www.w3.org/ns/ssn/systems/SystemCapability";
    
    public static final String MEAS_RANGE_DEF = "http://www.w3.org/ns/ssn/systems/MeasurementRange";
    public static final String MEAS_RANGE_LABEL = "Measurement Range";
    public static final String DYNAMIC_RANGE_DEF = SWEHelper.getPropertyUri("DynamicRange");
    public static final String DYNAMIC_RANGE_LABEL = "Dynamic Range";
    public static final String SAMPLING_FREQ_DEF = SWEHelper.getPropertyUri("SamplingFrequency");
    public static final String SAMPLING_FREQ_LABEL = "Sampling Frequency";
    public static final String REPORTING_FREQ_DEF = SWEHelper.getPropertyUri("ReportingFrequency");
    public static final String REPORTING_FREQ_LABEL = "Reporting Frequency";
    public static final String INTEGRATION_TIME_DEF = SWEHelper.getPropertyUri("IntegrationTime");
    public static final String INTEGRATION_TIME_LABEL = "Integration Time";
    public static final String RESPONSE_TIME_DEF = "http://www.w3.org/ns/ssn/systems/ResponseTime";
    public static final String RESPONSE_TIME_LABEL = "Response Time";
    public static final String SENSITIVITY_DEF = "http://www.w3.org/ns/ssn/systems/Sensitivity";
    public static final String SENSITIVITY_LABEL = "Sensitivity";
    public static final String RESOLUTION_DEF = "http://www.w3.org/ns/ssn/systems/Resolution";
    public static final String RESOLUTION_LABEL = "Resolution";
    public static final String ABSOLUTE_ACCURACY_DEF = SWEHelper.getPropertyUri("AbsoluteAccuracy");
    public static final String ABSOLUTE_ACCURACY_LABEL = "Absolute Accuracy";
    public static final String RELATIVE_ACCURACY_DEF = SWEHelper.getPropertyUri("RelativeAccuracy");
    public static final String RELATIVE_ACCURACY_LABEL = "Relative Accuracy";
    public static final String PRECISION_DEF = "http://www.w3.org/ns/ssn/systems/Precision";
    public static final String PRECISION_LABEL = "Precision";
    public static final String SNR_DEF = SWEHelper.getPropertyUri("SNR");
    public static final String SNR_LABEL = "Signal-to-Noise Ratio";
    public static final String FOV_DEF = SWEHelper.getPropertyUri("FieldOfView");
    public static final String FOV_LABEL = "Field of View";
    
    
    public CommonCapabilities(SMLHelper sml)
    {
        super(sml);
    }
    
    
    /**
     * Creates a characteristic list describing the system's operating range
     * @return A characteristic list builder, pre-configured with operating range semantics
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
    
    public QuantityBuilder integrationTime(double timeMs)
    {
        return sml.createQuantity()
            .definition(INTEGRATION_TIME_DEF)
            .label(INTEGRATION_TIME_LABEL)
            .uomCode("ms")
            .value(timeMs);
    }
    
    public QuantityBuilder responseTime(double timeMs)
    {
        return sml.createQuantity()
            .definition(RESPONSE_TIME_DEF)
            .label(RESPONSE_TIME_LABEL)
            .uomCode("ms")
            .value(timeMs);
    }
    
    public QuantityBuilder resolution(double value, String uom)
    {
        return sml.createQuantity()
            .definition(RESOLUTION_DEF)
            .label(RESOLUTION_LABEL)            
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
        checkUom(uom, ANGLE_UNIT);
        
        return sml.createQuantity()
            .definition(FOV_DEF)
            .label(FOV_LABEL)            
            .uomCode(uom)
            .value(value);
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
