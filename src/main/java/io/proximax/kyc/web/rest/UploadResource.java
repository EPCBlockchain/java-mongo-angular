package io.proximax.kyc.web.rest;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
// @RequestMapping(value = "upload")
@RequestMapping("/api")

public class UploadResource {

    // @PostMapping("/file")
    // @RequestMapping(value = "/file/", method = RequestMethod.POST)
    @RequestMapping(value = "/file", method = RequestMethod.POST)
    public String file(@RequestParam("file") MultipartFile file)
        throws IOException {        
        /*
        Path currentRelativePath = Paths.get("");
        Path currentDir = currentRelativePath.toAbsolutePath(); // <-- Get the Path and use resolve on it.
        String filename = "data" + File.separatorChar + "foo.txt";
        Path filepath = currentDir.resolve(filename);
        // "data/foo.txt"
        System.out.println(filepath);
        */

        Path currentRelativePath = Paths.get("");
        String s = currentRelativePath.toAbsolutePath().toString() + "/src/main/webapp/content/img/";
        System.out.println(" ****************** Current relative path is: " + s);

        StringBuilder builder = new StringBuilder();
        // builder.append(System.getProperty("user.home"));
        // builder.append(File.separator);
        // builder.append("spring_upload_example"); // => /root/spring_upload_example/
        builder.append(s);
        builder.append(File.separator);
        builder.append(file.getOriginalFilename());


        /*
        StringBuilder builder = new StringBuilder();
        builder.append(System.getProperty("user.home"));
        builder.append(File.separator);
        builder.append("spring_upload_example"); // => /root/spring_upload_example/
        builder.append(File.separator);
        builder.append(file.getOriginalFilename());
        */

        
        

        byte[] fileBytes = file.getBytes();
        Path path = Paths.get(builder.toString());
        Files.write(path, fileBytes);
        return "OK";
    }

}
