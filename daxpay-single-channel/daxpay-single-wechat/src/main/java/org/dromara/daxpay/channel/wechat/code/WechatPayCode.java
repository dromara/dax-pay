package org.dromara.daxpay.channel.wechat.code;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 微信参数
 *
 * @author xxm
 * @since 2021/6/21
 */
public interface WechatPayCode {

    // 版本
    String API_V2 = "apiV2";

    String API_V3 = "apiV3";


    /**
     * User-Agent头部值格式：WechatPay-Java/版本 操作系统/版本 Java/版本 Credential/Credential信息 Validator/Validator信息
     * HttpClient信息 示例： WechatPay-Java/0.0.1 (Linux/3.10.0-957.el7.x86_64) Java/1.8.0_222
     * Crendetial/WechatPay2Crendetial Validator/WechatPay2Validator okhttp3/4.9.3
     */
    String USER_AGENT_FORMAT =
            "WechatPay-Java/%s (%s) Java/%s Credential/%s Validator/%s %s";

    String OS =
            System.getProperty("os.name") + "/" + System.getProperty("os.version");
    String VERSION = System.getProperty("java.version");
    String AUTHORIZATION = "Authorization";
    String REQUEST_ID = "Request-ID";
    String WECHAT_PAY_SERIAL = "Wechatpay-Serial";
    String WECHAT_PAY_SIGNATURE = "Wechatpay-Signature";
    String WECHAT_PAY_TIMESTAMP = "Wechatpay-Timestamp";
    String WECHAT_PAY_NONCE = "Wechatpay-Nonce";
    String USER_AGENT = "User-Agent";
    String ACCEPT = "Accept";
    String CONTENT_TYPE = "Content-Type";

    List<String> PRIMARY_API_DOMAIN =
            List.of("api.mch.weixin.qq.com", "api.wechatpay.cn");
    String SECONDARY_API_DOMAIN = "api2.wechatpay.cn";

}
