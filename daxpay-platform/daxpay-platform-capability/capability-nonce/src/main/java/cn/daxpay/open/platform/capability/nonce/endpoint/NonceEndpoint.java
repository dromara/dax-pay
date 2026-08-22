package cn.daxpay.open.platform.capability.nonce.endpoint;

import cn.daxpay.open.platform.capability.nonce.result.NonceResult;
import cn.daxpay.open.platform.capability.nonce.service.NonceService;
import cn.daxpay.open.platform.core.annotation.IgnoreAuth;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/// # Nonce生成端点
///
/// 使用 POST 而非 GET: 一次性凭证接口若用 GET, 响应会被 CDN(如腾讯 EdgeOne)按默认规则缓存,
/// 导致所有用户拿到同一份 nonce 而无法登录
@IgnoreAuth
@Tag(name = "防重放Nonce")
@RestController
@RequestMapping("/nonce")
@RequiredArgsConstructor
public class NonceEndpoint {

    private final NonceService nonceService;

    @Operation(summary = "获取Nonce值")
    @PostMapping("/generate")
    public Result<NonceResult> generate() {
        return Res.ok(nonceService.generate());
    }

}
