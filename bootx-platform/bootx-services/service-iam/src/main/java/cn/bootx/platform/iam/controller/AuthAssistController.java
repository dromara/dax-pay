package cn.bootx.platform.iam.controller;

import cn.bootx.platform.common.core.annotation.IgnoreAuth;
import cn.bootx.platform.common.core.rest.Res;
import cn.bootx.platform.common.core.rest.ResResult;
import cn.bootx.platform.iam.core.auth.service.AuthAssistService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xxm
 * @since 2021/9/9
 */
@Tag(name = "认证支撑接口")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthAssistController {

    private final AuthAssistService authAssistService;

    @IgnoreAuth
    @Operation(summary = "发送短信验证码")
    @PostMapping("/sendSmsCaptcha")
    public ResResult<Void> sendSmsCaptcha(String phone) {
        authAssistService.sendSmsCaptcha(phone);
        return Res.ok();
    }

}
