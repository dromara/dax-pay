package cn.bootx.platform.iam.handler;

import cn.bootx.platform.common.core.entity.UserDetail;
import cn.bootx.platform.common.spring.util.WebServletUtil;
import cn.bootx.platform.iam.core.upms.service.RolePathService;
import cn.bootx.platform.starter.auth.entity.UserStatus;
import cn.bootx.platform.starter.auth.service.RouterCheck;
import cn.bootx.platform.starter.auth.service.UserStatusService;
import cn.bootx.platform.starter.auth.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import java.util.List;
import java.util.Optional;

/**
 * 用户路径路由拦截
 *
 * @author xxm
 * @since 2021/12/21
 */
@Component
@RequiredArgsConstructor
public class UserPathRouterCheck implements RouterCheck {

    private final RolePathService rolePathService;

    private final UserStatusService userStatusService;

    private final AntPathMatcher matcher = new AntPathMatcher();

    @Override
    public int sortNo() {
        return 10;
    }

    @Override
    public boolean check(Object handler) {
        String method = WebServletUtil.getMethod();
        String path = WebServletUtil.getPath();

        Optional<UserDetail> UserDetailOpt = SecurityUtil.getCurrentUser();
        if (!UserDetailOpt.isPresent()) {
            return false;
        }
        // 初始密码或者密码过期
        UserStatus userStatus = userStatusService.getUserStatus();
        if (userStatus.isExpirePassword()||userStatus.isInitialPassword()){
            return false;
        }
        UserDetail userDetail = UserDetailOpt.get();
        List<String> paths = rolePathService.findSimplePathsByUser(method, userDetail.getId());
        return paths.stream().anyMatch(pattern -> matcher.match(pattern, path));
    }

}
