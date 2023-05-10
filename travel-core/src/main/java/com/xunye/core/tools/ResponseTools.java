package com.xunye.core.tools;//package com.boyiz.jtar.core.tools;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;



public class ResponseTools {

    /**
     * 写入数据到response
     *
     * @param response 响应
     * @param data     数据
     */
    public static void writerAndFlush(HttpServletResponse response, Object data) {
        response.setContentType("application/json;charset=" + "UTF-8");
        try (PrintWriter writer = response.getWriter()) {
            writer.write(GsonTools.getGson().toJson(data));
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
