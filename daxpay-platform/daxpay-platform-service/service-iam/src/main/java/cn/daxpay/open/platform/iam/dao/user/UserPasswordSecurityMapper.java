package cn.daxpay.open.platform.iam.dao.user;

import cn.daxpay.open.platform.iam.entity.user.UserPasswordSecurity;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/// # 用户密码安全信息
///
@Mapper
public interface UserPasswordSecurityMapper extends MPJBaseMapper<UserPasswordSecurity> {
}
