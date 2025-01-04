package org.dromara.daxpay.channel.wechat.entity.config;

import cn.bootx.platform.common.mybatisplus.function.ToResult;
import cn.bootx.platform.core.util.JsonUtil;
import cn.hutool.core.util.StrUtil;
import lombok.Data;
import lombok.experimental.Accessors;
import org.dromara.daxpay.channel.wechat.code.WechatPayCode;
import org.dromara.daxpay.channel.wechat.convert.config.WechatPayConfigConvert;
import org.dromara.daxpay.channel.wechat.result.config.WechatPayConfigResult;
import org.dromara.daxpay.core.enums.ChannelEnum;
import org.dromara.daxpay.service.entity.config.ChannelConfig;

import java.math.BigDecimal;

/**
 * 微信支付配置
 *
 * @author xxm
 * @since 2021/3/1
 */
@Data
@Accessors(chain = true)
public class WechatPayConfig implements ToResult<WechatPayConfigResult> {

    /** 主键 */
    private Long id;

    /** 微信商户Id */
    private String wxMchId;

    /** 微信应用appId */
    private String wxAppId;

    /** 子商户号 */
    private String subMchId;

    /** 子应用号 */
    private String subAppId;

    /** 是否启用 */
    private Boolean enable;

    /** 授权认证地址 */
    private String authUrl;

    /** 是否为ISV商户(特约商户) */
    private boolean isv;

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

    /** 商户API证书(apiclient_cert.pem)base64编码 */
    private String privateCert;

    /** 商户API证书私钥(apiclient_key.pem)证书base64编码 */
    private String privateKey;

    /** 商户API证书序列号 */
    private String certSerialNo;

    /** p12证书Base64 */
    private String p12;

    /** 备注 */
    private String remark;

    /** 商户AppId */
    private String appId;

    /**
     * 转换为通道配置
     */
    public ChannelConfig toChannelConfig() {
        ChannelConfig channelConfig = new ChannelConfig();
        channelConfig.setId(this.getId());
        channelConfig.setOutAppId(this.getWxAppId());
        channelConfig.setOutMchNo(this.getWxMchId());
        channelConfig.setAppId(this.getAppId());
        channelConfig.setEnable(this.getEnable());
        channelConfig.setChannel(this.isv? ChannelEnum.WECHAT_ISV.getCode():ChannelEnum.WECHAT.getCode());
        WechatPayConfig copy = WechatPayConfigConvert.CONVERT.copy(this);
        // 清空不需要序列化的字段
        copy.setId(null).setAppId(null).setEnable(null).setWxMchId(null).setWxAppId(null);
        String jsonStr = JsonUtil.toJsonStr(copy);
        channelConfig.setExt(jsonStr);
        return channelConfig;
    }

    /**
     * 从通道配置转换为微信支付配置
     */
    public static WechatPayConfig convertConfig(ChannelConfig channelConfig) {
        WechatPayConfig config = JsonUtil.toBean(channelConfig.getExt(), WechatPayConfig.class);

        config.setId(channelConfig.getId())
                .setWxAppId(channelConfig.getOutAppId())
                .setWxMchId(channelConfig.getOutMchNo())
                .setAppId(channelConfig.getAppId())
                .setEnable(channelConfig.isEnable());
        return config;
    }


    public String getAuthUrl() {
        return StrUtil.removeSuffix(authUrl, "/");
    }

    @Override
    public WechatPayConfigResult toResult() {
        return WechatPayConfigConvert.CONVERT.toResult(this);
    }
}
