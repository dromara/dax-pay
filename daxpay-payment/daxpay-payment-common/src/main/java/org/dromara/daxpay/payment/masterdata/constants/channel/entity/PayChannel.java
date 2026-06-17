package org.dromara.daxpay.payment.masterdata.constants.channel.entity;

import org.dromara.daxpay.platform.common.mybatisplus.function.ToResult;
import org.dromara.daxpay.platform.common.mybatisplus.base.MpBaseEntity;
import org.dromara.daxpay.payment.masterdata.constants.channel.convert.PayChannelConvert;
import org.dromara.daxpay.payment.masterdata.constants.channel.result.PayChannelResult;
import org.dromara.daxpay.platform.core.enums.pay.channel.ChannelEnum;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 支付通道
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("pay_md_channel")
public class PayChannel extends MpBaseEntity implements ToResult<PayChannelResult> {

    /// 通道编码
    /// @see ChannelEnum
    private String code;

    /// 排序
    private Integer sortNo;

    /// 通道介绍
    private String description;

    /// 图标
    private String icon;

    /// 转换
    @Override
    public PayChannelResult toResult() {
        return PayChannelConvert.CONVERT.toResult(this);
    }
}