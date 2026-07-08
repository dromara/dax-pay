package cn.daxpay.open.channel.fuyou.client.req;

import cn.daxpay.open.channel.fuyou.client.credential.FuyouSdkCredential;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/// # 富友关闭订单请求(主应用侧)
@Data
public class FuyouCloseReq {

    @NotNull(message = "{validation.field.credential.notNull}")
    private FuyouSdkCredential credential;

    @NotBlank(message = "{validation.field.relationOrderNo.notBlank}")
    private String relationOrderNo;

    @NotBlank(message = "{validation.field.tradeProduct.notBlank}")
    private String tradeProduct;
}
