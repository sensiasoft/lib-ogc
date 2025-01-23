/***************************** BEGIN COPYRIGHT BLOCK **************************

Copyright (C) 2024 Delta Air Lines, Inc. All Rights Reserved.

Notice: All information contained herein is, and remains the property of
Delta Air Lines, Inc. The intellectual and technical concepts contained herein
are proprietary to Delta Air Lines, Inc. and may be covered by U.S. and Foreign
Patents, patents in process, and are protected by trade secret or copyright law.
Dissemination, reproduction or modification of this material is strictly
forbidden unless prior written permission is obtained from Delta Air Lines, Inc.

******************************* END COPYRIGHT BLOCK ***************************/

package org.vast.data;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import net.opengis.swe.v20.DataBlock;


/**
 * <p>
 * Base interface for all generated DataBlock accessor classes.
 * </p>
 *
 * @author Alex Robin
 * @since Jan 21, 2025
 */
public interface IDataAccessor
{
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface SweMapping {
        public String path() default "";
    }
    
    
    public void wrap(DataBlock db);
}
