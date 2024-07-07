package cn.bootx.platform.starter.audit.log.core.db.dao;

import cn.bootx.platform.starter.audit.log.core.db.entity.LoginLogDb;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 登录日志
 *
 * @author xxm
 * @since 2021/8/12
 */
@Mapper
public interface LoginLogDbMapper extends MPJBaseMapper<LoginLogDb> {

}
