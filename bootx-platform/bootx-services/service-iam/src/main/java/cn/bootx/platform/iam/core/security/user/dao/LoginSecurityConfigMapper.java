package cn.bootx.platform.iam.core.security.user.dao;

import cn.bootx.platform.iam.core.security.user.entity.LoginSecurityConfig;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 登录安全策略
 * @author xxm
 * @since 2023-09-19
 */
@Mapper
public interface LoginSecurityConfigMapper extends BaseMapper<LoginSecurityConfig> {
}
