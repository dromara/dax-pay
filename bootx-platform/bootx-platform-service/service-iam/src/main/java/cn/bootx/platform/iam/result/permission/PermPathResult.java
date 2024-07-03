package cn.bootx.platform.iam.result.permission;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 请求资源
 * @author xxm
 */
@Data
@Accessors(chain = true)
@Schema(title = "请求资源")
public class PermPathResult {

    @Schema(description = "id")
    private Long id;

    @Schema(description = "权限标识")
    private String code;

    @Schema(description = "权限名称")
    private String name;

    @Schema(description = "分组名称")
    private String groupName;

    @Schema(description = "请求类型")
    private String requestType;

    @Schema(description = "请求路径")
    private String path;

    @Schema(description = "启用")
    private boolean enable;

    @Schema(description = "是否通过系统生成的权限")
    private boolean generate;

    @Schema(description = "描述")
    private String remark;

}
