package cn.daxpay.open.payment.trade.runtime.bo;

import cn.daxpay.open.payment.common.enums.PayFundStatusEnum;
import cn.daxpay.open.platform.core.enums.pay.channel.PayProviderEnum;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

/// # 支付同步结果业务对象
///
/// 策略层与服务层之间传递的同步结果
@Data
@Accessors(chain = true)
public class PaySyncResultBo {

    /// 同步是否成功
    private boolean syncSuccess = true;

    /// 支付网关订单资金状态
    /// @see PayFundStatusEnum
    private PayFundStatusEnum payStatus;

    /// 通道订单号
    private String outOrderNo;

    /// 交易金额（最小货币单位）
    private Long amount;

    /// 实收金额（最小货币单位）
    private Long realAmount;

    /// 支付完成时间
    private OffsetDateTime finishTime;

    /// 同步时网关返回的对象，序列化为 json 字符串
    private String syncData;

    /// 错误提示码
    private String syncErrorCode;

    /// 错误提示
    private String syncErrorMsg;

    /// 付款用户 ID
    private String buyerId;

    /// 用户标识
    private String userId;

    /// 支付产品（三方通道所使用的支付产品或类型）
    private String tradeProduct;

    /// 交易方式
    private String tradeWay;

    /// 银行卡类型（借记卡/贷记卡）
    private String bankType;

    /// 支付渠道
    private PayProviderEnum provider;

    /// 透传订单号
    private String transOrderNo;

    /// 活动类型
    private String promotionType;

    /// 是否需要远程关闭网关交易
    /// 本地超时且通道订单仍存活(PROCESSING)时由核心层置 true, 触发主动调用通道关单; 通道策略无需设置
    private boolean remoteClose;
}
