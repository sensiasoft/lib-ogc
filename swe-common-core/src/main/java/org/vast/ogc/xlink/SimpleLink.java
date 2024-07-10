/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.
 
Copyright (C) 2024 Sensia Software LLC. All Rights Reserved.
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ogc.xlink;


public class SimpleLink<T> implements IXlinkReference<T>
{
    protected String href;
    protected String title;
    protected String role;
    protected String arcRole;

    
    @Override
    public String getHref()
    {
        return href;
    }


    @Override
    public void setHref(String href)
    {
        this.href = href;
    }


    @Override
    public String getTitle()
    {
        return title;
    }


    @Override
    public void setTitle(String title)
    {
        this.title = title;
    }


    @Override
    public String getRole()
    {
        return role;
    }


    @Override
    public void setRole(String role)
    {
        this.role = role;
    }


    @Override
    public String getArcRole()
    {
        return arcRole;
    }


    @Override
    public void setArcRole(String arcRole)
    {
        this.arcRole = arcRole;
    }


    @Override
    public String getMediaType()
    {
        return null;
    }


    @Override
    public void setMediaType(String mediaType)
    {
        throw new UnsupportedOperationException();
    }


    @Override
    public String getTargetUID()
    {
        return null;
    }


    @Override
    public void setTargetUID(String targetUID)
    {
        throw new UnsupportedOperationException();
    }


    @Override
    public String getTargetInterface()
    {
        return null;
    }


    @Override
    public void setTargetInterface(String targetInterface)
    {
        throw new UnsupportedOperationException();
    }


    @Override
    public T getTarget()
    {
        return null;
    }
}
