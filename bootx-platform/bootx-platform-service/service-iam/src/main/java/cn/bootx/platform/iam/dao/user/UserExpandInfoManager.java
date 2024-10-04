package cn.bootx.platform.iam.dao.user;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.iam.entity.user.UserExpandInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

/**
 * 用户扩展信息
 *
 * @author xxm
 * @since 2022/1/8
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class UserExpandInfoManager extends BaseManager<UserExpandInfoMapper, UserExpandInfo> {

}
