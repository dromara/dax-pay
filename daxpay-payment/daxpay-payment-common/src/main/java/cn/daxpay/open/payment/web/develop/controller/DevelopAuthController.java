package cn.daxpay.open.payment.web.develop.controller;

import cn.daxpay.open.payment.unipay.result.assist.AuthResult;
import cn.daxpay.open.payment.unipay.result.assist.AuthUrlResult;
import cn.daxpay.open.payment.web.develop.service.DevelopAuthService;
import cn.daxpay.open.platform.core.annotation.PermCode;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/// 认证调试(管理)
///
/// 首期仅支付宝: 生成授权中间页链接并轮询认证结果。
@Validated
@PermCode(menuCode = "develop:auth")
@Tag(name = "认证调试服务")
@RestController
@RequestMapping("/admin/develop/auth")
@RequiredArgsConstructor
public class DevelopAuthController {

    private final DevelopAuthService developAuthService;

    @PermCode(code = "view", nameCn = "查看", nameEn = "View")
    @Operation(summary = "生成支付宝授权链接")
    @PostMapping("/generate-auth-url")
    public Result<AuthUrlResult> generateAuthUrl() {
        return Res.ok(developAuthService.generateAlipayAuthUrl());
    }

    @PermCode(code = "view", nameCn = "查看", nameEn = "View")
    @Operation(summary = "通过查询码获取认证结果")
    @GetMapping("/query-auth-result")
    public Result<AuthResult> queryAuthResult(
            @NotBlank(message = "{validation.field.queryCode.notBlank}") String queryCode) {
        return Res.ok(developAuthService.queryAuthResult(queryCode));
    }
}
