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

import com.horstmann.violet.framework.file.IFile;
import com.horstmann.violet.framework.file.naming.ExtensionFilter;
import com.horstmann.violet.framework.file.naming.FileNamingService;
import com.horstmann.violet.framework.file.persistence.IFileReader;
import com.horstmann.violet.framework.file.persistence.IFileWriter;
import com.horstmann.violet.framework.file.persistence.JNLPFileReader;
import com.horstmann.violet.framework.file.persistence.JNLPFileWriter;
import com.horstmann.violet.framework.injection.bean.ManiocFramework.BeanInjector;
import com.horstmann.violet.framework.injection.bean.ManiocFramework.InjectedBean;
import com.horstmann.violet.framework.injection.bean.ManiocFramework.ManagedBean;
import java.io.IOException;
import java.util.ArrayList;
import javax.jnlp.FileContents;
import javax.jnlp.FileOpenService;
import javax.jnlp.FileSaveService;
import javax.jnlp.ServiceManager;
import javax.jnlp.UnavailableServiceException;

/**
 * This class provides a FileService for Java Web Start. Note that file saving is strange under Web Start. You first save the data,
 * and the dialog is only displayed when the output stream is closed. Therefore, the file name is not available until after the file
 * has been written.
 */
@ManagedBean(registeredManually=true)
public class JNLPFileChooserService implements IFileChooserService
{

    /**
     * Default constructor
     * 
     * @param namingService
     */
    public JNLPFileChooserService()
    {
        BeanInjector.getInjector().inject(this);
        try
        {
            openService = (FileOpenService) ServiceManager.lookup("javax.jnlp.FileOpenService");
            saveService = (FileSaveService) ServiceManager.lookup("javax.jnlp.FileSaveService");
        }
        catch (UnavailableServiceException ex)
        {
            ex.printStackTrace();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.horstmann.violet.framework.file.chooser.IFileChooserService#isWebStart()
     */
    public boolean isWebStart()
    {
        return true;
    }

    @Override
    public IFileReader getFileReader(IFile file) throws IOException
    {
        String currentDirectory = file.getDirectory();
        String filename = file.getFilename();
        ExtensionFilter[] filters = this.fileNamingService.getFileFilters();
        ArrayList<String> fileExtensions = new ArrayList<String>();
        for (ExtensionFilter aFilter : filters)
        {
            String aFilterExtension = aFilter.getExtension();
            if (filename.endsWith(aFilterExtension)) {
                fileExtensions.add(aFilterExtension);
            }
        }
        String[] fileExtensionsStrings = (String[]) fileExtensions.toArray(new String[fileExtensions.size()]);
        final FileContents contents = openService.openFileDialog(currentDirectory, fileExtensionsStrings);
        return new JNLPFileReader(contents);
    }    
    
    @Override
    public IFileReader chooseAndGetFileReader(ExtensionFilter... filters) throws IOException
    {
        String currentDirectory = ".";
        ArrayList<String> fileExtensions = new ArrayList<String>();
        //ExtensionFilter[] filters = this.fileNamingService.getFileFilters();
        for (int i = 0; i < filters.length; i++)
        {
            ExtensionFilter aFilter = filters[i];
            String filterExtension = aFilter.getExtension();
            fileExtensions.add(filterExtension);
        }
        String[] fileExtensionsStrings = (String[]) fileExtensions.toArray(new String[fileExtensions.size()]);
        final FileContents contents = openService.openFileDialog(currentDirectory, fileExtensionsStrings);
        return new JNLPFileReader(contents);
    }

    @Override
    public IFileWriter getFileWriter(IFile file) throws IOException
    {

        String defaultDirectory = file.getDirectory();
        ArrayList<String> fileExtensions = new ArrayList<String>();
        ExtensionFilter[] filters = this.fileNamingService.getFileFilters();
        for (int i = 0; i < filters.length; i++)
        {
            ExtensionFilter aFilter = filters[i];
            String filterExtension = aFilter.getExtension();
            fileExtensions.add(filterExtension);
        }
        String[] fileExtensionsStrings = (String[]) fileExtensions.toArray(new String[fileExtensions.size()]);
        FileContents contents = saveService.saveAsFileDialog(defaultDirectory, fileExtensionsStrings, null);
        if (contents == null)
        {
            return null;
        }
        return new JNLPFileWriter(contents);
    }
    
    @Override
    public IFileWriter chooseAndGetFileWriter(final ExtensionFilter... filters) throws IOException
    {
        String defaultDirectory = ".";
        ArrayList<String> fileExtensions = new ArrayList<String>();
        for (int i = 0; i < filters.length; i++)
        {
            ExtensionFilter aFilter = filters[i];
            String filterExtension = aFilter.getExtension();
            fileExtensions.add(filterExtension);
        }
        String[] fileExtensionsStrings = (String[]) fileExtensions.toArray(new String[fileExtensions.size()]);
        FileContents contents = saveService.saveAsFileDialog(defaultDirectory, fileExtensionsStrings, null);
        if (contents == null)
        {
            return null;
        }
        return new JNLPFileWriter(contents);
    }    

    /**
     * JNLP service
     */
    private FileOpenService openService;

    /**
     * JNLP service
     */
    private FileSaveService saveService;
    
    /**
     * Handle file names
     */
    @InjectedBean
    private FileNamingService fileNamingService;

}
