package cn.daxpay.open.channel.kuaiqian.client.req;

import cn.daxpay.open.channel.kuaiqian.client.credential.KuaiqianSdkCredential;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/// # 快钱通道订单同步请求(主应用侧)
///
/// 走 MAS 网关 A7006 当面付订单查询(按快钱订单控制号 idOrderCtrl)。
@Data
public class KuaiqianSyncReq {

    /// 通道调用凭证
    @NotNull(message = "{validation.field.credential.notNull}")
    private KuaiqianSdkCredential credential;

    /// 商户订单号(主应用支付交易号)
    @NotBlank(message = "{validation.field.outTradeNo.notBlank}")
    private String outTradeNo;

    /// 快钱订单控制号(idOrderCtrl, 下单响应/回调获得)
    @NotBlank(message = "{validation.field.tradeNo.notBlank}")
    private String outOrderNo;
}
