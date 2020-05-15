package net.opengis.swe.v20;


/**
 * Tagging interface to allow processing of Category and CategoryRange components
 * with common logic
 */
public interface CategoryOrRange extends SimpleComponent, HasCodeSpace, HasConstraints<AllowedTokens> {

}
