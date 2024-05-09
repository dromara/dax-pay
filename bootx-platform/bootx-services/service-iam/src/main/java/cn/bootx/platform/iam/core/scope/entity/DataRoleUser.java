package cn.bootx.platform.iam.core.scope.entity;

import cn.bootx.platform.common.mybatisplus.base.MpIdEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 数据范围用户关联配置
 *
 * @author xxm
 * @since 2021/12/23
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@TableName("iam_data_role_user")
public class DataRoleUser extends MpIdEntity {

    /** 数据范围id */
    private Long roleId;

    /** 用户id */
    private Long userId;

}
