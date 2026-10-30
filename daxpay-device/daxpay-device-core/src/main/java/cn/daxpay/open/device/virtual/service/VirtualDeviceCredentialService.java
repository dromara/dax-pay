package cn.daxpay.open.device.virtual.service;

import cn.daxpay.open.device.virtual.client.service.VirtualDevicePushClient;
import cn.daxpay.open.device.virtual.client.service.VirtualOnlineTracker;
import cn.daxpay.open.device.virtual.dao.VirtualDeviceManager;
import cn.daxpay.open.device.virtual.entity.VirtualDevice;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/// # 虚拟设备凭证生命周期门面
///
/// 虚拟设备独立台账在设备脱离归属或删除时的凭证回收入口:
/// 踢线/清在线/清凭证的次序语义收敛在虚拟设备域内, 管理端/商户端只调本门面、不感知实现。
///
/// 虚拟设备脱离归属时主动踢出并清空播报凭证(未激活跳过):
/// 先下发 kick 明文通知(在线设备秒级感知立即下线), 再移除在线成员并清空 secret;
/// 离线设备收不到 kick, 由既有 ack 自愈兜底(旧 APP 下一次心跳即 revoked)。
/// 不采用轮换: 清空后设备回到"未激活"态, 重新绑定商户后可重新激活, 也可直接删除;
/// ZREM 防 15 分钟窗口内 ZCOUNT 在线数虚高(窗口后自然出窗, 清理为口径收敛)。
///
/// 注意: 开源版无商户删除级联(DeviceMigrationSupport 不移植), 删除商户前须先解绑/删除设备。
@Slf4j
@Service
@RequiredArgsConstructor
public class VirtualDeviceCredentialService {

    private final VirtualDeviceManager virtualDeviceManager;
    private final VirtualDevicePushClient virtualDevicePushClient;
    private final VirtualOnlineTracker virtualOnlineTracker;

    /// 台账凭证回收(解绑时调用, 按 id 批量)
    public void revokeByIds(List<Long> ids, String reason) {
        revoke(virtualDeviceManager.findAllByIds(ids), reason);
    }

    /// 台账凭证回收(已持有实体的场景)
    public void revoke(List<VirtualDevice> devices, String reason) {
        devices.stream()
                .filter(device -> StrUtil.isNotBlank(device.getSecret()))
                .forEach(device -> {
                    // 踢出通知须在清 secret 之前发出, 在线设备才能在凭证失效前收到并立即下线
                    virtualDevicePushClient.kick(device.getDeviceNo(), reason);
                    virtualOnlineTracker.removeOnline(device.getDeviceNo());
                    virtualDeviceManager.clearSecret(device.getId());
                });
    }

    /// 删除设备后清在线 ZSET 残留成员(常规路径解绑时已清, 此处防御残留)
    public void onDeleted(VirtualDevice entity) {
        virtualOnlineTracker.removeOnline(entity.getDeviceNo());
    }
}
