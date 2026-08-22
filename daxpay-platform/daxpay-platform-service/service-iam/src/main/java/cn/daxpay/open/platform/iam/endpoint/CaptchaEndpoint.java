package cn.daxpay.open.platform.iam.endpoint;

import cn.daxpay.open.platform.core.annotation.IgnoreAuth;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.result.Result;
import cn.daxpay.open.platform.iam.auth.service.CaptchaService;
import cn.daxpay.open.platform.iam.result.captcha.CaptchaDataResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/// # 验证码端点
///
/// 使用 POST 而非 GET: 一次性凭证接口若用 GET, 响应会被 CDN(如腾讯 EdgeOne)按默认规则缓存,
/// 导致所有用户拿到同一张验证码, 防刷机制失效
@IgnoreAuth
@Tag(name = "验证码")
@RestController
@RequestMapping("/captcha")
@RequiredArgsConstructor
public class CaptchaEndpoint {

    private final CaptchaService captchaService;

    @Operation(summary = "获取图形验证码")
    @PostMapping("/image")
    public Result<CaptchaDataResult> getImageCaptcha() {
        return Res.ok(captchaService.generateCaptcha());
    }
}
