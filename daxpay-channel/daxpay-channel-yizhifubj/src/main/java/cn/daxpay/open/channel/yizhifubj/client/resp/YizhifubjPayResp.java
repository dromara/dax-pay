package cn.daxpay.open.channel.yizhifubj.client.resp;

import cn.daxpay.open.channel.yizhifubj.client.enums.YizhifubjPayBodyType;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 一直付通道支付响应(主应用侧)
///
/// 子应用调用 5upay 网关下单后回传。下单恒为异步支付(complete=false)。
@Data
@Accessors(chain = true)
public class YizhifubjPayResp {

    /// 商户订单号(透传)
    private String outTradeNo;

    /// 支付内容(二维码链接 / 跳转 URL / JsApi 调起串)
    private String payBody;

    /// 支付内容类型
    private YizhifubjPayBodyType payBodyType;

    /// 是否已终态完成(恒 false, 等待异步通知)
    private Boolean complete;

    /// 通道流水号(serialNumber, 若下单响应携带则回传)
    private String outOrderNo;

    /// 订单总金额(单位: 分)
    private Long totalAmount;
}
