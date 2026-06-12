package org.dromara.daxpay.payment.pay.entity.config;

import org.dromara.daxpay.platform.common.mybatisplus.function.ToResult;
import org.dromara.daxpay.platform.core.enums.pay.channel.ChannelEnum;
import org.dromara.daxpay.payment.common.entity.merchant.MchAppBaseEntity;
import org.dromara.daxpay.payment.pay.convert.config.ChannelConfigConvert;
import org.dromara.daxpay.payment.pay.result.config.ChannelConfigResult;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 通道支付配置
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("pay_channel_config" )
public class ChannelConfig extends MchAppBaseEntity implements ToResult<ChannelConfigResult> {

    /// 支付通道
    /// @see ChannelEnum
    private String channel;

    /// 是否启用
    private boolean enable;

    @Override
    public ChannelConfigResult toResult() {
        return ChannelConfigConvert.INSTANCE.toResult(this);
    }
}

