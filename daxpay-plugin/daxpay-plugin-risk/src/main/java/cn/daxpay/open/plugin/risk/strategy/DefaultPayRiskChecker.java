package cn.daxpay.open.plugin.risk.strategy;

import cn.daxpay.open.payment.strategy.risk.PayRiskCheckContext;
import cn.daxpay.open.payment.strategy.risk.PayRiskChecker;
import cn.daxpay.open.payment.strategy.risk.RegionCodeResolver;
import cn.daxpay.open.platform.capability.audit.log.service.ip2region.IpRegion;
import cn.daxpay.open.platform.capability.audit.log.service.ip2region.IpToRegionService;
import cn.daxpay.open.platform.core.code.PayErrorCode;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.plugin.risk.entity.PayBlacklist;
import cn.daxpay.open.plugin.risk.enums.PayBlacklistTypeEnum;
import cn.daxpay.open.plugin.risk.enums.PayRiskHitPhaseEnum;
import cn.daxpay.open.plugin.risk.service.PayBlacklistService;
import cn.daxpay.open.plugin.risk.service.PayRiskHitService;
import cn.hutool.core.lang.Validator;
import cn.hutool.core.net.NetUtil;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

/// # 默认支付风控检查器
///
/// 实现 [PayRiskChecker] SPI，由 daxpay-plugin-risk 自动配置注入。
/// 事前防线三层: L1 IP 黑名单 → 海外 IP 拦截 → L2 省市地区黑名单;
/// 用户标识黑名单、门店地理围栏、支付后补录为商业版独占, 基础版不做。
@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultPayRiskChecker implements PayRiskChecker {

    private final PayBlacklistService payBlacklistService;
    private final PayRiskHitService payRiskHitService;
    private final IpToRegionService ipToRegionService;
    private final RegionCodeResolver regionCodeResolver;

    @Override
    public void checkBeforePay(PayRiskCheckContext ctx) {
        if (ctx == null) {
            return;
        }
        ctx.setPhase(PayRiskHitPhaseEnum.BEFORE_PAY.getCode());
        // null/true=阻断下单；false=仅落命中（对齐 riskBlockBeforePay）
        boolean throwOnHit = !Boolean.FALSE.equals(ctx.getBlockOnHit());
        // L1 黑名单（IP, 受 blacklistEnabled 开关控制）; 用户标识黑名单为商业版独占
        if (Boolean.TRUE.equals(ctx.getBlacklistEnabled())) {
            // IP 名单
            rejectIfBlocked(ctx, PayBlacklistTypeEnum.IP.getCode(), ctx.getClientIp(), null, throwOnHit);
        }
        // IP 海外拦截（地域策略, 非黑名单）
        if (Boolean.TRUE.equals(ctx.getBlockOverseasIp())) {
            checkOverseasIp(ctx, throwOnHit);
        }
        // L2 IP 地区黑名单（全局地区名单, 受 regionBlacklistEnabled 开关控制; 省级命中后不执行市级检查）
        checkRegionBlacklist(ctx, throwOnHit);
    }

    @Override
    public void checkAfterPay(PayRiskCheckContext ctx) {
        // 基础版不做事后补录（用户标识比对 / 省市地区 / 地理围栏补录均为商业版独占），直接返回
        return;
    }

    @Override
    public boolean hasOpenIdBlacklist() {
        // 基础版不启用用户标识黑名单, 恒返回 false（网关层不会触发强制 OAuth 取 openId）
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
        } else if (isInnerIpv4(ip)) {
            // IPv4 内网/回环地址直通放行(非法/非 IPv4 格式落到后续 getRegionByIp→null fail-open)
            return false;
        }
        IpRegion region = ipToRegionService.getRegionByIp(ip);
        // 解析失败 → fail-open
        if (region == null) {
            return false;
        }
        // 回填 IP 归属城市快照(直辖市: 城市即省份; 普通市: 城市为 city), 供命中落库
        String clientCity = region.isProvinceLevel() ? region.getProvince() : region.getCity();
        ctx.setClientCity(clientCity);
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
        } else if (isInnerIpv4(ip)) {
            // IPv4 内网/回环地址直通放行(网络层判定, 不查库; 非法格式落到后续 region null 放行)
            return;
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

    /// 判定 IPv4 内网/回环地址
    ///
    /// 先校验 IPv4 格式再调 [NetUtil#isInnerIP]: hutool 的 NetUtil.isInnerIP 对非法字符串(非 IPv4 非 IPv6,
    /// 如 "abc"/"999.1.1.1"/XFF 多值)会直接抛 IllegalArgumentException, 击穿支付前风控链路导致下单 500。
    /// 非法或非 IPv4 格式一律返回 false(fail-open), 由调用方后续 getRegionByIp 解析失败兜底放行。
    private boolean isInnerIpv4(String ip) {
        if (!Validator.isIpv4(ip)) {
            return false;
        }
        try {
            return NetUtil.isInnerIP(ip);
        } catch (Exception e) {
            return false;
        }
    }
}
