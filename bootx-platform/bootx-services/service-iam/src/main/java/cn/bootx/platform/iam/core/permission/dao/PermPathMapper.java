package cn.bootx.platform.iam.core.permission.dao;

import cn.bootx.platform.iam.core.permission.entity.PermPath;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
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
public interface PermPathMapper extends BaseMapper<PermPath> {

    void saveAll(@Param("permPaths") List<PermPath> permPaths);

}
