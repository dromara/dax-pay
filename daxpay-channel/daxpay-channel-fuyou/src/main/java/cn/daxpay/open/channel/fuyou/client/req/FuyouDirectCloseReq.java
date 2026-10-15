package cn.daxpay.open.channel.fuyou.client.req;

import cn.daxpay.open.channel.fuyou.client.credential.FuyouDirectSdkCredential;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/// # 富友直连关闭订单请求
@Data
public class FuyouDirectCloseReq {

    /// 通道调用凭证
    @NotNull(message = "{validation.field.credential.notNull}")
    private FuyouDirectSdkCredential credential;

    /// 关联订单号(富友 mchnt_order_no)
    @NotBlank(message = "{validation.field.relationOrderNo.notBlank}")
    private String relationOrderNo;

    /// 支付厂商(WECHAT/ALIPAY/UNIONPAY, 对应 order_type)
    @NotBlank(message = "{validation.field.tradeProduct.notBlank}")
    private String tradeProduct;
}
