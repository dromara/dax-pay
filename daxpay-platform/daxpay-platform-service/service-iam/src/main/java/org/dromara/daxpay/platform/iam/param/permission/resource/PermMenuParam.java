package org.dromara.daxpay.platform.iam.param.permission.resource;

import org.dromara.daxpay.platform.core.validation.ValidationGroup;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;


@Data
@Accessors(chain = true)
@Schema(title = "菜单权限")
public class PermMenuParam {

    @NotNull(groups = { ValidationGroup.edit.class }, message = "{validation.field.id.notNull}")
    @Schema(description = "主键")
    private Long id;

    @Schema(description = "父id")
    private Long pid;

    @Schema(description = "菜单编码")
    private String menuCode;

    @NotBlank(message = "{validation.field.clientCode.notBlank}")
    @Schema(description = "关联应用code")
    private String clientCode;

    @NotBlank(message = "{validation.field.titleCn.notBlank}")
    @Schema(description = "菜单标题-中文")
    private String titleCn;

    @NotBlank(message = "{validation.field.titleEn.notBlank}")
    @Schema(description = "菜单标题-英文")
    private String titleEn;

    @NotBlank(message = "{validation.field.i18nKey.notBlank}")
    @Schema(description = "国际化key")
    private String i18nKey;

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

    @Schema(description = "是否开启页面缓存")
    private boolean keepAlive;

    @Schema(description = "是否固定标签页")
    private boolean affixTab;

    @Schema(description = "菜单类型 @see org.dromara.daxpay.platform.core.enums.perm.MenuTypeEnum")
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

}
