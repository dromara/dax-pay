package cn.daxpay.multi.channel.alipay.entity;

import cn.bootx.platform.common.mybatisplus.function.ToResult;
import cn.daxpay.multi.channel.alipay.code.AliPayCode;
import cn.daxpay.multi.channel.alipay.convert.AlipayConfigConvert;
import cn.daxpay.multi.channel.alipay.result.config.AlipayConfigResult;
import cn.daxpay.multi.core.enums.ChannelEnum;
import cn.daxpay.multi.service.entity.channel.ChannelConfig;
import cn.hutool.json.JSONUtil;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * 支付宝配置
 * @author xxm
 * @since 2024/6/25
 */
@Data
@Accessors(chain = true)
public class AliPayConfig implements ToResult<AlipayConfigResult> {

    /** 主键 */
    private Long id;

    /** 支付宝商户appId */
    private String outAppId;

    /** 是否启用, 只影响支付和退款操作 */
    private Boolean enable;

    /** 支付限额 */
    private BigDecimal limitAmount;

    /**
     * 服务器异步通知页面路径, 需要填写本网关服务的地址, 不可以直接填写业务系统的地址
     * 1. 需http://或者https://格式的完整路径，
     * 2. 不能加?id=123这类自定义参数，必须外网可以正常访问
     * 3. 调用顺序 支付宝网关 -> 本网关进行处理 -> 发送消息通知业务系统
     */
    private String notifyUrl;

    /**
     * 服务器同步通知页面路径, 需要填写本网关服务的地址, 不可以直接填写业务系统的地址
     * 1. 需http://或者https://格式的完整路径，
     * 2. 不能加?id=123这类自定义参数，必须外网可以正常访问
     * 3. 消息顺序 支付宝网关 -> 本网关进行处理 -> 重定向到业务系统中
     */
    private String returnUrl;

    /** 支付网关地址 */
    private String serverUrl;

    /** 授权回调地址 */
    private String redirectUrl;

    /**
     * 认证类型 证书/公钥
     * @see AliPayCode#AUTH_TYPE_KEY
     */
    private String authType;

    /** 签名类型 RSA2 */
    public String signType;

    /**
     * 是商家与支付宝签约后，商家获得的支付宝商家唯一识别码，以 2088 开头的 16 位数字组成，在开放平台中账户中心获取
     */
    private String alipayUserId;

    /** 支付宝公钥 */
    public String alipayPublicKey;

    /** 私钥 */
    private String privateKey;

    /** 应用公钥证书 */
    private String appCert;

    /** 支付宝公钥证书 */
    private String alipayCert;

    /** 支付宝CA根证书 */
    private String alipayRootCert;

    /** 是否沙箱环境 */
    private boolean sandbox;

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
        channelConfig.setOutAppId(this.getOutAppId());
        channelConfig.setAppId(this.getAppId());
        channelConfig.setMchNo(this.getMchNo());
        channelConfig.setEnable(this.getEnable());
        channelConfig.setChannel(ChannelEnum.ALI.getCode());
        AliPayConfig copy = AlipayConfigConvert.CONVERT.copy(this);
        // 清空不需要序列化的字段
        copy.setId(null).setAppId(null).setEnable(null).setOutAppId(null).setAppId(null).setMchNo(null);
        String jsonStr = JSONUtil.toJsonStr(copy);
        channelConfig.setExt(jsonStr);
        return channelConfig;
    }

    /**
     * 从通道配置转换为支付宝配置
     */
    public static AliPayConfig convertConfig(ChannelConfig channelConfig) {
        AliPayConfig config = JSONUtil.toBean(channelConfig.getExt(), AliPayConfig.class);

        config.setId(channelConfig.getId())
                .setAppId(channelConfig.getOutAppId())
                .setEnable(channelConfig.isEnable());
        return config;
    }

    @Override
    public AlipayConfigResult toResult() {
        return AlipayConfigConvert.CONVERT.toResult(this);
    }
}
