package org.dromara.daxpay.channel.wechat.entity.config;

import cn.bootx.platform.common.jackson.util.JacksonUtil;
import cn.bootx.platform.common.mybatisplus.function.ToResult;
import org.dromara.daxpay.channel.wechat.convert.WechatPaySubConfigConvert;
import org.dromara.daxpay.channel.wechat.enums.WechatAuthTypeEnum;
import org.dromara.daxpay.channel.wechat.result.config.WechatPaySubConfigResult;
import org.dromara.daxpay.core.enums.ChannelEnum;
import org.dromara.daxpay.service.pay.entity.config.ChannelConfig;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Objects;

/**
 * 微信特约商户配置类
 * @author xxm
 * @since 2024/12/27
 */
@Data
@Accessors(chain = true)
public class WechatPaySubConfig implements ToResult<WechatPaySubConfigResult> {
    /** 主键 */
    private Long id;

    /** 微信特约商户号/二级商户号 */
    private String subMchId;

    /** 是否启用 */
    private Boolean enable;

    /**
     * 授权类型
     * @see WechatAuthTypeEnum
     */
    private String authType;

    /** 微信特约商户/二级商户AppId */
    private String subAppId;

    /** 微信特约商户/二级商户密钥 */
    private String wxAppSecret;

    /** 微信特约商户/二级商户授权认证地址 */
    private String wxAuthUrl;

    /** 商户号 */
    private String mchNo;

    /** 商户AppId */
    private String appId;


    /**
     * 默认使用服务商配置
     */
    public String getAuthType() {
        return Objects.equals(authType, WechatAuthTypeEnum.SUB.getCode())?
                WechatAuthTypeEnum.SUB.getCode():WechatAuthTypeEnum.SP.getCode();
    }


    /**
     * 转换为通道配置
     */
    public ChannelConfig toChannelConfig() {
        var channelConfig = new ChannelConfig();
        channelConfig.setId(this.getId());
        channelConfig.setOutMchNo(this.getSubMchId());
        channelConfig.setOutAppId(this.getSubAppId());
        channelConfig.setAppId(this.getAppId());
        channelConfig.setMchNo(this.getMchNo());
        channelConfig.setEnable(this.getEnable());
        channelConfig.setSub(true);
        channelConfig.setChannel(ChannelEnum.WECHAT_ISV.getCode());
        var copy = WechatPaySubConfigConvert.CONVERT.copy(this);
        // 清空不需要序列化的字段
        copy.setId(null).setMchNo(null).setAppId(null).setEnable(null).setSubMchId(null);
        String jsonStr = JacksonUtil.toJson(copy);
        channelConfig.setExt(jsonStr);
        channelConfig.setSub(true);
        return channelConfig;
    }

    /**
     * 从通道配置转换为支付配置
     */
    public static WechatPaySubConfig convertConfig(ChannelConfig channelConfig) {
        var config = JacksonUtil.toBean(channelConfig.getExt(), WechatPaySubConfig.class);
        config.setId(channelConfig.getId())
                .setAppId(channelConfig.getAppId())
                .setMchNo(channelConfig.getMchNo())
                .setSubAppId(channelConfig.getOutAppId())
                .setSubMchId(channelConfig.getOutMchNo())
                .setEnable(channelConfig.isEnable());
        return config;
    }

    @Override
    public WechatPaySubConfigResult toResult() {
        return WechatPaySubConfigConvert.CONVERT.toResult(this);
    }
}
