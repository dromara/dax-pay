package cn.daxpay.open.channel.jeepay.client.resp;

import lombok.Data;

import java.time.OffsetDateTime;

/// # Jeepay通道订单同步响应(主应用侧)
///
/// tradeState 为 Jeepay 原始状态码("1"支付中/"2"成功/"3"失败/"4"撤销/"5"已退款/"6"关闭),
/// 到平台抽象态的映射由主应用完成。
@Data
public class JeepaySyncResp {

    /// 同步原始数据(JSON 全量, 落同步记录)
    private String syncData;

    /// 商户订单号(mchOrderNo)
    private String outTradeNo;

    /// Jeepay支付订单号(payOrderId)
    private String tradeNo;

    /// 渠道订单号(channelOrderNo, 微信/支付宝侧单号)
    private String channelOrderNo;

    /// 交易状态(Jeepay 原始码)
    private String tradeState;

    /// 订单金额(单位: 分, 成功态回填)
    private Long totalAmount;

    /// 完成时间(东八区 OffsetDateTime)
    private OffsetDateTime finishTime;
}
