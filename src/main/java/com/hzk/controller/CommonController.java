package com.hzk.controller;

import com.hzk.util.Result;
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
import java.io.IOException;
import java.util.UUID;

/**
 * 文件上传和下载的饿Controller
 */
@RestController
@RequestMapping("/common")
public class CommonController {
    //路径定义在配置文件中，偏于修改
    @Value("${reggie.path}")
    private String basePath;
    /**
     * 文件上传，上传到服务器端
     * @param file
     * @return
     */
    @PostMapping("/upload")
    public Result<String> upload(MultipartFile file){
        //获得原始的文件名
        String filename = file.getOriginalFilename();
        //截取后面的文件格式后缀
       String suffix =  filename.substring(filename.lastIndexOf("."));
        //使用UUID重新生成文件名称，防止文件名称重复
        String filenames = UUID.randomUUID().toString() +suffix;
        //这里就会得到一个上传到图片的，完后将临时图片转存到指定位置
        //创建一个新的目录
        File file1 = new File(basePath);
        //判断保存的时候是否存在一个这样的目录，如果存在的话，则保存，如果不存在则创建一个
        if (!file1.exists()){
            //目录不存在,则创建
            file1.mkdirs();
        }
        try {
            file.transferTo(new File(basePath + filenames));
        } catch (IOException e) {
            e.printStackTrace();
        }
        //返回文件名称，保存到数据库中
        return Result.success(filenames);
    }

    /**
     * 文件下载
     * @param name
     * @param response
     */
    @GetMapping("/download")
    public void download(String name , HttpServletResponse response){

        try {
            //输入流，通过输入流读取文件内容
            FileInputStream fileInputStream = new FileInputStream(new File(basePath+name));
            //输出流，通过输出流将文件写回浏览器，在浏览器中展示图片了
            ServletOutputStream outputStream = response.getOutputStream();
            //设置响应的格式
            response.setContentType("image/jpeg");
            int len = 0;
            //读到的放到这个数组里面
            byte[] bytes = new byte[1024];
            while ((len=fileInputStream.read(bytes))!=-1){
                outputStream.write(bytes,0,len);
                outputStream.flush();
            }
            outputStream.close();
            fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
