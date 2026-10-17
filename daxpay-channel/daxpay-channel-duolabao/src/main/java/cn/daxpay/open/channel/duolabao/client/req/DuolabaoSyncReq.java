package cn.daxpay.open.channel.duolabao.client.req;

import cn.daxpay.open.channel.duolabao.client.credential.DuolabaoSdkCredential;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/// # 哆啦宝通道订单同步请求(主应用侧)
///
/// 走 `/api/queryOrderPayDetail`(按商户单号 requestNum 查询, 不依赖通道订单号)。
@Data
public class DuolabaoSyncReq {

    /// 通道调用凭证
    @NotNull(message = "{validation.field.credential.notNull}")
    private DuolabaoSdkCredential credential;

    /// 商户订单号(主应用支付交易号, 即下单时的 requestNum)
    @NotBlank(message = "{validation.field.outTradeNo.notBlank}")
    private String outTradeNo;
}
