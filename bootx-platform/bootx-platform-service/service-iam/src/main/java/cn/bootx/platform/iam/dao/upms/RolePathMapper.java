package cn.bootx.platform.iam.dao.upms;

import cn.bootx.platform.iam.entity.upms.RolePath;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 角色权限
 *
 * @author xxm
 * @since 2020/11/14
 */
@Mapper
public interface RolePathMapper extends MPJBaseMapper<RolePath> {

    void saveAll(@Param("rolePaths") List<RolePath> rolePaths);

}
