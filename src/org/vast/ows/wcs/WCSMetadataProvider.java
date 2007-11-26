package org.vast.ows.wcs;

import org.w3c.dom.Element;


public interface WCSMetadataProvider
{
	
	public Element getCoverageSummary();
		
	public Element getCoverageDescription();

}
