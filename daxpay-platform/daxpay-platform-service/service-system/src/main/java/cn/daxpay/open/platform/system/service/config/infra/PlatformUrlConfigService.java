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
    /// @param param 端点类型与可选 URL
    /// @return 探测结果
    public ConnectivityCheckResult checkUrl(PlatformUrlCheckParam param) {
        String urlType = StrUtil.trim(param.getUrlType());
        String targetUrl = StrUtil.blankToDefault(StrUtil.trim(param.getUrl()), resolveSavedUrl(urlType));
        if (StrUtil.isBlank(targetUrl)) {
            return ConnectivityCheckResult.fail(I18nUtil.get("error.system.url.notConfigured"));
        }
        targetUrl = StrUtil.removeSuffix(targetUrl, "/");

        // 后端 API 走 /echo 严格校验; 前端 SPA 仅校验 HTTP 可达
        boolean backendEcho = "backend".equalsIgnoreCase(urlType);
        String probeUrl = backendEcho ? targetUrl + "/echo" : targetUrl;

        long start = System.currentTimeMillis();
        try (HttpResponse response = HttpRequest.get(probeUrl)
                .timeout(CHECK_TIMEOUT_MS)
                .setFollowRedirects(true)
                .execute()) {
            long latency = System.currentTimeMillis() - start;
            int status = response.getStatus();
            if (status < 200 || status >= 400) {
                return ConnectivityCheckResult.fail(
                        I18nUtil.get("error.system.url.checkFailed", String.valueOf(status)),
                        latency,
                        status);
            }
            if (backendEcho) {
                String body = StrUtil.nullToEmpty(response.body());
                if (!body.startsWith("echo")) {
                    return ConnectivityCheckResult.fail(
                            I18nUtil.get("error.system.url.echoMismatch"),
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
        if (config == null || StrUtil.isBlank(urlType)) {
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
