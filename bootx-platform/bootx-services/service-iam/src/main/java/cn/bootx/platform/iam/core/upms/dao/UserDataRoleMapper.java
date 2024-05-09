package cn.bootx.platform.iam.core.upms.dao;

import cn.bootx.platform.iam.core.upms.entity.UserDataRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户数据权限角色关系
 * @author xxm
 * @since 2021/12/23
 */
@Mapper
public interface UserDataRoleMapper extends BaseMapper<UserDataRole> {

    void saveAll(@Param("userDataRoles") List<UserDataRole> userDataRoles);

}
