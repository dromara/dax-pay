package cn.daxpay.multi.service.entity.channel;

import cn.daxpay.multi.service.code.PayChannelEnum;
import cn.daxpay.multi.service.common.entity.MchEntity;
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
public class ChannelConfig extends MchEntity {

    /**
     * 支付通道
     * @see PayChannelEnum
     */
    private String channel;

    /** 通道商户号 */
    private String outMchNo;

    /** 通道APPID */
    private String outAppId;

    /** 是否启用 */
    private boolean enable;

    /** 签名类型 */
    private String signType;

    /** 扩展存储 */
    private String ext;

}
