package org.dromara.daxpay.platform.iam.entity.upms;

import org.dromara.daxpay.platform.common.mybatisplus.base.MpIdEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/// # 角色权限码关联关系
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("iam_role_code")
public class RoleCode extends MpIdEntity {

    /// 角色id
    private Long roleId;

    /// 权限码
    private Long codeId;
}
