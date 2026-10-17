package cn.daxpay.open.channel.lakala.client.resp;

import cn.daxpay.open.channel.lakala.client.enums.LakalaPayBodyType;
import lombok.Data;

/// # 拉卡拉 MOSS 支付响应(主应用侧, 与子应用镜像)
///
/// MOSS 下单均为异步收款(收银台/扫码/JSAPI), 恒 `complete=false`。
@Data
public class MossPayResp {

    /// 商户订单号(透传 MossPayReq.outTradeNo)
    private String outTradeNo;

    /// MOSS 交易号(trade_no)
    private String tradeNo;

    /// 支付内容(counter_url 收银台链接 / 二维码内容 / JSAPI调起参数JSON / prepay_id)
    private String payBody;

    /// 支付内容类型
    private LakalaPayBodyType payBodyType;

    /// 是否已终态完成(恒 false)
    private Boolean complete;
}
