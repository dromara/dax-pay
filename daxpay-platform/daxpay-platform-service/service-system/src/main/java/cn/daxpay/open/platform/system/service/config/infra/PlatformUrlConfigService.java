package cn.daxpay.open.platform.system.service.config.infra;

import cn.daxpay.open.platform.common.i18n.util.I18nUtil;
import cn.daxpay.open.platform.system.convert.config.infra.PlatformUrlConfigConvert;
import cn.daxpay.open.platform.system.entity.config.platform.infra.PlatformUrlConfig;
import cn.daxpay.open.platform.system.enums.PlatformConfigTypeEnum;
import cn.daxpay.open.platform.system.param.config.infra.PlatformUrlCheckParam;
import cn.daxpay.open.platform.system.param.config.infra.PlatformUrlConfigParam;
import cn.daxpay.open.platform.system.result.config.infra.ConnectivityCheckResult;
import cn.daxpay.open.platform.system.result.config.infra.PlatformUrlConfigResult;
import cn.daxpay.open.platform.system.service.config.SystemPlatformConfigService;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.Objects;

/// # 平台端点配置服务
///
/// 管理系统访问地址等端点配置
@Slf4j
@Service
@RequiredArgsConstructor
public class PlatformUrlConfigService {

    private final SystemPlatformConfigService systemConfigService;

    /// HTTP 探测超时(毫秒)
    private static final int CHECK_TIMEOUT_MS = 5000;

    /// 后端 API 自检探针路径(部署自检端点, 见 UnipayPingController)
    private static final String BACKEND_PROBE_PATH = "/unipay/callback/ping";

    /// 探针响应标识(须与 UnipayPingController#PING_MARKER 一致, 跨模块不共享常量)
    private static final String BACKEND_PROBE_MARKER = "daxpay-ping:unipay-callback";

    /// 获取端点配置
    public PlatformUrlConfig getUrlConfig() {
        return systemConfigService.getOrCreateConfig(PlatformConfigTypeEnum.URL,
                PlatformUrlConfig.class,
                new PlatformUrlConfig());
    }

    /// 获取端点配置
    public PlatformUrlConfigResult findUrlConfig() {
        return PlatformUrlConfigConvert.CONVERT.toUrlResult(this.getUrlConfig());
    }

    /// 更新端点配置
    public void updateUrlConfig(PlatformUrlConfigParam param) {
        PlatformUrlConfig data = this.getUrlConfig();
        PlatformUrlConfigConvert.CONVERT.copy(param, data);
        systemConfigService.updateConfig(PlatformConfigTypeEnum.URL, data);
    }

    /// 检查端点连通性
    ///
    /// 后端 API 地址探测部署自检探针 [#BACKEND_PROBE_PATH], 其余端点仅校验 HTTP 可达。
    ///
    /// @param param 端点类型与可选 URL
    /// @return 探测结果
    public ConnectivityCheckResult checkUrl(PlatformUrlCheckParam param) {
        String urlType = StrUtil.trim(param.getUrlType());
        String targetUrl = StrUtil.blankToDefault(StrUtil.trim(param.getUrl()), resolveSavedUrl(urlType));
        if (StrUtil.isBlank(targetUrl)) {
            return ConnectivityCheckResult.fail(I18nUtil.get("error.system.url.notConfigured"));
        }
        targetUrl = StrUtil.removeSuffix(targetUrl, "/");

        // 后端 API 走部署自检探针(严格校验); 前端 SPA 仅校验 HTTP 可达
        boolean backendProbe = "backend".equalsIgnoreCase(urlType);
        String probeUrl = backendProbe ? targetUrl + BACKEND_PROBE_PATH : targetUrl;

        long start = System.currentTimeMillis();
        try (HttpResponse response = HttpRequest.get(probeUrl)
                .timeout(CHECK_TIMEOUT_MS)
                .setFollowRedirects(true)
                .execute()) {
            long latency = System.currentTimeMillis() - start;
            int status = response.getStatus();
            // 404 单独提示: 部署面板网关未开放该接口组(面板部署下检查失败的常见原因)
            if (backendProbe && status == 404) {
                return ConnectivityCheckResult.fail(
                        I18nUtil.get("error.system.url.notExposed", BACKEND_PROBE_PATH),
                        latency,
                        status);
            }
            if (status < 200 || status >= 400) {
                return ConnectivityCheckResult.fail(
                        I18nUtil.get("error.system.url.checkFailed", String.valueOf(status)),
                        latency,
                        status);
            }
            if (backendProbe) {
                // 响应体须带探针标识, 排除域名被 CDN/其他站点接管后返回 200 的情况
                String body = StrUtil.nullToEmpty(response.body());
                if (!body.contains(BACKEND_PROBE_MARKER)) {
                    return ConnectivityCheckResult.fail(
                            I18nUtil.get("error.system.url.probeMismatch"),
                            latency,
                            status);
                }
            }
            return ConnectivityCheckResult.ok(
                    I18nUtil.get("error.system.url.checkSuccess"),
                    latency,
                    status);
        } catch (Exception e) {
            long latency = System.currentTimeMillis() - start;
            log.warn("端点连通性检查失败: type={}, url={}, err={}", urlType, probeUrl, e.getMessage());
            return ConnectivityCheckResult.fail(
                    I18nUtil.get("error.system.url.networkError"),
                    latency,
                    null);
        }
    }

    /// 从已保存配置解析对应端点地址
    private String resolveSavedUrl(String urlType) {
        PlatformUrlConfig config = this.getUrlConfig();
        if (Objects.isNull(config) || StrUtil.isBlank(urlType)) {
            return null;
        }
        return switch (urlType.toLowerCase()) {
            case "admin" -> config.getAdminBaseUrl();
            case "merchant" -> config.getMerchantBaseUrl();
            case "paymentgateway" -> config.getPaymentGatewayBaseUrl();
            case "backend" -> config.getBackendBaseUrl();
            default -> null;
        };
    }
}
