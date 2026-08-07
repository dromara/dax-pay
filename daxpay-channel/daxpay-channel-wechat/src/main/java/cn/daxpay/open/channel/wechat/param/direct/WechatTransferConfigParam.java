package cn.daxpay.open.channel.wechat.param.direct;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 微信转账配置保存参数
///
/// 一对一 upsert: 存在则更新, 不存在则新增。`transferScene` 与 `transferAppRefId` 均允许为空
/// (支持分步配置), 但发起转账时两者必须齐备。
///
@Data
@Accessors(chain = true)
@Schema(title = "微信转账配置保存参数")
public class WechatTransferConfigParam {

    @NotBlank(message = "{validation.field.mchNo.notBlank}")
    @Schema(description = "商户号")
    private String mchNo;

    @NotBlank(message = "{validation.field.channelMerchantNo.notBlank}")
    @Schema(description = "通道商户号")
    private String channelMchNo;

    @Schema(description = "转账场景ID(微信 transfer_scene, 8 枚举之一, 允许为空待补配)")
    private String transferScene;

    @Schema(description = "转账发起应用引用(指向 wx_mch_app 主键, 须为公众号类型)")
    private Long transferAppRefId;
}
