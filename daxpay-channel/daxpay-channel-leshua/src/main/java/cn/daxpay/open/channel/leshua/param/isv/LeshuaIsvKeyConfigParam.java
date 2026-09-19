package cn.daxpay.open.channel.leshua.param.isv;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 乐刷服务商密钥配置保存参数
///
@Data
@Accessors(chain = true)
@Schema(title = "乐刷服务商密钥配置保存参数")
public class LeshuaIsvKeyConfigParam {

    @NotBlank(message = "{validation.field.product.notBlank}")
    @Schema(description = "产品编码")
    private String product;

    @NotBlank(message = "{validation.field.tradeKey.notBlank}")
    @Schema(description = "交易密钥(交易/进件等所有服务商接口请求签名, 加密存储)")
    private String tradeKey;

    @NotBlank(message = "{validation.field.notifyKey.notBlank}")
    @Schema(description = "异步通知密钥(异步通知回调验签, 加密存储)")
    private String notifyKey;

    @NotBlank(message = "{validation.field.signType.notBlank}")
    @Schema(description = "签名类型(MD5 / SM3)")
    private String signType;

    @NotBlank(message = "{validation.field.lsIsvNo.notBlank}")
    @Schema(description = "乐刷服务商号(进件等接口场景必传)")
    private String lsIsvNo;

    @NotNull(message = "{validation.field.sandbox.notNull}")
    @Schema(description = "是否沙箱环境")
    private Boolean sandbox;
}
