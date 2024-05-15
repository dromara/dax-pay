package cn.bootx.platform.iam.core.upms.dao;

import cn.bootx.platform.iam.core.upms.entity.RoleMenu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 角色权限关系
 *
 * @author xxm
 * @since 2021/8/3
 */
@Mapper
public interface RoleMenuMapper extends BaseMapper<RoleMenu> {

    void saveAll(@Param("roleMenus") List<RoleMenu> roleMenus);

}
