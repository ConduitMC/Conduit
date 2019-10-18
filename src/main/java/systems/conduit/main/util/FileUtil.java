package systems.conduit.main.util;

import systems.conduit.main.Conduit;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author Innectic
 * @since 10/17/2019
 */
public class FileUtil {

    public static String readFile(String file) {
        try {
            return new String(Files.readAllBytes(Paths.get(file)));
        } catch (IOException e) {
            Conduit.getLogger().error("Failed to read configuration file: " + file);
            e.printStackTrace();
        }
        return "";
    }
}
