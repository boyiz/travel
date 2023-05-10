package com.xunye.core.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletResponse;

/**
 * 将系统内文件下载至浏览器所用到的一些通用方法
 */
public class DownLoadHelper {

    public static void outPut(String url, HttpServletResponse response) throws IOException {
        File file = new File(url);
        String filename = file.getName();
        //设置响应的信息
        response.reset();
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(filename, "utf8"));
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        //设置浏览器接受类型为流
        response.setContentType("application/octet-stream;charset=UTF-8");

        FileInputStream in = new FileInputStream(file);
        /* 将文件写入输入流 */
        OutputStream out = response.getOutputStream();
        //其他类型的文件，按照普通文件传输 如（zip、rar等压缩包）
        int len;
        //一次传输1KB大小字节
        byte[] bytes = new byte[1024];
        while ((len = in.read(bytes)) != -1) {
            out.write(bytes, 0, len);
        }


    }

}
