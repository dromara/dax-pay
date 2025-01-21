package cn.bootx.platform.iam.entity.upms;

import cn.bootx.platform.common.mybatisplus.base.MpIdEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 角色菜单关联关系
 *
 * @author xxm
 * @since 2021/8/3
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("iam_role_menu")
public class RoleMenu extends MpIdEntity {

    /**
     * 角色id
     */
    private Long roleId;

    /**
     * 终端编码
     */
    private String clientCode;

    /**
     * 菜单
     */
    private Long menuId;

}
