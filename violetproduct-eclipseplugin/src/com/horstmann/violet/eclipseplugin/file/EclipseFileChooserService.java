package com.horstmann.violet.eclipseplugin.file;

import java.io.IOException;

import org.eclipse.core.runtime.IProgressMonitor;

import com.horstmann.violet.framework.file.IFile;
import com.horstmann.violet.framework.file.chooser.IFileChooserService;
import com.horstmann.violet.framework.file.naming.ExtensionFilter;
import com.horstmann.violet.framework.file.persistence.IFileReader;
import com.horstmann.violet.framework.file.persistence.IFileWriter;
import com.horstmann.violet.framework.injection.bean.ManiocFramework.ManagedBean;

@ManagedBean(registeredManually=true)
public class EclipseFileChooserService implements IFileChooserService
{

    private org.eclipse.core.resources.IFile eclipseFile;

    private EclipseFileSaver eclipseFileSaver;

    private IFileReader eclipseFileOpener;

    private EclipseFileSaver getFileSaver()
    {
        if (this.eclipseFileSaver == null)
        {
            if (this.eclipseFile == null) throw new RuntimeException("Eclipse file must be set before EclipseFileSaver creation");
            this.eclipseFileSaver = new EclipseFileSaver(this.eclipseFile);
        }
        return this.eclipseFileSaver;
    }

    public void setEclipseFile(org.eclipse.core.resources.IFile eclipseFile)
    {
        this.eclipseFile = eclipseFile;
    }

    public void changeProgressMonitor(IProgressMonitor progressMonitor)
    {
        getFileSaver().setProgressMonitor(progressMonitor);
    }

    @Override
    public boolean isWebStart()
    {
        return false;
    }

    @Override
    public IFileReader chooseAndGetFileReader() throws IOException
    {
        if (this.eclipseFileOpener == null)
        {
            if (this.eclipseFile == null) throw new RuntimeException("Eclipse file must be set before EclipseFileOpener creation");
            this.eclipseFileOpener = new EclipseFileOpener(this.eclipseFile);
        }
        return this.eclipseFileOpener;
    }

    @Override
    public IFileReader getFileReader(IFile arg0) throws IOException
    {
        if (this.eclipseFileOpener == null)
        {
            if (this.eclipseFile == null) throw new RuntimeException("Eclipse file must be set before EclipseFileOpener creation");
            this.eclipseFileOpener = new EclipseFileOpener(this.eclipseFile);
        }
        return this.eclipseFileOpener;
    }

    @Override
    public IFileWriter chooseAndGetFileWriter(ExtensionFilter... arg0) throws IOException
    {
        return this.getFileSaver();
    }

    @Override
    public IFileWriter getFileWriter(IFile arg0) throws IOException
    {
        return this.getFileSaver();
    }

}
