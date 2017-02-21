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

package com.horstmann.violet.framework.file.chooser;

import java.io.IOException;

import com.horstmann.violet.framework.file.IFile;
import com.horstmann.violet.framework.file.naming.ExtensionFilter;
import com.horstmann.violet.framework.file.persistence.IFileReader;
import com.horstmann.violet.framework.file.persistence.IFileWriter;


/**
 * Services needed to choose a file on the filesystem when opening or saving a graph. This service 
 * could seem bizarre but, for example, the dialog box will be different when the program
 * is started from Eclipse than when it is started from JavaWebStart. M'kay!!!...
 * @author Alexandre de Pellegrin
 *
 */
public interface IFileChooserService
{

    /**
     * Tests whether the service is provided by WebStart
     * 
     * @return true if this service is provided by WebStart
     */
    public boolean isWebStart();

    /**
     * Pick up a file and return a file handler
     * 
     * @return the Open object for the selected file
     * @throws IOException
     */
    public IFileReader chooseAndGetFileReader(ExtensionFilter... extensions) throws IOException;
    
    /**
     * Returns a file handler to read the given file
     * 
     * @param file : existing file definition
     * @return the Open object for the selected file
     * @throws IOException
     */
    public IFileReader getFileReader(IFile file) throws IOException;

    /**
     * Choose a file and return a file handler to save content 
     * 
     * @param acceptable file filters
     * @return the Save object for the selected file
     * @throws IOException
     */
    public IFileWriter chooseAndGetFileWriter(ExtensionFilter... extensions) throws IOException;
    
    /**
     * Returns a file handler to save the given file
     * 
     * @param extensions acceptable extension filters
     * @return the Save object for the selected file
     * @throws IOException
     */
    public IFileWriter getFileWriter(IFile file) throws IOException;

}
