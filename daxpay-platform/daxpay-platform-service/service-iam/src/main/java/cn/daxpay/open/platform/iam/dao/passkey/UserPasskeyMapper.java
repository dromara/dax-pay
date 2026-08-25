package cn.daxpay.open.platform.iam.dao.passkey;

import cn.daxpay.open.platform.iam.entity.passkey.UserPasskey;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/// # 用户通行密钥 Mapper
///
@Mapper
public interface UserPasskeyMapper extends MPJBaseMapper<UserPasskey> {
}
