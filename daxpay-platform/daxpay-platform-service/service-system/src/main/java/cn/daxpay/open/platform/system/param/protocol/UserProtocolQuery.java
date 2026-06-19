package cn.daxpay.open.platform.system.param.protocol;

import cn.daxpay.open.platform.common.mybatisplus.query.entity.SortParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 用户协议
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "用户协议")
public class UserProtocolQuery extends SortParam {

    /// 名称
    @Schema(description = "名称")
    private String name;

    /// 显示名称
    @Schema(description = "显示名称")
    private String showName;

    /// 类型
    @Schema(description = "类型")
    private String type;

    /// 端类型
    @Schema(description = "端类型")
    private String clientType;
}

