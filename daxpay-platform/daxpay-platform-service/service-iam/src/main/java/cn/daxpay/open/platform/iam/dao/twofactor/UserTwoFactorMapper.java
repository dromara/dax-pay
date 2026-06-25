package cn.daxpay.open.platform.iam.dao.twofactor;

import cn.daxpay.open.platform.iam.entity.twofactor.UserTwoFactor;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/// # 用户双因素认证绑定 Mapper
///
@Mapper
public interface UserTwoFactorMapper extends MPJBaseMapper<UserTwoFactor> {
}
