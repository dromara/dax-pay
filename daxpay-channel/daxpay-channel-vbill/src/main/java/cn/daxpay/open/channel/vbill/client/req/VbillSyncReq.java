package cn.daxpay.open.channel.vbill.client.req;

import cn.daxpay.open.channel.vbill.client.credential.VbillSdkCredential;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class VbillSyncReq {

    @NotNull(message = "{validation.field.credential.notNull}")
    private VbillSdkCredential credential;

    /// 商户订单号(主应用支付交易号, 对应随行付 ordNo)
    private String outTradeNo;

    /// 随行付网关订单号(uuid, 优先使用)
    private String outOrderNo;
}
