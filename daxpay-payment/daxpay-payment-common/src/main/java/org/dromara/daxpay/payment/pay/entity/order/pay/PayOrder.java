package org.dromara.daxpay.payment.pay.entity.order.pay;

import org.dromara.daxpay.platform.common.mybatisplus.function.ToResult;
import org.dromara.daxpay.payment.common.entity.merchant.MchAppBaseEntity;
import org.dromara.daxpay.payment.pay.convert.order.pay.PayOrderConvert;
import org.dromara.daxpay.platform.core.enums.pay.channel.*;
import org.dromara.daxpay.platform.core.enums.pay.pay.*;
import org.dromara.daxpay.platform.core.enums.pay.trade.*;
import org.dromara.daxpay.payment.pay.result.order.pay.PayOrderVo;
import org.dromara.daxpay.platform.core.enums.unipay.PayLimitPayEnum;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Objects;

/// # 支付订单
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("pay_order")
public class PayOrder extends MchAppBaseEntity implements ToResult<PayOrderVo> {

    /// 商户订单号
    private String bizOrderNo;

    /// 订单号
    private String orderNo;

    /// 通道订单号
    private String outOrderNo;

    /// 特殊通道关联订单号
    /// 部分通道对订单号有要求, 需要使用指定前缀且有长度限制, 无法使用系统订单关联, 使用这个进行关联
    private String relationOrderNo;

    /// 透传订单号
    /// 三方通道使用微信/支付宝/银联支付时产生的订单号
    private String transOrderNo;

    /// 标题
    private String title;

    /// 描述
    private String description;

    /// 是否支持分账
    private Boolean allocation;

    /// 自动分账
    private Boolean autoAllocation;

    /// 支付产品
    /// @see ProductEnum
    private String product;

    /// 支付通道
    /// @see ChannelEnum
    private String channel;

    /// 支付方式
    /// @see PayMethodEnum
    private String method;

    /// 其他支付方式, 只有在 支付方式编码(method) 为 其他支付(other)时才会生效
    /// 用于处理各种通道特殊的支付方式
    private String otherMethod;

    /// 限制用户支付类型, 目前支持限制信用卡
    /// @see PayLimitPayEnum
    private String limitPay;

    /// 金额(元)
    private BigDecimal amount;

    /// 可退金额(元)
    private BigDecimal refundableBalance;

    /// 支付状态
    /// @see PayStatusEnum
    private String status;

    /// 退款状态
    /// @see PayRefundStatusEnum
    private String refundStatus;

    /// 分账状态
    /// @see PayAllocStatusEnum
    private String allocStatus;

    /// 结算状态, 为空不需要进行结算
    /// @see SettleStatusEnum
    private String settleStatus;

    /// 支付渠道 微信/支付宝/银联
    /// @see PayProviderEnum
    private String provider;

    /// 过期时间
    private OffsetDateTime expiredTime;

    /// 支付成功时间
    private OffsetDateTime payTime;

    /// 关闭时间
    private OffsetDateTime closeTime;

    /// 订单来源
    /// @see TradeSourceEnum
    private String source;

    /// 错误信息
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String errorMsg;

    public PayOrder setErrorMsg(String errorMsg) {
        this.errorMsg = StrUtil.sub(errorMsg,0,300);
        return this;
    }

    public boolean getAllocation() {
        return Objects.equals(true, allocation);
    }

    public boolean getAutoAllocation() {
        return Objects.equals(true, autoAllocation);
    }

    @Override
    public PayOrderVo toResult() {
        return PayOrderConvert.CONVERT.toVo(this);
    }
}

