package cn.daxpay.open.platform.iam.dao.upms;

import cn.daxpay.open.platform.iam.entity.upms.RoleMenu;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/// # 角色权限关系
///
@Mapper
public interface RoleMenuMapper extends MPJBaseMapper<RoleMenu> {

    void saveAll(@Param("roleMenus") List<RoleMenu> roleMenus);

}
