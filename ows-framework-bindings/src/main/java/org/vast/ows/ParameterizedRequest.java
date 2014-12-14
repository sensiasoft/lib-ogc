package org.vast.ows;

import org.vast.swe.SWEData;


public interface ParameterizedRequest
{
	public SWEData getParameters();

	public void setParameters(SWEData parameters);
}
