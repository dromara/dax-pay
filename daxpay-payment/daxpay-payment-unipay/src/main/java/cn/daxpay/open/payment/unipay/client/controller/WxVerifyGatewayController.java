package cn.daxpay.open.payment.unipay.client.controller;

import cn.daxpay.open.payment.merchant.service.wxverify.WxDomainVerifyService;
import cn.daxpay.open.platform.core.annotation.IgnoreAuth;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

/// # 微信域名校验文件网关响应
///
/// 响应微信对 MP_verify_xxx.txt 的校验请求（公众号网页授权域名 / JS接口安全域名 / 小程序业务域名）
/// 该端点对所有访问开放，由 Nginx 将 `/MP_verify_*.txt` 反代到后端
@Slf4j
@IgnoreAuth
@Tag(name = "微信域名校验")
@RestController
@RequestMapping
@RequiredArgsConstructor
public class WxVerifyGatewayController {

    private final WxDomainVerifyService wxDomainVerifyService;

    /// 响应微信域名校验文件内容，查不到返回 404
    @Operation(summary = "微信域名校验文件")
    @GetMapping("/MP_verify_{code}.txt")
    public ResponseEntity<String> verify(@PathVariable("code") String code) {
        Optional<String> content = wxDomainVerifyService.findContentByVerifyCode(code);
        return content.map(body -> ResponseEntity.ok()
                        .contentType(MediaType.TEXT_PLAIN)
                        .body(body))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

}
