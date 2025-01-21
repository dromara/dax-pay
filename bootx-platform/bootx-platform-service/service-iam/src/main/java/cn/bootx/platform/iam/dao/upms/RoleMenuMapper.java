package cn.bootx.platform.iam.dao.upms;

import cn.bootx.platform.iam.entity.upms.RoleMenu;
import com.github.yulichang.base.MPJBaseMapper;
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
public interface RoleMenuMapper extends MPJBaseMapper<RoleMenu> {

    void saveAll(@Param("roleMenus") List<RoleMenu> roleMenus);

}
