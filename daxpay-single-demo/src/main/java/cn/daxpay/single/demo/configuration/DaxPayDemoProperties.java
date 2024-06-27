package cn.daxpay.single.demo.configuration;

import cn.daxpay.single.sdk.code.SignTypeEnum;
import cn.hutool.core.util.StrUtil;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 演示模块配置类
 * @author xxm
 * @since 2024/2/8
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "dax-pay.demo")
public class DaxPayDemoProperties {
    /** 服务地址 */
    private String serverUrl;

    /** 微信授权回调地址 */
    private String wxRedirectUrl;

    /** 前端地址(h5) */
    private String frontH5Url;

    /** 签名方式 */
    private SignTypeEnum signType = SignTypeEnum.MD5;

    /** 签名秘钥 */
    private String signSecret;

    /** 请求超时时间 */
    private int reqTimeout = 30000;

    public String getServerUrl() {
        return StrUtil.removeSuffix(serverUrl, "/");
    }

    public String getWxRedirectUrl() {
        return StrUtil.removeSuffix(wxRedirectUrl, "/");
    }

    public String getFrontH5Url() {
        return StrUtil.removeSuffix(frontH5Url, "/");
    }
}
