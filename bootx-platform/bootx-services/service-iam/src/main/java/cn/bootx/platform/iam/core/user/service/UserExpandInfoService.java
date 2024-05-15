package cn.bootx.platform.iam.core.user.service;

import cn.bootx.platform.common.core.exception.BizException;
import cn.bootx.platform.iam.core.user.dao.UserExpandInfoManager;
import cn.bootx.platform.iam.core.user.entity.UserExpandInfo;
import cn.bootx.platform.iam.event.user.UserChangePasswordEvent;
import cn.bootx.platform.iam.event.user.UserRestartPasswordEvent;
import cn.bootx.platform.iam.exception.user.UserInfoNotExistsException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户扩展信息服务
 *
 * @author xxm
 * @since 2022/1/8
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserExpandInfoService {

    private final UserExpandInfoManager userExpandInfoManager;

    /**
     * 更新登录时间
     */
    @Async("asyncExecutor")
    public void updateLoginTime(Long userId) {
        UserExpandInfo userExpandInfo = userExpandInfoManager.findById(userId).orElseThrow(BizException::new);
        userExpandInfo.setLastLoginTime(userExpandInfo.getCurrentLoginTime()).setCurrentLoginTime(LocalDateTime.now());
        userExpandInfoManager.updateById(userExpandInfo);
    }

    /**
     * 密码过期处理, 更新状态用户状态
     */
    public void userExpirePwd(Long userId){
        UserExpandInfo userInfo = userExpandInfoManager.findById(userId)
                .orElseThrow(UserInfoNotExistsException::new);
        userInfo.setExpirePassword(true);
        userExpandInfoManager.updateById(userInfo);
    }

    /**
     * 密码修改
     */
    @EventListener(UserChangePasswordEvent.class)
    public void changePassword(UserChangePasswordEvent event){
        List<UserExpandInfo> userExpandInfos = userExpandInfoManager.findAllByIds(event.getUserIds());
        userExpandInfos.forEach(userExpandInfo -> userExpandInfo.setExpirePassword(false).setInitialPassword(false));
        userExpandInfoManager.updateAllById(userExpandInfos);
    }

    /**
     * 密码重置
     */
    @EventListener(UserRestartPasswordEvent.class)
    public void restartPassword(UserRestartPasswordEvent event){
        List<UserExpandInfo> userExpandInfos = userExpandInfoManager.findAllByIds(event.getUserIds());
        userExpandInfos.forEach(userExpandInfo -> userExpandInfo.setExpirePassword(false).setInitialPassword(false));
        userExpandInfoManager.updateAllById(userExpandInfos);
    }
}
