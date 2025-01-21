package cn.bootx.platform.iam.result.upms;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 *
 * @author xxm
 * @since 2024/7/10
 */
@Data
@Accessors(chain = true)
@Schema(title = "权限码")
public class RolePermCodeResult {

    @Schema(description = "权限码id")
    private Long id;

    @Schema(description = "权限码")
    private String code;

    @Schema(description = "权限名称")
    private String name;

    @Schema(description = "子类")
    private List<RolePermCodeResult> children;

}
