package org.dromara.daxpay.channel.alipay.entity.config;

import cn.bootx.platform.common.jackson.util.JacksonUtil;
import cn.bootx.platform.common.mybatisplus.function.ToResult;
import lombok.Data;
import lombok.experimental.Accessors;
import org.dromara.daxpay.channel.alipay.convert.AlipaySubConfigConvert;
import org.dromara.daxpay.channel.alipay.result.config.AlipaySubConfigResult;
import org.dromara.daxpay.core.enums.ChannelEnum;
import org.dromara.daxpay.service.pay.entity.config.ChannelConfig;

/**
 * 支付宝特约商户配置
 * @author xxm
 * @since 2024/6/25
 */
@Data
@Accessors(chain = true)
public class AlipaySubConfig implements ToResult<AlipaySubConfigResult> {

    /** 主键 */
    private Long id;

    /** 支付宝特约商户Token */
    private String appAuthToken;

    /** 是否启用 */
    private Boolean enable;

    /** 商户号 */
    private String mchNo;

    /** 商户AppId */
    private String appId;

    /**
     * 支付宝商家唯一识别码
     * 是商家与支付宝签约后，商家获得的支付宝商家唯一识别码，以 2088 开头的 16 位数字组成，在开放平台中账户中心获取
     */
    private String alipayUserId;

    /**
     * 转换为通道配置
     */
    public ChannelConfig toChannelConfig() {
        ChannelConfig channelConfig = new ChannelConfig();
        channelConfig.setId(this.getId());
        channelConfig.setAppId(this.getAppId());
        channelConfig.setMchNo(this.getMchNo());
        channelConfig.setEnable(this.getEnable());
        channelConfig.setSub(true);
        channelConfig.setChannel(ChannelEnum.ALIPAY_ISV.getCode());
        AlipaySubConfig copy = AlipaySubConfigConvert.CONVERT.copy(this);
        // 清空不需要序列化的字段
        copy.setId(null).setMchNo(null).setAppId(null).setEnable(null);
        String jsonStr = JacksonUtil.toJson(copy);
        channelConfig.setExt(jsonStr);
        channelConfig.setSub(true);
        return channelConfig;
    }

    /**
     * 从通道配置转换为支付宝配置
     */
    public static AlipaySubConfig convertConfig(ChannelConfig channelConfig) {
        AlipaySubConfig config = JacksonUtil.toBean(channelConfig.getExt(), AlipaySubConfig.class);
        config.setId(channelConfig.getId())
                .setAppId(channelConfig.getAppId())
                .setMchNo(channelConfig.getMchNo())
                .setEnable(channelConfig.isEnable());
        return config;
    }

    @Override
    public AlipaySubConfigResult toResult() {
        return AlipaySubConfigConvert.CONVERT.toResult(this);
    }
}
