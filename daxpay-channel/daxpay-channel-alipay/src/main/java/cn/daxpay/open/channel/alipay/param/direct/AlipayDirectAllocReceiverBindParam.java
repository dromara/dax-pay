package cn.daxpay.open.channel.alipay.param.direct;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 支付宝直连分账接收方重新绑定参数
///
/// 应用引用可选: 传入新值时替换落库的应用引用后发起绑定,
/// 留空则沿用落库引用; 接收方账号不可修改(改账号即新接收方, 走新增)。
/// 支付宝绑定关系挂收款商户(与 app_id 无关), 换应用仅影响调用凭证。
@Data
@Accessors(chain = true)
@Schema(title = "支付宝直连分账接收方重新绑定参数")
public class AlipayDirectAllocReceiverBindParam {

    /// 记录 id
    @Schema(description = "记录id")
    @NotNull(message = "{validation.field.id.notNull}")
    private Long id;

    /// 发起绑定的支付宝应用引用(alipay_direct_app 主键, 留空沿用落库值)
    @Schema(description = "支付宝应用引用(留空沿用)")
    private Long appRefId;
}
