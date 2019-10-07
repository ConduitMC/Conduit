package me.ifydev.conduit.plugin;

import me.ifydev.conduit.Conduit;

import java.io.File;
import java.lang.instrument.Instrumentation;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Paths;
import java.util.jar.JarFile;

/**
 * @author Innectic
 * @since 10/6/2019
 */
public class PluginLoader {

    private static Instrumentation inst = null;

    /**
     * Ensures that the server folder has all the required folders to enable
     * plugin loading.
     */
    public boolean checkEnvironment() {
        File pluginsFolder = getPluginsFolder();
        if (!pluginsFolder.exists()) {
            // Plugins folder does not exist, make it.
            Conduit.LOGGER.info("Creating plugins folder...");
            if (!pluginsFolder.mkdirs()) {
                Conduit.LOGGER.fatal("Failed to create plugins folder!");
                return false;
            }
        }
        return true;
    }

    public void loadPlugins() {
        File pluginsFolder = getPluginsFolder();
        File[] files = pluginsFolder.listFiles();
        if (files == null) return;

        for (File file : files) {
            // Skip folders
            if (!file.isFile()) continue;
            // Make sure that it ends with .jar
            if (!file.getName().endsWith(".jar")) continue;

            // Since it is a file, and it ends with .jar, we can proceed with attempting to load it.
            addPluginToClassLoader(file);
        }
    }

    private File getPluginsFolder() {
        return Paths.get("plugins").toFile();
    }

    private void addPluginToClassLoader(File file) {
        ClassLoader cl = ClassLoader.getSystemClassLoader();
        try {
            if (inst != null) {
                inst.appendToSystemClassLoaderSearch(new JarFile(file));
                return;
            }
            Method m = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
            m.setAccessible(true);
            m.invoke(cl, file.toURI().toURL());
        } catch (Throwable e) {
            System.out.println("Add to classpath error!");
            e.printStackTrace();
            System.exit(0);
        }
    }
}
