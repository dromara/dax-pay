package cn.bootx.platform.iam.core.permission.dao;

import cn.bootx.platform.iam.core.permission.entity.PermMenu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 权限配置
 *
 * @author xxm
 * @since 2021/8/3
 */
@Mapper
public interface PermMenuMapper extends BaseMapper<PermMenu> {

}
