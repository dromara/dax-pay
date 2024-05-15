package cn.bootx.platform.starter.auth.endpoint;

import cn.bootx.platform.common.core.rest.Res;
import cn.bootx.platform.common.core.rest.ResResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 基础登录退出操作
 *
 * @author xxm
 * @since 2021/7/30
 */
@Tag(name = "认证相关")
@RestController
@RequestMapping("/token")
@AllArgsConstructor
public class TokenEndpoint {

    private final TokenService tokenService;

    @Operation(summary = "普通登录")
    @PostMapping("/login")
    public ResResult<String> login(HttpServletRequest request, HttpServletResponse response) {
        return Res.ok(tokenService.login(request, response));
    }

    @Operation(summary = "退出")
    @PostMapping("/logout")
    public ResResult<String> logout() {
        tokenService.logout();
        return Res.ok();
    }

}
