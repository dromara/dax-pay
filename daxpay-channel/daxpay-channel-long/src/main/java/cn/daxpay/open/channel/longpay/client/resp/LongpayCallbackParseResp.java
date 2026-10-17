package cn.daxpay.open.channel.longpay.client.resp;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

/// # 建行龙支付回调验签解析响应(子应用 → 主应用)
@Data
@Accessors(chain = true)
public class LongpayCallbackParseResp {

    /// 验签是否通过 + 解析是否成功
    private Boolean success;

    /// 商户侧单号(ORDERID = 支付时上送的平台支付交易号)
    private String outTradeNo;

    /// 交易状态(SUCCESS 字段原始值: Y 成功 / N 失败)
    private String tradeStatus;

    /// 订单金额(单位: 分; PAYMENT 字段元 → 分换算)
    private Long amount;

    /// 入账完成时间(ACCDATE, 东八区)
    private OffsetDateTime finishTime;

    /// 底层渠道标识(ACC_TYPE: AL 支付宝 / WX 微信 / 其他为建行/银联跨行)
    private String accType;

    /// 买家标识(OPENID / SUB_OPENID)
    private String buyerId;

    /// 通道外部交易号(INSTALLNUM 建行流水号)
    private String outTransNo;

    /// 回给建行的纯文本应答(SUCCESS / FAIL)
    private String ackBody;

    /// 错误描述(验签失败或解析异常时填充)
    private String errorMsg;

    /// 原始回调报文(用于对账/排障)
    private String syncData;
}
