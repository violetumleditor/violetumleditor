package com.horstmann.violet.application.jni;

import java.io.IOException;

import com.horstmann.violet.framework.file.IFile;
import com.horstmann.violet.framework.file.chooser.IFileChooserService;
import com.horstmann.violet.framework.file.naming.ExtensionFilter;
import com.horstmann.violet.framework.file.persistence.IFileReader;
import com.horstmann.violet.framework.file.persistence.IFileWriter;
import com.horstmann.violet.framework.injection.bean.ManiocFramework.ManagedBean;

/**
 * Browser-oriented file chooser used with CheerpJ.
 *
 * It supports file writing by delegating save operations to browser download APIs
 * through JavaScript native methods.
 */
@ManagedBean(registeredManually=true)
public class CheerpJFileChooserService implements IFileChooserService {

    private static final String DEFAULT_BASENAME = "diagram";

    @Override
    public boolean isWebStart() {
        return false;
    }

    @Override
    public IFileReader chooseAndGetFileReader(ExtensionFilter... extensions) throws IOException {
        throw new IOException("Opening local files is not supported in browser mode yet");
    }

    @Override
    public IFileReader getFileReader(IFile file) throws IOException {
        throw new IOException("Opening local files is not supported in browser mode yet");
    }

    @Override
    public IFileWriter chooseAndGetFileWriter(ExtensionFilter... extensions) throws IOException {
        String extension = resolveDefaultExtension(extensions);
        return new CheerpJDownloadFileWriter(DEFAULT_BASENAME + extension);
    }

    @Override
    public IFileWriter getFileWriter(IFile file) throws IOException {
        String filename = file == null ? null : file.getFilename();
        if (filename == null || filename.trim().isEmpty()) {
            filename = DEFAULT_BASENAME + ".violet.html";
        }
        return new CheerpJDownloadFileWriter(filename);
    }

    private String resolveDefaultExtension(ExtensionFilter... extensions) {
        if (extensions != null && extensions.length > 0 && extensions[0] != null) {
            String extension = extensions[0].getExtension();
            if (extension != null && !extension.trim().isEmpty()) {
                return extension;
            }
        }
        return ".violet.html";
    }
}
