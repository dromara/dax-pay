package cn.daxpay.open.channel.baofu.client.resp;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

/// # 宝付通道订单同步响应(主应用侧)
///
/// tradeState 为宝付原始交易状态(txnState: SUCCESS 成功, 其他处理中/未支付)。
@Data
@Accessors(chain = true)
public class BaofuSyncResp {

    /// 商户订单号(透传)
    private String outTradeNo;

    /// 宝付交易号(tradeNo)
    private String outOrderNo;

    /// 宝付交易状态(txnState 原始码)
    private String tradeState;

    /// 用户实付金额(succAmt, 单位: 分, 成功时返回)
    private Long realAmount;

    /// 完成时间(finishTime, 成功时返回)
    private OffsetDateTime finishTime;

    /// 买家标识(支付宝 buyer_id, 成功时返回)
    private String buyerId;

    /// 渠道外部交易号(微信 transaction_id / 支付宝 trade_no / 云闪付 voucherNum, 成功时返回)
    private String outTransNo;

    /// 渠道侧请求流水号(reqChlNo)
    private String reqChlNo;

    /// 原始响应明文(用于对账/排障)
    private String syncData;
}
