package com.zbx.utilapi.controller;

import com.zbx.utilapi.common.AuthLogin;
import com.zbx.utilapi.common.Result;
import com.zbx.utilapi.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @创建人 zbx
 * @创建时间 2021/8/24
 * @描述
 */
@Slf4j
@RestController
@CrossOrigin
public class HomeController {

    @Autowired
    JsonUtil util;

    @PostMapping("/parse")
    public int parseJson(@RequestParam("file") MultipartFile file){

        String filename = file.getOriginalFilename();

        if (filename != null && filename.endsWith(".json")) {
            InputStream in = null;
            try {
                in = file.getInputStream();
            } catch (IOException e) {
                log.warn("无法获取文件输入流: {}", filename);
                e.printStackTrace();
                return -1;
            }
//            OutputStream out = response.getOutputStream();
//            response.setHeader("content-disposition","attachment;fileName="+filename);
//            response.setHeader("Content-Type","application/octet-stream");
            int i = util.JsonFormat(in, filename);
            try {
                in.close();
            } catch (IOException e) {
                log.warn("无法关闭文件输入流: {}", filename);
                return -1;
            }
            log.info("{} 解析成功,修改次数: {}",filename,i);
            return i;
//                out.close();
        } else {
            log.warn("上传的文件不是json: {}", filename);
            return -1;
        }
    }

    @PostMapping("/login")
    public Result<String> login(@RequestBody AuthLogin auth) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHH");
        String psd ="admin" + formatter.format(new Date());
        if (psd.equals(auth.getAuth())) {
           return Result.success("4QrcOUm6Wau+VuBX8g+IPg==");
        } else {
            return Result.failed();
        }
    }
}
