package cn.bootx.platform.iam.handler;

import cn.bootx.platform.iam.core.user.dao.UserExpandInfoManager;
import cn.bootx.platform.iam.core.user.entity.UserExpandInfo;
import cn.bootx.platform.iam.exception.user.UserInfoNotExistsException;
import cn.bootx.platform.starter.auth.cache.SessionCacheLocal;
import cn.bootx.platform.starter.auth.entity.UserStatus;
import cn.bootx.platform.starter.auth.service.UserStatusService;
import cn.bootx.platform.starter.auth.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * 用户状态获取实现类
 * @author xxm
 * @since 2023/11/25
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserStatusServiceImpl implements UserStatusService {

    private final UserExpandInfoManager userExpandInfoManager;

    /**
     * 获取用户状态
     */
    @Override
    public UserStatus getUserStatus(){
        UserStatus userStatus = SessionCacheLocal.getUserStatusContext();
        if (Objects.isNull(userStatus)){
            UserExpandInfo userExpandInfo = userExpandInfoManager.findById(SecurityUtil.getUserId()).orElseThrow(UserInfoNotExistsException::new);
            userStatus = new UserStatus()
                    .setExpirePassword(userExpandInfo.isExpirePassword())
                    .setInitialPassword(userExpandInfo.isInitialPassword());
            SessionCacheLocal.putUserStatus(userStatus);
        }
        return userStatus;
    }
}
