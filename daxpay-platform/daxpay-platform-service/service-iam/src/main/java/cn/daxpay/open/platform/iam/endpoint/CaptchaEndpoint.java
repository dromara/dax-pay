package cn.daxpay.open.platform.iam.endpoint;

import cn.daxpay.open.platform.core.annotation.IgnoreAuth;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.result.Result;
import cn.daxpay.open.platform.iam.auth.service.CaptchaService;
import cn.daxpay.open.platform.iam.result.captcha.CaptchaDataResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/// # 验证码端点
///
@IgnoreAuth
@Tag(name = "验证码")
@RestController
@RequestMapping("/captcha")
@RequiredArgsConstructor
public class CaptchaEndpoint {

    private final CaptchaService captchaService;

    @Operation(summary = "获取图形验证码")
    @GetMapping("/image")
    public Result<CaptchaDataResult> getImageCaptcha() {
        return Res.ok(captchaService.generateCaptcha());
    }
}
