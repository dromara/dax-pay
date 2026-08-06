package cn.daxpay.open.payment.strategy.risk;

import cn.daxpay.open.platform.core.i18n.I18nSupport;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Optional;

/// # 地理围栏策略
///
/// 控制门店市级地理围栏的容错范围, 由 [cn.daxpay.open.plugin.risk.strategy.DefaultPayRiskChecker#checkStoreGeoFence]
/// 读取平台全局配置后决定放行城市集合。
///
/// - [STRICT]: 仅允许门店所在市, 无容错(交界处 IP 误判会被拦截, 风控最严)
/// - [BALANCED]: 门店所在市 + 接壤邻市(吸收 IP 库在市边界的误判, 推荐默认)
/// - [LOOSE]: 门店所在市 + 邻市 + 同省所有市(跨市连锁 / 省级经营商户)
@Getter
@RequiredArgsConstructor
public enum GeoFenceStrategyEnum implements I18nSupport {

    /// 严格: 仅允许门店所在市
    STRICT("strict"),
    /// 平衡: 门店市 + 邻市(推荐默认)
    BALANCED("balanced"),
    /// 宽松: 门店市 + 邻市 + 同省
    LOOSE("loose");

    private final String code;

    @Override
    public String getI18nPrefix() {
        return "enum.geo_fence_strategy";
    }

    public static Optional<GeoFenceStrategyEnum> findByCode(String code) {
        return Arrays.stream(values()).filter(e -> e.code.equals(code)).findFirst();
    }
}
