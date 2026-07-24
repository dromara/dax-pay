package cn.daxpay.open.plugin.risk.service;

import cn.daxpay.open.payment.strategy.risk.PayRiskCheckContext;
import cn.daxpay.open.platform.capability.auth.util.SecurityUtil;
import cn.daxpay.open.platform.common.mybatisplus.util.MpUtil;
import cn.daxpay.open.platform.common.translate.service.TransService;
import cn.daxpay.open.platform.core.code.PayErrorCode;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import cn.daxpay.open.plugin.risk.dao.PayRiskHitManager;
import cn.daxpay.open.plugin.risk.entity.PayBlacklist;
import cn.daxpay.open.plugin.risk.entity.PayRiskHit;
import cn.daxpay.open.plugin.risk.enums.PayRiskHitHandleStatusEnum;
import cn.daxpay.open.plugin.risk.enums.PayRiskHitPhaseEnum;
import cn.daxpay.open.plugin.risk.enums.PayRiskHitSceneEnum;
import cn.daxpay.open.plugin.risk.param.PayRiskHitHandleParam;
import cn.daxpay.open.plugin.risk.param.PayRiskHitQuery;
import cn.daxpay.open.plugin.risk.result.PayRiskHitResult;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

/// # 风险命中服务
///
@Slf4j
@Service
@RequiredArgsConstructor
public class PayRiskHitService {

    private final PayRiskHitManager payRiskHitManager;
    private final PayBlacklistService payBlacklistService;
    private final TransService transService;

    /// 分页
    public PageResult<PayRiskHitResult> page(PageParam pageParam, PayRiskHitQuery query) {
        PageResult<PayRiskHitResult> pageResult = MpUtil.toPageResult(payRiskHitManager.page(pageParam, query));
        // 翻译商户名称(mchNo -> mchName)
        transService.translate(pageResult);
        return pageResult;
    }

    /// 详情
    public PayRiskHitResult findById(Long id) {
        PayRiskHitResult result = getEntity(id).toResult();
        // 翻译商户名称
        transService.translate(result);
        return result;
    }

    /// 处理命中
    @Transactional(rollbackFor = Exception.class)
    public void handle(PayRiskHitHandleParam param) {
        PayRiskHitHandleStatusEnum status = PayRiskHitHandleStatusEnum.findByCode(param.getHandleStatus())
                .orElseThrow(() -> new BizInfoException(PayErrorCode.OPERATION_FAIL,
                        "pay.error.risk.handleStatusInvalid"));
        PayRiskHit entity = getEntity(param.getId());
        if (status == PayRiskHitHandleStatusEnum.ADDED_BLACKLIST) {
            // 微信名单需 wxAppId；命中快照无该字段时无法自动写入
            PayBlacklist bl = payBlacklistService.ensureBlacklist(
                    entity.getHitType(),
                    entity.getHitValue(),
                    entity.getChannel(),
                    null,
                    param.getHandleRemark());
            entity.setBlacklistId(bl.getId());
        }
        entity.setHandleStatus(status.getCode());
        entity.setHandleRemark(param.getHandleRemark());
        entity.setHandleUserId(SecurityUtil.getUserIdOrDefaultId());
        entity.setHandleTime(OffsetDateTime.now(ZoneOffset.UTC));
        payRiskHitManager.updateById(entity);
    }

    /// 记录命中（支付接入 / 检查器调用）
    @Transactional(rollbackFor = Exception.class)
    public void recordHit(PayRiskCheckContext ctx, String hitType, String hitValue, Long blacklistId) {
        if (StrUtil.isBlank(hitType) || StrUtil.isBlank(hitValue)) {
            return;
        }
        PayRiskHit hit = new PayRiskHit()
                .setPhase(StrUtil.blankToDefault(ctx.getPhase(), PayRiskHitPhaseEnum.BEFORE_PAY.getCode()))
                .setHitType(hitType)
                .setHitValue(hitValue)
                .setBlacklistId(blacklistId)
                .setMchNo(ctx.getMchNo())
                .setAppId(ctx.getAppId())
                .setTradeNo(ctx.getTradeNo())
                .setOrderNo(ctx.getOrderNo())
                .setBizOrderNo(ctx.getBizOrderNo())
                .setTradeType(ctx.getTradeType())
                .setMethod(ctx.getMethod())
                .setProduct(ctx.getProduct())
                .setChannel(ctx.getChannel())
                .setClientIp(ctx.getClientIp())
                .setOpenid(ctx.getOpenId())
                .setBuyerId(ctx.getBuyerId())
                .setScene(StrUtil.blankToDefault(ctx.getScene(), PayRiskHitSceneEnum.UNKNOWN.getCode()))
                .setHandleStatus(PayRiskHitHandleStatusEnum.PENDING.getCode());
        payRiskHitManager.save(hit);
    }

    private PayRiskHit getEntity(Long id) {
        return payRiskHitManager.findById(id)
                // 风险命中记录不存在
                .orElseThrow(() -> new DataNotExistException("pay.error.risk.hitNotFound"));
    }
}
