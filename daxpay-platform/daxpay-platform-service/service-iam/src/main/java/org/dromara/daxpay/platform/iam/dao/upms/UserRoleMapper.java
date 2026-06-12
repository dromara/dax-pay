package org.dromara.daxpay.platform.iam.dao.upms;

import org.dromara.daxpay.platform.iam.entity.upms.UserRole;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/// # 用户角色关系
///
@Mapper
public interface UserRoleMapper extends MPJBaseMapper<UserRole> {

    void saveAll(@Param("userRoles") List<UserRole> userRoles);

}
