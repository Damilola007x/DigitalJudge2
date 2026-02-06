package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.io.File;

@SpringBootApplication
public class DigitalJudgeApplication {

    public static void main(String[] args) {
        // FORCE the temp directory to be inside your project folder
        String projectFolder = System.getProperty("user.dir");
        String tempFolder = projectFolder + File.separator + "target" + File.separator + "temp";

        File dir = new File(tempFolder);
        if (!dir.exists()) dir.mkdirs();

        // This overrides the C:\WINDOWS default
        System.setProperty("java.io.tmpdir", tempFolder);

        SpringApplication.run(DigitalJudgeApplication.class, args);
    }
}