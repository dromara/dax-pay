package cn.daxpay.multi.channel.wechat.entity.config;

import cn.bootx.platform.common.mybatisplus.function.ToResult;
import cn.daxpay.multi.channel.wechat.code.WechatPayCode;
import cn.daxpay.multi.channel.wechat.convert.config.WechatPayConfigConvert;
import cn.daxpay.multi.channel.wechat.result.config.WechatPayConfigResult;
import cn.daxpay.multi.core.enums.ChannelEnum;
import cn.daxpay.multi.service.entity.config.ChannelConfig;
import cn.hutool.json.JSONUtil;
import lombok.Data;
import lombok.experimental.Accessors;

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

    /** apiclient_key. pem证书base64编码 */
    private String privateKey;

    /** apiclient_cert. pem证书base64编码 */
    private String privateCert;

    /** 证书序列号 */
    private String certSerialNo;

    /** API证书中p12证书Base64 */
    private String p12;

    /** 是否沙箱环境 */
    private boolean sandbox;

    /** 备注 */
    private String remark;

    /** 商户号 */
    private String mchNo;

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
        channelConfig.setMchNo(this.getMchNo());
        channelConfig.setEnable(this.getEnable());
        channelConfig.setChannel(ChannelEnum.WECHAT.getCode());
        WechatPayConfig copy = WechatPayConfigConvert.CONVERT.copy(this);
        // 清空不需要序列化的字段
        copy.setId(null).setAppId(null).setEnable(null).setWxMchId(null).setAppId(null).setAppId(null).setMchNo(null);
        String jsonStr = JSONUtil.toJsonStr(copy);
        channelConfig.setExt(jsonStr);
        return channelConfig;
    }

    /**
     * 从通道配置转换为微信支付配置
     */
    public static WechatPayConfig convertConfig(ChannelConfig channelConfig) {
        WechatPayConfig config = JSONUtil.toBean(channelConfig.getExt(), WechatPayConfig.class);

        config.setId(channelConfig.getId())
                .setWxAppId(channelConfig.getOutAppId())
                .setWxMchId(channelConfig.getOutMchNo())
                .setAppId(channelConfig.getAppId())
                .setMchNo(channelConfig.getMchNo())
                .setEnable(channelConfig.isEnable());
        return config;
    }

    @Override
    public WechatPayConfigResult toResult() {
        return WechatPayConfigConvert.CONVERT.toResult(this);
    }
}
