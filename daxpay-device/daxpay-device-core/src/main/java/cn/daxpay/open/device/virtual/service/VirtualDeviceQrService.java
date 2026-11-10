package cn.daxpay.open.device.virtual.service;

import cn.daxpay.open.device.virtual.entity.DeviceVirtual;
import org.springframework.stereotype.Service;

/// # 虚拟设备激活二维码内容服务
///
/// 开源版扫码激活协议(不兼容 plus 旧协议): 二维码内容字符串 `daxpay:activate:{sn}`,
/// 后端只生成内容字符串, 前端负责渲染成二维码图。
/// - 服务器地址由 APP 编译期内置, 二维码不再携带 baseUrl, 扫码仅传入设备号;
/// - `{sn}` 为设备号(单因子准入凭据)。
@Service
public class VirtualDeviceQrService {

    /// 扫码协议 scheme(与 APP 端解析器约定, 改动须双侧同步)
    public static final String ACTIVATE_SCHEME = "daxpay:activate:";

    /// 生成设备激活二维码内容字符串
    public String qrContent(DeviceVirtual device) {
        return qrContent(device.getDeviceNo());
    }

    /// 生成设备号维度的激活二维码内容字符串
    public String qrContent(String deviceNo) {
        return ACTIVATE_SCHEME + deviceNo;
    }
}
