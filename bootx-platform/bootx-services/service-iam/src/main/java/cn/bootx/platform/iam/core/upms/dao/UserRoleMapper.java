package cn.bootx.platform.iam.core.upms.dao;

import cn.bootx.platform.iam.core.upms.entity.UserRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户角色关系
 *
 * @author xxm
 * @since 2021/8/3
 */
@Mapper
public interface UserRoleMapper extends BaseMapper<UserRole> {

    void saveAll(@Param("userRoles") List<UserRole> userRoles);

}
