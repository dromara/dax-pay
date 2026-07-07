package cn.daxpay.open.channel.vbill.client.req;

import cn.daxpay.open.channel.vbill.client.credential.VbillSdkCredential;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class VbillCloseReq {

    @NotNull(message = "{validation.field.credential.notNull}")
    private VbillSdkCredential credential;

    /// 随行付网关订单号(uuid)
    private String outOrderNo;
}
