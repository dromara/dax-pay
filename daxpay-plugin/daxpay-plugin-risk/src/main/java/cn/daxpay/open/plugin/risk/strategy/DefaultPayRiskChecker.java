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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

/// # 默认支付风控检查器
///
/// 实现 [PayRiskChecker] SPI。本期支付主链路尚未调用，便于联调与后续接入。
@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultPayRiskChecker implements PayRiskChecker {

    private final PayBlacklistService payBlacklistService;
    private final PayRiskHitService payRiskHitService;

    @Override
    public void checkBeforePay(PayRiskCheckContext ctx) {
        if (ctx == null) {
            return;
        }
        ctx.setPhase(PayRiskHitPhaseEnum.BEFORE_PAY.getCode());
        // IP
        rejectIfBlocked(ctx, PayBlacklistTypeEnum.IP.getCode(), ctx.getClientIp(), null, null, true);
        // openId
        rejectIfBlocked(ctx, PayBlacklistTypeEnum.OPEN_ID.getCode(), ctx.getOpenId(),
                ctx.getChannel(), ctx.getChannelAppId(), true);
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

    private void rejectIfBlocked(PayRiskCheckContext ctx, String type, String value,
                                 String channel, String channelAppId, boolean throwOnHit) {
        if (StrUtil.isBlank(value)) {
            return;
        }
        Optional<PayBlacklist> hit = payBlacklistService.findActive(type, value, channel, channelAppId);
        if (hit.isEmpty()) {
            return;
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
    }
}
