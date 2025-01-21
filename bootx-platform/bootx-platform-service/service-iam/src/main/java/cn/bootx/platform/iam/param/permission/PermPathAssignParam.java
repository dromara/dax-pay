package cn.bootx.platform.iam.param.permission;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 请求路径分配参数
 * @author xxm
 * @since 2024/7/5
 */
@Data
@Accessors(chain = true)
@Schema(title = "请求路径分配参数")
public class PermPathAssignParam {

    /** 角色ID */
    @Schema(description = "角色ID")
    private Long roleId;
    /** 终端编码 */
    @Schema(description = "终端编码")
    private String clientCode;
    /** 路径ID */
    @Schema(description = "路径ID")
    private List<Long> pathIds;
    /** 是否更新子孙角色 */
    @Schema(description = "是否更新子孙角色")
    private boolean updateChildren;
}
