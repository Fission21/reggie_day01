package com.ux.controller;

import com.ux.common.R;
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

/**
 * 文件上传和下载
 * @author john
 * @version 1.1
 */
@RestController
@Slf4j
@RequestMapping("/common")
public class CommonController {

    // 导入yml文件中配置的路径
    @Value("${reggie.path}")
    private String basePath;

    /**
     * file 是一个临时文件，不转存就会消失
     * 注意类型和参数名
     * @param file
     * @return
     */
    @PostMapping("/upload")
    public R<String> upload(MultipartFile file){
        // 获得原始文件名
        String originalFilename = file.getOriginalFilename();
        // 获得文件名后缀
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        // 使用UUID重新生成文件名，防止文件名称重复造成文件覆盖
        String fileName = UUID.randomUUID().toString()+suffix;
        // 判断当前目录是否存在
        File dir = new File(basePath);
        if (!dir.exists()){
            dir.mkdirs();
        }
        try {
            // 将零时文件转存指定位置
            file.transferTo(new File(basePath+fileName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return R.success(fileName);
    }

    /**
     * 文件下载
     * @param name
     * @param response
     */
    @GetMapping("/download")
    public void download(String name, HttpServletResponse response){
        log.info(name);
        try {
            // 输入流，读取文件内容
            FileInputStream fileInputStream = new FileInputStream(new File(basePath + name));
            // 输出流，通过输出流将文件写回游览器
            ServletOutputStream outputStream = response.getOutputStream();
            response.setContentType("image/jpeg");
            int len = 0;
            byte[] bytes = new byte[1024];
            while ((len = fileInputStream.read(bytes))!= -1){
                outputStream.write(bytes,0,len);
                outputStream.flush();
            }
            fileInputStream.close();
            outputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
