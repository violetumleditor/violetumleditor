package com.horstmann.violet.framework.plugin;

import com.horstmann.violet.framework.file.persistence.IFilePersistenceService;
import com.horstmann.violet.framework.file.persistence.Violet016BackportFormatService;
import com.horstmann.violet.framework.injection.bean.ManiocFramework.InjectedBean;
import com.horstmann.violet.framework.injection.bean.ManiocFramework.ManagedBean;
import com.horstmann.violet.framework.plugin.extensionpoint.Violet016FileFilterExtensionPoint;
import java.io.File;
import java.io.FileFilter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemManager;
import org.apache.commons.vfs.VFS;
import org.apache.commons.vfs.impl.VFSClassLoader;

@ManagedBean
public class PluginLoader extends ClassLoader
{
    public void installPlugins()
    {
        ServiceLoader<IDiagramPlugin> list = ServiceLoader.load(IDiagramPlugin.class, this.getClass().getClassLoader());
        for (IDiagramPlugin aPlugin : list)
        {
            this.pluginRegistry.register(aPlugin);
            if (aPlugin instanceof Violet016FileFilterExtensionPoint)
            {
                Violet016FileFilterExtensionPoint extensionPoint = (Violet016FileFilterExtensionPoint) aPlugin;
                Map<String, String> mappingToKeepViolet016Compatibility = extensionPoint.getMappingToKeepViolet016Compatibility();
                Violet016BackportFormatService.addViolet016CompatibilityEntries(mappingToKeepViolet016Compatibility);
            }
        }
    }

    private ClassLoader getExternalClassLoader()
    {
        String pluginDirName = System.getProperty("violet.plugin.dir");
        if (pluginDirName == null) return new URLClassLoader(new URL[0]);
        File pluginDir = new File(pluginDirName);
        File[] pluginJars = pluginDir.listFiles(new FileFilter()
        {
            public boolean accept(File pathname)
            {
                return pathname.toString().endsWith(".jar");
            }
        });

        URL[] pluginJarUrls = new URL[pluginJars.length];
        for (int i = 0; i < pluginJars.length; i++)
            try
            {
                pluginJarUrls[i] = pluginJars[i].toURI().toURL();
            }
            catch (MalformedURLException ex)
            {
                ex.printStackTrace();
            }

        return new URLClassLoader(pluginJarUrls);
    }

    private ClassLoader getJarInJarClassLoader()
    {
        try
        {
            FileSystemManager fsManager = VFS.getManager();
            List<FileObject> innerJarFiles = new ArrayList<FileObject>();
            String classPathContent = System.getProperty("java.class.path");
            String[] classPathFiles = classPathContent.split(File.pathSeparator);
            for (String aClassPathFile : classPathFiles)
            {
                File testingFile = new File(aClassPathFile);
                if (!testingFile.exists())
                {
                    continue;
                }
                if (testingFile.isDirectory())
                {
                    continue;
                }
                if (testingFile.getName().toLowerCase().endsWith(".jar"))
                {
                    continue;
                }
                JarFile jarFile = new JarFile(testingFile);
                Enumeration<JarEntry> jarEntries = jarFile.entries();
                while (jarEntries.hasMoreElements())
                {
                    JarEntry entry = jarEntries.nextElement();
                    if (entry.isDirectory()) continue;
                    if (!entry.getName().toLowerCase().endsWith(".jar")) continue;
                    FileObject innetJarFile = fsManager.resolveFile("jar:" + entry.getName());
                    innerJarFiles.add(innetJarFile);
                }
            }
            VFSClassLoader cl = new VFSClassLoader(innerJarFiles.toArray(new FileObject[innerJarFiles.size()]), fsManager);
            return cl;
        }
        catch (Exception e1)
        {
            throw new RuntimeException(e1);
        }
    }
    
    /** Registry where we register loaded plugins */
    @InjectedBean
    private PluginRegistry pluginRegistry;
    
    /** Service to convert IGraph to XML content (and XML to IGraph of course) */
    @InjectedBean
    private IFilePersistenceService filePersistenceService;
}
