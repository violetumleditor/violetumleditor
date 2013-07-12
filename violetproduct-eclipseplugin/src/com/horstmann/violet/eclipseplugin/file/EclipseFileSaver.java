package com.horstmann.violet.eclipseplugin.file;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

import com.horstmann.violet.framework.file.IFile;
import com.horstmann.violet.framework.file.persistence.IFileWriter;

public class EclipseFileSaver implements IFileWriter {

	private org.eclipse.core.resources.IFile eclipseFile;

	private IFile fileDefinition;

	private IProgressMonitor progressMonitor;
	

	public EclipseFileSaver(org.eclipse.core.resources.IFile eclipseFile) {
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
	public OutputStream getOutputStream() throws IOException {
		if (getProgressMonitor() == null) {
			throw new RuntimeException("Eclipse progress monitor must be initialized before saving file");
		}
		ByteArrayOutputStream bos = new ByteArrayOutputStream() {
			@Override
			public void close() throws IOException {
			    super.close();
				byte[] byteArray = toByteArray();
				ByteArrayInputStream bis = new ByteArrayInputStream(byteArray);
				try {
					eclipseFile.setContents(bis, true, true, getProgressMonitor());
				} catch (CoreException e) {
					throw new IOException(e);
				}
			}
		};
		return bos;
	}

	public void setProgressMonitor(IProgressMonitor progressMonitor) {
		this.progressMonitor = progressMonitor;
	}

	private IProgressMonitor getProgressMonitor() {
		return this.progressMonitor;
	}

}
