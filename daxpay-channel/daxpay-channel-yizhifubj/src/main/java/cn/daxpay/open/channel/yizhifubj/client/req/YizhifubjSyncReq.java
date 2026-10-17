package cn.daxpay.open.channel.yizhifubj.client.req;

import cn.daxpay.open.channel.yizhifubj.client.credential.YizhifubjSdkCredential;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/// # 一直付通道订单同步请求(主应用侧)
///
/// 走 5upay 网关 `/onlinePay/query`, 按商户订单号(requestId)查询。
@Data
public class YizhifubjSyncReq {

    /// 通道调用凭证
    @NotNull(message = "{validation.field.credential.notNull}")
    private YizhifubjSdkCredential credential;

    /// 商户订单号(原下单 requestId = 主应用支付交易号)
    @NotBlank(message = "{validation.field.outTradeNo.notBlank}")
    private String outTradeNo;
}
