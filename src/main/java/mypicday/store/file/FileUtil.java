package mypicday.store.file;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

@Slf4j
public final class FileUtil {
    public static String getImageUrls(String filename) {
        try {
            Path path = Paths.get("upload").resolve(filename);
            byte[] bytes = Files.readAllBytes(path);
            return Base64.getEncoder().encodeToString(bytes);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return null;
    }

}
