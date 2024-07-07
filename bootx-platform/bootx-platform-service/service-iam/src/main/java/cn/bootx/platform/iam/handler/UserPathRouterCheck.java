package cn.bootx.platform.iam.handler;

import cn.bootx.platform.common.config.BootxConfigProperties;
import cn.bootx.platform.common.spring.util.WebServletUtil;
import cn.bootx.platform.core.entity.UserDetail;
import cn.bootx.platform.iam.service.upms.UserRolePremService;
import cn.bootx.platform.starter.auth.service.RouterCheck;
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

    private final UserRolePremService userRoleService;

    private final BootxConfigProperties configProperties;

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
        if (UserDetailOpt.isEmpty()) {
            return false;
        }
        UserDetail userDetail = UserDetailOpt.get();
        // 获取用户拥有的请求路径权限
        List<String> paths = userRoleService.findPathByUser(userDetail.getId(), method, configProperties.getClientCode());
        return paths.stream().anyMatch(pattern -> matcher.match(pattern, path));
    }

}
