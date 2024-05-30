package cn.bootx.platform.iam.core.permission.entity;

import cn.bootx.platform.common.core.function.EntityBaseFunction;
import cn.bootx.platform.common.mybatisplus.base.MpBaseEntity;
import cn.bootx.platform.iam.code.PermissionCode;
import cn.bootx.platform.iam.core.permission.convert.PermConvert;
import cn.bootx.platform.iam.dto.permission.PermMenuDto;
import cn.bootx.platform.iam.param.permission.PermMenuParam;
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
public class PermMenu extends MpBaseEntity implements EntityBaseFunction<PermMenuDto> {

    /** 父id */
//    @TableField(updateStrategy = NEVER)
    private Long parentId;

    /** 关联终端code */
    private String clientCode;

    /** 菜单标题 */
    private String title;

    /** 路由名称，建议唯一 */
    private String name;

    /** 资源编码(权限码) */
    private String permCode;

    /** 是否有效 */
    private boolean effect;

    /** 菜单图标 */
    private String icon;

    /** 是否隐藏 */
    private boolean hidden;

    /** 是否隐藏子菜单 */
    private boolean hideChildrenInMenu;

    /** 组件 */
    private String component;

    /** 路径 */
    private String path;

    /**
     * 菜单跳转地址(重定向)
     */
    private String redirect;

    /**
     * 菜单排序
     */
    private Double sortNo;

    /**
     * 类型（0：一级菜单；1：子菜单 ；2：资源）
     * @see PermissionCode
     */
    private Integer menuType;

    /* meta */
    /**
     * 是否缓存页面
     */
    private boolean keepAlive;

    /** 打开方式是否为外部打开 */
    private boolean targetOutside;

    /** 隐藏的标题内容 */
    private boolean hiddenHeaderContent;

    /**
     *
     * TODO 替换为非关键词, 来兼容更多类型数据库
     */
    @TableField("`admin`")
    private boolean admin;

    /** 描述 */
    private String remark;

    public static PermMenu init(PermMenuParam in) {
        return PermConvert.CONVERT.convert(in);
    }

    @Override
    public PermMenuDto toDto() {
        return PermConvert.CONVERT.convert(this);
    }

}
