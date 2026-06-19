package cn.daxpay.open.platform.iam.dao.user;

import cn.daxpay.open.platform.iam.entity.user.UserPasswordHistory;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/// # 用户密码历史
///
@Mapper
public interface UserPasswordHistoryMapper extends BaseMapper<UserPasswordHistory> {
}
