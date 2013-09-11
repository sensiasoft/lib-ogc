package org.vast.ows;

import org.vast.sweCommon.SWEData;

public interface ParameterizedRequest
{
	public SWEData getParameters();

	public void setParameters(SWEData parameters);
}
