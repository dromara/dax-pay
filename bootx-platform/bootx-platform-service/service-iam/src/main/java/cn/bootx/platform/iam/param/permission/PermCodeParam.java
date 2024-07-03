package cn.bootx.platform.iam.param.permission;

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
public class PermCodeParam {

    @Schema(description = "id")
    private Long id;

    @Schema(description = "权限码")
    private String code;
}
