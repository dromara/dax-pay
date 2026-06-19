package cn.daxpay.open.platform.baseapi.controller.base;

import cn.daxpay.open.platform.baseapi.service.base.KeyGenService;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.result.Result;
import cn.daxpay.open.platform.core.rest.result.RsaKeyPairResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/// # 密钥生成控制器
///
@Tag(name = "密钥生成")
@RestController
@RequestMapping("/key-gen")
@RequiredArgsConstructor
public class KeyGenController {

    private final KeyGenService keyGenService;

    @Operation(summary = "生成RSA密钥对")
    @PostMapping("/gen-rsa-key-pair")
    public Result<RsaKeyPairResult> genRsaKeyPair() {
        return Res.ok(keyGenService.genRsaKeyPair());
    }

    @Operation(summary = "生成AES通信密钥")
    @PostMapping("/gen-aes-secret-key")
    public Result<String> genAesSecretKey() {
        return Res.ok(keyGenService.genAesSecretKey());
    }
}
