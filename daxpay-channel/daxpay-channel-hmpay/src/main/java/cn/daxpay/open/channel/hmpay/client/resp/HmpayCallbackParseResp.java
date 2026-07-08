package cn.daxpay.open.channel.hmpay.client.resp;

import lombok.Data;

import java.time.OffsetDateTime;

/// # 河马付通道回调验签解析响应(主应用侧)
@Data
public class HmpayCallbackParseResp {

    /// 验签 + 解析是否成功
    private Boolean success;

    /// 回调类型(PAY / REFUND)
    private String tradeType;

    /// 商户单号
    private String outTradeNo;

    /// 杉德流水号(plat_trx_no)
    private String tradeNo;

    /// 交易状态(SUCCESS)
    private String tradeStatus;

    /// 金额(单位: 分)
    private Long amount;

    /// 完成时间
    private OffsetDateTime finishTime;

    /// 支付方式(仅支付回调: pay_way_code)
    private String tradeWay;

    /// 买家标识(仅支付回调)
    private String buyerId;

    /// 通道外部交易号(仅支付回调)
    private String outTransNo;

    /// 错误描述
    private String errorMsg;

    /// 原始回调报文
    private String syncData;
}
