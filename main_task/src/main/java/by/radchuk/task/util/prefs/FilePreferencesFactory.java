package by.radchuk.task.util.prefs;

import by.radchuk.task.model.User;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.prefs.Preferences;
import java.util.prefs.PreferencesFactory;
import java.util.prefs.BackingStoreException;

/**
 * PreferencesFactory implementation that stores the preferences in a user-defined file. To use it,
 * set the system property <i>java.util.prefs.PreferencesFactory</i> to
 * <i>by.radchuk.task.util.prefs.FilePreferencesFactory</i>
 * <p>
 * The file defaults to [user.home]/preferences/preferences.xml,
 * but may be overridden with the system property
 * <i>by.radchuk.task.util.prefs.file</i>
 *
 * Empty preferences file should look like the following:
 * <pre>
 * &lt;?xml version="1.0" encoding="UTF-8" standalone="no"?&gt;
 * &lt;!DOCTYPE properties SYSTEM "http://java.sun.com/dtd/properties.dtd"&gt;
 * &lt;properties&gt;
 * &lt;comment&gt;FilePreferences&lt;/comment&gt;
 * &lt;/properties&gt;
 * </pre>
 * The key field looks like the following:
 * <pre>
 *     &lt;entry key="by.radchuk.task.model.Number"&gt;1556568239155&lt;/entry&gt;
 * </pre>
 *
 * @author Dmitry Radchuk
 */
@Slf4j
public class FilePreferencesFactory implements PreferencesFactory {
    private static final String PROPERTY_FILE_PATH
            = "by.radchuk.task.util.prefs.file";
    private static Path filePath;
    private Preferences rootPreferences;


    @Override
    public Preferences systemRoot()
    {
        return userRoot();
    }

    @Override
    public Preferences userRoot()
    {
        if (rootPreferences == null) {
            log.info("Instantiating root preferences");
            rootPreferences = new FilePreferences(null, "");
        }
        return rootPreferences;
    }


    static Path getPreferencesPath() {
        if (filePath == null) {
            String prefsPath = System.getProperty(PROPERTY_FILE_PATH);
            if (prefsPath == null || prefsPath.length() == 0) {
                filePath = Paths.get("prefs", "app.preferences");
            } else {
                filePath = Paths.get(prefsPath);
            }
        }
        return filePath;
    }

    public static void main(String[] args) throws BackingStoreException
    {
        System.setProperty("java.util.prefs.PreferencesFactory", FilePreferencesFactory.class.getName());

        Preferences p = Preferences.userNodeForPackage(User.class);

        for (String s : p.keys()) {
            System.out.println("p[" + s + "]=" + p.get(s, null));
        }

        p.putBoolean("hi", true);
        p.put("Number", String.valueOf(System.currentTimeMillis()));
    }
}