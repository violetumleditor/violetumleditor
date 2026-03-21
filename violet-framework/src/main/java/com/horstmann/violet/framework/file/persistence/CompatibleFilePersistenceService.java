package com.horstmann.violet.framework.file.persistence;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import com.horstmann.violet.framework.injection.bean.ManiocFramework.ManagedBean;
import com.horstmann.violet.product.diagram.abstracts.IGraph;

@ManagedBean(registeredManually=true)
public class CompatibleFilePersistenceService implements IFilePersistenceService
{

    private final IFilePersistenceService standardService = new StandardJavaFilePersistenceService();

    private final IFilePersistenceService legacyService = new LegacyVioletXmlPersistenceService();

    @Override
    public void write(IGraph graph, OutputStream out)
    {
        this.standardService.write(graph, out);
    }

    @Override
    public IGraph read(InputStream in) throws IOException
    {
        String xmlContent = readAll(in);

        if (isLikelyStandardJavaXml(xmlContent))
        {
            return readWithFallback(xmlContent, this.standardService, this.legacyService);
        }

        return readWithFallback(xmlContent, this.legacyService, this.standardService);
    }

    private IGraph readWithFallback(String xmlContent, IFilePersistenceService primaryService, IFilePersistenceService fallbackService)
            throws IOException
    {

        try
        {
            return primaryService.read(new ByteArrayInputStream(xmlContent.getBytes(StandardCharsets.UTF_8)));
        }
        catch (Exception primaryException)
        {
            try
            {
                return fallbackService.read(new ByteArrayInputStream(xmlContent.getBytes(StandardCharsets.UTF_8)));
            }
            catch (Exception fallbackException)
            {
                throw new IOException("Unable to read graph content using standard or legacy format", fallbackException);
            }
        }
    }

    private boolean isLikelyStandardJavaXml(String xmlContent)
    {
        if (xmlContent == null)
        {
            return false;
        }
        String trimmed = xmlContent.trim();
        if (trimmed.startsWith("<?xml"))
        {
            int closing = trimmed.indexOf("?>");
            if (closing >= 0 && closing + 2 < trimmed.length())
            {
                trimmed = trimmed.substring(closing + 2).trim();
            }
        }
        return trimmed.startsWith("<java");
    }

    private String readAll(InputStream in) throws IOException
    {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        byte[] chunk = new byte[4096];
        int read;
        while ((read = in.read(chunk)) != -1)
        {
            buffer.write(chunk, 0, read);
        }
        return buffer.toString(StandardCharsets.UTF_8);
    }
}
