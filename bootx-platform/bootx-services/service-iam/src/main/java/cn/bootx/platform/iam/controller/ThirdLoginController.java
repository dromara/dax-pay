package cn.bootx.platform.iam.controller;

import cn.bootx.platform.common.core.annotation.IgnoreAuth;
import cn.bootx.platform.starter.auth.entity.ThirdAuthCode;
import cn.bootx.platform.iam.core.auth.service.ThirdLoginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.zhyd.oauth.model.AuthCallback;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 三方登录
 *
 * @author xxm
 * @since 2022/6/29
 */
@Slf4j
@IgnoreAuth
@Tag(name = "三方登录")
@RestController
@RequestMapping("/auth/third")
@RequiredArgsConstructor
public class ThirdLoginController {

    private final ThirdLoginService thirdLoginService;

    @Operation(summary = "跳转到登陆页")
    @GetMapping("/toLoginUrl/{loginType}")
    public void toLoginUrl(@PathVariable("loginType") String loginType, HttpServletResponse response)
            throws IOException {
        String loginUrl = thirdLoginService.getLoginUrl(loginType);
        response.sendRedirect(loginUrl);
    }

    @Operation(summary = "扫码后回调")
    @GetMapping("/callback/{loginType}")
    public ModelAndView callback(@PathVariable("loginType") String loginType, AuthCallback callback) {
        ThirdAuthCode authCode = thirdLoginService.getAuthCode(loginType, callback);
        // 回传给前台
        return new ModelAndView("thirdLoginCallback").addObject("authCode", authCode);
    }

}
