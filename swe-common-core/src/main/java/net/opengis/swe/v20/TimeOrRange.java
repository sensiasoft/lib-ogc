package net.opengis.swe.v20;

import java.time.OffsetDateTime;


/**
 * Tagging interface to allow processing of Time and TimeRange components
 * with common logic
 */
public interface TimeOrRange extends SimpleComponent, HasRefFrames, HasUom, HasConstraints<AllowedTimes>
{

    /**
     * Gets the referenceTime property
     */
    public OffsetDateTime getReferenceTime();


    /**
     * Checks if referenceTime is set
     */
    public boolean isSetReferenceTime();


    /**
     * Sets the referenceTime property
     */
    public void setReferenceTime(OffsetDateTime referenceTime);


    /**
     * @return true if time is encoded as ISO8601 string
     */
    public boolean isIsoTime();
}
