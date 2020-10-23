package com.skshazena.blogFinalProject.util;

import com.skshazena.blogFinalProject.config.WebConfig;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Year;
import java.time.YearMonth;

/**
 *
 * @author Shazena Khan
 *
 * Date Created: Oct 23, 2020
 */
public class FileUtil {

    public static void createDirectoryIfItDoesntExist(String dir) {
        final Path path = Paths.get(dir);

        if (Files.notExists(path)) {
            try {
                Files.createDirectories(path);
            } catch (IOException ie) {
                ie.printStackTrace();
            }
        }
    }

    public static Path getImagePath(String fileName) {
        StringBuilder sb = new StringBuilder();
        sb.append(WebConfig.IMAGE_FILE_BASE);
        sb.append(Year.now().getValue());

        createDirectoryIfItDoesntExist(sb.toString());

        sb.append("/");
        sb.append(YearMonth.now().getMonthValue());

        createDirectoryIfItDoesntExist(sb.toString());

        sb.append("/");
        sb.append(fileName);

        return Paths.get(sb.toString());
    }

    public static String getImageUrl(String imageFileName) {
        String baseUrl = WebConfig.BASE_URL;
        StringBuilder sb = new StringBuilder();

        sb.append(baseUrl);

        if (baseUrl.endsWith("/")) {
            baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
        }

        sb.append(WebConfig.IMAGE_RESOURCE_BASE);
        sb.append(getYearAndMonthUrlFragment());
        sb.append(imageFileName);

        return sb.toString();
    }

    private static String getYearAndMonthUrlFragment() {
        StringBuilder sb = new StringBuilder();
        sb.append(Year.now().getValue());
        sb.append("/");
        sb.append(YearMonth.now().getMonthValue());
        sb.append("/");

        return sb.toString();
    }

}
