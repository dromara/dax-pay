package cn.bootx.platform.iam.dao.role;

import cn.bootx.platform.iam.entity.role.Role;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 角色
 *
 * @author xxm
 * @since 2021/8/3
 */
@Mapper
public interface RoleMapper extends MPJBaseMapper<Role> {

}
