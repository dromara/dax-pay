package cn.daxpay.open.channel.douyin.param.direct;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 抖音转账配置保存参数
///
/// 一对一 upsert: 存在则更新, 不存在则新增。`transferAppRefId` 允许为空(支持清空),
/// 但发起转账时必须已配置, 由转账策略校验。
///
@Data
@Accessors(chain = true)
@Schema(title = "抖音转账配置保存参数")
public class DouyinTransferConfigParam {

    @NotBlank(message = "{validation.field.mchNo.notBlank}")
    @Schema(description = "商户号")
    private String mchNo;

    @NotBlank(message = "{validation.field.channelMerchantNo.notBlank}")
    @Schema(description = "通道商户号")
    private String channelMchNo;

    @Schema(description = "转账发起应用引用(指向 dy_mch_app 主键, 须为网站应用 web_app)")
    private Long transferAppRefId;
}
