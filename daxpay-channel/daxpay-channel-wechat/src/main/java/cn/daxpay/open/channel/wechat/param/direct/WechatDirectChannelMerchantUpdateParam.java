package cn.daxpay.open.channel.wechat.param.direct;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 微信直连通道商户更新参数
///
/// 当前仅支持更新转账场景与微信商户号, 通道商户号创建后不可变。
@Data
@Accessors(chain = true)
@Schema(title = "微信直连通道商户更新参数")
public class WechatDirectChannelMerchantUpdateParam {

    /// 通道商户号(定位用, 不可变)
    @Schema(description = "通道商户号")
    @NotBlank(message = "{validation.field.channelMerchantNo.notBlank}")
    private String channelMchNo;

    /// 微信直连商户号
    @Schema(description = "微信直连商户号")
    @Size(max = 32, message = "微信直连商户号不可超过32位")
    private String wxMchId;

    /// 转账场景ID(商家转账到零钱, 微信转账时必填)
    /// @see cn.daxpay.open.channel.wechat.enums.WechatTransferSceneEnum
    @Schema(description = "转账场景ID")
    private String transferScene;
}
