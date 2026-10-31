package cn.daxpay.open.channel.mock.param;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

/// # Mock 模拟通道商户创建参数
///
/// 直连单产品(mock_pay)建档形态: 纯本地登记商户档案(或显式指定通道商户号),
/// 直连商户级接入密钥由密钥端点后置维护, 故创建参数无凭证字段。
/// 沙箱标记不走参数: 模拟通道仅 DEV 装配、天然属联调/演示语义, 创建时固定为沙箱商户(见 Service)。
@Data
@Accessors(chain = true)
@Schema(title = "Mock模拟通道商户创建参数")
public class MockChannelMerchantCreateParam {

    /// 商户号
    @Schema(description = "商户号")
    @NotBlank(message = "{validation.field.mchNo.notBlank}")
    private String mchNo;

    /// 通道商户号(可空; 空则按 MOCK 前缀自动生成, 非空则登记运营在模拟网关控制台创建的商户号)
    @Schema(description = "通道商户号(可空, 空则自动生成)")
    private String channelMchNo;

    /// 通道商户名称
    @Schema(description = "通道商户名称")
    @NotBlank(message = "{validation.field.channelMerchantName.notBlank}")
    private String channelMerchantName;

    /// 所属支付产品(一期仅 mock_pay)
    @Schema(description = "所属支付产品")
    @NotBlank(message = "{validation.field.product.notBlank}")
    private String product;
}
