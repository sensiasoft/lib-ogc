package net.opengis.swe.v20;


/**
 * Tagging interface to allow processing of Quantity and QuantityRange components
 * with common logic
 */
public interface QuantityOrRange extends SimpleComponent, HasUom, HasConstraints<AllowedValues> {

}
