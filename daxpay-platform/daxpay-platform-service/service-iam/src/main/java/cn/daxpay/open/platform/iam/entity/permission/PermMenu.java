package cn.daxpay.open.platform.iam.entity.permission;

import cn.daxpay.open.platform.common.mybatisplus.base.MpBaseEntity;
import cn.daxpay.open.platform.common.mybatisplus.function.ToResult;
import cn.daxpay.open.platform.iam.convert.permission.PermMenuConvert;
import cn.daxpay.open.platform.iam.param.permission.resource.PermMenuParam;
import cn.daxpay.open.platform.iam.result.permission.resource.PermMenuResult;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 权限配置
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("iam_perm_menu")
public class PermMenu extends MpBaseEntity implements ToResult<PermMenuResult> {

    /// 父菜单ID,0表示根菜单
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private Long pid;

    /// 菜单编码
    private String menuCode;

    /// 关联终端code
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String clientCode;

    /// 国际化key
    private String i18nKey;

    /// 菜单图标
    private String icon;

    /// 是否隐藏
    private boolean hidden;

    /// 是否隐藏子菜单
    private boolean hideChildrenMenu;

    /// 组件
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String component;

    /// 访问路径
    private String path;

    /// 菜单跳转地址(重定向)
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String redirect;

    /// 菜单排序
    private Double sortNo;

    /// 是否开启页面缓存
    private boolean keepAlive;

    /// 是否固定标签页
    private boolean affixTab;

    /// 菜单类型
    /// @see cn.daxpay.open.platform.core.enums.perm.MenuTypeEnum
    private String menuType;

    /// 徽章显示文本
    private String badge;

    /// 徽章类型: dot-圆点, normal-文本
    private String badgeType;

    /// 徽章样式变体
    private String badgeVariants;

    /// 内嵌页面URL地址
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String iframeSrc;

    /// 外部链接URL地址
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String link;

    public static PermMenu init(PermMenuParam in) {
        return PermMenuConvert.CONVERT.convert(in);
    }

    @Override
    public PermMenuResult toResult() {
        return PermMenuConvert.CONVERT.convert(this);
    }

}

