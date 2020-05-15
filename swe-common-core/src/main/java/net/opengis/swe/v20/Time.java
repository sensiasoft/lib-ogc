/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.

Copyright (C) 2012-2015 Sensia Software LLC. All Rights Reserved.

******************************* END LICENSE BLOCK ***************************/

package net.opengis.swe.v20;

import org.vast.data.DateTimeOrDouble;


/**
 * POJO class for XML type TimeType(@http://www.opengis.net/swe/2.0).
 *
 * This is a complex type.
 */
@SuppressWarnings("javadoc")
public interface Time extends ScalarComponent, TimeOrRange
{
    public static final String ISO_TIME_UNIT = "http://www.opengis.net/def/uom/ISO-8601/0/Gregorian";


    @Override
    public Time copy();


    /**
     * Gets the value property
     */
    public DateTimeOrDouble getValue();


    /**
     * Sets the value property
     */
    public void setValue(DateTimeOrDouble value);
}
