package by.radchuk.task.util.prefs;

import by.radchuk.task.model.User;
import lombok.extern.slf4j.Slf4j;

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
 * The file defaults to [user.home]/.fileprefs, but may be overridden with the system property
 * <i>by.radchuk.task.util.prefs.file</i>
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
                filePath = Paths.get(System.getProperty("user.home"), "WebAppPreferences", ".preferences");
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