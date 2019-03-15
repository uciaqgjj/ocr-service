package com.daogu.data.ocrservice;

import com.google.common.collect.Maps;
import com.google.common.io.Files;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.UUID;

@RestController
public class OrcController {

    @Value("${orc.basePath}")
    private String basePath;

    @Autowired
    private BaiduAipService baiduAipService;


    @PostMapping("/upload-file")
    public ResponseEntity uploadFile(@RequestParam("file")MultipartFile file) throws IOException {
        LocalDate localDate = LocalDate.now();
        String timePrefixPath =
                String.format("%d/%02d/%d", localDate.getYear(), localDate.getMonthValue(), localDate.getDayOfMonth());
        File ocrDir = Paths.get(basePath, timePrefixPath).toFile();
        if (!ocrDir.exists()) {
            ocrDir.mkdirs();
        }
        String originalFileName = file.getOriginalFilename();
        int dotIndex = originalFileName.lastIndexOf('.');
        String fileExtention =  (dotIndex == -1) ? "" : originalFileName.substring(dotIndex + 1);

        String toStoreFileName = String.format("%s.%s",UUID.randomUUID().toString(), fileExtention);
        file.transferTo(new File(ocrDir, toStoreFileName));
        return ResponseEntity.ok("成功");
    }


    @HystrixCommand(commandProperties = {
        @HystrixProperty(name = "circuitBreaker.enabled", value = "false"),
        @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "3000")
    }, threadPoolProperties = {
        @HystrixProperty(name = "coreSize", value = "2"),
        @HystrixProperty(name = "queueSizeRejectionThreshold", value = "20"),
        @HystrixProperty(name = "maxQueueSize", value = "20")
    })
    @PostMapping("/aip")
    public ResponseEntity performanceAip(@RequestParam("file")MultipartFile file) throws IOException {

        byte[] bytes = file.getBytes();
        LocalDate localDate = LocalDate.now();
        String timePrefixPath =
            String.format("%d/%02d/%d", localDate.getYear(), localDate.getMonthValue(), localDate.getDayOfMonth());
        File ocrDir = Paths.get(basePath, timePrefixPath).toFile();
        if (!ocrDir.exists()) {
            ocrDir.mkdirs();
        }
        String originalFileName = file.getOriginalFilename();
        int dotIndex = originalFileName.lastIndexOf('.');
        String fileExtention =  (dotIndex == -1) ? "" : originalFileName.substring(dotIndex + 1);

        String toStoreFileName = String.format("%s.%s",UUID.randomUUID().toString(), fileExtention);

        Files.write(bytes, new File(ocrDir, toStoreFileName));

        String result = baiduAipService.doOcr(file.getBytes(), Maps.newHashMap());
        //TODO: 持久化
        return ResponseEntity.ok(result);
    }


}
