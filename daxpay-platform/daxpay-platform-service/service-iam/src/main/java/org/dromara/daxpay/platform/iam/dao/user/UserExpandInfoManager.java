package org.dromara.daxpay.platform.iam.dao.user;

import org.dromara.daxpay.platform.common.mybatisplus.impl.BaseManager;
import org.dromara.daxpay.platform.iam.entity.user.UserExpandInfo;
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
