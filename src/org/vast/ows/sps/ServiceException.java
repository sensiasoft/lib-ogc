/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are subject to the Mozilla Public License Version
 1.1 (the "License"); you may not use this file except in compliance with
 the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/MPL-1.1.html
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.
 
 The Original Code is the "OGC Service Framework".
 
 The Initial Developer of the Original Code is the VAST team at the
 University of Alabama in Huntsville (UAH). <http://vast.uah.edu>
 Portions created by the Initial Developer are Copyright (C) 2007
 the Initial Developer. All Rights Reserved.

 Please Contact Mike Botts <mike.botts@uah.edu> for more information.
 
 Contributor(s): 
    Johannes Echterhoff <echterhoff@uni-muenster.de>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ows.sps;

import java.util.ArrayList;

import net.opengis.ows.ExceptionDocument;
import net.opengis.ows.ExceptionReportDocument;
import net.opengis.ows.ExceptionType;
import net.opengis.ows.ExceptionReportDocument.ExceptionReport;

import org.apache.log4j.Logger;
import org.apache.xmlbeans.XmlObject;

/**
 * Represents an OGC ExceptionReport. A ServiceException offers many methods to
 * conveniently create, modify and send an OGC ExceptionReport.
 * 
 * @author Johannes Echterhoff
 */
public class ServiceException extends Exception {

	/**
	 * Enumerates the possible values of the exceptionCode attribute of an
	 * Exception element in an ExceptionReport returned by the 52north SPS in
	 * response to a request issued by the client. The codes have the following
	 * meaning:
	 * <p>
	 * <table border="1">
	 * <tr>
	 * <th>exceptionCode value</th>
	 * <th>Meaning of code</th>
	 * <th>“locator” value</th>
	 * </tr>
	 * <tr>
	 * <td>InvalidParameterValue (standard OGC code)</td>
	 * <td>Operation request contains an invalid parameter value</td>
	 * <td>Name of parameter with invalid value</td>
	 * </tr>
	 * <tr>
	 * <td>InvalidUpdateSequence (standard OGC code)</td>
	 * <td>Value of (optional) updateSequence parameter in GetCapabilities
	 * operation request is greater than current value of service metadata
	 * updateSequence number</td>
	 * <td>None, omit 'locator' parameter</td>
	 * </tr>
	 * <tr>
	 * <td>MissingParameterValue (standard OGC code)</td>
	 * <td>Operation request does not include a parameter value, and this
	 * server did not declare a default value for that parameter</td>
	 * <td>Name of missing parameter</td>
	 * </tr>
	 * <tr>
	 * <td>NoApplicableCode (standard OGC code)</td>
	 * <td>No other exceptionCode specified by this service and server applies
	 * to this exception</td>
	 * <td>None, omit “locator” parameter</td>
	 * </tr>
	 * <tr>
	 * <td>OperationNotSupported (standard OGC code)</td>
	 * <td>Request is for an operation that is not supported by this server</td>
	 * <td>Name of operation not supported</td>
	 * </tr>
	 * <tr>
	 * <td>VersionNegotiationFailed (standard OGC code)</td>
	 * <td>List of versions in 'AcceptVersions' parameter value in
	 * GetCapabilities operation request did not include any version supported
	 * by this server</td>
	 * <td>None, omit 'locator' parameter</td>
	 * </tr>
	 * <tr>
	 * <td>InvalidRequest (SPS code)</td>
	 * <td>Request is not conform to the schema for the requested operation</td>
	 * <td>Error message from the schema-validator</td>
	 * </tr>
	 * <tr>
	 * <td>FeasibilityIDExpired (SPS code)</td>
	 * <td>feasibilityID that has been issued by the client is no longer
	 * supported by the service</td>
	 * <td>None, omit 'locator' parameter</td>
	 * </tr>
	 * <tr>
	 * <td>TaskIDExpired (SPS code)</td>
	 * <td>taskID that has been issued by the client is no longer supported by
	 * the service</td>
	 * <td>None, omit 'locator' parameter</td>
	 * </tr>	
	 * </table>
	 * 
	 * @author Johannes Echterhoff
	 */
	public enum ExceptionCode {

		/**
		 * feasibilityID that has been issued by the client is no longer
		 * supported by the service
		 */
		FeasibilityIDExpired,

		/**
		 * Operation request contains an invalid parameter value
		 */
		InvalidParameterValue,

		/**
		 * Request is not conform to the schema for the requested operation
		 */
		InvalidRequest,

		/**
		 * Value of (optional) updateSequence parameter in GetCapabilities
		 * operation request is greater than current value of service metadata
		 * updateSequence number
		 */
		InvalidUpdateSequence,

		/**
		 * Operation request does not include a parameter value, and this server
		 * did not declare a default value for that parameter
		 */
		MissingParameterValue,

		/**
		 * No other exceptionCode specified by this service and server applies
		 * to this exception
		 */
		NoApplicableCode,

		/**
		 * Request is for an operation that is not supported by this server
		 */
		OperationNotSupported,

		/**
		 * taskID that has been issued by the client is no longer supported by
		 * the service
		 */
		TaskIDExpired,

		/**
		 * List of versions in 'AcceptVersions' parameter value in
		 * GetCapabilities operation request did not include any version
		 * supported by this server
		 */
		VersionNegotiationFailed,

	}

	/**
	 * Enumerates the available level of details for reporting Java exceptions.
	 * <p>
	 * <table border="1">
	 * <tr>
	 * <th>ExceptionLevel</th>
	 * <th>Meaning</th>
	 * </tr>
	 * <tr>
	 * <td>DetailedExceptions</td>
	 * <td>Each exception will be described in detail, i.e. with stack traces
	 * and possibly contained causes.</td>
	 * </tr>
	 * <tr>
	 * <td>PlainExceptions</td>
	 * <td>Only the messages of exceptions are reported.</td>
	 * </tr>
	 * </table>
	 * 
	 * @author Johannes Echterhoff
	 */
	public enum ExceptionLevel {

		/**
		 * Each exception will be described in detail, i.e. with stack traces
		 * and possibly contained causes.
		 */
		DetailedExceptions,

		/**
		 * Only the messages of exceptions are reported.
		 */
		PlainExceptions
	}

	private static Logger logger = Logger.getLogger(ServiceException.class);

	private static final long serialVersionUID = -5206278211779894171L;

	/**
	 * Determines whether this exception shall contain detailed or plain
	 * exceptions.
	 */
	private ExceptionLevel excLevel = null;

	/**
	 * Contains all Exception elements that shall be included in the
	 * ExceptionReport represented by this ServiceException.
	 */
	private ArrayList<ExceptionType> excs = new ArrayList<ExceptionType>();

	/**
	 * Creates a new ServiceException with ExceptionLevel.PlainExceptions.
	 */
	public ServiceException() {

		this.excLevel = ExceptionLevel.PlainExceptions;
	}

	/**
	 * Creates a new ServiceException with the given ExceptionLevel.
	 * 
	 * @param excLevelIn
	 *            level of detail to use when reporting Java exceptions.
	 */
	public ServiceException(ExceptionLevel excLevelIn) {

		this.excLevel = excLevelIn;
	}

	/**
	 * Adds a new Exception to the ExceptionReport represented by this
	 * ServiceException. The Exception will have the given ExceptionCode and
	 * locator.
	 * 
	 * @param code
	 *            value of the exceptionCode attribute of the new Exception.
	 * @param locator
	 *            value of the locator attribute, may be null to avoid locator.
	 */
	public void addCodedException(ExceptionCode code, String locator) {

		ExceptionType et = ExceptionType.Factory.newInstance();
		et.setExceptionCode(code.toString());

		if (!(locator == null || locator.equals(""))) {
			et.setLocator(locator);
		}

		this.excs.add(et);
	}

	/**
	 * Adds a new Exception to the ExceptionReport represented by this
	 * ServiceException. The Exception will have the given ExceptionCode,
	 * locator and contains a text message created from the given exception.
	 * <p>
	 * Depending on the ExceptionLevel of this ServiceException the exception
	 * will be described in detail (with stack traces and causes) or not.
	 * 
	 * @param code
	 *            value of the exceptionCode attribute of the new Exception.
	 * @param locator
	 *            value of the locator attribute, may be null to avoid locator.
	 * @param e
	 *            the exception to create the message of.
	 */
	public void addCodedException(ExceptionCode code, String locator,
			Exception e) {

		ExceptionType et = ExceptionType.Factory.newInstance();
		et.setExceptionCode(code.toString());
		if (!(locator == null || locator.equals(""))) {
			et.setLocator(locator);
		}

		String message = e.getMessage();
		StackTraceElement[] stackTraces = e.getStackTrace();

		// TODO log every exception?

		StringBuffer sb = new StringBuffer();
		sb.append("[EXC] An exception occurred");
		if (this.excLevel.compareTo(ExceptionLevel.PlainExceptions) == 0) {
			sb.append(". Message: " + message);
		} else if (this.excLevel.compareTo(ExceptionLevel.DetailedExceptions) == 0) {
			sb.append(": " + e.getClass().getName() + ": " + message + "\n");
			for (StackTraceElement element : stackTraces) {
				sb.append("[EXC]   " + element.toString() + "\n");
			}

			// print out causes
			Throwable cause = e.getCause();
			while (cause != null) {

				sb.append("[EXC] Caused by: " + cause.getClass().getName()
						+ ": " + cause.getMessage() + "\n");

				int maxNumberOfTracesToPrint = 3;
				StackTraceElement[] stackTracesCause = cause.getStackTrace();
				for (int i = 0; i < stackTracesCause.length
						&& i < maxNumberOfTracesToPrint; i++) {
					StackTraceElement element = stackTracesCause[i];
					sb.append("[EXC]   " + element.toString() + "\n");
				}
				if (stackTracesCause.length > maxNumberOfTracesToPrint) {
					int numOfMoreTraces = stackTracesCause.length
							- maxNumberOfTracesToPrint;
					sb.append("[EXC] ..." + numOfMoreTraces + " more\n");
				}

				cause = cause.getCause();
			}

		} else {
			ServiceException.logger
					.warn("addCodedException: unknown ExceptionLevel " + "("
							+ this.excLevel.toString() + ")occurred.");
		}

		et.addExceptionText(sb.toString());
		// TODO i guess there is a better way to format an exception

		this.excs.add(et);
	}

	/**
	 * Adds a new Exception to the ExceptionReport represented by this
	 * ServiceException. The Exception will have the given ExceptionCode,
	 * locator and given message.
	 * 
	 * @param code
	 *            value of the exceptionCode attribute of the new Exception.
	 * @param locator
	 *            value of the locator attribute, may be null to avoid locator.
	 * @param message
	 *            exception message.
	 */
	public void addCodedException(ExceptionCode code, String locator,
			String message) {

		ExceptionType et = ExceptionType.Factory.newInstance();
		et.setExceptionCode(code.toString());

		if (!(locator == null || locator.equals(""))) {
			et.setLocator(locator);
		}
		if (message != null) {
			et.addExceptionText(message);
		}

		this.excs.add(et);
	}

	public void addCodedException(ExceptionCode code, String locator,
			String[] messages) {

		ExceptionType et = ExceptionType.Factory.newInstance();
		et.setExceptionCode(code.toString());
		if (!(locator == null || locator.equals(""))) {
			et.setLocator(locator);
		}
		for (String string : messages) {
			et.addExceptionText(string);
		}
		this.excs.add(et);
	}

	/**
	 * Adds a new Exception element to the ExceptionReport represented by this
	 * ServiceException with given exceptionCode, locator and
	 * ExceptionInformationContent.
	 * 
	 * @param code
	 *            exceptionCode to use
	 * @param locator
	 *            locator to use, may be null if locator shall be omitted
	 * @param exceptionInformationContent
	 *            content for the ExceptionInformation element, may be null if
	 *            this element shall be omitted
	 */
	public void addCodedException(ExceptionCode code, String locator,
			XmlObject exceptionInformationContent) {

		ExceptionType et = ExceptionType.Factory.newInstance();
		et.setExceptionCode(code.toString());

		if (!(locator == null || locator.equals(""))) {
			et.setLocator(locator);
		}
		if (exceptionInformationContent != null) {
			// TODO
			et.addExceptionText(exceptionInformationContent.xmlText());
			// et.addNewExceptionInformation().set(exceptionInformationContent);
		}

		this.excs.add(et);
	}

	/**
	 * Adds the given Exception element to the ExceptionReport represented by
	 * this ServiceException.
	 * 
	 * @param ed
	 *            shall be added to this ServiceException.
	 */
	public void addExceptionReport(ExceptionDocument ed) {

		this.excs.add(ed.getException());
	}

	/**
	 * Adds new Exception elements to the ExceptionReport represented by this
	 * ServiceException using the elements contained in the given document.
	 * 
	 * @param erd
	 *            shall be added to this ServiceException.
	 */
	public void addExceptionReport(ExceptionReportDocument erd) {

		for (ExceptionType et : erd.getExceptionReport().getExceptionArray()) {
			this.excs.add(et);
		}
	}

	/**
	 * Adds the contents of the given ServiceException to this ServiceException.
	 * 
	 * @param seIn
	 *            ServiceException to incorporate the contained Exceptions from.
	 */
	public void addServiceException(ServiceException seIn) {

		this.excs.addAll(seIn.getExceptions());
	}

	/**
	 * Determines whether this ServiceExceptions contains an Exception with the
	 * given exception code.
	 * 
	 * @param ec
	 *            exception code to search for.
	 * @return true if an Exception element with this code was found in this
	 *         ServiceException, else false.
	 */
	public boolean containsCode(ExceptionCode ec) {

		for (ExceptionType et : this.excs) {
			if (et.getExceptionCode().equalsIgnoreCase(ec.toString())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Determines whether this ServiceException contains any exceptions.
	 * 
	 * @return true if exceptions are contained in this ServiceException. else
	 *         false.
	 */
	public boolean containsExceptions() {

		return this.excs.size() > 0;
	}

	/**
	 * Gets the ExceptionReport document represented by this ServiceException.
	 * 
	 * @return the ExceptionReport document represented by this
	 *         ServiceException.
	 */
	public ExceptionReportDocument getDocument() {

		ExceptionReportDocument erd = ExceptionReportDocument.Factory
				.newInstance();
		ExceptionReport er = ExceptionReport.Factory.newInstance();
		er.setLanguage("en");
		er.setVersion(SPSConstants.VERSION);
		er.setExceptionArray(this.excs.toArray(new ExceptionType[this.excs
				.size()]));
		erd.setExceptionReport(er);

		return erd;
	}

	/**
	 * Gets the list of Exceptions contained in this ServiceExceptions.
	 * 
	 * @return list of contained Exceptions.
	 */
	public ArrayList<ExceptionType> getExceptions() {

		return this.excs;
	}

	/**
	 * Gets the complete ExceptionReport represented by this ServiceException as
	 * a String.
	 * 
	 * @see java.lang.Throwable#getMessage()
	 */
	public String getMessage() {

		return this.getDocument().toString();
	}

}
