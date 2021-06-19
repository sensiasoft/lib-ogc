/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.
 
Copyright (C) 2012-2017 Sensia Software LLC. All Rights Reserved.
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.process;

import net.opengis.swe.v20.DataComponent;


public interface IDataConnection
{

    public void setSource(IProcessExec process, DataComponent component);


    public IProcessExec getSourceProcess();


    public DataComponent getSourcePort();


    public DataComponent getSourceComponent();


    public void setDestination(IProcessExec process, DataComponent component);


    public IProcessExec getDestinationProcess();


    public DataComponent getDestinationPort();


    public DataComponent getDestinationComponent();


    /**
     * Transfer data to destination component
     * @param block Set to true if processing should wait until data is
     * available on this connection, false if processing should continue without it
     * @return True if data was actually transferred, false otherwise 
     * @throws InterruptedException if interrupted while waiting
     */
    public boolean transferData(boolean block) throws InterruptedException;


    /**
     * Publish data from source component on this connection
     * @throws InterruptedException if interrupted while waiting
     */
    public void publishData() throws InterruptedException;


    /**
     * @return true if data is available (i.e. ready to be transfered)
     * on this connection
     */
    public boolean isDataAvailable();
    
    
    /**
     * Clear all remaining data on this connection
     */
    public void clear();

}