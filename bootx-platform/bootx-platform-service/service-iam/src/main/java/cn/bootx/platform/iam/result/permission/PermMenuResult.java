package cn.bootx.platform.iam.result.permission;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 菜单资源
 * @author xxm
 * @since 2021/7/12
 */
@Data
@Accessors(chain = true)
@Schema(title = "菜单资源")
public class PermMenuResult {

    @Schema(description = "菜单id")
    private Long id;

    @Schema(description = "父id")
    private Long pid;

    @Schema(description = "关联终端code")
    private String clientCode;

    @Schema(description = "菜单标题/资源名称")
    private String title;

    @Schema(description = "路由名称，建议唯一")
    private String name;

    @Schema(description = "资源编码(权限码)")
    private String permCode;

    @Schema(description = "图标")
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
     * meta 元信息
     */
    @Schema(description = "是否缓存页面")
    private boolean keepAlive;

    @Schema(description = "打开方式是否为外部打开")
    private boolean targetOutside;

    @Schema(description = "是否全屏打开")
    private boolean fullScreen;

    @Schema(description = "描述")
    private String remark;

    @Schema(description = "系统内置")
    private boolean internal;

    @Schema(description = "子节点")
    private List<PermMenuResult> children;

    public Double getSortNo(){
        return sortNo == null ? 0 : sortNo;
    }

}
