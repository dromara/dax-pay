package cn.daxpay.open.payment.unipay.result.assist;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 签名自检探针结果
///
/// 回显平台侧的解析结果, 供对接方核对「平台认到的商户身份」与「自己声明的」是否一致;
/// `serverSignStr` 为平台按规范字面量计算的待签串, 与验签失败响应中回吐的是同一份,
/// 双方比对可快速定位签名串构造差异(字段排序/空值剔除/时间格式)。
@Data
@Accessors(chain = true)
@Schema(title = "签名自检探针结果")
public class UnipayPingResult {

    /// 平台解析到的商户号(即验签所用公钥归属的商户)
    @Schema(description = "商户号")
    private String mchNo;

    /// 解析到的应用号(显式传入或商户默认应用)
    @Schema(description = "应用号")
    private String appId;

    /// 是否使用了商户默认应用(appId 未传时为 true)
    @Schema(description = "是否默认应用")
    private Boolean appFromDefault;

    /// 服务端待签串(平台规范字面量, 供双方比对签名串构造)
    @Schema(description = "服务端待签串")
    private String serverSignStr;
}
