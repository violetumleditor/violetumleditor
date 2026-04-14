package com.horstmann.violet.application.cheerpj;

import java.io.IOException;

import com.horstmann.violet.framework.file.IFile;
import com.horstmann.violet.framework.file.chooser.IFileChooserService;
import com.horstmann.violet.framework.file.naming.ExtensionFilter;
import com.horstmann.violet.framework.file.persistence.IFileReader;
import com.horstmann.violet.framework.file.persistence.IFileWriter;
import com.horstmann.violet.framework.injection.bean.ManiocFramework.ManagedBean;


@ManagedBean
public class CheerpJFileChooserService implements IFileChooserService {

    private static final String DEFAULT_BASENAME = "diagram";

    @Override
    public boolean isWebStart() {
        return false;
    }

    @Override
    public IFileReader chooseAndGetFileReader(ExtensionFilter... extensions) throws IOException {
        String acceptedExtensions = buildAcceptedExtensions(extensions);
        boolean fileSelected = CheerpJInterfaceService.showOpenDialog(acceptedExtensions);
        if (!fileSelected) {
            throw new IOException("File selection cancelled by user");
        }
        String filename = CheerpJInterfaceService.getFileName();
        if (filename == null || filename.trim().isEmpty()) {
            throw new IOException("No filename received after file selection");
        }
        return new CheerpJFileReader(filename);
    }

    @Override
    public IFileReader getFileReader(IFile file) throws IOException {
        if (file == null) {
            throw new IOException("No file specified");
        }
        return new CheerpJFileReader(file.getFilename());
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

    /**
     * Build accepted file extensions string for HTML5 file input.
     * Format: ".ext1,.ext2,.ext3"
     */
    private String buildAcceptedExtensions(ExtensionFilter... extensions) {
        if (extensions == null || extensions.length == 0) {
            return ".violet.html";
        }
        
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < extensions.length; i++) {
            if (extensions[i] != null) {
                String ext = extensions[i].getExtension();
                if (ext != null && !ext.trim().isEmpty()) {
                    if (i > 0) {
                        sb.append(",");
                    }
                    sb.append(ext);
                }
            }
        }
        
        return sb.length() > 0 ? sb.toString() : ".violet.html";
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