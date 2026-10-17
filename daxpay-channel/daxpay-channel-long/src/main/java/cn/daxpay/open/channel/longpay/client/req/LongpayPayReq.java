package cn.daxpay.open.channel.longpay.client.req;

import cn.daxpay.open.channel.longpay.client.credential.LongpaySdkCredential;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.time.OffsetDateTime;

/// # 建行龙支付通道支付请求(主应用 → 子应用)
@Data
public class LongpayPayReq {

    /// 通道调用凭证
    @NotNull(message = "{validation.field.credential.notNull}")
    private LongpaySdkCredential credential;

    /// 商户订单号(主应用支付交易号, 作为 ORDERID 上送, 异步通知凭此反查; 最长 30 位)
    @NotBlank(message = "{validation.field.outTradeNo.notBlank}")
    private String outTradeNo;

    /// 订单金额(单位: 分; 建行 PAYMENT 字段单位为元, 子应用负责换算)
    @NotNull(message = "{validation.field.amount.notNull}")
    @Positive(message = "{validation.field.amount.positive}")
    private Long amount;

    /// 商品标题(escape 编码后作为 PROINFO 上送)
    @NotBlank(message = "{validation.field.title.notBlank}")
    private String title;

    /// 客户端IP
    private String clientIp;

    /// 订单超时时间(可选, 转 TIMEOUT=yyyyMMddHHmmss 上送, 超时后聚合码失效)
    private OffsetDateTime expireTime;
}
