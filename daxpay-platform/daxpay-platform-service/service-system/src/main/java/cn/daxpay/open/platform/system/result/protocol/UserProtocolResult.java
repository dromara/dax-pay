package cn.daxpay.open.platform.system.result.protocol;

import cn.daxpay.open.platform.core.result.BaseResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 用户协议管理
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "用户协议管理")
public class UserProtocolResult extends BaseResult {

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

    /// 内容格式
    @Schema(description = "内容格式")
    private String contentFormat;

    /// 默认协议
    @Schema(description = "默认协议")
    private Boolean defaultProtocol;

    /// 内容
    @Schema(description = "内容")
    private String content;
}

