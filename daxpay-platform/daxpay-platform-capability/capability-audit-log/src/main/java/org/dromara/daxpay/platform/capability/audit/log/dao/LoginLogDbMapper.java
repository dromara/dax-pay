package org.dromara.daxpay.platform.capability.audit.log.dao;

import org.dromara.daxpay.platform.capability.audit.log.entity.LoginLogDb;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/// # 登录日志
///
@Mapper
public interface LoginLogDbMapper extends MPJBaseMapper<LoginLogDb> {

}
