package cn.bootx.platform.daxpay.service.core.payment.refund.service;

import cn.bootx.platform.common.core.exception.ValidationFailedException;
import cn.bootx.platform.common.core.util.CollUtil;
import cn.bootx.platform.daxpay.code.PayRefundStatusEnum;
import cn.bootx.platform.daxpay.code.PayStatusEnum;
import cn.bootx.platform.daxpay.service.common.context.AsyncRefundLocal;
import cn.bootx.platform.daxpay.service.common.context.NoticeLocal;
import cn.bootx.platform.daxpay.service.common.context.PlatformLocal;
import cn.bootx.platform.daxpay.service.common.local.PaymentContextLocal;
import cn.bootx.platform.daxpay.service.core.order.pay.entity.PayOrder;
import cn.bootx.platform.daxpay.service.core.order.pay.service.PayOrderService;
import cn.bootx.platform.daxpay.service.core.order.refund.dao.PayRefundOrderManager;
import cn.bootx.platform.daxpay.service.core.order.refund.entity.PayRefundOrder;
import cn.bootx.platform.daxpay.exception.pay.PayFailureException;
import cn.bootx.platform.daxpay.param.pay.RefundChannelParam;
import cn.bootx.platform.daxpay.param.pay.RefundParam;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * 支付退款支撑服务
 * @author xxm
 * @since 2023/12/26
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PayRefundAssistService {
    private final PayOrderService payOrderService;

    private final PayRefundOrderManager payRefundOrderManager;

    /**
     * 初始化上下文
     */
    public void initRefundContext(RefundParam param){
        // 初始化通知相关上下文
        this.initNotice(param);
    }

    /**
     * 初始化通知相关上下文
     */
    private void initNotice(RefundParam param) {
        NoticeLocal noticeInfo = PaymentContextLocal.get().getNoticeInfo();
        PlatformLocal platform = PaymentContextLocal.get().getPlatform();
        // 异步回调
        if (!param.isNotNotify()){
            noticeInfo.setNotifyUrl(param.getReturnUrl());
            if (StrUtil.isNotBlank(param.getNotifyUrl())){
                noticeInfo.setNotifyUrl(platform.getNotifyUrl());
            }
        }
    }

    /**
     * 根据退款参数获取支付订单, 并进行检查
     */
    public PayOrder getPayOrderAndCheckByRefundParam(RefundParam param, boolean simple){
        if (!param.isRefundAll()) {
            if (CollUtil.isEmpty(param.getRefundChannels())) {
                throw new ValidationFailedException("退款通道参数不能为空");
            }
            if (Objects.isNull(param.getRefundNo())) {
                throw new ValidationFailedException("部分退款时退款单号必传");
            }
        }

        PayOrder payOrder = null;
        if (Objects.nonNull(param.getPaymentId())){
            payOrder = payOrderService.findById(param.getPaymentId())
                    .orElseThrow(() -> new PayFailureException("未查询到支付订单"));
        }
        if (Objects.isNull(payOrder)){
            payOrder = payOrderService.findByBusinessNo(param.getBusinessNo())
                    .orElseThrow(() -> new PayFailureException("未查询到支付订单"));
        }

        // 简单退款处理
        if (simple){
            // 简单退款校验
            if (payOrder.isCombinationPay()){
                throw new PayFailureException("组合支付不可以使用简单退款方式");
            }
            // 设置退款参数的通道配置
            String channel = payOrder.getRefundableInfos()
                    .get(0)
                    .getChannel();
            param.getRefundChannels().get(0).setChannel(channel);
        }


        // 状态判断, 支付中/失败/取消等不能进行退款
        List<String> tradesStatus = Arrays.asList(
                PayStatusEnum.PROGRESS.getCode(),
                PayStatusEnum.CLOSE.getCode(),
                PayStatusEnum.FAIL.getCode());
        if (tradesStatus.contains(payOrder.getStatus())) {
            PayStatusEnum statusEnum = PayStatusEnum.findByCode(payOrder.getStatus());
            throw new PayFailureException("当前状态["+statusEnum.getName()+"]不允许状态非法, 无法退款");
        }
        return payOrder;
    }

    /**
     * 保存退款记录 成不成功都记录
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveOrder(RefundParam refundParam, PayOrder payOrder){
        AsyncRefundLocal asyncRefundInfo = PaymentContextLocal.get().getAsyncRefundInfo();
        // 退款金额
        Integer amount = refundParam.getRefundChannels()
                .stream()
                .map(RefundChannelParam::getAmount)
                .reduce(0, Integer::sum);

        PayRefundOrder refundOrder = new PayRefundOrder()
                .setRefundRequestNo(asyncRefundInfo.getRefundNo())
                .setAmount(amount)
                .setRefundableBalance(payOrder.getRefundableBalance())
                .setPaymentId(payOrder.getId())
                .setBusinessNo(payOrder.getBusinessNo())
                .setRefundTime(LocalDateTime.now())
                .setTitle(payOrder.getTitle())
                .setErrorMsg(asyncRefundInfo.getErrorMsg())
                .setErrorCode(asyncRefundInfo.getErrorCode())
                .setStatus(Objects.isNull(asyncRefundInfo.getErrorCode()) ? PayRefundStatusEnum.SUCCESS.getCode() : PayRefundStatusEnum.FAIL.getCode())
                .setClientIp(refundParam.getClientIp())
                .setReqId(PaymentContextLocal.get().getRequest().getReqId());
        payRefundOrderManager.save(refundOrder);
    }
}
