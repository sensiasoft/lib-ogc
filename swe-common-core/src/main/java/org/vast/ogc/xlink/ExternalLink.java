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


public class ExternalLink extends SimpleLink<Object> implements IXlinkReference<Object>
{
    protected String mediaType;
    protected String targetUID;
    protected String targetInterface;


    public String getMediaType()
    {
        return mediaType;
    }


    public void setMediaType(String mediaType)
    {
        this.mediaType = mediaType;
    }


    public String getTargetUID()
    {
        return targetUID;
    }


    public void setTargetUID(String targetUID)
    {
        this.targetUID = targetUID;
    }


    public String getTargetInterface()
    {
        return targetInterface;
    }


    public void setTargetInterface(String targetInterface)
    {
        this.targetInterface = targetInterface;
    }


    @Override
    public Object getTarget()
    {
        return null;
    }

}
