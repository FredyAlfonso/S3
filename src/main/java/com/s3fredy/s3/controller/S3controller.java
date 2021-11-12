package com.s3fredy.s3.controller;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/s3")
public class S3controller {

    @Autowired
    AmazonS3 amazonS3;

    @PostMapping("/upload")
    public void upload(@RequestParam("file") MultipartFile file){
        File mainFile = new File(file.getOriginalFilename());
        try (FileOutputStream stream = new FileOutputStream(mainFile)) {
            stream.write(file.getBytes());
            //String newFileName = System.currentTimeMillis() + "_" + mainFile.getName();
            String newFileName = mainFile.getName();
            PutObjectRequest request = new PutObjectRequest("myprimerbucketfredy", newFileName, mainFile);
            PutObjectResult result=amazonS3.putObject(request);
            System.out.println(result.getETag());
            System.out.println(result.getVersionId());
            System.out.println(result.getMetadata());
            System.out.println(newFileName);
        } catch (Exception e) {
            System.out.println("pailas");
        }
    }

    @GetMapping("lista")
    public List<String> getList(){
        ListObjectsV2Result result = amazonS3.listObjectsV2("myprimerbucketfredy");
        List<S3ObjectSummary> objects = result.getObjectSummaries();
        List<String> list = objects.stream().map(item -> {
            return item.getKey();
        }).collect(Collectors.toList());
        return list;
    }

    @GetMapping("objeto")
    public String downloadFile(@RequestParam("key") String key) throws IOException {
        S3Object object = amazonS3.getObject("myprimerbucketfredy", key);
        generarPDF(object.getObjectContent().readAllBytes(),object.getKey());
        //return object.getObjectContent();
        return object.getKey();
    }

    @DeleteMapping("objeto")
    public void deleteFile (@RequestParam("key") String key){
        amazonS3.deleteObject("myprimerbucketfredy", key);
    }

    private void generarPDF(byte[] File64, String fileName) {
        OutputStream outputStream = null;
        try {
            File file = new File("C:\\Users\\Fredy\\Desktop" + File.separator + fileName);
            outputStream = new FileOutputStream(file);
            outputStream.write(File64);
            outputStream.close();
        } catch (Exception e) {
            System.out.println(e.getCause());
        }
    }
}
