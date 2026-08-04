package cn.daxpay.open.payment.masterdata.entity.channel;

import cn.daxpay.open.platform.common.mybatisplus.function.ToResult;
import cn.daxpay.open.platform.common.mybatisplus.base.MpBaseEntity;
import cn.daxpay.open.payment.masterdata.convert.channel.PayChannelConvert;
import cn.daxpay.open.payment.masterdata.result.channel.PayChannelResult;
import cn.daxpay.open.platform.core.enums.pay.channel.ChannelEnum;
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

    /// 图标
    private String icon;

    /// 转换
    @Override
    public PayChannelResult toResult() {
        return PayChannelConvert.CONVERT.toResult(this);
    }
}