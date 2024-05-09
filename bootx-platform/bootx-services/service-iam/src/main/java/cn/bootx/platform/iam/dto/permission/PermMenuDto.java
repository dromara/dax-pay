package cn.bootx.platform.iam.dto.permission;

import cn.bootx.platform.common.core.rest.dto.BaseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * @author xxm
 * @since 2021/7/12
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "菜单和资源权限")
public class PermMenuDto extends BaseDto implements Serializable {

    private static final long serialVersionUID = 1935403328634321841L;

    @Schema(description = "父id")
    private Long parentId;

    @Schema(description = "关联终端code")
    private String clientCode;

    @Schema(description = "菜单标题/资源名称")
    private String title;

    @Schema(description = "路由名称，建议唯一")
    private String name;

    @Schema(description = "资源编码(权限码)")
    private String permCode;

    @Schema(description = "是否有效")
    private boolean effect;

    @Schema(description = "图标")
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
     * meta 元信息
     */
    @Schema(description = "是否缓存页面")
    private boolean keepAlive;

    @Schema(description = "打开方式是否为外部打开")
    private boolean targetOutside;

    @Schema(description = "隐藏的标题内容")
    private boolean hiddenHeaderContent;

    @Schema(description = "描述")
    private String remark;

    @Schema(description = "系统内置")
    private boolean admin;

    @Schema(description = "子节点")
    private List<PermMenuDto> children;

}
