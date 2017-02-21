/*
 Violet - A program for editing UML diagrams.

 Copyright (C) 2007 Cay S. Horstmann (http://horstmann.com)
 Alexandre de Pellegrin (http://alexdp.free.fr);

 This program is free software; you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation; either version 2 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program; if not, write to the Free Software
 Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package com.horstmann.violet.product.diagram.common;

import com.horstmann.violet.framework.file.IFile;
import com.horstmann.violet.framework.file.LocalFile;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;

/**
 * This class is a link to a physical file. It is used to perform links between diagrams. Tested successfully in applet mode
 * 
 * @author Alexandre de Pellegrin
 * 
 */
public class DiagramLink
{

    /**
     * The file
     */
    private IFile file;
    
    /**
     * Flag to indicate if file needs to be opened
     */
    private Boolean openFlag = new Boolean(false);

    public DiagramLink()
    {
        super();
    }

    /**
     * Get linked file
     * 
     * @return
     */
    public IFile getFile()
    {
        return this.file;
    }

    /**
     * Set file a link
     * 
     * @param path
     */
    public void setFile(IFile file)
    {
        this.file = file;
    }

    /**
     * Return true if file needs to be opened
     * 
     * @return
     */
    public Boolean getOpenFlag()
    {
        return this.openFlag;
    }

    /**
     * Set flag to indicate if file needs to be opened
     * 
     * @param openFlag
     */
    public void setOpenFlag(Boolean flag)
    {
        this.openFlag = flag;
    }

    /**
     * @deprecated kept for compatibility
     */
    public URL getURL()
    {
        if (this.file == null) {
            return null;
        }
        try {
            LocalFile localFile = new LocalFile(this.file);
            File fileImpl = localFile.toFile();
            if (fileImpl.exists()) {
                return fileImpl.toURL();
            }
            return null;
        } catch (IOException e) {
            throw new RuntimeException (e);
        }
    }

    /**
     * @deprecated kept for compatibility
     */
    public void setURL(URL url)
    {
        try {
            URI uri = url.toURI();
            File fileImpl = new File(uri);
            if (fileImpl.exists()) {
                this.file = new LocalFile(fileImpl);
            }
        } catch (Exception e) {
            throw new RuntimeException (e);
        }
    }
    
}
