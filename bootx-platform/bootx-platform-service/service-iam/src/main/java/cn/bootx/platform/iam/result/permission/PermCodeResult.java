package cn.bootx.platform.iam.result.permission;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 权限码
 * @author xxm
 * @since 2024/7/3
 */
@Data
@Accessors(chain = true)
@Schema(title = "权限码")
public class PermCodeResult {

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "父ID")
    private Long pid;

    @Schema(description = "权限码")
    private String code;

    @Schema(description = "权限名称")
    private String name;
}
