package cn.daxpay.open.channel.adapay.client.resp;

import cn.daxpay.open.channel.adapay.client.enums.AdapayPayBodyType;
import lombok.Data;
import lombok.experimental.Accessors;

/// # Adapay 通道支付响应(主应用侧)
@Data
@Accessors(chain = true)
public class AdapayPayResp {

    /// 商户订单号(回显)
    private String outTradeNo;

    /// Adapay 支付对象 ID(查询/关单/退款的关键凭证)
    private String paymentId;

    /// 支付内容
    private String payBody;

    /// 支付内容类型
    private AdapayPayBodyType payBodyType;

    /// 是否支付完成(条码支付同步返回; 预下单返回 false)
    private Boolean complete;

    /// 订单金额(单位: 分)
    private Long totalAmount;

    /// 实付金额(单位: 分)
    private Long realAmount;

    /// 支付完成时间
    private String finishTime;

    /// 买家标识
    private String buyerId;
}
