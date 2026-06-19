package cn.daxpay.open.platform.system.result.protocol;

import cn.daxpay.open.platform.core.result.BaseResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 用户协议项管理
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "用户协议项管理")
public class UserProtocolItemResult extends BaseResult {

    /// 协议id
    @Schema(description = "协议id")
    private Long protocolId;

    /// 菜单排序
    @Schema(description = "菜单排序")
    private Double sortNo;

    /// 协议内容
    @Schema(description = "协议内容")
    private String content;
}
