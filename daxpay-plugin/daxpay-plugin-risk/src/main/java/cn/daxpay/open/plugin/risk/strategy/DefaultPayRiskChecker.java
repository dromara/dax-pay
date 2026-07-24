package cn.daxpay.open.plugin.risk.strategy;

import cn.daxpay.open.payment.strategy.risk.PayRiskCheckContext;
import cn.daxpay.open.payment.strategy.risk.PayRiskChecker;
import cn.daxpay.open.platform.core.code.PayErrorCode;
import cn.daxpay.open.platform.core.enums.pay.channel.ChannelEnum;
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

    /// 用户标识黑名单存在性缓存（短 TTL 30s）
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
        // null/true=阻断下单；false=仅落命中（对齐 riskBlockBeforePay）
        boolean throwOnHit = !Boolean.FALSE.equals(ctx.getBlockOnHit());
        // IP 名单（全局生效）
        rejectIfBlocked(ctx, PayBlacklistTypeEnum.IP.getCode(), ctx.getClientIp(), null, throwOnHit);
        // 用户标识：按通道映射名单类型
        boolean identityBlocked = checkUserIdentity(ctx, ctx.getOpenId(), throwOnHit);
        if (!identityBlocked && StrUtil.isBlank(ctx.getOpenId())) {
            log.warn("支付前 openId 缺失, 用户标识黑名单降级为仅 IP 校验 + 事后补录: "
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
        rejectIfBlocked(ctx, PayBlacklistTypeEnum.IP.getCode(), ctx.getClientIp(), null, false);
        checkUserIdentity(ctx, ctx.getOpenId(), false);
        // buyerId 按用户标识维度比对（主扫补洞）
        if (StrUtil.isNotBlank(ctx.getBuyerId()) && !StrUtil.equals(ctx.getBuyerId(), ctx.getOpenId())) {
            checkUserIdentity(ctx, ctx.getBuyerId(), false);
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
}
