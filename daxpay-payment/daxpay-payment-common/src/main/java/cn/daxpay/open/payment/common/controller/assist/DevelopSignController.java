package cn.daxpay.open.payment.common.controller.assist;

import cn.daxpay.open.platform.core.annotation.IgnoreAuth;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.result.Result;
import cn.daxpay.open.payment.old.pay.service.develop.DevelopSignService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/// # 签名开发调试服务
///
@IgnoreAuth
@Validated
@Tag(name = "签名开发调试服务")
@RestController
@RequestMapping("/develop/sign")
@RequiredArgsConstructor
public class DevelopSignController {

    private final DevelopSignService developSignService;

    @Operation(summary = "生成签名字符串")
    @PostMapping("/gen-sign-str")
    public Result<String> genSignStr(@RequestBody GenSignParam param) {
        return Res.ok(developSignService.genSignStr(param.getParam()));
    }

    @Operation(summary = "生成签名")
    @PostMapping("/gen-sign")
    public Result<String> genSign(@RequestBody GenSignParam param) {
        return Res.ok(developSignService.genSign(param.getParam(), param.getPrivateKey()));
    }

    @Operation(summary = "验证签名")
    @PostMapping("/verify-sign")
    public Result<Boolean> verifySign(@RequestBody VerifySignParam param) {
        return Res.ok(developSignService.verifySign(param.getParam(), param.getPublicKey()));
    }

    /// # 生成签名参数
    ///
    @Data
    @Accessors(chain = true)
    public static class GenSignParam {
        /// 待签名参数
        private String param;
        /// 私钥
        private String privateKey;

    }

    /// # 验证签名参数
    ///
    @Data
    @Accessors(chain = true)
    public static class VerifySignParam {
        /// 待验证参数
        private String param;
        /// 公钥
        private String publicKey;
    }
}

