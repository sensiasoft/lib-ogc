/**************************************************************

This software is the property of Spot Image S.A.

This software is distributed under the License and on an
"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
KIND, either express or implied. See the License for the
specific language governing permissions and limitations
under the License.

Copyright 2002-2007, Spot Image S.A. ALL RIGHTS RESERVED

***************************************************************/
package org.vast.ows.wns;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

/**
 * <p><b>Title:</b><br/>
 * SingleUser
 * </p>
 *
 * <p><b>Description:</b><br/>
 * 
 * </p>
 *
 * <p>Copyright (c) 2007</p>
 * @author Alexandre Robin <alexandre.robin@spotimage.fr>
 * @date 16 janv. 08
 * @version 1.0
 */
public class SingleUser extends WNSUser
{
	protected Hashtable<String, List<String>> protocolTable;

	
	public SingleUser()
	{
		protocolTable = new Hashtable<String, List<String>>();
	}
	
	
	public Hashtable<String, List<String>> getProtocolTable()
	{
		return protocolTable;
	}
	
	
	public void addProtocol(String protocol, String address)
	{
		// add to list if already there, create one otherwise
		List<String> addressList = protocolTable.get(protocol);
		if (addressList == null)
		{
			addressList = new ArrayList<String>(1);
			protocolTable.put(protocol, addressList);
		}
		
		addressList.add(address);
	}
}
