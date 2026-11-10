package cn.daxpay.open.channel.mock.param;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

/// # Mock 模拟通道商户创建参数
///
/// 直连(mock_pay)与服务商(mock_isv)共用建档形态: 纯本地登记——商户档案由运营在模拟通道控制台
/// 自行创建, 此处登记其通道商户号, 不调用模拟通道任何接口(模拟通道为独立通道, 主应用侧无对接接口)。
/// mock_isv 由 Service 额外写一行服务商归属绑定(归属恒为全局唯一模拟服务商);
/// 接入密钥统一为全局单份, 故创建参数无凭证字段。
/// 沙箱标记不走参数: 模拟通道仅 DEV 装配、天然属联调/演示语义, 创建时固定为沙箱商户(见 Service)。
@Data
@Accessors(chain = true)
@Schema(title = "Mock模拟通道商户创建参数")
public class MockChannelMerchantCreateParam {

    /// 商户号
    @Schema(description = "商户号")
    @NotBlank(message = "{validation.field.mchNo.notBlank}")
    private String mchNo;

    /// 通道商户号(在模拟通道控制台创建商户后登记其商户号)
    @Schema(description = "通道商户号")
    @NotBlank(message = "{validation.field.channelMchNo.notBlank}")
    private String channelMchNo;

    /// 通道商户名称
    @Schema(description = "通道商户名称")
    @NotBlank(message = "{validation.field.channelMerchantName.notBlank}")
    private String channelMerchantName;

    /// 所属支付产品(mock_pay 直连 / mock_isv 服务商)
    @Schema(description = "所属支付产品(mock_pay/mock_isv)")
    @NotBlank(message = "{validation.field.product.notBlank}")
    private String product;
}
