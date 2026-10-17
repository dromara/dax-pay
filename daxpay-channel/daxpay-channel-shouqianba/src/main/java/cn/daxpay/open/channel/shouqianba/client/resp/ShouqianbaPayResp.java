package cn.daxpay.open.channel.shouqianba.client.resp;

import cn.daxpay.open.channel.shouqianba.client.enums.ShouqianbaPayBodyType;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 收钱吧通道支付响应(主应用侧)
///
/// 子应用调用收钱吧预下单/WAP 网关后回传。
/// 扫码与 WAP 收银台均为异步支付(complete=false), 等待异步通知确认最终状态。
@Data
@Accessors(chain = true)
public class ShouqianbaPayResp {

    /// 商户订单号(透传)
    private String outTradeNo;

    /// 支付内容(扫码=二维码内容; WAP=收银台跳转链接)
    private String payBody;

    /// 支付内容类型(扫码 QR_CODE / WAP LINK)
    private ShouqianbaPayBodyType payBodyType;

    /// 是否已终态完成(下单恒 false)
    private Boolean complete;

    /// 订单总金额(单位: 分)
    private Long totalAmount;
}
