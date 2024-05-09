package cn.bootx.platform.iam.dto.scope;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 用户数据范围权限详细信息
 *
 * @author xxm
 * @since 2022/1/5
 */
@Data
@Accessors(chain = true)
@Schema(title = "用户数据范围权限详细信息")
public class DataRoleUserDto {

    @Schema(description = "权限关联id")
    private Long id;

    @Schema(description = "用户id")
    private Long userId;

    @Schema(description = "用户名称")
    private String name;

    @Schema(description = "用户账号")
    private String username;

}
