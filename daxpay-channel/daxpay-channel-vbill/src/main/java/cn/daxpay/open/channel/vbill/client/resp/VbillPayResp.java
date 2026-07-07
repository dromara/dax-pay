package cn.daxpay.open.channel.vbill.client.resp;

import cn.daxpay.open.channel.vbill.client.enums.VbillPayBodyType;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class VbillPayResp {

    /// 商户订单号(透传 VbillPayReq.outTradeNo)
    private String outTradeNo;

    /// 随行付网关订单号(uuid)
    private String outOrderNo;

    /// 支付内容(二维码链接 / JSAPI 调起参数 JSON / 跳转链接)
    private String payBody;

    /// 支付内容类型
    private VbillPayBodyType payBodyType;

    /// 是否已终态完成(付款码同步成功时为 true)
    private Boolean complete;

    /// 订单总金额(单位: 分, 付款码同步成功时返回)
    private Long totalAmount;

    /// 用户实付金额(单位: 分, 付款码同步成功时返回, 来自 settleAmt)
    private Long realAmount;

    /// 完成时间
    private OffsetDateTime finishTime;

    /// 买家标识
    private String buyerId;

    /// 渠道交易单号(transactionId)
    private String transOrderNo;

    /// 支付厂商(WECHAT/ALIPAY/UNIONPAY)
    private String tradeProduct;
}
