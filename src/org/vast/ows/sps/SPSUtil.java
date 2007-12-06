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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import javax.xml.namespace.QName;

import net.opengis.ows.ExceptionDocument;
import net.opengis.ows.ExceptionReportDocument;

import org.apache.log4j.Logger;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlError;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.XmlTokenSource;

/**
 * Utility class which provides methods (frequently) used by more than one
 * class.
 * 
 * @author Johannes Echterhoff
 */
public class SPSUtil {

   private static Logger logger = Logger.getLogger(SPSUtil.class);

   /**
    * Gets a String representation of the exception plus its stack trace and
    * causes (in abbreviated form with incomplete stack traces).
    * 
    * @param e
    *           exception that occurred somewhere.
    * @return a String representing exception details (message, stack trace and
    *         causes)
    */
   public static String getExceptionDetails(Exception e) {

      String message = e.getMessage();
      StackTraceElement[] stackTraces = e.getStackTrace();

      StringBuffer sb = new StringBuffer();
      sb.append("[EXC] An exception occurred");

      sb.append(": " + e.getClass().getName() + ": " + message + "\n");
      for (int i = 0; i < stackTraces.length; i++) {
         StackTraceElement element = stackTraces[i];
         sb.append("[EXC]   " + element.toString() + "\n");
      }

      // print out causes
      Throwable cause = e.getCause();
      while (cause != null) {

         sb.append("[EXC] Caused by: " + cause.getClass().getName() + ": "
               + cause.getMessage() + "\n");

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
            sb.append("[EXC] ... " + numOfMoreTraces + " more\n");
         }

         cause = cause.getCause();
      }

      return sb.toString();
   }

   /**
    * Used for sending requests to another service in chain and getting the XML
    * encoded response back.
    * 
    * @param xts
    *           the xml encoded content of the request.
    * @param url
    *           location of the service to send the request to.
    * 
    * @throws SPSException
    *            if the request could not be sent.
    * @throws ServiceException
    *            if the response contained a ServiceException report
    */
   public static XmlObject sendPOSTRequest(XmlTokenSource xts, URL url)
         throws SPSException, ServiceException {

      BufferedReader brServiceAnswer = null;

      try {
         // send Request to URL
         HttpURLConnection con = (HttpURLConnection) url.openConnection();
         con.setRequestMethod("POST");
         con.setDoOutput(true);

         OutputStream out = con.getOutputStream();
         xts.save(out);
         out.flush();
         out.close();

         // get answer from service
         brServiceAnswer = new BufferedReader(new InputStreamReader(
               con.getInputStream()));

         // parse answer
         XmlObject xobj = XmlObject.Factory.parse(brServiceAnswer);

         if (xobj instanceof ExceptionReportDocument) {

            ExceptionReportDocument erd = (ExceptionReportDocument) xobj;
            if (!erd.validate()) {
               throw new SPSException(
                     "Received invalid ExceptionReportDocument from " + url);
            } // else
            ServiceException se = new ServiceException();
            se.addExceptionReport(erd);
            throw se;
         } else if (xobj instanceof ExceptionDocument) {

            ExceptionDocument ed = (ExceptionDocument) xobj;
            if (!ed.validate()) {
               throw new SPSException(
                     "Received invalid ExceptionDocument from " + url);
            } // else
            ServiceException se = new ServiceException();
            se.addExceptionReport(ed);
            throw se;
         } else {
            return xobj; // should be the correct answer
         }

      } catch (IOException e) {
         throw new SPSException("Could not send request to service " + url, e);
      } catch (XmlException e) {
         XmlError error = e.getError();
         if (error != null) {
            logger.debug(error);
         }
         throw new SPSException("Answer from service " + url
               + " did not contain correct XML.", e);
      } finally {

         if (brServiceAnswer != null) {
            try {
               brServiceAnswer.close();
            } catch (IOException e) {
               logger.debug("Could not close brServiceAnswer.", e);
            }
         }
      }
   }

   /**
    * Sends the given XML message to the given URL. The given schema location is
    * added to the XML.
    * <p>
    * The XML will be pretty printed.
    * 
    * @param message
    *           contains the XML message.
    * @param schemaLocation
    *           will be added to the XML if not null.
    * @param destination
    *           the message will be sent to this URL.
    * 
    * @throws SPSException
    *            if the message could not be sent.
    */
   public static void sendXmlMessage(XmlTokenSource message,
         String schemaLocation, URL destination) throws SPSException {

      URLConnection con = null;
      OutputStream out = null;

      try {
         con = destination.openConnection();
         out = con.getOutputStream();

         XmlOptions options = new XmlOptions();

         options.setSavePrettyPrint();
         options.setUseDefaultNamespace();
         options.setSaveAggressiveNamespaces();

         // If the schemaLocation is null do not insert schemaLocation to
         // xml-instance
         if (schemaLocation != null && schemaLocation.trim().length() > 0) {
            XmlCursor c = message.newCursor();
            c.toFirstChild();
            c.setAttributeText(new QName(
                  "http://www.w3.org/2001/XMLSchema-instance",
                  "schemaLocation", "xsi"), schemaLocation);
            c.dispose();
         }

         message.save(out, options);

         con.getInputStream();

      } catch (IOException e) {
         throw new SPSException("Could not send message ", e);
      } finally {
         if (out != null) {
            try {
               out.flush();
               out.close();
            } catch (IOException e) {
               logger.warn("cannot close URLs output stream", e);
            }
         }
      }
   }
}
