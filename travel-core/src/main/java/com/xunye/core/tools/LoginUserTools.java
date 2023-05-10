package com.xunye.core.tools;//package com.boyiz.jtar.core.tools;
//
//import javax.annotation.Resource;
//
//import com.alibaba.fastjson2.JSON;
//import com.alibaba.fastjson2.JSONArray;
//import com.alibaba.fastjson2.JSONObject;
//
//import com.boyiz.jtar.core.exception.BusinessException;
//import com.boyiz.jtar.entity.User;
//import com.boyiz.jtar.entity.to.UserParseTO;
//import org.springframework.beans.BeanUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//
///**
// * @ClassName LoginUserTools
// * @Description
// * @Author boyiz
// * @Date 2023/3/23 15:13
// * @Version 1.0
// **/
//@Component
//public class LoginUserTools {
//
//    @Value("${sa-token.session-prefix}")
//    private static String SESSION_PREFIX;
//
//    public LoginUserTools() {
//
//    }
//
//    @Resource
//    private static RedisUtil redisUtil;
//
//    /**
//     * 获取用户信息
//     *
//     * @return
//     */
//    public static User getCurrentRealUser(String userId) {
//        User currentRealUser = new User();
//        Object o = redisUtil.get(SESSION_PREFIX + ":" + userId);
//        JSONObject data = JSON.parseObject(JSON.toJSONString(o));
//        JSONObject data1 = data.getJSONObject("dataMap");
//        JSONArray reportForms = data1.getJSONArray("user");
//        if (CheckTools.isNotNullOrEmpty(reportForms) && reportForms.size() == 1) {
//            UserParseTO userParseTO = JSON.parseObject(JSON.toJSONString(reportForms.get(0)), UserParseTO.class);
//            if (CheckTools.isNotNullOrEmpty(userParseTO)) {
//                BeanUtils.copyProperties(userParseTO, currentRealUser);
//                return currentRealUser;
//            }
//            throw new BusinessException("获取登录用户信息失败");
//        } else {
//            throw new BusinessException("获取登录用户信息失败");
//        }
//    }
//
//}
