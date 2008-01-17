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
import java.util.List;

/**
 * <p><b>Title:</b><br/>
 * MultiUser
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
public class MultiUser extends WNSUser
{
	protected List<String> userIds;

	
	public MultiUser()
	{
		userIds = new ArrayList<String>();
	}
	
	
	public List<String> getUserIds()
	{
		return userIds;
	}  
}
