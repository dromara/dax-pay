package cn.daxpay.open.platform.iam.entity.upms;

import cn.daxpay.open.platform.common.mybatisplus.base.MpIdEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/// # 角色菜单关联关系
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("iam_role_menu")
public class RoleMenu extends MpIdEntity {

    /// 角色id
    private Long roleId;

    /// 菜单
    private Long menuId;

}
