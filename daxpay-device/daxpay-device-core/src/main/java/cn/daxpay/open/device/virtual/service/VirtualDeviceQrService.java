package cn.daxpay.open.device.virtual.service;

import cn.daxpay.open.device.virtual.entity.VirtualDevice;
import cn.daxpay.open.platform.system.entity.config.platform.infra.PlatformUrlConfig;
import cn.daxpay.open.platform.system.service.config.infra.PlatformUrlConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/// # 虚拟设备激活二维码内容服务
///
/// 开源版扫码激活协议(不兼容 plus 旧协议): 二维码内容字符串 `daxpay:activate:{serverBaseUrl}:{sn}`,
/// 后端只生成内容字符串, 前端负责渲染成二维码图。
/// - `{serverBaseUrl}` 取平台端点配置「后端 API 地址」([PlatformUrlConfig#getBackendBaseUrl]),
///   APP 扫码后以此为基址重建 ApiClient(替代 plus 激活码体系, 扫码即取后端接口地址);
/// - `{sn}` 为设备号(单因子准入凭据)。
/// 配置为空时返回内容中该段为空串(手填兜底路径), 由 APP 引导用户手填服务器地址。
@Service
@RequiredArgsConstructor
public class VirtualDeviceQrService {

    /// 扫码协议 scheme(与 APP 端解析器约定, 改动须双侧同步)
    public static final String ACTIVATE_SCHEME = "daxpay:activate:";

    private final PlatformUrlConfigService platformUrlConfigService;

    /// 生成设备激活二维码内容字符串
    public String qrContent(VirtualDevice device) {
        return qrContent(device.getDeviceNo());
    }

    /// 生成设备号维度的激活二维码内容字符串
    ///
    /// serverBaseUrl 容忍为空: 平台端点配置未填时不阻断二维码生成,
    /// 内容退化为 `daxpay:activate::{sn}`, APP 识别到空地址段引导手填。
    public String qrContent(String deviceNo) {
        PlatformUrlConfig urlConfig = platformUrlConfigService.getUrlConfig();
        String serverBaseUrl = urlConfig == null ? "" : urlConfig.getBackendBaseUrl();
        return ACTIVATE_SCHEME + (serverBaseUrl == null ? "" : serverBaseUrl) + ":" + deviceNo;
    }
}
