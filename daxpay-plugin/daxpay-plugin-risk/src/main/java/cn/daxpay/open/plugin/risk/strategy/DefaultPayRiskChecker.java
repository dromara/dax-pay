package cn.daxpay.open.plugin.risk.strategy;

import cn.daxpay.open.payment.strategy.risk.GeoFenceUtil;
import cn.daxpay.open.payment.strategy.risk.PayRiskCheckContext;
import cn.daxpay.open.payment.strategy.risk.PayRiskChecker;
import cn.daxpay.open.payment.strategy.risk.RegionCodeResolver;
import cn.daxpay.open.platform.core.code.PayErrorCode;
import cn.daxpay.open.platform.core.enums.pay.channel.ChannelEnum;
import cn.daxpay.open.platform.capability.audit.log.service.ip2region.IpRegion;
import cn.daxpay.open.platform.capability.audit.log.service.ip2region.IpToRegionService;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.plugin.risk.entity.PayBlacklist;
import cn.daxpay.open.plugin.risk.enums.PayBlacklistTypeEnum;
import cn.daxpay.open.plugin.risk.enums.PayRiskHitPhaseEnum;
import cn.daxpay.open.plugin.risk.service.PayBlacklistService;
import cn.daxpay.open.plugin.risk.service.PayRiskHitService;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Validator;
import cn.hutool.core.net.NetUtil;
import cn.hutool.core.util.StrUtil;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

/// # 默认支付风控检查器
///
/// 实现 [PayRiskChecker] SPI，由 daxpay-plugin-risk 自动配置注入。
/// 支付前命中黑名单抛异常拒绝下单；支付后用通道回写 buyerId 补洞（仅记录不阻断）。
@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultPayRiskChecker implements PayRiskChecker {

    private final PayBlacklistService payBlacklistService;
    private final PayRiskHitService payRiskHitService;
    private final IpToRegionService ipToRegionService;
    private final RegionCodeResolver regionCodeResolver;

    /// 用户标识黑名单存在性缓存（短 TTL 30s）
    ///
    /// 仅供网关层判断是否触发强制 OAuth 取 openId, 非关键路径,
    /// 30s 延迟可接受（黑名单 CRUD 不会立刻反映到 OAuth 触发判定）
    private final Cache<String, Boolean> openIdBlacklistCache = Caffeine.newBuilder()
            .expireAfterWrite(30, TimeUnit.SECONDS)
            .maximumSize(1)
            .build();

    private static final String OPEN_ID_BLACKLIST_CACHE_KEY = "hasOpenId";

    /// 地理围栏命中类型（非黑名单来源, 记录 IP 城市与门店城市不一致）
    private static final String HIT_TYPE_GEO_FENCE = "geo_fence";

    @Override
    public void checkBeforePay(PayRiskCheckContext ctx) {
        if (ctx == null) {
            return;
        }
        ctx.setPhase(PayRiskHitPhaseEnum.BEFORE_PAY.getCode());
        // null/true=阻断下单；false=仅落命中（对齐 riskBlockBeforePay）
        boolean throwOnHit = !Boolean.FALSE.equals(ctx.getBlockOnHit());
        // L1 黑名单（IP / 用户标识, 受 blacklistEnabled 开关控制）
        if (Boolean.TRUE.equals(ctx.getBlacklistEnabled())) {
            // IP 名单
            rejectIfBlocked(ctx, PayBlacklistTypeEnum.IP.getCode(), ctx.getClientIp(), null, throwOnHit);
            // 用户标识：按通道映射名单类型
            boolean identityBlocked = checkUserIdentity(ctx, ctx.getOpenId(), throwOnHit);
            if (!identityBlocked && StrUtil.isBlank(ctx.getOpenId())) {
                log.warn("支付前 openId 缺失, 用户标识黑名单降级为仅 IP 校验 + 事后补录: "
                        + "tradeType={}, method={}, mchNo={}, clientIp={}",
                        ctx.getTradeType(), ctx.getMethod(), ctx.getMchNo(), ctx.getClientIp());
            }
        }
        // IP 海外拦截（地域策略, 非黑名单）
        if (Boolean.TRUE.equals(ctx.getBlockOverseasIp())) {
            checkOverseasIp(ctx, throwOnHit);
        }
        // IP 地区黑名单（第二层全局地区名单, 受 regionBlacklistEnabled 开关控制; 省级命中后不执行市级检查）
        checkRegionBlacklist(ctx, throwOnHit);
        // L3 地理围栏（门店市级: IP 归属城市与门店城市比对）
        if (Boolean.TRUE.equals(ctx.getGeoFenceEnabled())) {
            checkStoreGeoFence(ctx, throwOnHit);
        }
    }

    @Override
    public void checkAfterPay(PayRiskCheckContext ctx) {
        if (ctx == null) {
            return;
        }
        ctx.setPhase(PayRiskHitPhaseEnum.AFTER_PAY.getCode());
        // 事后只记命中，不抛错
        // L1 黑名单（IP / 用户标识, 受 blacklistEnabled 开关控制）
        if (Boolean.TRUE.equals(ctx.getBlacklistEnabled())) {
            rejectIfBlocked(ctx, PayBlacklistTypeEnum.IP.getCode(), ctx.getClientIp(), null, false);
            checkUserIdentity(ctx, ctx.getOpenId(), false);
            // buyerId 按用户标识维度比对（主扫补洞）
            if (StrUtil.isNotBlank(ctx.getBuyerId()) && !StrUtil.equals(ctx.getBuyerId(), ctx.getOpenId())) {
                checkUserIdentity(ctx, ctx.getBuyerId(), false);
            }
        }
        // 海外 IP 访问记录（事后仅记录, 不阻断）
        if (Boolean.TRUE.equals(ctx.getBlockOverseasIp())) {
            checkOverseasIp(ctx, false);
        }
        // IP 地区黑名单（事后补录, 受 regionBlacklistEnabled 开关控制; 省级命中后不补录市级）
        checkRegionBlacklist(ctx, false);
        // L3 地理围栏（事后补录: IP 归属城市与门店城市比对）
        if (Boolean.TRUE.equals(ctx.getGeoFenceEnabled())) {
            checkStoreGeoFence(ctx, false);
        }
    }

    @Override
    public boolean hasOpenIdBlacklist() {
        Boolean cached = openIdBlacklistCache.getIfPresent(OPEN_ID_BLACKLIST_CACHE_KEY);
        if (cached != null) {
            return cached;
        }
        boolean exists = payBlacklistService.hasActiveOpenIdBlacklist();
        openIdBlacklistCache.put(OPEN_ID_BLACKLIST_CACHE_KEY, exists);
        return exists;
    }

    /// 按请求通道检查支付宝 / 微信用户标识名单
    private boolean checkUserIdentity(PayRiskCheckContext ctx, String identity, boolean throwOnHit) {
        if (StrUtil.isBlank(identity)) {
            return false;
        }
        String channel = ctx.getChannel();
        if (ChannelEnum.ALIPAY.getCode().equals(channel)) {
            return rejectIfBlocked(ctx, PayBlacklistTypeEnum.ALIPAY_USER.getCode(), identity, null, throwOnHit);
        }
        if (ChannelEnum.WECHAT.getCode().equals(channel)) {
            return rejectIfBlocked(ctx, PayBlacklistTypeEnum.WECHAT_OPENID.getCode(), identity,
                    ctx.getChannelAppId(), throwOnHit);
        }
        // 其它通道本期不查用户标识名单
        return false;
    }

    /// 返回是否命中（用于外层判断是否需要打降级日志）
    private boolean rejectIfBlocked(PayRiskCheckContext ctx, String type, String value,
                                     String wxAppId, boolean throwOnHit) {
        if (StrUtil.isBlank(value)) {
            return false;
        }
        Optional<PayBlacklist> hit = payBlacklistService.findActive(type, value, wxAppId);
        if (hit.isEmpty()) {
            return false;
        }
        PayBlacklist bl = hit.get();
        try {
            payRiskHitService.recordHit(ctx, type, value, bl.getId());
        } catch (Exception e) {
            log.warn("记录风险命中失败 type={} value={}: {}", type, value, e.getMessage());
        }
        if (throwOnHit) {
            // 交易被限制（模糊文案，防探测）
            throw new BizInfoException(PayErrorCode.OPERATION_FAIL, "pay.error.risk.blacklist");
        }
        return true;
    }

    /// IP 地区黑名单（第二层全局地区名单, 含省级 + 市级）
    ///
    /// 受 regionBlacklistEnabled 开关控制。先按 IP 归属省份匹配省级黑名单(value 存省行政区划编码, 如"44"),
    /// 命中即拦截不再执行市级检查; 未命中再按 IP 归属城市匹配市级黑名单(value 存市行政区划编码, 如"4403";
    /// 直辖市无独立市级, 城市名单存省编码, 由 [RegionCodeResolver#resolveCityCode] 回落为省编码)。
    /// 省/市名单数据在「支付安全 → 黑名单」中按 type 区分维护。
    /// IPv6(受 ipv6MatchEnabled 控制) / 内网 / 解析失败 / 无法映射编码 → fail-open 放行。
    /// @return 是否命中（省级或市级）
    private boolean checkRegionBlacklist(PayRiskCheckContext ctx, boolean throwOnHit) {
        // 地区拦截开关关闭时跳过
        if (!Boolean.TRUE.equals(ctx.getRegionBlacklistEnabled())) {
            return false;
        }
        String ip = ctx.getClientIp();
        if (StrUtil.isBlank(ip)) {
            return false;
        }
        // IPv6 受 ipv6MatchEnabled 开关控制（默认关闭, 离线数据精度有限）
        if (Validator.isIpv6(ip)) {
            if (!Boolean.TRUE.equals(ctx.getIpv6MatchEnabled())) {
                return false;
            }
            // 开关开启: 跳过 NetUtil.isInnerIP（仅支持 IPv4）, IPv6 内网由 xdb 查询 fail-open 兜底
        } else {
            // IPv4 内网/回环地址直通放行
            if (NetUtil.isInnerIP(ip)) {
                return false;
            }
        }
        IpRegion region = ipToRegionService.getRegionByIp(ip);
        // 解析失败 → fail-open
        if (region == null) {
            return false;
        }
        // 省级名单: IP 归属省份 → 行政区划编码(港澳台/"0"段/未知无法映射 → 跳过省级, 继续市级)
        String provinceCode = regionCodeResolver.resolveProvinceCode(region.getProvince());
        if (StrUtil.isNotBlank(provinceCode)
                && rejectIfBlocked(ctx, PayBlacklistTypeEnum.PROVINCE.getCode(), provinceCode, null, throwOnHit)) {
            // 省命中即拦截, 不再执行市级检查
            return true;
        }
        // 市级名单: IP 归属城市 → 行政区划编码(直辖市回落省编码; "0"段/未知无法映射 → fail-open)
        String cityCode = regionCodeResolver.resolveCityCode(region.getProvince(), region.getCity());
        if (StrUtil.isBlank(cityCode)) {
            return false;
        }
        return rejectIfBlocked(ctx, PayBlacklistTypeEnum.CITY.getCode(), cityCode, null, throwOnHit);
    }

    /// 海外 IP 地域拦截（country≠中国, 港澳台放行; 未知/内网 fail-open）
    ///
    /// 与黑名单不同, 海外命中不关联名单行（blacklistId=null）, 命中类型为 [PayBlacklistTypeEnum#OVERSEAS_IP]。
    /// 内网/回环地址由网段判定直通, 不依赖 xdb 文本标注:
    /// 新版 xdb 对保留地址段返回 country=Reserved/isp=0, 老版标注 isp=内网IP, 文本格式不可靠。
    private void checkOverseasIp(PayRiskCheckContext ctx, boolean throwOnHit) {
        String ip = ctx.getClientIp();
        if (StrUtil.isBlank(ip)) {
            return;
        }
        // IPv6 受 ipv6MatchEnabled 开关控制（默认关闭, 离线数据精度有限）
        if (Validator.isIpv6(ip)) {
            if (!Boolean.TRUE.equals(ctx.getIpv6MatchEnabled())) {
                return;
            }
            // 开关开启: 跳过 NetUtil.isInnerIP（仅支持 IPv4, 传 IPv6 会抛异常）, IPv6 内网由 xdb fail-open 兜底
        } else {
            // IPv4 内网/回环地址直通放行(网络层判定, 不查库)
            if (NetUtil.isInnerIP(ip)) {
                return;
            }
        }
        IpRegion region = ipToRegionService.getRegionByIp(ip);
        // 未知(IPv6/查询失败)/国内(含港澳台) → 放行
        if (region == null || region.isChinaIp()) {
            return;
        }
        // 兼容 xdb 文本标注的双保险: 老版 isp=内网IP / 新版 country=Reserved
        if (region.isInnerIp() || "Reserved".equals(region.getCountry())) {
            return;
        }
        // 海外 → 命中记录（blacklistId=null, 非黑名单来源）
        try {
            payRiskHitService.recordHit(ctx, PayBlacklistTypeEnum.OVERSEAS_IP.getCode(), ip, null);
        } catch (Exception e) {
            log.warn("记录海外IP命中失败 ip={}: {}", ip, e.getMessage());
        }
        if (throwOnHit) {
            // 交易被限制（模糊文案，防探测）
            throw new BizInfoException(PayErrorCode.OPERATION_FAIL, "pay.error.risk.blacklist");
        }
    }

    /// 门店市级地理围栏
    ///
    /// IP 归属城市是否落在门店放行城市集合([PayRiskCheckContext#getStoreAllowedCities])内, 不在则命中。
    /// 放行集合由 [cn.daxpay.open.payment.trade.runtime.service.pay.common.PayRiskAssistService] 按平台策略
    /// (strict 仅门店市 / balanced +邻市 / loose +同省)预算并归一化, 直辖市用 [IpRegion#isProvinceLevel] 归一。
    /// 门店无地址(storeCity 为空 / 放行集合空)时 fail-open + 告警; IPv6/内网/解析失败 fail-open。
    private void checkStoreGeoFence(PayRiskCheckContext ctx, boolean throwOnHit) {
        // 门店无地址或放行集合缺失: fail-open
        if (StrUtil.isBlank(ctx.getStoreCity()) || CollUtil.isEmpty(ctx.getStoreAllowedCities())) {
            if (ctx.getStoreNo() != null && StrUtil.isNotBlank(ctx.getStoreCity())) {
                // 门店有城市但放行集合为空(策略数据缺失), 降级放行并告警
                log.warn("围栏开启但放行城市集合为空, storeNo={}, mchNo={}, strategy={}, 跳过围栏校验",
                        ctx.getStoreNo(), ctx.getMchNo(), ctx.getGeoFenceStrategy());
            }
            return;
        }
        String ip = ctx.getClientIp();
        if (StrUtil.isBlank(ip)) {
            return;
        }
        // IPv6 受 ipv6MatchEnabled 开关控制（默认关闭, 离线数据精度有限）
        if (Validator.isIpv6(ip)) {
            if (!Boolean.TRUE.equals(ctx.getIpv6MatchEnabled())) {
                return;
            }
            // 开关开启: 跳过 NetUtil.isInnerIP（仅支持 IPv4）, IPv6 内网由 xdb 查询 fail-open 兜底
        } else {
            // IPv4 内网/回环地址直通放行
            if (NetUtil.isInnerIP(ip)) {
                return;
            }
        }
        IpRegion region = ipToRegionService.getRegionByIp(ip);
        if (region == null) {
            return;
        }
        // 解析出城市或为直辖市才继续, 否则 fail-open
        boolean hasCity = StrUtil.isNotBlank(region.getCity());
        boolean isDirect = region.isProvinceLevel();
        if (!hasCity && !isDirect) {
            return;
        }
        // 直辖市: 城市即省份; 普通市: 城市为 city
        String clientCity = isDirect ? region.getProvince() : region.getCity();
        // 回填 ctx 供命中落库
        ctx.setClientCity(clientCity);
        // 归一化后判定是否落在门店放行集合内
        String clientNorm = GeoFenceUtil.normalizeRegionName(clientCity);
        if (!ctx.getStoreAllowedCities().contains(clientNorm)) {
            // 记录围栏命中(非黑名单来源, blacklistId=null)
            try {
                payRiskHitService.recordHit(ctx, HIT_TYPE_GEO_FENCE, clientCity, null);
            } catch (Exception e) {
                log.warn("记录地理围栏命中失败 clientCity={}, storeCity={}: {}",
                        clientCity, ctx.getStoreCity(), e.getMessage());
            }
            if (throwOnHit) {
                // 交易被限制（模糊文案，防探测）
                throw new BizInfoException(PayErrorCode.OPERATION_FAIL, "pay.error.risk.blacklist");
            }
        }
    }
}
