package org.dromara.daxpay.payment.old.pay.bo.trade;

import org.dromara.daxpay.platform.core.enums.unipay.PayBodyTypeEnum;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

/// # 支付结果业务类
///
@Data
@Accessors(chain = true)
public class PayResultBo {

    /// 第三方支付网关生成的订单号, 用与将记录关联起来
    /// 1. 如付款码支付直接成功时会出现
    /// 2. 部分通道创建订单是会直接返回
    private String outOrderNo;

    /// 是否支付完成
    private boolean complete;

    /// 实收金额
    private BigDecimal realAmount;

    /// 完成时间
    private OffsetDateTime finishTime;

    /// 支付参数体(通常用于发起支付的参数)
    private String payBody;

    /// 支付参数类型, 默认为链接类型
    private PayBodyTypeEnum payBodyType = PayBodyTypeEnum.LINK;

    /// 付款用户ID(微信支付宝)
    private String buyerId;

    /// 用户标识(通道内部)
    private String userId;

    /// 通道方支付产品
    private String tradeProduct;

    /// 通道方交易方式
    private String tradeWay;

    /// 银行卡类型
    /// 借记卡/贷记卡
    private String bankType;

    /// 透传账号
    /// 三方通道使用微信/支付宝/银联支付时产生的订单号
    private String transOrderNo;

    /// 特殊通道关联订单号
    /// 部分通道对订单号有要求, 需要使用指定前缀且有长度限制, 无法使用系统订单关联, 使用这个进行关联
    private String relationOrderNo;

    /// 参加活动类型
    private String promotionType;

}

