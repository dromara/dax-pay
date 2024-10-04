package cn.bootx.platform.iam.result.role;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author xxm
 */
@Data
@Accessors(chain = true)
@Schema(title = "角色")
public class RoleResult {

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "角色code")
    private String code;

    @Schema(description = "角色名称")
    private String name;

    @Schema(description = "是否系统内置")
    private boolean internal;

    @Schema(description = "父级Id")
    private Long pid;

    @Schema(description = "描述")
    private String remark;

    @Schema(description = "子节点")
    private List<RoleResult> children;

}
