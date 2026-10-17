package cn.daxpay.open.channel.umfpay.client.resp;

import cn.daxpay.open.channel.umfpay.client.enums.UmfpayPayBodyType;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 联动优势通道支付响应(主应用侧)
///
/// 子应用调用联动优势老网关后回传, 下单恒异步(complete=false)等待通知确认。
@Data
@Accessors(chain = true)
public class UmfpayPayResp {

    /// 商户订单号(透传 UmfpayPayReq.outTradeNo)
    private String outTradeNo;

    /// 支付内容(扫码族为二维码链接; 公众号为 GET 跳转链接)
    private String payBody;

    /// 支付内容类型(扫码族 QR_CODE / 公众号 LINK)
    private UmfpayPayBodyType payBodyType;

    /// 是否已终态完成(联动优势下单恒 false)
    private Boolean complete;

    /// 订单总金额(单位: 分)
    private Long totalAmount;
}
