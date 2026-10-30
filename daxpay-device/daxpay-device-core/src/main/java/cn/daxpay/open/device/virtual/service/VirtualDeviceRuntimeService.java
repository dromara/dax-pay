package cn.daxpay.open.device.virtual.service;

import cn.daxpay.open.device.virtual.client.service.VirtualOnlineTracker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

/// # 虚拟设备运行态装配器
///
/// 台账列表页(运营端虚拟设备、商户端收款设备)展示 virtual 行在线态的唯一口径来源,
/// 在线判定与"Redis 优先 DB 兜底"的最近在线时间取值收敛一处, 各页只做结果装配:
/// online = 在线 ZSET score 距今 15 分钟内; lastOnlineTime = max(DB 节流落库值, Redis score)。
/// activated(secret 非空)等实体在手边的判定由调用方自行设置。
///
/// 移植自 plus `daxpay-device-core`(快照 773134af), 时间类型 OffsetDateTime 化(开源版实体约定)。
@Service
@RequiredArgsConstructor
public class VirtualDeviceRuntimeService {

    private final VirtualOnlineTracker virtualOnlineTracker;

    /// 批量装载运行态(入参与返回按下标一一对应, 保序; ZSET 无 score 的设备 online=false、时间回落 DB 值)
    public List<VirtualRuntime> load(List<RuntimeInput> inputs) {
        if (inputs.isEmpty()) {
            return List.of();
        }
        Map<String, Long> scores = virtualOnlineTracker.getOnlineScores(
                inputs.stream().map(RuntimeInput::deviceNo).toList());
        return inputs.stream()
                .map(input -> {
                    Long score = scores.get(input.deviceNo());
                    return new VirtualRuntime(
                            VirtualOnlineTracker.isOnlineScore(score),
                            virtualOnlineTracker.resolveLastOnlineTime(input.dbLastOnlineTime(), score));
                })
                .toList();
    }

    /// 运行态装载入参(设备号 + 台账节流落库的最近在线时间)
    public record RuntimeInput(String deviceNo, OffsetDateTime dbLastOnlineTime) {
    }

    /// 设备运行态(在线标记与最近在线时间)
    public record VirtualRuntime(boolean online, OffsetDateTime lastOnlineTime) {
    }
}
