package com.daogu.data.ocrservice;

import com.google.common.io.Files;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;

@Slf4j
public class EnvTest {
    @Test
    public void testEnv() {
        log.info("hello, uc");
    }

    @Test
    public void testTimePrefixPath() {
        LocalDate localDate = LocalDate.now();
        String timePrefixPath =
                String.format("%d/%02d/%d", localDate.getYear(), localDate.getMonthValue(), localDate.getDayOfMonth());
        File ocrDir = Paths.get("d:/tmp/orc-files", timePrefixPath).toFile();
        if (!ocrDir.exists()) {
            ocrDir.mkdirs();
        }
        log.info(ocrDir.toString());
    }

    @Test
    public void testGetFileExt() {
        log.info( Files.getFileExtension("1.jpg"));

    }
}
