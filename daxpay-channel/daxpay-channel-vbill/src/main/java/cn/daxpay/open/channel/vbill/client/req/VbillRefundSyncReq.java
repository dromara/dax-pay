package cn.daxpay.open.channel.vbill.client.req;

import cn.daxpay.open.channel.vbill.client.credential.VbillSdkCredential;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class VbillRefundSyncReq {

    @NotNull(message = "{validation.field.credential.notNull}")
    private VbillSdkCredential credential;

    /// 商户退款单号(主应用退款交易号, 对应随行付 ordNo)
    private String outRefundNo;

    /// 随行付网关退款单号(uuid, 优先使用)
    private String outRefundOrderNo;
}
