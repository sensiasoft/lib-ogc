/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.

Copyright (C) 2020 Sensia Software LLC. All Rights Reserved.

******************************* END LICENSE BLOCK ***************************/

package org.vast.swe.helper;

import org.vast.swe.SWEBuilders.DataRecordBuilder;
import org.vast.swe.SWEHelper;
import org.vast.util.Asserts;
import net.opengis.swe.v20.Count;
import net.opengis.swe.v20.DataArray;
import net.opengis.swe.v20.DataComponent;
import net.opengis.swe.v20.DataType;
import net.opengis.swe.v20.ScalarComponent;


/**
 * <p>
 * Helper class to create SWE structures used for various kinds of raster
 * datasets, including various images and coverage types<br/>
 * </p>
 *
 * @author Alex Robin
 * @since May 2020
 */
public class RasterHelper extends VectorHelper
{
    public static final String DEF_IMAGE = SWEHelper.getPropertyUri("RasterImage");
    public static final String DEF_RASTER_WIDTH = SWEHelper.getPropertyUri("GridWidth");
    public static final String DEF_RASTER_HEIGHT = SWEHelper.getPropertyUri("GridHeight");

    public static final String DEF_RED_CHANNEL = SWEHelper.getPropertyUri("RedChannel");
    public static final String DEF_GREEN_CHANNEL = SWEHelper.getPropertyUri("GreenChannel");
    public static final String DEF_BLUE_CHANNEL = SWEHelper.getPropertyUri("BlueChannel");
    public static final String DEF_GRAY_CHANNEL = SWEHelper.getPropertyUri("GrayChannel");


    /**
     * Creates a 2D-array representing a raster image
     * @param width Image width component
     * @param height Image height component
     * @param channels List of image channels
     * @return the new DataArray component object
     */
    public DataArray newRasterImage(Count width, Count height, ScalarComponent... channels)
    {
        Asserts.checkArgument(width != null && (width.isSetId() || width.hasData()), "Width component must have a value or an ID");
        if (width.hasData())
            Asserts.checkArgument(width.getData().getIntValue() > 0, "Width must be > 0");

        Asserts.checkArgument(height != null && (height.isSetId() || height.hasData()), "Height component must have a value or an ID");
        if (height.hasData())
            Asserts.checkArgument(width.getData().getIntValue() > 0, "Height must be > 0");

        Asserts.checkArgument(channels != null && channels.length > 0, "At least one channel must be provided");

        // add definitions and dimensions axes if not already set
        if (!width.isSetAxisID())
            width.setAxisID("X");
        if (!width.isSetDefinition())
            width.setDefinition(DEF_RASTER_WIDTH);
        if (!height.isSetAxisID())
            height.setAxisID("Y");
        if (!height.isSetDefinition())
            height.setDefinition(DEF_RASTER_HEIGHT);

        // pixel structure (mono or multi-channels)
        DataComponent pixel;
        if (channels.length > 1)
        {
            DataRecordBuilder builder = createRecord();
            for (ScalarComponent channel: channels)
                builder.addField(channel.getName(), channel);
            pixel = builder.build();
        }
        else
            pixel = channels[0];

        return createArray()
            .definition(DEF_IMAGE)
            .withSizeComponent(height)
            .withElement("row", createArray()
                .withSizeComponent(width)
                .withElement("pixel", pixel)
                .build())
            .build();
    }


    /**
     * Creates a fixed size 2D-array representing a raster image
     * @param width Image width
     * @param height Image height
     * @param channels List of image channels
     * @return the new DataArray component object
     */
    public DataArray newRasterImage(int width, int height, ScalarComponent... channels)
    {
        Asserts.checkArgument(channels != null && channels.length > 0, "At least one channel must be provided");

        return newRasterImage(
            createCount().value(width).build(),
            createCount().value(height).build(),
            channels);
    }


    /**
     * Creates a 2D-array representing a generic grayscale image.<br/>
     * @param width Width component (fixed or variable size, see {@link DataArray#setElementCount(Count)})
     * @param height Height component (fixed or variable size, see {@link DataArray#setElementCount(Count)})
     * @param dataType Data type used for image samples
     * @return the new DataArray component object
     */
    public DataArray newGrayscaleImage(Count width, Count height, DataType dataType)
    {
        boolean useCount = dataType.isIntegralType();
        ScalarComponent channel = (useCount ? createCount() : createQuantity())
            .definition(DEF_GRAY_CHANNEL)
            .dataType(dataType)
            .build();

        return newRasterImage(width, height, channel);
    }


    /**
     * Creates a fixed size 2D-array representing a generic grayscale image
     * @param width Image width in pixels
     * @param height Image height in pixels
     * @param dataType Data type of each image sample
     * @return the new DataArray component object
     */
    public DataArray newGrayscaleImage(int width, int height, DataType dataType)
    {
        return newGrayscaleImage(
            createCount().value(width).build(),
            createCount().value(height).build(),
            dataType);
    }


    /**
     * Creates a 2D-array representing a generic RGB image.<br/>
     * @param width Width component (fixed or variable size, see {@link DataArray#setElementCount(Count)})
     * @param height Height component (fixed or variable size, see {@link DataArray#setElementCount(Count)})
     * @param dataType Data type of each image sample (3 samples per pixel)
     * @return the new DataArray component object
     */
    public DataArray newRgbImage(Count width, Count height, DataType dataType)
    {
        boolean useCount = dataType.isIntegralType();

        ScalarComponent red = (useCount ? createCount() : createQuantity())
            .definition(DEF_RED_CHANNEL)
            .name("red")
            .dataType(dataType)
            .build();

        ScalarComponent green = (useCount ? createCount() : createQuantity())
            .definition(DEF_GREEN_CHANNEL)
            .name("green")
            .dataType(dataType)
            .build();

        ScalarComponent blue = (useCount ? createCount() : createQuantity())
            .definition(DEF_BLUE_CHANNEL)
            .name("blue")
            .dataType(dataType)
            .build();

        return newRasterImage(width, height, red, green, blue);
    }


    /**
     * Creates a fixed size 2D-array representing a generic RGB image
     * @param width Image width in pixels
     * @param height Image height in pixels
     * @param dataType Data type of each image sample (3 samples per pixel)
     * @return the new DataArray component object
     */
    @Override
    public DataArray newRgbImage(int width, int height, DataType dataType)
    {
        return newRgbImage(
            createCount().value(width).build(),
            createCount().value(height).build(),
            dataType);
    }
}
