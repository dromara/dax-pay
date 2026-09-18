package cn.daxpay.open.channel.easypay.client.resp;

import lombok.Data;

import java.time.OffsetDateTime;

/// # 易支付通道订单同步响应
///
/// 子应用查询易支付订单后回传给主应用。
/// tradeState 为易支付原始状态码("1"已支付/"2"已退款/其他支付中),
/// 到平台抽象态的映射由主应用完成。
@Data
public class EasyPaySyncResp {

    /// 同步原始数据(JSON 全量, 落同步记录)
    private String syncData;

    /// 商户订单号
    private String outTradeNo;

    /// 易支付交易号
    private String tradeNo;

    /// 交易状态(易支付原始码: 1=已支付 2=已退款 其他=支付中)
    private String tradeState;

    /// 订单金额(单位: 分, 成功态回填)
    private Long totalAmount;

    /// 完成时间(东八区 OffsetDateTime, endtime)
    private OffsetDateTime finishTime;

    /// 买家标识(buyer)
    private String buyerId;
}
