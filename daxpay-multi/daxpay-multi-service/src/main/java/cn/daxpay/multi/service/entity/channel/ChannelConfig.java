package cn.daxpay.multi.service.entity.channel;

import cn.bootx.platform.common.mybatisplus.base.MpBaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 通道支付配置
 * @author xxm
 * @since 2024/5/27
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("pay_channel_config")
public class ChannelConfig extends MpBaseEntity {

    /** 支付通道 */
    private String channel;

    /** 商户号 */
    private String mchNo;

    /** APPID */
    private String appId;

    /** 通道商户号 */
    private String outMchNo;

    /** 通道APPID */
    private String outAppId;

    /** 签名类型 */
    private String signType;



}
