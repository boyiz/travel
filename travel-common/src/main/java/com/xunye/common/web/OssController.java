//package com.xunye.common.web;
//
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.LinkedHashMap;
//import java.util.Map;
//
//import cn.dev33.satoken.stp.StpUtil;
//import com.aliyun.oss.OSS;
//import com.aliyun.oss.OSSClientBuilder;
//import com.aliyun.oss.common.utils.BinaryUtil;
//import com.aliyun.oss.model.MatchMode;
//import com.aliyun.oss.model.PolicyConditions;
//
//import com.xunye.core.result.R;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
///**
// * @ClassName OssController
// * @Description 对象存储获取签名
// * @Author boyiz
// * @Date 2022/10/3 11:15
// * @Version 1.0
// **/
//@RequestMapping("/common")
//@RestController
//public class OssController {
//    @Value("${aliyun.oss.endpoint}")
//    private String endpoint;
//    @Value("${aliyun.bucket}")
//    private String bucket;
//    @Value("${aliyun.access-key}")
//    private String accessId;
//    @Value("${aliyun.secret-key}")
//    private String accessKey;
//    @Value("${aliyun.user-dir}")
//    private String userFolder;
//    @Value("${aliyun.pet-dir}")
//    private String petFolder;
//
//    @GetMapping("/oss/policy/{typeId}")
//    public R policy(@PathVariable String typeId) {
//        StpUtil.checkLogin();
////        int id = StpUtil.getLoginIdAsInt();
//        //https://gulimall-tstst.oss-cn-shanghai.aliyuncs.com/photo.png
//
//        // 填写Host地址，格式为https://bucketname.endpoint。
//        String host = "https://" + bucket + "." + endpoint;
//        // 设置上传回调URL，即回调服务器地址，用于处理应用服务器与OSS之间的通信。OSS会在文件上传完成后，把文件上传信息通过此回调URL发送给应用服务器。
////        String callbackUrl = "https://192.168.0.0:8888";
//        // 设置上传到OSS文件的前缀，可置空此项。置空后，文件将上传至Bucket的根目录下。
//        String dateFormat = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
//        String dir = null;
//        if ("0".equals(typeId)) {
//            dir = userFolder + "/" + dateFormat + "/";
//        } else if ("1".equals(typeId)) {
//            dir = petFolder + "/" + dateFormat + "/";
//        } else {
//            return R.failure("类型获取异常");
//        }
//        Map<String, String> respMap = null;
//        try {
//            long expireTime = 30;
//            long expireEndTime = System.currentTimeMillis() + expireTime * 1000;
//            Date expiration = new Date(expireEndTime);
//            PolicyConditions policyConds = new PolicyConditions();
//            policyConds.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, 1048576000);
//            policyConds.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, dir);
//
//            OSS client = new OSSClientBuilder().build(endpoint, accessId, accessKey);
//
//            String postPolicy = client.generatePostPolicy(expiration, policyConds);
//            byte[] binaryData = postPolicy.getBytes("utf-8");
//            String encodedPolicy = BinaryUtil.toBase64String(binaryData);
//            String postSignature = client.calculatePostSignature(postPolicy);
//
//            respMap = new LinkedHashMap<String, String>();
//            respMap.put("accessid", accessId);
//            respMap.put("policy", encodedPolicy);
//            respMap.put("signature", postSignature);
//            respMap.put("dir", dir);
//            respMap.put("host", host);
//            respMap.put("expire", String.valueOf(expireEndTime / 1000));
//            // respMap.put("expire", formatISO8601Date(expiration));
//        } catch (Exception e) {
//            //TODO log记录
//            // Assert.fail(e.getMessage());
//            System.out.println(e.getMessage());
//        }
//        return R.success(respMap);
//    }
//}
