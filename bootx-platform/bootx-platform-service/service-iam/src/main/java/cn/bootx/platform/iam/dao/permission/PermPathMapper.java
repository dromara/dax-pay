package cn.bootx.platform.iam.dao.permission;

import cn.bootx.platform.iam.entity.permission.PermPath;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 权限
 *
 * @author xxm
 * @since 2020/5/10 23:26
 */
@Mapper
public interface PermPathMapper extends MPJBaseMapper<PermPath> {

    void saveAll(@Param("permPaths") List<PermPath> permPaths);

}
