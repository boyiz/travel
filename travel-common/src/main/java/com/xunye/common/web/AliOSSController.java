package com.xunye.common.web;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import cn.dev33.satoken.stp.StpUtil;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.MatchMode;
import com.aliyun.oss.model.PolicyConditions;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.auth.sts.AssumeRoleRequest;
import com.aliyuncs.auth.sts.AssumeRoleResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.xunye.core.result.R;
import com.xunye.core.tools.CheckTools;
import com.xunye.common.param.AliOssParams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/common/oss")
public class AliOSSController {

    @Autowired
    private AliOssParams aliOssParams;

    /**
     * 后续基于此进行权限配置
     * 详情参阅：https://help.aliyun.com/document_detail/93739.html?spm=a2c8b.12215508.help.dexternal.ff46336aNSJIIz
     */
    String policy = "{\n" +
        "    \"Version\": \"1\",\n" +
        "    \"Statement\": [\n" +
        "        {\n" +
        "            \"Effect\": \"Allow\",\n" +
        "            \"Action\": \"oss:*\",\n" +
        "            \"Resource\": [\n" +
        "                \"acs:oss:*:*:sahuan\",\n" +
        "                \"acs:oss:*:*:sahuan/*\"\n" +
        "            ]\n" +
        "        }\n" +
        "    ]\n" +
        "}";

    /**
     * 创建访问Token
     *
     * @return
     */
    @GetMapping("/stsToken")
    public R<AssumeRoleResponse.Credentials> createToken() {
        try {
            /* 创建client */
            DefaultProfile.addEndpoint("", "", "Sts",
                aliOssParams.getEndpoint());
            IClientProfile profile = DefaultProfile.getProfile("cn-shanghai",
                aliOssParams.getAccessKeyId(), aliOssParams.getAccessKeySecret());
            DefaultAcsClient client = new DefaultAcsClient(profile);

            /* 构造请求 */
            final AssumeRoleRequest request = new AssumeRoleRequest();
            request.setMethod(MethodType.POST);
            request.setRoleArn(aliOssParams.getRole());
            request.setRoleSessionName(aliOssParams.getSessionName());
            request.setPolicy(policy);
            request.setDurationSeconds(
                CheckTools.isNotNullOrEmpty(aliOssParams.getDuration())
                    ? aliOssParams.getDuration()
                    : 3600L
            );

            /* 向OSS服务发起请求获取Token */
            final AssumeRoleResponse response = client.getAcsResponse(request);
            return R.success(response.getCredentials());
        } catch (ClientException e) {
            e.printStackTrace();
            return R.failure(e.getErrMsg());
        }
    }

    @GetMapping("/apiToken")
    public R<Map<String, String>> policy() {
        //StpUtil.checkLogin();
        //        int id = StpUtil.getLoginIdAsInt();

        // 填写Host地址，格式为https://bucketname.endpoint。
        String host = "https://" + aliOssParams.getBucket() + "." + aliOssParams.getEndpoint();
        // 设置上传回调URL，即回调服务器地址，用于处理应用服务器与OSS之间的通信。OSS会在文件上传完成后，把文件上传信息通过此回调URL发送给应用服务器。
        //        String callbackUrl = "https://192.168.0.0:8888";
        // 设置上传到OSS文件的前缀，可置空此项。置空后，文件将上传至Bucket的根目录下。
        String dateFormat = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String dir = null;
        dir = dateFormat + "/";
        //if ("0".equals(typeId)) {
        //    dir = userFolder + "/" + dateFormat + "/";
        //} else if ("1".equals(typeId)) {
        //    dir = petFolder + "/" + dateFormat + "/";
        //} else {
        //    return R.failure("类型获取异常");
        //}
        Map<String, String> respMap = null;
        try {
            long expireTime = 30;
            long expireEndTime = System.currentTimeMillis() + expireTime * 1000;
            Date expiration = new Date(expireEndTime);
            PolicyConditions policyConds = new PolicyConditions();
            policyConds.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, 1048576000);
            policyConds.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, dir);

            OSS client = new OSSClientBuilder().build(aliOssParams.getEndpoint(), aliOssParams.getAccessKeyId(),
                aliOssParams.getAccessKeySecret());

            String postPolicy = client.generatePostPolicy(expiration, policyConds);
            byte[] binaryData = postPolicy.getBytes("utf-8");
            String encodedPolicy = BinaryUtil.toBase64String(binaryData);
            String postSignature = client.calculatePostSignature(postPolicy);

            respMap = new LinkedHashMap<String, String>();
            respMap.put("accessid", aliOssParams.getAccessKeyId());
            respMap.put("policy", encodedPolicy);
            respMap.put("signature", postSignature);
            respMap.put("dir", dir);
            respMap.put("host", host);
            respMap.put("expire", String.valueOf(expireEndTime / 1000));
            // respMap.put("expire", formatISO8601Date(expiration));
        } catch (Exception e) {
            //TODO log记录
            // Assert.fail(e.getMessage());
            System.out.println(e.getMessage());
        }
        return R.success(respMap);
    }

}

