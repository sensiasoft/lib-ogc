package org.vast.ows.sps;

import org.vast.sweCommon.SWEData;

public interface ParameterizedRequest
{
	public SWEData getTaskingParameters();

	public void setTaskingParameters(SWEData taskingParameters);

	public SWEData getAdditionalParameters();

	public void setAdditionalParameters(SWEData additionalParameters);
}
