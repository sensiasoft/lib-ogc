package net.opengis.swe.v20;

import net.opengis.IDateTime;


/**
 * Tagging interface to allow processing of Time and TimeRange components
 * with common logic
 */
public interface TimeOrRange extends SimpleComponent, HasRefFrames, HasUom, HasConstraints<AllowedTimes>
{

    /**
     * Gets the referenceTime property
     */
    public IDateTime getReferenceTime();


    /**
     * Checks if referenceTime is set
     */
    public boolean isSetReferenceTime();


    /**
     * Sets the referenceTime property
     */
    public void setReferenceTime(IDateTime referenceTime);


    /**
     * @return true if time is encoded as ISO8601 string
     */
    public boolean isIsoTime();
}
