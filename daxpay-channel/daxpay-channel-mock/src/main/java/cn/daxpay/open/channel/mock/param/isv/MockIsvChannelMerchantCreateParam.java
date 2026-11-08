package cn.daxpay.open.channel.mock.param.isv;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.Accessors;

/// # Mock 模拟服务商通道商户创建参数
///
/// 服务商形态建档: 纯本地登记——商户档案由运营在模拟通道控制台自行创建, 此处登记其通道商户号,
/// 不调用模拟通道任何接口; 通道侧无对应档案(或归属不一致)的商户号支付时将被拒单。
/// 子商户号(通道商户号)必填: 登记模拟通道控制台已创建的子商户号(与直连建档口径一致, 不再自动生成)。
/// 归属服务商在创建时选定、锁死不可改。
/// 沙箱标记不走参数: 模拟通道天然属联调/演示语义, 创建时固定为沙箱商户(见 Service)。
@Data
@Accessors(chain = true)
@Schema(title = "Mock模拟服务商通道商户创建参数")
public class MockIsvChannelMerchantCreateParam {

    /// 商户号(运营端建档必填, 显式写入实体不依赖商户上下文装载)
    @Schema(description = "商户号")
    @NotBlank(message = "{validation.field.mchNo.notBlank}")
    @Size(max = 32, message = "{validation.field.mchNo.size}")
    private String mchNo;

    /// 归属模拟服务商ID(指向 mock_isv_info.id)
    @Schema(description = "归属模拟服务商ID")
    @NotNull(message = "{validation.field.isvId.notNull}")
    private Long isvId;

    /// 子商户号即通道商户号(在模拟通道控制台创建商户后登记其商户号)
    @Schema(description = "通道商户号")
    @NotBlank(message = "{validation.field.channelMchNo.notBlank}")
    @Size(max = 64, message = "{validation.field.channelMchNo.size}")
    private String channelMchNo;

    /// 通道商户名称
    @Schema(description = "通道商户名称")
    @NotBlank(message = "{validation.field.channelMerchantName.notBlank}")
    private String channelMerchantName;
}
