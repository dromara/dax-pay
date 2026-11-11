package cn.daxpay.open.platform.iam.dao.user;

import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import cn.daxpay.open.platform.iam.entity.user.UserExpandInfo;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;

/// # 用户扩展信息
///
@Slf4j
@Repository
@RequiredArgsConstructor
public class UserExpandInfoManager extends BaseManager<UserExpandInfoMapper, UserExpandInfo> {

    /// 记录登录轨迹(登录成功时调用)
    ///
    /// 用户详情的「最后登录时间/IP/登录次数」直接读本表三列, 此前无任何写入路径导致恒为空;
    /// 登录次数用 DB 级原子自增, 避免并发登录互相覆盖。
    /// lastLoginTime 与 currentLoginTime 均置本次登录时间(页面「最后登录时间」语义为最近一次成功登录)。
    public void recordLoginTrace(Long userId, String loginIp, OffsetDateTime loginTime) {
        if (findById(userId).isEmpty()) {
            // 兜底补建: 兼容历史用户缺少扩展信息记录的情况
            var expandInfo = new UserExpandInfo();
            expandInfo.setId(userId);
            expandInfo.setLastLoginTime(loginTime);
            expandInfo.setCurrentLoginTime(loginTime);
            expandInfo.setLastLoginIp(loginIp);
            expandInfo.setLoginCount(1);
            save(expandInfo);
            return;
        }
        var update = lambdaUpdate()
                .eq(UserExpandInfo::getId, userId)
                .set(UserExpandInfo::getLastLoginTime, loginTime)
                .set(UserExpandInfo::getCurrentLoginTime, loginTime)
                .setSql("login_count = COALESCE(login_count, 0) + 1");
        // IP 未取到时保留原值, 不写"未知"占位
        if (StrUtil.isNotBlank(loginIp)) {
            update.set(UserExpandInfo::getLastLoginIp, loginIp);
        }
        update.update();
    }

}
