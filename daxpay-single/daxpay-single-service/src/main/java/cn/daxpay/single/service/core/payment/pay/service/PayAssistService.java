package cn.daxpay.single.service.core.payment.pay.service;

import cn.bootx.platform.common.core.util.CollUtil;
import cn.bootx.platform.common.core.util.LocalDateTimeUtil;
import cn.daxpay.single.code.PayChannelEnum;
import cn.daxpay.single.code.PayOrderRefundStatusEnum;
import cn.daxpay.single.exception.pay.PayFailureException;
import cn.daxpay.single.param.payment.pay.PayParam;
import cn.daxpay.single.result.pay.PayResult;
import cn.daxpay.single.service.common.context.ApiInfoLocal;
import cn.daxpay.single.service.common.context.NoticeLocal;
import cn.daxpay.single.service.common.context.PayLocal;
import cn.daxpay.single.service.common.context.PlatformLocal;
import cn.daxpay.single.service.common.local.PaymentContextLocal;
import cn.daxpay.single.service.core.order.pay.builder.PayBuilder;
import cn.daxpay.single.service.core.order.pay.dao.PayOrderExtraManager;
import cn.daxpay.single.service.core.order.pay.entity.PayOrder;
import cn.daxpay.single.service.core.order.pay.entity.PayOrderExtra;
import cn.daxpay.single.service.core.order.pay.service.PayOrderQueryService;
import cn.daxpay.single.service.core.order.pay.service.PayOrderService;
import cn.daxpay.single.service.core.payment.sync.service.PaySyncService;
import cn.daxpay.single.util.PayUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static cn.daxpay.single.code.PayStatusEnum.*;

/**
 * 支付支持服务
 * @author xxm
 * @since 2023/12/24
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PayAssistService {

    private final PaySyncService paySyncService;

    private final PayOrderService payOrderService;

    private final PayOrderQueryService payOrderQueryService;

    private final PayOrderExtraManager payOrderExtraManager;

    /**
     * 初始化支付相关上下文
     */
    public void initPayContext(PayOrder order, PayParam payParam) {
        // 初始化支付订单超时时间
        this.initExpiredTime(order, payParam);
        // 初始化通知相关上下文
        this.initNotice(payParam);
    }


    /**
     * 初始化支付订单超时时间
     * 1. 如果支付记录为空, 超时时间读取顺序 PayParam -> 平台设置
     * 2. 如果支付记录不为空, 超时时间通过支付记录进行反推
     */
    public void initExpiredTime(PayOrder order, PayParam payParam) {
        // 钱包没有超时时间
        if (PayChannelEnum.WALLET.getCode()
                .equals(payParam.getChannel())) {
            return;
        }
        PayLocal payInfo = PaymentContextLocal.get()
                .getPayInfo();
        PlatformLocal platform = PaymentContextLocal.get()
                .getPlatformInfo();
        // 支付订单是非为空
        if (Objects.nonNull(order)) {
            payInfo.setExpiredTime(order.getExpiredTime());
            return;
        }
        // 支付参数传入
        if (Objects.nonNull(payParam.getExpiredTime())) {
            payInfo.setExpiredTime(payParam.getExpiredTime());
            return;
        }
        // 读取本地时间
        LocalDateTime paymentExpiredTime = PayUtil.getPaymentExpiredTime(platform.getOrderTimeout());
        payInfo.setExpiredTime(paymentExpiredTime);
    }

    /**
     * 初始化通知相关上下文
     * 1. 异步通知参数: 读取参数配置 -> 读取接口配置 -> 读取平台参数
     * 2. 同步跳转参数: 读取参数配置 -> 读取平台参数
     * 3. 中途退出地址: 读取参数配置
     */
    private void initNotice(PayParam payParam) {
        NoticeLocal noticeInfo = PaymentContextLocal.get()
                .getNoticeInfo();
        ApiInfoLocal apiInfo = PaymentContextLocal.get()
                .getApiInfo();
        PlatformLocal platform = PaymentContextLocal.get()
                .getPlatformInfo();
        // 异步回调为开启状态
        if (!Objects.equals(payParam.getNotNotify(), false) && apiInfo.isNotice()) {
            // 首先读取请求参数
            noticeInfo.setNotifyUrl(payParam.getNotifyUrl());
            // 读取接口配置
            if (StrUtil.isBlank(noticeInfo.getNotifyUrl())) {
                noticeInfo.setNotifyUrl(apiInfo.getNoticeUrl());
            }
            // 读取平台配置
            if (StrUtil.isBlank(noticeInfo.getNotifyUrl())) {
                noticeInfo.setNotifyUrl(platform.getNotifyUrl());
            }
        }
        // 同步回调
        noticeInfo.setReturnUrl(payParam.getReturnUrl());
        if (StrUtil.isBlank(noticeInfo.getReturnUrl())) {
            noticeInfo.setReturnUrl(platform.getNotifyUrl());
        }
        // 退出回调地址
        noticeInfo.setQuitUrl(payParam.getQuitUrl());
    }


    /**
     * 创建支付订单并保存, 返回支付订单
     */
    @Transactional(rollbackFor = Exception.class)
    public PayOrder createPayOrder(PayParam payParam) {
        PayLocal payInfo = PaymentContextLocal.get()
                .getPayInfo();
        // 构建支付订单并保存
        PayOrder order = PayBuilder.buildPayOrder(payParam);
        payOrderService.save(order);
        // 构建支付订单扩展表并保存
        PayOrderExtra payOrderExtra = PayBuilder.buildPayOrderExtra(payParam, order.getId());
        payOrderExtraManager.save(payOrderExtra);
        payInfo.setPayOrder(order)
                .setPayOrderExtra(payOrderExtra);
        return order;
    }

    /**
     * 根据新传入的支付订单更新订单和扩展信息
     */
    @Transactional(rollbackFor = Exception.class)
    public void updatePayOrder(PayParam payParam,PayOrder order, PayOrderExtra payOrderExtra) {
        // 订单信息
        order.setAllocation(payParam.getAllocation())
                .setChannel(payParam.getChannel())
                .setMethod(payParam.getMethod());
        if (!order.getAllocation()) {
            order.setAllocStatus(null);
        }

        // 扩展信息
        NoticeLocal noticeInfo = PaymentContextLocal.get().getNoticeInfo();
        payOrderExtra.setClientIp(payParam.getClientIp())
                .setNotifyUrl(noticeInfo.getNotifyUrl())
                .setReturnUrl(noticeInfo.getReturnUrl())
                .setAttach(payParam.getAttach())
                .setClientIp(payParam.getClientIp())
                .setReqTime(payParam.getReqTime());
        if (CollUtil.isNotEmpty(payParam.getExtraParam())){
            payOrderExtra.setExtraParam(JSONUtil.toJsonStr(payParam.getExtraParam()));
        } else {
            payOrderExtra.setExtraParam(null);
        }

        payOrderService.updateById(order);
        payOrderExtraManager.updateById(payOrderExtra);
    }

    /**
     * 校验支付状态，支付成功则返回，支付失败则抛出对应的异常
     */
    public PayOrder getOrderAndCheck(String bizOrderNo) {
        // 根据订单查询支付记录
        PayOrder payOrder = payOrderQueryService.findByBizOrderNo(bizOrderNo)
                .orElse(null);
        if (Objects.nonNull(payOrder)) {
            // 待支付
            if (Objects.equals(payOrder.getStatus(), PROGRESS.getCode())) {
                // 如果支付超时, 触发订单同步操作, 同时抛出异常
                if (Objects.nonNull(payOrder.getExpiredTime()) && LocalDateTimeUtil.ge(LocalDateTime.now(), payOrder.getExpiredTime())) {
                    paySyncService.syncPayOrder(payOrder);
                    throw new PayFailureException("支付已超时，请重新确认支付状态");
                }
                return payOrder;
            }
            // 已经支付状态
            if (SUCCESS.getCode()
                    .equals(payOrder.getStatus())) {
                throw new PayFailureException("已经支付成功，请勿重新支付");
            }
            // 支付失败类型状态
            List<String> tradesStatus = Arrays.asList(FAIL.getCode(), CLOSE.getCode(), CANCEL.getCode());
            if (tradesStatus.contains(payOrder.getStatus())) {
                throw new PayFailureException("支付失败或已经被关闭");
            }
            // 退款类型状态
            if (Objects.equals(payOrder.getRefundStatus(), PayOrderRefundStatusEnum.REFUNDING.getCode())) {
                throw new PayFailureException("该订单处于退款状态");
            }
            // 其他状态直接抛出兜底异常
            throw new PayFailureException("订单不是待支付状态，请重新确认订单状态");
        }
        return null;
    }

    /**
     * 检验订单是否超过限额
     */
    public void validationLimitAmount(PayParam payParam) {
        // 总额校验
        PlatformLocal platformInfo = PaymentContextLocal.get()
                .getPlatformInfo();
        if (payParam.getAmount() > platformInfo.getLimitAmount()) {
            throw new PayFailureException("支付金额超过限额");
        }
    }


    /**
     * 根据支付订单构建支付结果
     * @param payOrder 支付订单
     * @return PayResult 支付结果
     */
    public PayResult buildResult(PayOrder payOrder) {
        PayResult payResult;
        payResult = new PayResult();
        payResult.setBizOrderNo(payOrder.getBizOrderNo());
        payResult.setOrderNo(payOrder.getOrderNo());
        payResult.setStatus(payOrder.getStatus());

        // 设置支付参数
        PayLocal payInfo = PaymentContextLocal.get()
                .getPayInfo();
        if (StrUtil.isNotBlank(payInfo.getPayBody())) {
            payResult.setPayBody(payInfo.getPayBody());
        }
        // 签名
        return payResult;
    }
}
