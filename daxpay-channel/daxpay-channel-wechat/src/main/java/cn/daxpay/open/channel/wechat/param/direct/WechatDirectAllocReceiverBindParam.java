package cn.daxpay.open.channel.wechat.param.direct;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 微信直连分账接收方重新绑定参数
///
/// 应用字段可选: 传入新值时替换落库的应用后发起绑定(选错应用的修正路径),
/// 留空则沿用落库应用; 接收方账号不可修改(改账号即新接收方, 走新增)。
@Data
@Accessors(chain = true)
@Schema(title = "微信直连分账接收方重新绑定参数")
public class WechatDirectAllocReceiverBindParam {

    /// 记录 id
    @Schema(description = "记录id")
    @NotNull(message = "{validation.field.id.notNull}")
    private Long id;

    /// 绑定所用商户档微信应用 appid(留空沿用落库值)
    @Schema(description = "绑定所用微信应用appid(留空沿用)")
    private String channelAppId;
}
