package cn.daxpay.open.payment.pay.bo;

import cn.daxpay.open.platform.core.enums.unipay.PayBodyTypeEnum;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

/// # 支付结果业务对象
///
/// 策略层与服务层之间传递的支付结果
@Data
@Accessors(chain = true)
public class PayTradeResultBo {

    /// 通道订单号（三方网关返回）
    private String outOrderNo;

    /// 是否支付完成
    private boolean complete;

    /// 实收金额（最小货币单位）
    private Long realAmount;

    /// 完成时间
    private OffsetDateTime finishTime;

    /// 支付参数体（用于发起支付的参数）
    private String payBody;

    /// 支付参数类型，默认为链接类型
    private PayBodyTypeEnum payBodyType = PayBodyTypeEnum.LINK;

    /// 付款用户 ID（微信支付宝）
    private String buyerId;

    /// 用户标识（通道内部）
    private String userId;

    /// 通道方支付产品
    private String tradeProduct;

    /// 通道方交易方式
    private String tradeWay;

    /// 银行卡类型（借记卡/贷记卡）
    private String bankType;

    /// 透传订单号（三方通道产生的订单号）
    private String transOrderNo;

    /// 特殊通道关联订单号
    private String relationOrderNo;

    /// 活动类型
    private String promotionType;

    /// 订单总金额(通道返回, 不入库, 仅用于 API 响应展示)
    private Long totalAmount;

    /// 买家实付金额(通道返回, 不入库)
    private Long buyerPayAmount;

    /// 买家登录账号(支付宝手机号/邮箱)
    private String buyerLogonId;
}
