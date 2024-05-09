package cn.bootx.platform.baseapi.controller;

import cn.bootx.platform.baseapi.dto.captcha.CaptchaDataResult;
import cn.bootx.platform.common.core.annotation.IgnoreAuth;
import cn.bootx.platform.common.core.rest.Res;
import cn.bootx.platform.common.core.rest.ResResult;
import cn.bootx.platform.baseapi.core.captcha.service.CaptchaService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 验证码服务
 *
 * @author xxm
 * @since 2021/8/2
 */
@Tag(name = "验证码服务")
@RestController
@RequestMapping("/captcha")
@RequiredArgsConstructor
public class CaptchaController {

    private final CaptchaService captchaService;

    @IgnoreAuth
    @Operation(summary = "获取图片验证码")
    @PostMapping("/imgCaptcha")
    public ResResult<CaptchaDataResult> imgCaptcha() {
        return Res.ok(captchaService.imgCaptcha());
    }

}
