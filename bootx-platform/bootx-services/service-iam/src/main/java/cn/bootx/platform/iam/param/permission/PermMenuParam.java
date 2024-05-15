package cn.bootx.platform.iam.param.permission;

import cn.bootx.platform.common.core.validation.ValidationGroup;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.io.Serializable;

/**
 * @author xxm
 * @since 2021/7/12
 */
@Data
@Accessors(chain = true)
@Schema(title = "菜单权限")
public class PermMenuParam implements Serializable {

    private static final long serialVersionUID = 3017200753543614579L;

    @Null(groups = { ValidationGroup.edit.class })
    @NotNull(groups = { ValidationGroup.add.class })
    @Schema(description = "主键")
    private Long id;

    @Schema(description = "父id")
    private Long parentId;

    @NotEmpty(groups = { ValidationGroup.add.class, ValidationGroup.edit.class })
    @Schema(description = "关联应用code")
    private String clientCode;

    @Schema(description = "菜单标题")
    private String title;

    @Schema(description = "路由名称，建议唯一")
    private String name;

    @Schema(description = "资源编码(权限码)")
    private String permCode;

    @Schema(description = "是否有效")
    private boolean effect;

    @Schema(description = "菜单图标")
    private String icon;

    @Schema(description = "是否隐藏")
    private boolean hidden;

    @Schema(description = "是否隐藏子菜单")
    private boolean hideChildrenInMenu;

    @Schema(description = "组件")
    private String component;

    @Schema(description = "路径")
    private String path;

    @Schema(description = "菜单跳转地址(重定向)")
    private String redirect;

    @Schema(description = "菜单排序")
    private Double sortNo;

    @Schema(description = "类型（0：一级菜单；1：子菜单 ；2：资源）")
    private Integer menuType;

    /**
     * meta
     */
    @Schema(description = "是否缓存页面")
    private boolean keepAlive;

    @Schema(description = "打开方式是否为外部打开")
    private boolean targetOutside;

    @Schema(description = "隐藏的标题内容")
    private boolean hiddenHeaderContent;

    @Schema(description = "描述")
    private String remark;

}
