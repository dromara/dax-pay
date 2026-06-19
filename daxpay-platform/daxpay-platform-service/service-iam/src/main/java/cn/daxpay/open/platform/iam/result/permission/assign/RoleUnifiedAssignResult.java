package cn.daxpay.open.platform.iam.result.permission.assign;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

/// # 角色统一授权结果
///
/// 用于前端统一授权弹窗初始化，包含当前终端菜单树以及角色已勾选的菜单、权限码数据。
@Data
@Accessors(chain = true)
@Schema(title = "角色统一授权结果")
public class RoleUnifiedAssignResult {

    @Schema(description = "角色ID")
    private Long roleId;

    @Schema(description = "终端编码")
    private String clientCode;

    @Schema(description = "菜单与权限码统一授权树")
    private List<RoleUnifiedAssignTreeResult> tree = new ArrayList<>();

    @Schema(description = "已选菜单ID")
    private List<Long> checkedMenuIds = new ArrayList<>();

    @Schema(description = "已选权限码ID")
    private List<Long> checkedCodeIds = new ArrayList<>();
}
