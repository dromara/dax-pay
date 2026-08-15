package cn.daxpay.open.channel.alipay.param.isv;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 支付宝服务商分账接收方重新绑定参数
///
/// 支付宝绑定关系挂收款商户(与 app_id 无关), 凭证由子商户授权自动决定,
/// 无应用可选, 仅传记录 id(保持五组端点契约同构的 POST body 形态)。
@Data
@Accessors(chain = true)
@Schema(title = "支付宝服务商分账接收方重新绑定参数")
public class AlipayIsvAllocReceiverBindParam {

    /// 记录 id
    @Schema(description = "记录id")
    @NotNull(message = "{validation.field.id.notNull}")
    private Long id;
}
