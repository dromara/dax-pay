package cn.daxpay.open.channel.douyin.param.assist;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 抖音 JSAPI sdk.config 查询参数
///
/// GET `/unipay/assist/channel/douyin/jsapi-config` 的 query 绑定对象。
/// 上下文三选一: orderNo(网关单) / code(码牌) / channelMchNo(+可选 capability/channelAppId)。
@Data
@Accessors(chain = true)
@Schema(title = "抖音 JSAPI sdk.config 查询参数")
public class DouyinJsapiConfigParam {

    @NotBlank(message = "{validation.field.url.notBlank}")
    @Schema(description = "当前页 URL(不含 hash)")
    private String url;

    @Schema(description = "网关订单号")
    private String orderNo;

    @Schema(description = "码牌编码")
    private String code;

    @Schema(description = "通道商户号")
    private String channelMchNo;

    @Schema(description = "能力编码")
    private String capability;

    @Schema(description = "通道应用ID")
    private String channelAppId;
}
