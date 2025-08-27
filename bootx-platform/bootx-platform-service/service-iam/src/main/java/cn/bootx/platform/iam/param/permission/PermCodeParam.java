package cn.bootx.platform.iam.param.permission;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.ibatis.annotations.Update;

/**
 * 权限码
 * @author xxm
 * @since 2024/7/3
 */
@Data
@Accessors(chain = true)
@Schema(title = "权限码")
public class PermCodeParam {

    @NotNull(message = "主键不可为空", groups = {Update.class})
    @Schema(description = "id")
    private Long id;

    @Schema(description = "父id")
    private Long pid;

    @NotNull(message = "编码不可为空")
    @Schema(description = "权限码")
    private String code;

    @NotNull(message = "名称不可为空")
    @Schema(description = "名称")
    private String name;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "是否为子节点")
    private boolean leaf;

}
