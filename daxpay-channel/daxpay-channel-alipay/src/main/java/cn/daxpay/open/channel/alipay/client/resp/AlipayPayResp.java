package cn.daxpay.open.channel.alipay.client.resp;

import cn.daxpay.open.channel.alipay.client.enums.AlipayPayBodyType;
import lombok.Data;

import java.time.OffsetDateTime;

/// # 支付宝通道支付响应
///
/// 与子应用 dax-pay-channel-one 的 `AlipayPayResp` 镜像, 字段对齐。
@Data
public class AlipayPayResp {
    /// 商户订单号(透传 Req.outTradeNo)
    private String outTradeNo;
    /// 支付宝交易号(trade_no)
    private String tradeNo;
    /// 支付内容
    private String payBody;
    /// 支付内容类型
    private AlipayPayBodyType payBodyType;
    /// 是否已终态完成(BARCODE 付款码可能同步成功)
    private Boolean complete;
    /// 完成时间
    private OffsetDateTime finishTime;
    /// 实付金额(单位: 分, BARCODE 同步成功时返回)
    private Long realAmount;
    /// 买家标识(BARCODE 同步成功时返回买家 openid)
    private String buyerId;
}
