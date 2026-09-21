package cn.daxpay.open.payment.unipay.client.controller;

import cn.daxpay.open.platform.core.annotation.IgnoreAuth;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/// # 开放接口自检探针
///
/// 部署自检专用端点, 供运营端「平台配置 -> 端点配置」检查「后端 API 地址」是否可用:
/// 后端发起 GET `{后端API地址}/unipay/callback/ping`, 按 HTTP 状态码与响应体标识判定。
///
/// 路径刻意落在 `/unipay/callback` 前缀内(与通道回调同组), 原因有二:
/// - 部署面板网关按接口组放行, 「通道回调」未开放时该前缀整体 404, 检查即可直接暴露出来;
/// - 该前缀正是通道回调地址(`{后端API地址}/unipay/callback/{mchNo}/{channelMchNo}/{通道}/...`)所在,
///   探针通过即代表通道回调链路可达——这正是「后端 API 地址」的用途。
///
/// 免登录、免签名、无副作用: 不查库/缓存, 不写审计, 只返回固定标识文本; 未启用网关直连后端同样可达。
@Tag(name = "开放接口自检探针")
@RestController
@RequestMapping("/unipay/callback")
@IgnoreAuth
public class UnipayPingController {

    /// 探针响应标识(检查端按该标识判定响应来自 daxpay 后端, 而非 CDN/其他站点)
    public static final String PING_MARKER = "daxpay-ping:unipay-callback";

    /// 自检探针
    @Operation(summary = "自检探针")
    @GetMapping("/ping")
    public String ping() {
        return PING_MARKER;
    }
}
