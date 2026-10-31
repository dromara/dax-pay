package cn.daxpay.open.payment.merchant.service.channel;

import cn.daxpay.open.payment.merchant.dao.appinfo.MchAppInfoManager;
import cn.daxpay.open.payment.merchant.dao.channel.ChannelMerchantManager;
import cn.daxpay.open.payment.merchant.dao.gateway.GatewayCashierItemManager;
import cn.daxpay.open.payment.merchant.dao.gateway.GatewayPayClientEnvManager;
import cn.daxpay.open.payment.merchant.dao.gateway.GatewayPayConfigManager;
import cn.daxpay.open.payment.merchant.entity.appinfo.MchAppInfo;
import cn.daxpay.open.payment.merchant.entity.gateway.GatewayCashierItem;
import cn.daxpay.open.payment.merchant.entity.gateway.GatewayPayClientEnv;
import cn.daxpay.open.payment.merchant.entity.gateway.GatewayPayConfig;
import cn.daxpay.open.payment.merchant.enums.ClientEnvEnum;
import cn.daxpay.open.payment.merchant.enums.CodePayFormEnum;
import cn.daxpay.open.payment.merchant.result.channel.ChannelMerchantReferenceAppResult;
import cn.daxpay.open.payment.merchant.result.channel.ChannelMerchantReferenceItemResult;
import cn.daxpay.open.payment.merchant.result.channel.ChannelMerchantReferenceResult;
import cn.daxpay.open.payment.route.dao.basic.PayRouteBasicConfigManager;
import cn.daxpay.open.payment.route.dao.scene.PayRouteSceneConfigManager;
import cn.daxpay.open.payment.route.dao.strategy.PayRouteStrategyManager;
import cn.daxpay.open.payment.route.entity.basic.PayRouteBasicConfig;
import cn.daxpay.open.payment.route.entity.scene.PayRouteSceneConfig;
import cn.daxpay.open.payment.route.entity.strategy.PayRouteStrategy;
import cn.daxpay.open.payment.route.service.support.PayRouteI18nHelper;
import cn.daxpay.open.platform.common.i18n.util.I18nUtil;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/// # 通道商户引用明细查询
///
/// 通道商户(channelMchNo)被四张配置子表软引用, 删除/停用通道商户会让这些配置静默失效,
/// 本服务聚合四表引用并按应用分组, 供前端在停用前展示影响面:
/// - [PayRouteSceneConfig]: 通道路由·场景模式行(经 strategyId 回查 [PayRouteStrategy] 取 appId/mchNo)
/// - [PayRouteBasicConfig]: 通道路由·基础模式行(同上回查策略)
/// - [GatewayPayClientEnv]: 网关支付配置环境行(经 configId 回查 [GatewayPayConfig] 取 appId/mchNo)
/// - [GatewayCashierItem]: 收银台支付入口行(自带 appId/mchNo)
///
/// 只读不写库; 引用行父记录(strategy/config)不可解析时跳过该行(正常数据经级联删除不会悬挂;
/// 商户端 TenantLine 隔离下跨商户引用行同样不可见, 跳过即防越权泄露)。
/// 引用类型名与人读摘要走后端 i18n(`payment.merchant.channelMerchantReference.*`), 随请求语言输出。
@Service
@RequiredArgsConstructor
public class ChannelMerchantReferenceService {

    /// 引用类型编码: 通道路由·场景模式
    private static final String TYPE_ROUTE_SCENE = "route_scene";
    /// 引用类型编码: 通道路由·基础模式
    private static final String TYPE_ROUTE_BASIC = "route_basic";
    /// 引用类型编码: 网关支付配置
    private static final String TYPE_GATEWAY_ENV = "gateway_env";
    /// 引用类型编码: 收银台支付入口
    private static final String TYPE_CASHIER_ITEM = "cashier_item";

    private final ChannelMerchantManager channelMerchantManager;
    private final PayRouteSceneConfigManager payRouteSceneConfigManager;
    private final PayRouteBasicConfigManager payRouteBasicConfigManager;
    private final GatewayPayClientEnvManager gatewayPayClientEnvManager;
    private final GatewayCashierItemManager gatewayCashierItemManager;
    private final PayRouteStrategyManager payRouteStrategyManager;
    private final GatewayPayConfigManager gatewayPayConfigManager;
    private final MchAppInfoManager mchAppInfoManager;

    /// 单条引用与应用归属的三元组(内部聚合用)
    private record AppRef(String appId, String mchNo, ChannelMerchantReferenceItemResult item) {
    }

    /// 查询通道商户被引用的明细, 按应用分组返回
    public ChannelMerchantReferenceResult referenceDetail(String channelMchNo) {
        // 通道商户不存在(商户端 TenantLine 隔离下不属于当前商户同样查不到, 即归属校验)
        channelMerchantManager.findByChannelMchNo(channelMchNo)
                .orElseThrow(() -> new DataNotExistException("error.payment.channel.channelMerchantNotExist"));

        // 1. 四张子表各自按通道商户号查未删行
        List<PayRouteSceneConfig> sceneConfigs = payRouteSceneConfigManager.findAllByChannelMchNo(channelMchNo);
        List<PayRouteBasicConfig> basicConfigs = payRouteBasicConfigManager.findAllByChannelMchNo(channelMchNo);
        List<GatewayPayClientEnv> clientEnvs = gatewayPayClientEnvManager.findAllByChannelMchNo(channelMchNo);
        List<GatewayCashierItem> cashierItems = gatewayCashierItemManager.findAllByChannelMchNo(channelMchNo);

        // 2. 整批回查父记录(strategy/config), 内存 map 供取 appId/mchNo
        Set<Long> strategyIds = new HashSet<>();
        sceneConfigs.forEach(scene -> strategyIds.add(scene.getStrategyId()));
        basicConfigs.forEach(basic -> strategyIds.add(basic.getStrategyId()));
        Map<Long, PayRouteStrategy> strategyMap = strategyIds.isEmpty()
                ? Map.of()
                : payRouteStrategyManager.findAllByIds(strategyIds).stream()
                        .collect(Collectors.toMap(PayRouteStrategy::getId, s -> s));
        Set<Long> configIds = new HashSet<>();
        clientEnvs.forEach(env -> configIds.add(env.getConfigId()));
        Map<Long, GatewayPayConfig> configMap = configIds.isEmpty()
                ? Map.of()
                : gatewayPayConfigManager.findAllByIds(configIds).stream()
                        .collect(Collectors.toMap(GatewayPayConfig::getId, c -> c));

        // 3. 逐表生成引用条目(顺序: 场景 → 基础 → 网关环境 → 收银台)
        List<AppRef> refs = new ArrayList<>();
        sceneConfigs.forEach(scene -> Optional.ofNullable(sceneAppRef(scene, strategyMap)).ifPresent(refs::add));
        basicConfigs.forEach(basic -> Optional.ofNullable(basicAppRef(basic, strategyMap)).ifPresent(refs::add));
        clientEnvs.forEach(env -> Optional.ofNullable(envAppRef(env, configMap)).ifPresent(refs::add));
        cashierItems.forEach(item -> refs.add(new AppRef(
                item.getAppId(), item.getMchNo(),
                buildItemResult(buildCashierDetail(item), TYPE_CASHIER_ITEM))));

        // 4. 按应用分组(LinkedHashMap 保持首次出现顺序, 组内保持收集顺序)
        Map<String, ChannelMerchantReferenceAppResult> appMap = new LinkedHashMap<>();
        for (AppRef ref : refs) {
            ChannelMerchantReferenceAppResult app = appMap.computeIfAbsent(ref.appId(), key -> new ChannelMerchantReferenceAppResult()
                    .setAppId(ref.appId())
                    .setMchNo(ref.mchNo()));
            app.getRefs().add(ref.item());
        }

        // 5. 批量补应用名, 应用组按应用号排序保证输出稳定
        if (!appMap.isEmpty()) {
            Map<String, String> appNameMap = mchAppInfoManager
                    .findAllByFields(MchAppInfo::getAppId, appMap.keySet())
                    .stream()
                    .collect(Collectors.toMap(MchAppInfo::getAppId, MchAppInfo::getAppName, (a, b) -> a));
            appMap.forEach((appId, app) -> app.setAppName(appNameMap.get(appId)));
        }
        List<ChannelMerchantReferenceAppResult> apps = appMap.values().stream()
                .sorted(Comparator.comparing(ChannelMerchantReferenceAppResult::getAppId,
                        Comparator.nullsLast(Comparator.naturalOrder())))
                .toList();

        return new ChannelMerchantReferenceResult()
                .setChannelMchNo(channelMchNo)
                .setTotal(refs.size())
                .setApps(apps);
    }

    /// 场景模式行 → 引用条目; 策略不可解析(已删悬挂/跨租户不可见)返回 null
    private AppRef sceneAppRef(PayRouteSceneConfig scene, Map<Long, PayRouteStrategy> strategyMap) {
        PayRouteStrategy strategy = strategyMap.get(scene.getStrategyId());
        if (strategy == null) {
            return null;
        }
        return new AppRef(strategy.getAppId(), strategy.getMchNo(),
                buildItemResult(buildSceneDetail(scene), TYPE_ROUTE_SCENE));
    }

    /// 基础模式行 → 引用条目; 策略不可解析返回 null
    private AppRef basicAppRef(PayRouteBasicConfig basic, Map<Long, PayRouteStrategy> strategyMap) {
        PayRouteStrategy strategy = strategyMap.get(basic.getStrategyId());
        if (strategy == null) {
            return null;
        }
        return new AppRef(strategy.getAppId(), strategy.getMchNo(),
                buildItemResult(buildBasicDetail(basic), TYPE_ROUTE_BASIC));
    }

    /// 网关支付环境行 → 引用条目; 主配置不可解析返回 null
    private AppRef envAppRef(GatewayPayClientEnv env, Map<Long, GatewayPayConfig> configMap) {
        GatewayPayConfig config = configMap.get(env.getConfigId());
        if (config == null) {
            return null;
        }
        return new AppRef(config.getAppId(), config.getMchNo(),
                buildItemResult(buildEnvDetail(env), TYPE_GATEWAY_ENV));
    }

    /// 组装引用条目(类型名走 i18n)
    private ChannelMerchantReferenceItemResult buildItemResult(String detail, String type) {
        return new ChannelMerchantReferenceItemResult()
                .setType(type)
                .setTypeName(I18nUtil.get("payment.merchant.channelMerchantReference.type." + type))
                .setDetail(detail);
    }

    /// 场景模式行摘要: 「微信小程序(wechat_mini)」(支付方式展示名 + 能力编码)
    private String buildSceneDetail(PayRouteSceneConfig scene) {
        String methodZh = PayRouteI18nHelper.payMethod(scene.getMethod());
        if (StrUtil.isBlank(scene.getCapability())) {
            return methodZh;
        }
        return I18nUtil.get("payment.merchant.channelMerchantReference.detail.sceneWithCapability", methodZh, scene.getCapability());
    }

    /// 基础模式行摘要: 「微信支付」(支付渠道展示名)
    private String buildBasicDetail(PayRouteBasicConfig basic) {
        return StrUtil.blankToDefault(PayRouteI18nHelper.provider(basic.getProvider()), "-");
    }

    /// 网关支付环境行摘要: 「支付宝环境 × H5」(客户端环境展示名 × 支付形态展示名)
    private String buildEnvDetail(GatewayPayClientEnv env) {
        return I18nUtil.get("payment.merchant.channelMerchantReference.detail.env",
                clientEnvZh(env.getClientEnv()), payFormZh(env.getPayForm()));
    }

    /// 收银台支付项行摘要: 「支付项：支付宝(推荐)」
    private String buildCashierDetail(GatewayCashierItem item) {
        String base = I18nUtil.get("payment.merchant.channelMerchantReference.detail.cashierItem",
                StrUtil.blankToDefault(item.getName(), "-"));
        if (Boolean.TRUE.equals(item.getRecommend())) {
            return I18nUtil.get("payment.merchant.channelMerchantReference.detail.cashierItemRecommend", base);
        }
        return base;
    }

    /// 客户端环境编码 → 展示名(枚举 i18n; 未知编码回退原编码, 防脏数据中断查询; 兼容历史 wechat_pay)
    private String clientEnvZh(String clientEnv) {
        String normalized = "wechat_pay".equals(clientEnv) ? ClientEnvEnum.WECHAT.getCode() : clientEnv;
        return java.util.Arrays.stream(ClientEnvEnum.values())
                .filter(e -> Objects.equals(e.getCode(), normalized))
                .map(I18nUtil::getEnumName)
                .map(name -> StrUtil.blankToDefault(name, normalized))
                .findFirst()
                .orElse(StrUtil.blankToDefault(clientEnv, "-"));
    }

    /// 支付形态编码 → 展示名(h5→H5, mini→小程序)
    private String payFormZh(String payForm) {
        return java.util.Arrays.stream(CodePayFormEnum.values())
                .filter(e -> Objects.equals(e.getCode(), payForm))
                .map(I18nUtil::getEnumName)
                .map(name -> StrUtil.blankToDefault(name, payForm))
                .findFirst()
                .orElse(StrUtil.blankToDefault(payForm, "-"));
    }
}
