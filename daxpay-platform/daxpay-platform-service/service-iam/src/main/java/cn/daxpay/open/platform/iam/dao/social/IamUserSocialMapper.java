package cn.daxpay.open.platform.iam.dao.social;

import cn.daxpay.open.platform.iam.entity.social.IamUserSocial;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/// # 用户第三方账号绑定 Mapper
///
@Mapper
public interface IamUserSocialMapper extends MPJBaseMapper<IamUserSocial> {
}
