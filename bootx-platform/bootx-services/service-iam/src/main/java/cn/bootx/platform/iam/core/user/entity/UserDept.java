package cn.bootx.platform.iam.core.user.entity;

import cn.bootx.platform.common.mybatisplus.base.MpIdEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 用户部门关联关系
 *
 * @author xxm
 * @since 2021/9/29
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@TableName("iam_user_dept")
public class UserDept extends MpIdEntity {

    /** 用户id */
    private Long userId;

    /** 部门id */
    private Long deptId;

}
