package cn.daxpay.open.channel.wechat.param.isv;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/// # 微信服务商支付能力关联应用批量保存参数
///
/// 全量覆盖「支付能力 → 服务商应用」的绑定关系：先清除旧记录，再按 items 批量插入。
/// items 为空表示清空所有绑定。
///
@Data
@Accessors(chain = true)
@Schema(title = "微信服务商支付能力关联应用批量保存参数")
public class WechatIsvAppCapabilityBatchParam {

    @Valid
    @Schema(description = "支付能力关联应用列表")
    private List<WechatIsvAppCapabilityItem> items;
}
