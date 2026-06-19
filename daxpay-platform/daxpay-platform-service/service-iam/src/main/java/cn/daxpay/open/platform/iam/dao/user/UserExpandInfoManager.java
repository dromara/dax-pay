package cn.daxpay.open.platform.iam.dao.user;

import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import cn.daxpay.open.platform.iam.entity.user.UserExpandInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

/// # 用户扩展信息
///
@Slf4j
@Repository
@RequiredArgsConstructor
public class UserExpandInfoManager extends BaseManager<UserExpandInfoMapper, UserExpandInfo> {

}
