package com.horstmann.violet.eclipseplugin.file;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.runtime.CoreException;

import com.horstmann.violet.framework.file.IFile;
import com.horstmann.violet.framework.file.persistence.IFileReader;

public class EclipseFileOpener implements IFileReader {

	private org.eclipse.core.resources.IFile eclipseFile;

	private IFile fileDefinition;
	
	
	public EclipseFileOpener(org.eclipse.core.resources.IFile eclipseFile) {
	    this.eclipseFile = eclipseFile;
    }

	@Override
	public IFile getFileDefinition() throws IOException {
		if (this.fileDefinition == null) {
			this.fileDefinition = new IFile() {
				@Override
				public String getDirectory() {
					IContainer parent = eclipseFile.getParent();
					URI parentURI = parent.getLocationURI();
					String directory = parentURI.getPath();
					return directory;
				}

				@Override
				public String getFilename() {
					String name = eclipseFile.getName();
					return name;
				}
			};
		}
		return this.fileDefinition;
	}

	@Override
	public InputStream getInputStream() throws IOException {
		try {
			return this.eclipseFile.getContents();
		} catch (CoreException e) {
			throw new IOException(e);
		}
	}

}
