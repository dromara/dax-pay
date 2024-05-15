package cn.bootx.platform.iam.core.upms.entity;

import cn.bootx.platform.common.mybatisplus.base.MpIdEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 用户数据范围关联关系
 *
 * @author xxm
 * @since 2021/12/23
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@TableName("iam_user_data_role")
public class UserDataRole extends MpIdEntity {

    /** 用户id */
    private Long userId;

    /** 数据权限id */
    private Long roleId;

}
