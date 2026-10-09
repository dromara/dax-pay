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
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

/// # 域名校验文件网关响应
///
/// 响应微信/支付宝对域名校验文件的抓取请求，由 Nginx 将校验文件形态的请求反代到后端，
/// 按完整文件名全局查库原样返回（查不到 404）：
/// - 域名根路径：微信公众号网页授权域名 / JS接口安全域名 / 小程序业务域名的 `MP_verify_*.txt`；
/// - 码牌与收银台目录（`/m/`、`/cm/`、`/am/`）：微信「扫普通链接二维码」随机名 .txt、
///   支付宝「关联普通二维码」32位hex .html（两平台均要求校验文件放规则 URL 最后一级子目录）。
///
/// 该端点对所有访问开放；支付宝要求校验文件 URL 不得重定向，本端点原样 200 返回内容。
@Slf4j
@IgnoreAuth
@Tag(name = "域名校验文件网关")
@RestController
@RequiredArgsConstructor
public class WxVerifyGatewayController {

    private final WxDomainVerifyService wxDomainVerifyService;

    /// 响应域名校验文件内容，查不到返回 404
    @Operation(summary = "域名校验文件")
    @GetMapping({
            "/{fileName:[A-Za-z0-9_]{1,64}\\.(?:txt|html)}",
            "/m/{fileName:[A-Za-z0-9_]{1,64}\\.(?:txt|html)}",
            "/cm/{fileName:[A-Za-z0-9_]{1,64}\\.(?:txt|html)}",
            "/am/{fileName:[A-Za-z0-9_]{1,64}\\.(?:txt|html)}"
    })
    public ResponseEntity<String> verify(@PathVariable("fileName") String fileName) {
        Optional<String> content = wxDomainVerifyService.findContentByFileName(fileName);
        return content.map(body -> ResponseEntity.ok()
                        .contentType(MediaType.TEXT_PLAIN)
                        .body(body))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

}
