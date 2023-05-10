package com.xunye.core.base;

import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletResponse;


public abstract class BaseController {

    protected void exportExcelBefore(HttpServletResponse response, String fileName) throws IOException {
        // 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String targetName = URLEncoder.encode(fileName, "UTF-8");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + targetName + ".xlsx");
    }

}
