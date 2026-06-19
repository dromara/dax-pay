package cn.daxpay.open.payment.old.pay.entity.record.flow;

import cn.daxpay.open.platform.common.mybatisplus.function.ToResult;
import cn.daxpay.open.platform.core.enums.pay.channel.ChannelEnum;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import cn.daxpay.open.platform.core.enums.pay.trade.TradeTypeEnum;
import cn.daxpay.open.payment.common.entity.merchant.MchBaseEntity;
import cn.daxpay.open.payment.old.pay.convert.record.TradeFlowRecordConvert;
import cn.daxpay.open.payment.old.pay.result.record.flow.TradeFlowRecordResult;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/// # 交易流水记录
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("pay_trade_flow_record")
public class TradeFlowRecord extends MchBaseEntity implements ToResult<TradeFlowRecordResult> {

    /// 应用号
    @TableField(updateStrategy = FieldStrategy.NEVER, fill = FieldFill.INSERT)
    private String appId;

    /// 订单标题
    private String title;

    /// 金额
    private BigDecimal amount;

    /// 业务类型
    /// @see TradeTypeEnum
    private String type;

    /// 支付产品
    /// @see ProductEnum
    private String product;

    /// 支付通道
    /// @see ChannelEnum
    private String channel;

    /// 平台交易号
    private String tradeNo;

    /// 商户交易号
    private String bizTradeNo;

    /// 通道交易号
    private String outTradeNo;

    /// 转换
    @Override
    public TradeFlowRecordResult toResult() {
        return TradeFlowRecordConvert.CONVERT.convert(this);
    }
}

