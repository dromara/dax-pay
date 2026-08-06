package cn.daxpay.open.payment.trade.runtime.service.pay.common;

import cn.daxpay.open.payment.merchant.dao.store.MchStoreInfoManager;
import cn.daxpay.open.payment.merchant.entity.config.MchRiskConfig;
import cn.daxpay.open.payment.merchant.entity.store.MchStoreInfo;
import cn.daxpay.open.payment.merchant.service.config.MchRiskConfigService;
import cn.daxpay.open.payment.strategy.pay.AbsNormalPayStrategy;
import cn.daxpay.open.payment.strategy.risk.GeoFenceStrategyEnum;
import cn.daxpay.open.payment.strategy.risk.GeoFenceUtil;
import cn.daxpay.open.payment.strategy.risk.PayRiskCheckContext;
import cn.daxpay.open.payment.strategy.risk.PayRiskChecker;
import cn.daxpay.open.payment.trade.enums.PayTradeTypeEnum;
import cn.daxpay.open.payment.trade.order.dao.GatewayPayOrderManager;
import cn.daxpay.open.payment.trade.order.dao.NormalPayOrderManager;
import cn.daxpay.open.payment.trade.order.entity.GatewayPayOrder;
import cn.daxpay.open.payment.trade.order.entity.NormalPayOrder;
import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import cn.daxpay.open.payment.unipay.param.trade.pay.NormalPayParam;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import cn.daxpay.open.platform.core.enums.pay.trade.TradeSourceEnum;
import cn.daxpay.open.platform.system.dao.region.CityManager;
import cn.daxpay.open.platform.system.entity.config.platform.security.PlatformPaySecurityConfig;
import cn.daxpay.open.platform.system.entity.region.City;
import cn.daxpay.open.platform.system.service.config.security.PlatformSecurityConfigService;
import cn.daxpay.open.platform.system.service.region.ChinaRegionAdjacencyService;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/// # 支付风控辅助
///
/// 统一封装事前/事后黑名单检查入口，供普通支付、网关支付、同步查单、异步回调复用。
/// 插件缺失或总开关关闭时视为放行。
@Slf4j
@Service
@RequiredArgsConstructor
public class PayRiskAssistService {

    private final ObjectProvider<PayRiskChecker> payRiskCheckerProvider;
    private final PlatformSecurityConfigService platformSecurityConfigService;
    private final NormalPayOrderManager normalPayOrderManager;
    private final GatewayPayOrderManager gatewayPayOrderManager;
    private final MchStoreInfoManager mchStoreInfoManager;
    private final CityManager cityManager;
    private final MchRiskConfigService mchRiskConfigService;
    private final ChinaRegionAdjacencyService chinaRegionAdjacencyService;

    /// 支付前检查（须在 [AbsNormalPayStrategy#doBeforePay] 之后调用，保证微信 channelAppId 已回填）
    ///
    /// `riskBlockBeforePay=false` 时仍执行检查并落命中，但不抛异常阻断下单。
    public void checkBeforePay(NormalPayParam payParam, String scene) {
        PayRiskChecker checker = payRiskCheckerProvider.getIfAvailable();
        if (checker == null || payParam == null) {
            return;
        }
        var config = platformSecurityConfigService.getPaySecurityConfig();
        if (!Boolean.TRUE.equals(config.getRiskEnabled())) {
            return;
        }
        PayRiskCheckContext ctx = buildContextFromParam(payParam, scene);
        // false=仅记录不拦截；缺省/true=命中拒绝下单
        ctx.setBlockOnHit(!Boolean.FALSE.equals(config.getRiskBlockBeforePay()));
        // 黑名单拦截开关（第一层: IP / 用户标识）
        ctx.setBlacklistEnabled(config.getBlacklistEnabled());
        // 海外 IP 拦截开关（地域策略）
        ctx.setBlockOverseasIp(config.getBlockOverseasIp());
        // 省级地区拦截开关（地域策略）
        ctx.setProvinceBlacklistEnabled(config.getProvinceBlacklistEnabled());
        // 市级地区拦截开关（地域策略, 独立于省级）
        ctx.setCityBlacklistEnabled(config.getCityBlacklistEnabled());
        // 地理围栏两级门控 + 策略 + 放行城市集合（第三层）
        applyGeoFence(ctx, config, payParam.getMchNo());
        checker.checkBeforePay(ctx);
    }

    /// 支付成功后补录（同步返回 / 查单 / 回调统一入口）；异常只告警，不阻断资金态
    ///
    /// 仅使用付款用户标识 [buyerId]（微信 openid / 支付宝 user_id），不读取通道内部 userId。
    public void checkAfterPay(PayTrade trade, String buyerId) {
        PayRiskChecker checker = payRiskCheckerProvider.getIfAvailable();
        if (checker == null || trade == null) {
            return;
        }
        PlatformPaySecurityConfig config = platformSecurityConfigService.getPaySecurityConfig();
        if (!Boolean.TRUE.equals(config.getRiskEnabled())
                || !Boolean.TRUE.equals(config.getRiskCheckAfterPay())) {
            return;
        }
        PayRiskCheckContext ctx = buildContextFromTrade(trade);
        // 海外 IP 拦截开关（地域策略, 事后仅记录海外访问）
        ctx.setBlockOverseasIp(config.getBlockOverseasIp());
        // 省级地区拦截开关（地域策略, 事后补录）
        ctx.setProvinceBlacklistEnabled(config.getProvinceBlacklistEnabled());
        // 市级地区拦截开关（地域策略, 事后补录, 独立于省级）
        ctx.setCityBlacklistEnabled(config.getCityBlacklistEnabled());
        // 地理围栏两级门控 + 策略 + 放行城市集合（第三层, 事后补录）
        applyGeoFence(ctx, config, trade.getMchNo());
        // 黑名单拦截开关（第一层, 事后补录）
        ctx.setBlacklistEnabled(config.getBlacklistEnabled());
        if (StrUtil.isNotBlank(buyerId)) {
            ctx.setBuyerId(buyerId);
        }
        // 下单未带 openId 时，用通道回写的 buyerId 补用户标识比对
        if (StrUtil.isBlank(ctx.getOpenId()) && StrUtil.isNotBlank(ctx.getBuyerId())) {
            ctx.setOpenId(ctx.getBuyerId());
        }
        try {
            checker.checkAfterPay(ctx);
        } catch (Exception e) {
            log.warn("支付后风控补录失败 tradeNo={}: {}", trade.getTradeNo(), e.getMessage());
        }
    }

    /// 从支付参数构建风控检查上下文
    private PayRiskCheckContext buildContextFromParam(NormalPayParam payParam, String scene) {
        PayRiskCheckContext ctx = new PayRiskCheckContext()
                .setScene(StrUtil.blankToDefault(scene, resolveSceneFromSource(payParam.getSource())))
                .setMchNo(payParam.getMchNo())
                .setAppId(payParam.getAppId())
                .setClientIp(payParam.getClientIp())
                .setOpenId(payParam.getOpenId())
                .setMethod(payParam.getMethod())
                .setProduct(payParam.getProduct())
                .setChannelAppId(payParam.getChannelAppId())
                .setBizOrderNo(payParam.getBizOrderNo());
        fillChannelByProduct(ctx, payParam.getProduct());
        // 门店号提取(围栏比对基准来源): 从 terminal.storeNo 提取; 门店城市与放行集合由 applyGeoFence 按策略预算
        String storeNo = payParam.getTerminal() != null ? payParam.getTerminal().getStoreNo() : null;
        ctx.setStoreNo(storeNo);
        return ctx;
    }

    /// 从资金凭证构建风控检查上下文(含容器回查)
    private PayRiskCheckContext buildContextFromTrade(PayTrade trade) {
        PayRiskCheckContext ctx = new PayRiskCheckContext()
                .setTradeNo(trade.getTradeNo())
                .setTradeType(trade.getTradeType())
                .setMchNo(trade.getMchNo())
                .setAppId(trade.getAppId());
        if (Objects.equals(trade.getTradeType(), PayTradeTypeEnum.GATEWAY.getCode())) {
            GatewayPayOrder order = gatewayPayOrderManager.findById(trade.getContainerId()).orElse(null);
            if (order != null) {
                ctx.setScene("gateway")
                        .setClientIp(order.getClientIp())
                        .setOpenId(order.getOpenid())
                        .setBuyerId(order.getBuyerId())
                        .setMethod(order.getMethod())
                        .setProduct(order.getProduct())
                        .setChannel(order.getChannel())
                        .setChannelAppId(order.getChannelAppId())
                        .setOrderNo(order.getOrderNo())
                        .setBizOrderNo(order.getBizOrderNo())
                        .setStoreNo(order.getStoreNo());
            }
        } else {
            NormalPayOrder order = normalPayOrderManager.findById(trade.getContainerId()).orElse(null);
            if (order != null) {
                ctx.setScene(resolveSceneFromSource(order.getSource()))
                        .setClientIp(order.getClientIp())
                        .setOpenId(order.getOpenid())
                        .setBuyerId(order.getBuyerId())
                        .setMethod(order.getMethod())
                        .setProduct(order.getProduct())
                        .setChannel(order.getChannel())
                        .setChannelAppId(order.getChannelAppId())
                        .setOrderNo(order.getOrderNo())
                        .setBizOrderNo(order.getBizOrderNo())
                        .setStoreNo(order.getStoreNo());
            }
        }
        if (StrUtil.isBlank(ctx.getChannel())) {
            fillChannelByProduct(ctx, ctx.getProduct());
        }
        // 门店号已从容器回填; 门店城市与放行集合由 applyGeoFence 按策略预算
        return ctx;
    }

    /// 从产品编码反推通道编码填入风控上下文
    private static void fillChannelByProduct(PayRiskCheckContext ctx, String product) {
        if (StrUtil.isBlank(product)) {
            return;
        }
        try {
            ctx.setChannel(ProductEnum.findByCode(product).getChannel());
        } catch (Exception ignored) {
            // 反推失败忽略, channel 留空
        }
    }

    /// 交易来源 → 风控场景编码: 码牌→code, 其余→api
    private static String resolveSceneFromSource(String source) {
        if (StrUtil.isNotBlank(source) && TradeSourceEnum.CASHIER_CODE.getCode().equals(source)) {
            return "code";
        }
        return "api";
    }

    /// 直辖市省份编码 → 城市名（base_city 对直辖市存"市辖区", 围栏比对需用省名）
    private static final Map<String, String> DIRECT_CITY_CODE_TO_NAME = Map.of(
            "11", "北京市", "12", "天津市", "31", "上海市", "50", "重庆市");

    /// 门店城市解析结果（围栏比对基准 + 放行集合构建输入）
    private record StoreCityInfo(String cityCode, String cityName, String provinceCode) {
    }

    /// 地理围栏两级门控 + 策略 + 放行城市集合预算
    ///
    /// 平台总闸(config.geoFenceEnabled) AND 商户开关(mch_risk_config.geoFenceEnabled) 同时开启才生效;
    /// 生效时按平台全局策略(strict/balanced/loose)预算门店放行城市集合注入 ctx, 检查器只需判定 IP 城市是否落集合内。
    /// 门店无地址时 storeCity/storeAllowedCities 留空 → 检查器 fail-open。
    private void applyGeoFence(PayRiskCheckContext ctx, PlatformPaySecurityConfig config, String mchNo) {
        if (!Boolean.TRUE.equals(config.getGeoFenceEnabled())) {
            ctx.setGeoFenceEnabled(false);
            return;
        }
        MchRiskConfig mchRisk = mchRiskConfigService.getConfigForPayment(mchNo);
        boolean mchOn = Boolean.TRUE.equals(mchRisk.getGeoFenceEnabled());
        ctx.setGeoFenceEnabled(mchOn);
        if (!mchOn) {
            return;
        }
        // 围栏策略为平台全局配置, 全商户共用
        String strategy = config.getGeoFenceStrategy();
        ctx.setGeoFenceStrategy(strategy);
        StoreCityInfo info = resolveStoreCityInfo(ctx.getStoreNo());
        if (info == null) {
            // 门店不存在或未录地址 → fail-open(检查器按 storeCity 为空处理)
            return;
        }
        ctx.setStoreCity(info.cityName());
        ctx.setStoreAllowedCities(buildAllowedCities(info, strategy));
    }

    /// 按 storeNo 查门店所在城市信息(城市码/城市名/省码)
    ///
    /// 门店 regionCode 为 6 位区县码; 直辖市用省名作为城市名, 其余查 base_city。
    private StoreCityInfo resolveStoreCityInfo(String storeNo) {
        if (StrUtil.isBlank(storeNo)) {
            return null;
        }
        MchStoreInfo store = mchStoreInfoManager.findByStoreNo(storeNo).orElse(null);
        if (store == null || StrUtil.isBlank(store.getRegionCode()) || store.getRegionCode().length() < 4) {
            return null;
        }
        String regionCode = store.getRegionCode();
        String cityCode = regionCode.substring(0, 4);
        String provinceCode = regionCode.substring(0, 2);
        String cityName = resolveCityName(cityCode);
        if (StrUtil.isBlank(cityName)) {
            return null;
        }
        return new StoreCityInfo(cityCode, cityName, provinceCode);
    }

    /// 城市编码 → 城市名(与 ip2region 返回格式对齐)
    ///
    /// 直辖市(省码 11/12/31/50)用省名(北京市), 因 base_city 对直辖市存"市辖区"; 其余查 base_city。
    private String resolveCityName(String cityCode) {
        if (StrUtil.isBlank(cityCode) || cityCode.length() < 2) {
            return null;
        }
        String directName = DIRECT_CITY_CODE_TO_NAME.get(cityCode.substring(0, 2));
        if (directName != null) {
            return directName;
        }
        return cityManager.findById(cityCode)
                .map(City::getName)
                .orElse(null);
    }

    /// 按策略构建门店放行城市集合(归一化后的城市名)
    ///
    /// strict={门店市}; balanced={门店市}+邻市; loose={门店市}+邻市+同省。
    private Set<String> buildAllowedCities(StoreCityInfo info, String strategy) {
        GeoFenceStrategyEnum st = GeoFenceStrategyEnum.findByCode(strategy)
                .orElse(GeoFenceStrategyEnum.BALANCED);
        Set<String> allowed = new HashSet<>();
        allowed.add(GeoFenceUtil.normalizeRegionName(info.cityName()));
        if (st == GeoFenceStrategyEnum.STRICT) {
            return allowed;
        }
        // balanced: 接壤邻市
        Set<String> adjCodes = chinaRegionAdjacencyService.findAdjacentCityCodes(info.cityCode());
        if (CollUtil.isNotEmpty(adjCodes)) {
            for (String adjCode : adjCodes) {
                String name = resolveCityName(adjCode);
                if (StrUtil.isNotBlank(name)) {
                    allowed.add(GeoFenceUtil.normalizeRegionName(name));
                }
            }
        }
        if (st == GeoFenceStrategyEnum.LOOSE) {
            // 宽松: 同省所有市(直辖市同省即自身, 已在集合内)
            String directName = DIRECT_CITY_CODE_TO_NAME.get(info.provinceCode());
            if (directName == null) {
                for (City city : cityManager.findAllByProvinceCode(info.provinceCode())) {
                    if (StrUtil.isNotBlank(city.getName())) {
                        allowed.add(GeoFenceUtil.normalizeRegionName(city.getName()));
                    }
                }
            }
        }
        return allowed;
    }
}
