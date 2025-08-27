package org.dromara.daxpay.channel.wechat.entity.config;

import cn.bootx.platform.common.jackson.util.JacksonUtil;
import cn.bootx.platform.common.mybatisplus.function.ToResult;
import org.dromara.daxpay.channel.wechat.code.WechatPayCode;
import org.dromara.daxpay.channel.wechat.convert.WechatIsvConfigConvert;
import org.dromara.daxpay.channel.wechat.result.config.WechatIsvConfigResult;
import org.dromara.daxpay.core.enums.ChannelEnum;
import org.dromara.daxpay.service.isv.entity.config.IsvChannelConfig;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * 微信服务商配置
 * @author xxm
 * @since 2024/11/1
 */
@Data
@Accessors(chain = true)
public class WechatIsvConfig implements ToResult<WechatIsvConfigResult> {

    /** 主键 */
    private Long id;

    /** 微信商户Id */
    private String wxMchId;

    /** 微信应用appId */
    private String wxAppId;

    /** 是否启用 */
    private Boolean enable;

    /** 支付限额 */
    private BigDecimal limitAmount;

    /**
     * 接口版本, 使用v2还是v3接口
     * @see WechatPayCode#API_V2
     */
    private String apiVersion;

    /** 商户平台「API安全」中的 APIv2 密钥 */
    private String apiKeyV2;

    /** 商户平台「API安全」中的 APIv3 密钥 */
    private String apiKeyV3;

    /** APPID对应的接口密码，用于获取微信公众号jsapi支付时使用 */
    private String appSecret;

    /** 支付公钥(pub_key.pem) */
    private String publicKey;

    /** 支付公钥ID */
    private String publicKeyId;

    /** apiclient_key. pem证书base64编码 */
    private String privateKey;

    /** apiclient_cert. pem证书base64编码 */
    private String privateCert;

    /** 证书序列号 */
    private String certSerialNo;

    /** API证书中p12证书Base64 */
    private String p12;

    /** 微信密钥 */
    private String wxAppSecret;

    /** 微信授权认证地址 */
    private String wxAuthUrl;

    /**
     * 转换为通道配置
     */
    public IsvChannelConfig toChannelConfig() {
        var channelConfig = new IsvChannelConfig();
        channelConfig.setId(this.getId());
        channelConfig.setEnable(this.getEnable());
        channelConfig.setChannel(ChannelEnum.WECHAT_ISV.getCode());
        WechatIsvConfig copy = WechatIsvConfigConvert.CONVERT.copy(this);
        // 清空不需要序列化的字段
        copy.setId(null).setEnable(null).setWxMchId(null);
        String jsonStr = JacksonUtil.toJson(copy);
        channelConfig.setExt(jsonStr);
        return channelConfig;
    }

    /**
     * 从通道配置转换为微信支付配置
     */
    public static WechatIsvConfig convertConfig(IsvChannelConfig channelConfig) {
        WechatIsvConfig config = JacksonUtil.toBean(channelConfig.getExt(), WechatIsvConfig.class);

        config.setId(channelConfig.getId())
                .setEnable(channelConfig.isEnable());
        return config;
    }

    @Override
    public WechatIsvConfigResult toResult() {
        return WechatIsvConfigConvert.CONVERT.toResult(this);
    }
}

