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


public abstract class WNSUser
{
	protected String id;
	protected String name;


	public String getId()
	{
		return id;
	}


	public void setId(String id)
	{
		this.id = id;
	}


	public String getName()
	{
		return name;
	}


	public void setName(String name)
	{
		this.name = name;
	}
}
