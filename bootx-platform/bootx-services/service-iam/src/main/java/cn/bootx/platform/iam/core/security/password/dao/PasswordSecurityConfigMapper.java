package cn.bootx.platform.iam.core.security.password.dao;

import cn.bootx.platform.iam.core.security.password.entity.PasswordSecurityConfig;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 密码安全策略
 * @author xxm
 * @since 2023-09-20
 */
@Mapper
public interface PasswordSecurityConfigMapper extends BaseMapper<PasswordSecurityConfig> {
}
