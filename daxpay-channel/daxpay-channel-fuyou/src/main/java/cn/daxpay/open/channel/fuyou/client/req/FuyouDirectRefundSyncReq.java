package cn.daxpay.open.channel.fuyou.client.req;

import cn.daxpay.open.channel.fuyou.client.credential.FuyouDirectSdkCredential;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/// # 富友直连退款同步请求
@Data
public class FuyouDirectRefundSyncReq {

    /// 通道调用凭证
    @NotNull(message = "{validation.field.credential.notNull}")
    private FuyouDirectSdkCredential credential;

    /// 退款单号(平台退款交易号, 作为富友 refund_order_no 查询)
    @NotBlank(message = "{validation.field.outRefundNo.notBlank}")
    private String outRefundNo;
}
