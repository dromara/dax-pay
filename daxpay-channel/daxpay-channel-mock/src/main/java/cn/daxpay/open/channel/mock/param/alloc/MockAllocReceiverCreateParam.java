package cn.daxpay.open.channel.mock.param.alloc;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.Accessors;

/// # Mock 通道分账接收方新增(绑定)参数
///
/// 提交后同步调通道子应用完成通道侧绑定, 失败时记录保留(状态 fail), 修正后可重新绑定。
/// mock 零资料接入, 无应用维度(不选 appid), 产品由通道商户反解, 调用方无需传。
@Schema(title = "Mock模拟通道分账接收方新增参数")
@Data
@Accessors(chain = true)
public class MockAllocReceiverCreateParam {

    /// 商户号(运营端建档必填, 显式写入实体不依赖商户上下文装载)
    @Schema(description = "商户号")
    @NotBlank(message = "{validation.field.mchNo.notBlank}")
    @Size(max = 32, message = "{validation.field.mchNo.size}")
    private String mchNo;

    /// 通道商户号
    @Schema(description = "通道商户号")
    @NotBlank(message = "{validation.field.channelMchNo.notBlank}")
    @Size(max = 64, message = "{validation.field.channelMchNo.size}")
    private String channelMchNo;

    /// 接收方类型(MERCHANT_ID/USER_ID)
    @Schema(description = "接收方类型")
    @NotBlank(message = "{validation.field.receiverType.notBlank}")
    @Size(max = 32, message = "{validation.field.receiverType.size}")
    private String receiverType;

    /// 接收方账号(商户号或用户ID)
    @Schema(description = "接收方账号")
    @NotBlank(message = "{validation.field.receiverAccount.notBlank}")
    @Size(max = 128, message = "{validation.field.receiverAccount.size}")
    private String receiverAccount;

    /// 接收方名称(MERCHANT_ID 时必填商户全称)
    @Schema(description = "接收方名称")
    @Size(max = 64, message = "{validation.field.receiverName.size}")
    private String receiverName;

    /// 接收方别名(本地备注, 不上送通道, 新增后可随时修改)
    @Schema(description = "接收方别名")
    @Size(max = 50, message = "{validation.field.alias.size}")
    private String alias;
}
