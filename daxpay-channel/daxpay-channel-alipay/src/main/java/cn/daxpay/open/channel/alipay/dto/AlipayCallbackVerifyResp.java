package cn.daxpay.open.channel.alipay.dto;

import lombok.Data;

import java.util.Map;

/// # 支付宝通道回调验签响应
///
/// 与子应用 `ChannelCallbackVerifyResp` 字段一致, 子应用完成验签与字段解析后返回。
@Data
public class AlipayCallbackVerifyResp {

    /// 是否验签通过
    private Boolean verified;

    /// 业务订单号(对应支付宝 out_trade_no)
    private String bizOrderNo;

    /// 通道订单号(支付宝 trade_no)
    private String outOrderNo;

    /// 交易状态(success/fail)
    private String status;

    /// 金额(分)
    private Long amount;

    /// 完成时间
    private String finishTime;

    /// 付款用户ID
    private String buyerId;

    /// 原始数据
    private Map<String, Object> rawData;
}
