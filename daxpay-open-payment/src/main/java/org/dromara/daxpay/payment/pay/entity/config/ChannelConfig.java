package org.dromara.daxpay.payment.pay.entity.config;

import cn.bootx.platform.common.mybatisplus.function.ToResult;
import cn.bootx.platform.common.mybatisplus.handler.DataEncryptTypeHandler;
import cn.bootx.platform.core.annotation.BigField;
import org.dromara.daxpay.payment.merchant.common.entity.MchAppBaseEntity;
import org.dromara.daxpay.payment.merchant.convert.config.ChannelConfigConvert;
import org.dromara.daxpay.payment.merchant.result.config.ChannelConfigResult;
import org.dromara.daxpay.payment.pay.enums.ChannelEnum;
import com.baomidou.mybatisplus.annotation.TableField;
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
@TableName("pay_channel_config" )
public class ChannelConfig extends MchAppBaseEntity implements ToResult<ChannelConfigResult> {

    /**
     * 支付通道
     * @see ChannelEnum
     */
    private String channel;

    /** 通道商户号 */
    @Deprecated
    private String outMchNo;

    /** 通道APPID */
    @Deprecated
    private String outAppId;

    /** 是否启用 */
    private boolean enable;

    /** 扩展存储 */
    @BigField
    @Deprecated
    @TableField(typeHandler = DataEncryptTypeHandler.class)
    private String ext;

    @Override
    public ChannelConfigResult toResult() {
        return ChannelConfigConvert.INSTANCE.toResult(this);
    }
}
