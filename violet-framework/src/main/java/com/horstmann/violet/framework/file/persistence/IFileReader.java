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

package com.horstmann.violet.framework.file.persistence;

import com.horstmann.violet.framework.file.IFile;
import java.io.IOException;
import java.io.InputStream;

/**
 * An FileOpenHandler object handles the stream and name of the file that the user selected for reading.
 */
public interface IFileReader
{

    
    /**
     * Gets the input stream corresponding to the user selection.
     * 
     * @return the input stream
     */
    public InputStream getInputStream() throws IOException;

    /**
     * Gets file definition
     * 
     * @return the file name
     */
    public IFile getFileDefinition() throws IOException;
    
    
    
    


}