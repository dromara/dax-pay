package cn.daxpay.open.channel.mock.client.resp;

import cn.daxpay.open.channel.mock.client.enums.MockPayBodyType;
import lombok.Data;

import java.time.OffsetDateTime;

/// # Mock 通道支付响应
///
/// 与通道子应用 one 侧 `cn.daxpay.open.channel.mock.resp.MockPayResp` 镜像, 字段对齐;
/// 金额单位恒为分(Long)。
@Data
public class MockPayResp {

    /// 商户订单号(透传 Req.outTradeNo)
    private String outTradeNo;

    /// Mock 通道交易号(MOCK 前缀)
    private String tradeNo;

    /// 支付内容(qr=收款确认页地址; bar=空)
    private String payBody;

    /// 支付内容类型
    private MockPayBodyType payBodyType;

    /// 是否已终态完成(qr 恒 false; bar 按场景引擎即时判定, 仅 SUCCESS 为 true)
    private Boolean complete;

    /// 完成时间(bar 即时成功时返回)
    private OffsetDateTime finishTime;

    /// 订单金额(单位: 分, bar 即时成功时返回)
    private Long amount;
}
