package cn.daxpay.open.platform.iam.result.permission.resource;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/// # 菜单资源
///
@Data
@Accessors(chain = true)
@Schema(title = "菜单资源")
public class PermMenuResult {

    @Schema(description = "菜单id")
    private Long id;

    @Schema(description = "父id")
    private Long pid;

    @Schema(description = "菜单编码")
    private String menuCode;

    @Schema(description = "关联终端code")
    private String clientCode;

    @Schema(description = "国际化key")
    private String i18nKey;

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

    @Schema(description = "是否开启页面缓存")
    private boolean keepAlive;

    @Schema(description = "是否固定标签页")
    private boolean affixTab;

    @Schema(description = "菜单类型: catalog-目录, menu-菜单, subpage-子页面, subpage_group-子页面分组, embedded-内嵌, link-外链")
    private String menuType;

    @Schema(description = "徽章显示文本")
    private String badge;

    @Schema(description = "徽章类型: dot-圆点, normal-文本")
    private String badgeType;

    @Schema(description = "徽章样式变体")
    private String badgeVariants;

    @Schema(description = "内嵌页面URL地址")
    private String iframeSrc;

    @Schema(description = "外部链接URL地址")
    private String link;

    @Schema(description = "子节点")
    private List<PermMenuResult> children;

    public Double getSortNo(){
        return sortNo == null ? 0 : sortNo;
    }

}
