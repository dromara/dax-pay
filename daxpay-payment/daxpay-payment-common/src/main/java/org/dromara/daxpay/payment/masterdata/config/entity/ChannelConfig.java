package org.dromara.daxpay.payment.masterdata.config.entity;

import org.dromara.daxpay.platform.common.mybatisplus.function.ToResult;
import org.dromara.daxpay.platform.core.enums.pay.channel.ChannelEnum;
import org.dromara.daxpay.payment.common.entity.merchant.MchBaseEntity;
import org.dromara.daxpay.payment.masterdata.config.convert.ChannelConfigConvert;
import org.dromara.daxpay.payment.masterdata.config.result.ChannelConfigResult;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
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
public class ChannelConfig extends MchBaseEntity implements ToResult<ChannelConfigResult> {

    /// 应用号
    @TableField(updateStrategy = FieldStrategy.NEVER, fill = FieldFill.INSERT)
    private String appId;

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

