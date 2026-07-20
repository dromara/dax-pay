package cn.daxpay.open.plugin.risk.strategy;

import cn.daxpay.open.payment.strategy.risk.PayRiskCheckContext;
import cn.daxpay.open.payment.strategy.risk.PayRiskChecker;
import cn.daxpay.open.platform.core.code.PayErrorCode;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.plugin.risk.entity.PayBlacklist;
import cn.daxpay.open.plugin.risk.enums.PayBlacklistTypeEnum;
import cn.daxpay.open.plugin.risk.enums.PayRiskHitPhaseEnum;
import cn.daxpay.open.plugin.risk.service.PayBlacklistService;
import cn.daxpay.open.plugin.risk.service.PayRiskHitService;
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

    /// openId 黑名单存在性缓存（短 TTL 30s）
    ///
    /// 仅供网关层判断是否触发强制 OAuth 取 openId, 非关键路径,
    /// 30s 延迟可接受（黑名单 CRUD 不会立刻反映到 OAuth 触发判定）
    private final Cache<String, Boolean> openIdBlacklistCache = Caffeine.newBuilder()
            .expireAfterWrite(30, TimeUnit.SECONDS)
            .maximumSize(1)
            .build();

    private static final String OPEN_ID_BLACKLIST_CACHE_KEY = "hasOpenId";

    @Override
    public void checkBeforePay(PayRiskCheckContext ctx) {
        if (ctx == null) {
            return;
        }
        ctx.setPhase(PayRiskHitPhaseEnum.BEFORE_PAY.getCode());
        // IP 名单（全局生效）
        rejectIfBlocked(ctx, PayBlacklistTypeEnum.IP.getCode(), ctx.getClientIp(), null, null, true);
        // openId 名单（按通道精细匹配）
        boolean openIdBlocked = rejectIfBlocked(ctx, PayBlacklistTypeEnum.OPEN_ID.getCode(), ctx.getOpenId(),
                ctx.getChannel(), ctx.getChannelAppId(), true);
        // openId 缺失降级: ctx.openId 为空且本次未触发 openId 拦截时, 打 WARN 供运营识别
        // 付款码 / APP / 外部浏览器 H5 等场景无 OAuth 时机, 依赖 IP + 事后 buyerId 补录
        if (!openIdBlocked && StrUtil.isBlank(ctx.getOpenId())) {
            log.warn("支付前 openId 缺失, openId 黑名单降级为仅 IP 校验 + 事后补录: "
                    + "tradeType={}, method={}, mchNo={}, clientIp={}",
                    ctx.getTradeType(), ctx.getMethod(), ctx.getMchNo(), ctx.getClientIp());
        }
    }

    @Override
    public void checkAfterPay(PayRiskCheckContext ctx) {
        if (ctx == null) {
            return;
        }
        ctx.setPhase(PayRiskHitPhaseEnum.AFTER_PAY.getCode());
        // 事后只记命中，不抛错
        rejectIfBlocked(ctx, PayBlacklistTypeEnum.IP.getCode(), ctx.getClientIp(), null, null, false);
        rejectIfBlocked(ctx, PayBlacklistTypeEnum.OPEN_ID.getCode(), ctx.getOpenId(),
                ctx.getChannel(), ctx.getChannelAppId(), false);
        // buyerId 按 open_id 维度比对（主扫补洞）
        if (StrUtil.isNotBlank(ctx.getBuyerId()) && !StrUtil.equals(ctx.getBuyerId(), ctx.getOpenId())) {
            rejectIfBlocked(ctx, PayBlacklistTypeEnum.OPEN_ID.getCode(), ctx.getBuyerId(),
                    ctx.getChannel(), ctx.getChannelAppId(), false);
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

    /// 返回是否命中（用于外层判断是否需要打降级日志）
    private boolean rejectIfBlocked(PayRiskCheckContext ctx, String type, String value,
                                     String channel, String channelAppId, boolean throwOnHit) {
        if (StrUtil.isBlank(value)) {
            return false;
        }
        Optional<PayBlacklist> hit = payBlacklistService.findActive(type, value, channel, channelAppId);
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
}
