package cn.bootx.platform.iam.entity.upms;

import cn.bootx.platform.common.mybatisplus.base.MpIdEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 角色权限码关联关系
 * @author xxm
 * @since 2024/7/3
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@RequiredArgsConstructor
@AllArgsConstructor
@TableName("iam_role_code")
public class RoleCode extends MpIdEntity {

    /**
     * 角色id
     */
    private Long roleId;

    /**
     * 权限码
     */
    private Long codeId;
}
