package cn.daxpay.open.channel.douyin.client.resp;

import lombok.Data;

/// # 抖音通道支付同步响应
@Data
public class DouyinSyncResp {
    private String outTradeNo;
    /// 抖音交易号
    private String transactionId;
    /// 交易状态(SUCCESS / REFUND / NOTPAY / USERPAYING / CLOSED / PAYERROR)
    private String tradeState;
    /// 订单金额(单位: 分)
    private Long totalAmount;
    /// 买家 openid
    private String openid;
    /// 支付成功时间(RFC3339)
    private String successTime;
    /// 查询失败错误码
    private String errorCode;
    /// 查询失败错误信息
    private String errorMsg;
}
