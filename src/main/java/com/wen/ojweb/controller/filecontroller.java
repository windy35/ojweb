package com.wen.ojweb.controller;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Controller
public class filecontroller {

    @RequestMapping(value = "uploadfile",method = RequestMethod.POST)
    public String uploadfiile(@RequestParam("file") MultipartFile file) throws IOException {
        if(!file.isEmpty()){
            FileUtils.copyInputStreamToFile(file.getInputStream(),new File("F:\\file\\",System.currentTimeMillis()+file.getOriginalFilename()));
        }
        return "success";
    }
}
