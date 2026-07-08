package cn.daxpay.open.channel.fuyou.client.req;

import cn.daxpay.open.channel.fuyou.client.credential.FuyouSdkCredential;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/// # 富友退款同步请求(主应用侧)
@Data
public class FuyouRefundSyncReq {

    @NotNull(message = "{validation.field.credential.notNull}")
    private FuyouSdkCredential credential;

    @NotBlank(message = "{validation.field.outRefundNo.notBlank}")
    private String outRefundNo;
}
