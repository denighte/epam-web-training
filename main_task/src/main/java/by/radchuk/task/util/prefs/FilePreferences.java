package by.radchuk.task.util.prefs;

import lombok.extern.slf4j.Slf4j;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.prefs.AbstractPreferences;
import java.util.prefs.BackingStoreException;
import java.io.IOException;

/**
 * Preferences implementation that stores to a user-defined file. See FilePreferencesFactory.
 *
 * @author Dmitry Radchuk
 */
@Slf4j
public class FilePreferences extends AbstractPreferences
{
    private Map<String, String> root;
    private Map<String, FilePreferences> children;
    private Lock mutex = new ReentrantLock();
    private boolean isRemoved = false;

    FilePreferences(AbstractPreferences parent, String name)
    {
        super(parent, name);
        log.info("Instantiating node " + name);

        root = new TreeMap<String, String>();
        children = new TreeMap<String, FilePreferences>();

        try {
            sync();
        }
        catch (BackingStoreException e) {
            log.error("Unable to sync on creation of node " + name, e);
        }
    }

    @Override
    protected void putSpi(String key, String value)
    {
        root.put(key, value);
        try {
            flush();
        }
        catch (BackingStoreException e) {
            log.error("Unable to flush after putting " + key, e);
        }
    }

    @Override
    protected String getSpi(String key)
    {
        return root.get(key);
    }

    @Override
    protected void removeSpi(String key)
    {
        root.remove(key);
        try {
            flush();
        }
        catch (BackingStoreException e) {
            log.error("Unable to flush after removing " + key, e);
        }
    }

    @Override
    protected void removeNodeSpi() throws BackingStoreException
    {
        isRemoved = true;
        flush();
    }

    @Override
    protected String[] keysSpi() throws BackingStoreException
    {
        return root.keySet().toArray(new String[0]);
    }

    @Override
    protected String[] childrenNamesSpi() throws BackingStoreException
    {
        return children.keySet().toArray(new String[0]);
    }

    @Override
    protected FilePreferences childSpi(String name)
    {
        FilePreferences child = children.get(name);
        if (child == null || child.isRemoved()) {
            child = new FilePreferences(this, name);
            children.put(name, child);
        }
        return child;
    }

    @Override
    protected void syncSpi() throws BackingStoreException
    {
        if (isRemoved()) return;

        Path filePath = FilePreferencesFactory.getPreferencesPath();

        if (Files.exists(filePath)) {
            return;
        }
        //Here should be synchronized( lock )
        //But due to the task, cat't do that
        mutex.lock();
        try {
            Properties p = new Properties();
            p.loadFromXML(Files.newInputStream(filePath));

            StringBuilder sb = new StringBuilder();
            getPath(sb);
            String path = sb.toString();

            final Enumeration<?> pnen = p.propertyNames();
            while (pnen.hasMoreElements()) {
                String propKey = (String) pnen.nextElement();
                if (propKey.startsWith(path)) {
                    String subKey = propKey.substring(path.length());
                    // Only load immediate descendants
                    if (subKey.indexOf('.') == -1) {
                        root.put(subKey, p.getProperty(propKey));
                    }
                }
            }
        } catch (IOException e) {
            throw new BackingStoreException(e);
        } finally {
            mutex.unlock();
        }


    }

    @Override
    protected void flushSpi() throws BackingStoreException
    {
        final Path filePath = FilePreferencesFactory.getPreferencesPath();
        mutex.lock();

        try {
            Properties p = new Properties();
            StringBuilder sb = new StringBuilder();
            getPath(sb);
            String path = sb.toString();

            if (Files.exists(filePath)) {
                p.loadFromXML(Files.newInputStream(filePath));
                List<String> toRemove = new ArrayList<String>();

                // Make a list of all direct children of this node to be removed
                final Enumeration<?> propertyNames = p.propertyNames();
                while (propertyNames.hasMoreElements()) {
                    String propKey = (String) propertyNames.nextElement();
                    if (propKey.startsWith(path)) {
                        String subKey = propKey.substring(path.length());
                        // Only do immediate descendants
                        if (subKey.indexOf('.') == -1) {
                            toRemove.add(propKey);
                        }
                    }
                }
                // Remove them now that the enumeration is done with
                for (String propKey : toRemove) {
                    p.remove(propKey);
                }
            }

            // If this node hasn't been removed, add back in any values
            if (!isRemoved) {
                for (String s : root.keySet()) {
                    p.setProperty(path + s, root.get(s));
                }
            }
            p.storeToXML(Files.newOutputStream(filePath), "FilePreferences");
        } catch (IOException e) {
            throw new BackingStoreException(e);
        } finally {
            mutex.unlock();
        }

    }

    private void getPath(StringBuilder sb) {
        final FilePreferences parent = (FilePreferences) parent();
        if (parent == null) return;
        parent.getPath(sb);
        sb.append(name()).append('.');
    }
}