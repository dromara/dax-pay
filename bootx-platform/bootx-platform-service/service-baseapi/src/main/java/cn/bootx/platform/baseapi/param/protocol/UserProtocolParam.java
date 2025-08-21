package cn.bootx.platform.baseapi.param.protocol;

import cn.bootx.platform.core.validation.ValidationGroup;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 用户协议管理
 * @author xxm
 * @since 2025/5/9
 */
@Data
@Accessors(chain = true)
@Schema(title = "用户协议管理")
public class UserProtocolParam {

    @Null(message = "Id需要为空", groups = ValidationGroup.add.class)
    @NotNull(message = "Id不可为空", groups = ValidationGroup.edit.class)
    @Schema(description = "主键")
    private Long id;

    /** 名称 */
    @NotBlank(message = "名称不能为空")
    @Schema(description = "名称")
    private String name;

    /** 显示名称 */
    @NotBlank(message = "显示名称不能为空")
    @Schema(description = "显示名称")
    private String showName;

    /** 类型 */
    @NotBlank(message = "类型不能为空")
    @Schema(description = "类型")
    private String type;

    /** 内容 */
    @Schema(description = "内容")
    private String content;
}
