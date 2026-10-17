package cn.daxpay.open.channel.xingyi.client.req;

import cn.daxpay.open.channel.xingyi.client.credential.XingyiSdkCredential;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/// # 星驿付通道订单同步请求(主应用侧)
///
/// 走 `/yyfsevr/order/orderQuery`: orderNo=商户订单号(平台支付交易号) + orderTime=订单创建日期(yyyyMMdd)。
@Data
public class XingyiSyncReq {

    /// 通道调用凭证
    @NotNull(message = "{validation.field.credential.notNull}")
    private XingyiSdkCredential credential;

    /// 商户订单号(主应用支付交易号)
    @NotBlank(message = "{validation.field.outTradeNo.notBlank}")
    private String outTradeNo;

    /// 订单创建日期(yyyyMMdd, 东八区, 星驿付按日期路由查单)
    @NotBlank(message = "{validation.field.orderTime.notBlank}")
    private String orderTime;
}
