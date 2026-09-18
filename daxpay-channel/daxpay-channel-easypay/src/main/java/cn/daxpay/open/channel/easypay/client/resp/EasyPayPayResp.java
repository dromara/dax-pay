package cn.daxpay.open.channel.easypay.client.resp;

import cn.daxpay.open.channel.easypay.client.enums.EasyPayPayBodyType;
import lombok.Data;

/// # 易支付通道支付响应
///
/// 子应用调用易支付 API 后回传给主应用。
/// 易支付统一下单为异步确认模式(扫码/跳转), 同步不返回终态, `complete` 恒为 false,
/// 最终状态由异步通知或轮询同步确认。
@Data
public class EasyPayPayResp {

    /// 商户订单号(透传 EasyPayPayReq.outTradeNo)
    private String outTradeNo;

    /// 易支付交易号(trade_no)
    private String tradeNo;

    /// 支付内容(易支付 pay_info: 跳转链接 / 二维码链接等, 形态由 payBodyType 标识)
    private String payBody;

    /// 支付内容类型(按响应 pay_type 动态判定)
    private EasyPayPayBodyType payBodyType;

    /// 是否已终态成功(易支付下单恒为 false, 等待异步通知确认)
    private Boolean complete;
}
