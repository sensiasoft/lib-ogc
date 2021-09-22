/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.

Copyright (C) 2020 Sensia Software LLC. All Rights Reserved.

******************************* END LICENSE BLOCK ***************************/

package org.vast.util;

import java.util.Collection;
import java.util.EnumSet;
import java.util.Set;
import com.google.common.collect.Range;

/**
 * Modified Asserts utility methods based on Guava implementation
 */
public final class Asserts
{
    static String NULL_ERROR_MSG = " cannot be null";
    
    
    private Asserts()
    {
    }


    /**
     * Ensures the truth of an expression involving one or more parameters to the calling method.
     *
     * @param expression a boolean expression
     * @throws IllegalArgumentException if {@code expression} is false
     */
    public static void checkArgument(boolean expression)
    {
        if (!expression)
            throw new IllegalArgumentException();
    }


    /**
     * Ensures the truth of an expression involving one or more parameters to the calling method.
     *
     * @param expression a boolean expression
     * @param errorMessage the exception message to use if the check fails; will be converted to a
     *     string using {@link String#valueOf(Object)}
     * @throws IllegalArgumentException if {@code expression} is false
     */
    public static void checkArgument(boolean expression, Object errorMessage)
    {
        if (!expression)
            throw new IllegalArgumentException(String.valueOf(errorMessage));
    }


    /**
     * Ensures the truth of an expression involving one or more parameters to the calling method.
     *
     * @param expression a boolean expression
     * @param errorMessageTemplate a template for the exception message should the check fail. The
     *     message is formed by replacing each '{}' placeholder in the template with an
     *     argument. These are matched by position - the first '{}' gets {@code
     *     errorMessageArgs[0]}, etc. Unmatched arguments will be appended to the formatted message in
     *     square braces. Unmatched placeholders will be left as-is.
     * @param errorMessageArgs the arguments to be substituted into the message template. Arguments
     *     are converted to strings using {@link String#valueOf(Object)}.
     * @throws IllegalArgumentException if {@code expression} is false
     * @throws NullPointerException if the check fails and either {@code errorMessageTemplate} or
     *     {@code errorMessageArgs} is null (don't let this happen)
     */
    public static void checkArgument(boolean expression, String errorMessageTemplate, Object... errorMessageArgs)
    {
        if (!expression)
            throw new IllegalArgumentException(format(errorMessageTemplate, errorMessageArgs));
    }

    
    /**
     * Ensures that a comparable value (e.g. numerical) is within the provided range
     * @param val value of comparable to be checked
     * @param range range of allowed value (bounds can be inclusive or exclusive)
     * @param desc name of variable being checked (will be used in error message)
     * @return The provided value if valid
     * @throws IllegalArgumentException if value is not in range
     */
    public static <T extends Comparable<T>> T checkValueInRange(T val, Range<T> range, String desc)
    {
        checkArgument(range.contains(val), "{} must be within {}", desc, range);
        return val;
    }
    
    
    /**
     * Ensures that a value is one of the allowed values in a set
     * @param val value of variable to be checked
     * @param allowedValues set of allowed values
     * @param desc name of variable being checked (will be used in error message)
     * @return The provided value if valid
     * @throws IllegalArgumentException if value is not in set
     */
    public static <T> T checkArgumentInSet(T val, Set<T> allowedValues, String desc)
    {
        checkArgument(allowedValues.contains(val), "{} must be one of {}", desc, allowedValues);
        return val;
    }
    
    
    /**
     * Ensures that a string value is one of the allowed values from an enum
     * @param val value of variable to be checked
     * @param enumClass enum class
     * @param desc name of variable being checked (will be used in error message)
     * @return The corresponding enum constant if it exists
     * @throws IllegalArgumentException if value is not in enum
     */
    public static <E extends Enum<E>> E checkArgumentInEnum(String val, Class<E> enumClass, String desc)
    {
        try
        {
            return Enum.valueOf(enumClass, val);
        }
        catch (Exception e)
        {
            throw new IllegalArgumentException(format("{} must be one of {}", val, EnumSet.allOf(enumClass)));
        }
    }


    /**
     * Ensures the truth of an expression involving the state of the calling instance, but not
     * involving any parameters to the calling method.
     *
     * @param expression a boolean expression
     * @throws IllegalStateException if {@code expression} is false
     */
    public static void checkState(boolean expression)
    {
        if (!expression)
            throw new IllegalStateException();
    }


    /**
     * Ensures the truth of an expression involving the state of the calling instance, but not
     * involving any parameters to the calling method.
     *
     * @param expression a boolean expression
     * @param errorMessage the exception message to use if the check fails; will be converted to a
     *     string using {@link String#valueOf(Object)}
     * @throws IllegalStateException if {@code expression} is false
     */
    public static void checkState(boolean expression, Object errorMessage)
    {
        if (!expression)
            throw new IllegalStateException(String.valueOf(errorMessage));
    }


    /**
     * Ensures the truth of an expression involving the state of the calling instance, but not
     * involving any parameters to the calling method.
     *
     * @param expression a boolean expression
     * @param errorMessageTemplate a template for the exception message should the check fail. The
     *     message is formed by replacing each '{}' placeholder in the template with an
     *     argument. These are matched by position - the first '{}' gets {@code
     *     errorMessageArgs[0]}, etc. Unmatched arguments will be appended to the formatted message in
     *     square braces. Unmatched placeholders will be left as-is.
     * @param errorMessageArgs the arguments to be substituted into the message template. Arguments
     *     are converted to strings using {@link String#valueOf(Object)}.
     * @throws IllegalStateException if {@code expression} is false
     * @throws NullPointerException if the check fails and either {@code errorMessageTemplate} or
     *     {@code errorMessageArgs} is null (don't let this happen)
     */
    public static void checkState(boolean expression, String errorMessageTemplate, Object... errorMessageArgs)
    {
        if (!expression)
            throw new IllegalStateException(format(errorMessageTemplate, errorMessageArgs));
    }


    /**
     * Ensures that an object reference passed as a parameter to the calling method is not null.
     *
     * @param reference an object reference
     * @return the non-null reference that was validated
     * @throws NullPointerException if {@code reference} is null
     */
    public static <T> T checkNotNull(T reference)
    {
        if (reference == null)
            throw new NullPointerException();
        return reference;
    }
    
    
    /**
     * Ensures that an object reference passed as a parameter to the calling method is not null.
     *
     * @param reference an object reference
     * @param errorMessage the exception message to use if the check fails; will be converted to a
     *     string using {@link String#valueOf(Object)}
     * @return the non-null reference that was validated
     * @throws NullPointerException if {@code reference} is null
     */
    public static <T> T checkNotNull(T reference, Object errorMessage)
    {
        if (reference == null)
        {
            String nameOrMsg = decode(errorMessage);
            if (nameOrMsg != null && !nameOrMsg.contains(" "))
                nameOrMsg += NULL_ERROR_MSG;
            throw new NullPointerException(nameOrMsg);
        }
        
        return reference;
    }


    /**
     * Ensures that an object reference passed as a parameter to the calling method is not null.
     *
     * @param reference an object reference
     * @param errorMessageTemplate a template for the exception message should the check fail. The
     *     message is formed by replacing each '{}' placeholder in the template with an
     *     argument. These are matched by position - the first '{}' gets {@code
     *     errorMessageArgs[0]}, etc. Unmatched arguments will be appended to the formatted message in
     *     square braces. Unmatched placeholders will be left as-is.
     * @param errorMessageArgs the arguments to be substituted into the message template. Arguments
     *     are converted to strings using {@link String#valueOf(Object)}.
     * @return the non-null reference that was validated
     * @throws NullPointerException if {@code reference} is null
     */
    public static <T> T checkNotNull(T reference, String errorMessageTemplate, Object... errorMessageArgs)
    {
        if (reference == null)
        {
            // If either of these parameters is null, the right thing happens anyway
            throw new NullPointerException(format(errorMessageTemplate, errorMessageArgs));
        }
        return reference;
    }
    
    
    /**
     * Ensures that a CharSequence is neither null nor empty
     * @param reference a CharSequence reference
     * @param errorMessage the exception message to use if the check fails; will be converted to a
     *     string using {@link String#valueOf(Object)}
     * @return the non-null reference that was validated
     * @throws NullPointerException if {@code reference} is null
     */
    public static <T extends CharSequence> T checkNotNullOrEmpty(T reference, Object errorMessage)
    {
        if (reference == null || reference.length() == 0)
            throwNullOrEmptyMessage(reference, errorMessage);
        return reference;
    }
    
    
    /**
     * Ensures that a collection is neither null nor empty
     * @param reference a collection reference
     * @param errorMessage the exception message to use if the check fails; will be converted to a
     *     string using {@link String#valueOf(Object)}
     * @return the non-null reference that was validated
     * @throws NullPointerException if {@code reference} is null
     */
    public static <T> Collection<T> checkNotNullOrEmpty(Collection<T> reference, Object errorMessage)
    {
        if (reference == null || reference.isEmpty())
            throwNullOrEmptyMessage(reference, errorMessage);
        return reference;
    }
    
    
    /**
     * Ensures that an array is neither null nor empty
     * @param array an array reference
     * @param errorMessage the exception message to use if the check fails; will be converted to a
     *     string using {@link String#valueOf(Object)}
     * @return the non-null reference that was validated
     * @throws NullPointerException if {@code reference} is null
     */
    public static <T> T[] checkNotNullOrEmpty(T[] array, Object errorMessage)
    {
        if (array == null || array.length == 0)
            throwNullOrEmptyMessage(array, errorMessage);
        return array;
    }
    
    
    /**
     * Ensures that a byte array is neither null nor empty
     * @param array an array reference
     * @param errorMessage the exception message to use if the check fails; will be converted to a
     *     string using {@link String#valueOf(Object)}
     * @return the non-null reference that was validated
     * @throws NullPointerException if {@code reference} is null
     */
    public static byte[] checkNotNullOrEmpty(byte[] array, Object errorMessage)
    {
        if (array == null || array.length == 0)
            throwNullOrEmptyMessage(array, errorMessage);
        return array;
    }
    
    
    /**
     * Ensures that a char array is neither null nor empty
     * @param array a array reference
     * @param errorMessage the exception message to use if the check fails; will be converted to a
     *     string using {@link String#valueOf(Object)}
     * @return the non-null reference that was validated
     * @throws NullPointerException if {@code reference} is null
     */
    public static char[] checkNotNullOrEmpty(char[] array, Object errorMessage)
    {
        if (array == null || array.length == 0)
            throwNullOrEmptyMessage(array, errorMessage);
        return array;
    }
    
    
    /**
     * Ensures that an int array is neither null nor empty
     * @param array an array reference
     * @param errorMessage the exception message to use if the check fails; will be converted to a
     *     string using {@link String#valueOf(Object)}
     * @return the non-null reference that was validated
     * @throws NullPointerException if {@code reference} is null
     */
    public static int[] checkNotNullOrEmpty(int[] array, Object errorMessage)
    {
        if (array == null || array.length == 0)
            throwNullOrEmptyMessage(array, errorMessage);
        return array;
    }
    
    
    /**
     * Ensures that a long array is neither null nor empty
     * @param array an array reference
     * @param errorMessage the exception message to use if the check fails; will be converted to a
     *     string using {@link String#valueOf(Object)}
     * @return the non-null reference that was validated
     * @throws NullPointerException if {@code reference} is null
     */
    public static long[] checkNotNullOrEmpty(long[] array, Object errorMessage)
    {
        if (array == null || array.length == 0)
            throwNullOrEmptyMessage(array, errorMessage);
        return array;
    }
    
    
    /**
     * Ensures that a float array is neither null nor empty
     * @param array an array reference
     * @param errorMessage the exception message to use if the check fails; will be converted to a
     *     string using {@link String#valueOf(Object)}
     * @return the non-null reference that was validated
     * @throws NullPointerException if {@code reference} is null
     */
    public static float[] checkNotNullOrEmpty(float[] array, Object errorMessage)
    {
        if (array == null || array.length == 0)
            throwNullOrEmptyMessage(array, errorMessage);
        return array;
    }
    
    
    /**
     * Ensures that a double array is neither null nor empty
     * @param array an array reference
     * @param errorMessage the exception message to use if the check fails; will be converted to a
     *     string using {@link String#valueOf(Object)}
     * @return the non-null reference that was validated
     * @throws NullPointerException if {@code reference} is null
     */
    public static double[] checkNotNullOrEmpty(double[] array, Object errorMessage)
    {
        if (array == null || array.length == 0)
            throwNullOrEmptyMessage(array, errorMessage);
        return array;
    }
    
    
    private static void throwNullOrEmptyMessage(Object reference, Object errorMessage)
    {
        String nameOrMsg = decode(errorMessage);
        boolean isName = nameOrMsg != null && !nameOrMsg.contains(" ");
        
        if (reference == null)
            throw new NullPointerException(isName ? nameOrMsg + NULL_ERROR_MSG : nameOrMsg);
        else
            throw new IllegalArgumentException(isName ? nameOrMsg + " cannot be empty" : nameOrMsg);
    }
    
    
    /**
     * Ensures that a String is not null and contains at least one non-whitespace character
     * @param str an array reference
     * @param errorMessage the exception message to use if the check fails; will be converted to a
     *     string using {@link String#valueOf(Object)}
     * @return the String that was validated
     * @throws IllegalArgumentException if {@code str} is null or blank
     */
    public static String checkNotNullOrBlank(String str, Object errorMessage)
    {
        if (str == null || str.isBlank())
        {
            String nameOrMsg = decode(errorMessage);
            boolean isName = nameOrMsg != null && !nameOrMsg.contains(" ");
            
            if (str == null)
                throw new NullPointerException(isName ? nameOrMsg + NULL_ERROR_MSG : nameOrMsg);
            else
                throw new IllegalArgumentException(isName ? nameOrMsg + " cannot be blank" : nameOrMsg);
        }
        
        return str;
    }
    
    
    /**
     * Ensures that a character sequence (e.g. String) is not null and has the correct size
     * @param str a character sequence reference
     * @param errorMessage the exception message to use if the check fails; will be converted to a
     *     string using {@link String#valueOf(Object)}
     * @param minSize
     * @param maxSize
     * @return the non-null reference that was validated
     * @throws NullPointerException if {@code reference} is null
     */
    public static <T extends CharSequence> T checkCharLength(T str, Object errorMessage, int minSize, int maxSize)
    {
        if (str == null || str.length() < minSize || str.length() > maxSize)
            throwNullOrInvalidSizeMessage(str, errorMessage);
        return str;
    }
    
    
    /**
     * Ensures that an array is not null and has the correct size
     * @param array an array reference
     * @param errorMessage the exception message to use if the check fails; will be converted to a
     *     string using {@link String#valueOf(Object)}
     * @param minSize
     * @param maxSize
     * @return the non-null reference that was validated
     * @throws NullPointerException if {@code reference} is null
     */
    public static <T> T[] checkArraySize(T[] array, Object errorMessage, int minSize, int maxSize)
    {
        if (array == null || array.length < minSize || array.length > maxSize)
            throwNullOrInvalidSizeMessage(array, errorMessage);
        return array;
    }
    
    
    /**
     * Ensures that a collection is not null and has the correct size
     * @param collection a collection reference
     * @param errorMessage the exception message to use if the check fails; will be converted to a
     *     string using {@link String#valueOf(Object)}
     * @param minSize
     * @param maxSize
     * @return the non-null reference that was validated
     * @throws NullPointerException if {@code reference} is null
     */
    public static <T extends Collection<?>> T checkCollectionSize(T collection, Object errorMessage, int minSize, int maxSize)
    {
        if (collection == null || collection.size() < minSize || collection.size() > maxSize)
            throwNullOrInvalidSizeMessage(collection, errorMessage);
        return collection;
    }
    
    
    private static void throwNullOrInvalidSizeMessage(Object reference, Object errorMessage)
    {
        String nameOrMsg = decode(errorMessage);
        boolean isName = nameOrMsg != null && !nameOrMsg.contains(" ");
        
        if (reference == null)
            throw new NullPointerException(isName ? nameOrMsg + NULL_ERROR_MSG : nameOrMsg);
        else
            throw new IllegalArgumentException(isName ? nameOrMsg + " has an incorrect size" : nameOrMsg);
    }


    /**
     * Ensures that {@code index} specifies a valid <i>element</i> in an array, list or string of size
     * {@code size}. An element index may range from zero, inclusive, to {@code size}, exclusive.
     *
     * @param index a user-supplied index identifying an element of an array, list or string
     * @param size the size of that array, list or string
     * @return the value of {@code index}
     * @throws IndexOutOfBoundsException if {@code index} is negative or is not less than {@code size}
     * @throws IllegalArgumentException if {@code size} is negative
     */
    public static int checkElementIndex(int index, int size)
    {
        return checkElementIndex(index, size, "index");
    }


    /**
     * Ensures that {@code index} specifies a valid <i>element</i> in an array, list or string of size
     * {@code size}. An element index may range from zero, inclusive, to {@code size}, exclusive.
     *
     * @param index a user-supplied index identifying an element of an array, list or string
     * @param size the size of that array, list or string
     * @param desc the text to use to describe this index in an error message
     * @return the value of {@code index}
     * @throws IndexOutOfBoundsException if {@code index} is negative or is not less than {@code size}
     * @throws IllegalArgumentException if {@code size} is negative
     */
    public static int checkElementIndex(int index, int size, String desc)
    {
        // Carefully optimized for execution by hotspot (explanatory comment above)
        if (index < 0 || index >= size)
        {
            throw new IndexOutOfBoundsException(badElementIndex(index, size, desc));
        }
        return index;
    }


    private static String badElementIndex(int index, int size, String desc)
    {
        if (index < 0)
        {
            return format("{} ({}) must not be negative", desc, index);
        }
        else if (size < 0)
        {
            throw new IllegalArgumentException("negative size: " + size);
        }
        else
        { // index >= size
            return format("{} ({}) must be less than size ({})", desc, index, size);
        }
    }


    /**
     * Ensures that {@code index} specifies a valid <i>position</i> in an array, list or string of
     * size {@code size}. A position index may range from zero to {@code size}, inclusive.
     *
     * @param index a user-supplied index identifying a position in an array, list or string
     * @param size the size of that array, list or string
     * @return the value of {@code index}
     * @throws IndexOutOfBoundsException if {@code index} is negative or is greater than {@code size}
     * @throws IllegalArgumentException if {@code size} is negative
     */
    public static int checkPositionIndex(int index, int size)
    {
        return checkPositionIndex(index, size, "index");
    }


    /**
     * Ensures that {@code index} specifies a valid <i>position</i> in an array, list or string of
     * size {@code size}. A position index may range from zero to {@code size}, inclusive.
     *
     * @param index a user-supplied index identifying a position in an array, list or string
     * @param size the size of that array, list or string
     * @param desc the text to use to describe this index in an error message
     * @return the value of {@code index}
     * @throws IndexOutOfBoundsException if {@code index} is negative or is greater than {@code size}
     * @throws IllegalArgumentException if {@code size} is negative
     */
    public static int checkPositionIndex(int index, int size, String desc)
    {
        // Carefully optimized for execution by hotspot (explanatory comment above)
        if (index < 0 || index > size)
        {
            throw new IndexOutOfBoundsException(badPositionIndex(index, size, desc));
        }
        return index;
    }


    private static String badPositionIndex(int index, int size, String desc)
    {
        if (index < 0)
        {
            return format("{} ({}) must not be negative", desc, index);
        }
        else if (size < 0)
        {
            throw new IllegalArgumentException("negative size: " + size);
        }
        else
        { // index > size
            return format("{} ({}) must not be greater than size ({})", desc, index, size);
        }
    }


    /**
     * Ensures that {@code start} and {@code end} specify a valid <i>positions</i> in an array, list
     * or string of size {@code size}, and are in order. A position index may range from zero to
     * {@code size}, inclusive.
     *
     * @param start a user-supplied index identifying a starting position in an array, list or string
     * @param end a user-supplied index identifying a ending position in an array, list or string
     * @param size the size of that array, list or string
     * @throws IndexOutOfBoundsException if either index is negative or is greater than {@code size},
     *     or if {@code end} is less than {@code start}
     * @throws IllegalArgumentException if {@code size} is negative
     */
    public static void checkPositionIndexes(int start, int end, int size)
    {
        // Carefully optimized for execution by hotspot (explanatory comment above)
        if (start < 0 || end < start || end > size)
        {
            throw new IndexOutOfBoundsException(badPositionIndexes(start, end, size));
        }
    }


    private static String badPositionIndexes(int start, int end, int size)
    {
        if (start < 0 || start > size)
        {
            return badPositionIndex(start, size, "start index");
        }
        if (end < 0 || end > size)
        {
            return badPositionIndex(end, size, "end index");
        }
        // end < start
        return format("end index ({}) must not be less than start index ({})", end, start);
    }
    
    
    static String decode(Object errorMessage)
    {
        if (errorMessage == null)
            return "";
        if (errorMessage instanceof Class)
            return ((Class<?>)errorMessage).getSimpleName();
        return String.valueOf(errorMessage);
    }


    /**
     * Substitutes each '{}' in {@code template} with an argument. These are matched by
     * position: the first '{}' gets {@code args[0]}, etc. If there are more arguments than
     * placeholders, the unmatched arguments will be appended to the end of the formatted message in
     * square braces.
     *
     * @param template a non-null string containing 0 or more '{}' placeholders.
     * @param args the arguments to be substituted into the message template. Arguments are converted
     *     to strings using {@link String#valueOf(Object)}. Arguments can be null.
     */
    static String format(String template, Object... args)
    {
        template = String.valueOf(template); // null -> "null"

        // start substituting the arguments into the '%s' placeholders
        StringBuilder builder = new StringBuilder(template.length() + 16 * args.length);
        int templateStart = 0;
        int i = 0;
        while (i < args.length)
        {
            int placeholderStart = template.indexOf("{}", templateStart);
            if (placeholderStart == -1)
            {
                break;
            }
            builder.append(template, templateStart, placeholderStart);
            builder.append(args[i++]);
            templateStart = placeholderStart + 2;
        }
        builder.append(template, templateStart, template.length());

        // if we run out of placeholders, append the extra args in square braces
        if (i < args.length)
        {
            builder.append(" [");
            builder.append(args[i++]);
            while (i < args.length)
            {
                builder.append(", ");
                builder.append(args[i++]);
            }
            builder.append(']');
        }

        return builder.toString();
    }
}
