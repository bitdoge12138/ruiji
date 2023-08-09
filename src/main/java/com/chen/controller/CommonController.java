package com.chen.controller;

import com.chen.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;


@RestController
@RequestMapping("/common")
@Slf4j
public class CommonController {


    @Value("${reggie.path}")
    private String basepath;


    @PostMapping("/upload")
    public R<String> upload(MultipartFile file) {  // 这里名称必须要是file，跟前端页面中name属性一致

        log.info(file.toString());

        String originalFilename = file.getOriginalFilename();

        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        String fileName = UUID.randomUUID().toString() + suffix;

        File dir = new File(basepath);

        if (!dir.exists()) dir.mkdir();


        try {
            file.transferTo(new File(basepath + fileName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return R.success(fileName);
    }


    @GetMapping("/download")
    public void download(String name, HttpServletResponse response) {
        // 使得上传的图像可以显示子在浏览器上
        try {
            FileInputStream fileInputStream = new FileInputStream(new File(basepath + name));
            ServletOutputStream outputStream = response.getOutputStream();

            response.setContentType("/image/jpeg");

            int len = 0;
            byte[] bytes = new byte[1024];

            while ((len = fileInputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, len);
                outputStream.flush();

            }

            fileInputStream.close();
            outputStream.close();

        } catch (IOException e) {
            e.printStackTrace();


        }


    }
}
