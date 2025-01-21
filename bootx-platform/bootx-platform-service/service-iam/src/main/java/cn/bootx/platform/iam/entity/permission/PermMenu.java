package cn.bootx.platform.iam.entity.permission;

import cn.bootx.platform.common.mybatisplus.base.MpBaseEntity;
import cn.bootx.platform.common.mybatisplus.function.ToResult;
import cn.bootx.platform.iam.convert.permission.PermMenuConvert;
import cn.bootx.platform.iam.param.permission.PermMenuParam;
import cn.bootx.platform.iam.result.permission.PermMenuResult;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 权限配置
 *
 * @author xxm
 * @since 2021/8/3
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("iam_perm_menu")
public class PermMenu extends MpBaseEntity implements ToResult<PermMenuResult> {

    /** 父id */
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private Long pid;

    /** 关联终端code */
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String clientCode;

    /** 菜单标题 */
    private String title;

    /** 路由名称，建议唯一 */
    private String name;

    /** 菜单图标 */
    private String icon;

    /** 是否隐藏 */
    private boolean hidden;

    /** 是否隐藏子菜单 */
    private boolean hideChildrenMenu;

    /** 组件 */
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String component;

    /** 访问路径 */
    private String path;

    /** 菜单跳转地址(重定向) */
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String redirect;

    /** 菜单排序 */
    private Double sortNo;

    /** 是否是一级菜单 */
    private boolean root;

    /* meta相关信息 */
    /**
     * 是否缓存页面
     */
    private boolean keepAlive;

    /** 是否为外部打开 */
    private boolean targetOutside;

    /** 是否全屏打开 */
    private boolean fullScreen;

    /** 描述 */
    private String remark;

    public static PermMenu init(PermMenuParam in) {
        return PermMenuConvert.CONVERT.convert(in);
    }

    @Override
    public PermMenuResult toResult() {
        return PermMenuConvert.CONVERT.convert(this);
    }

}
