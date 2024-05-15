package cn.bootx.platform.iam.core.upms.dao;

import cn.bootx.platform.iam.core.upms.entity.RolePath;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
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
public interface RolePathMapper extends BaseMapper<RolePath> {

    void saveAll(@Param("rolePaths") List<RolePath> rolePaths);

}
