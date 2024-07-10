package cn.bootx.platform.iam.param.permission;

import cn.bootx.platform.core.validation.ValidationGroup;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author xxm
 * @since 2021/7/12
 */
@Data
@Accessors(chain = true)
@Schema(title = "菜单权限")
public class PermMenuParam {

    @Null(groups = { ValidationGroup.edit.class })
    @NotNull(groups = { ValidationGroup.add.class })
    @Schema(description = "主键")
    private Long id;

    @Schema(description = "父id")
    private Long pid;

    @NotEmpty(groups = { ValidationGroup.add.class, ValidationGroup.edit.class })
    @Schema(description = "关联应用code")
    private String clientCode;

    @Schema(description = "菜单标题")
    private String title;

    @Schema(description = "路由名称，建议唯一")
    private String name;

    @Schema(description = "菜单图标")
    private String icon;

    @Schema(description = "是否隐藏")
    private boolean hidden;

    @Schema(description = "是否隐藏子菜单")
    private boolean hideChildrenMenu;

    @Schema(description = "组件")
    private String component;

    @Schema(description = "路径")
    private String path;

    @Schema(description = "菜单跳转地址(重定向)")
    private String redirect;

    @Schema(description = "菜单排序")
    private Double sortNo;

    @Schema(description = "是否是一级菜单")
    private boolean root;

    /**
     * meta
     */
    @Schema(description = "是否缓存页面")
    private boolean keepAlive;

    @Schema(description = "打开方式是否为外部打开")
    private boolean targetOutside;

    @Schema(description = "是否全屏打开")
    private boolean fullScreen;

    @Schema(description = "描述")
    private String remark;

}
