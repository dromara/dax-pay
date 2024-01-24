package cn.bootx.platform.daxpay.service.core.payment.pay.service;

import cn.bootx.platform.common.core.exception.DataNotExistException;
import cn.bootx.platform.common.core.util.LocalDateTimeUtil;
import cn.bootx.platform.daxpay.code.PayChannelEnum;
import cn.bootx.platform.daxpay.exception.pay.PayFailureException;
import cn.bootx.platform.daxpay.param.pay.PayChannelParam;
import cn.bootx.platform.daxpay.param.pay.PayParam;
import cn.bootx.platform.daxpay.service.common.context.AsyncPayLocal;
import cn.bootx.platform.daxpay.service.common.context.NoticeLocal;
import cn.bootx.platform.daxpay.service.common.context.PlatformLocal;
import cn.bootx.platform.daxpay.service.common.local.PaymentContextLocal;
import cn.bootx.platform.daxpay.service.core.order.pay.builder.PayBuilder;
import cn.bootx.platform.daxpay.service.core.order.pay.dao.PayChannelOrderManager;
import cn.bootx.platform.daxpay.service.core.order.pay.dao.PayOrderExtraManager;
import cn.bootx.platform.daxpay.service.core.order.pay.entity.PayChannelOrder;
import cn.bootx.platform.daxpay.service.core.order.pay.entity.PayOrder;
import cn.bootx.platform.daxpay.service.core.order.pay.entity.PayOrderExtra;
import cn.bootx.platform.daxpay.service.core.order.pay.service.PayOrderQueryService;
import cn.bootx.platform.daxpay.service.core.order.pay.service.PayOrderService;
import cn.bootx.platform.daxpay.service.core.payment.sync.service.PaySyncService;
import cn.bootx.platform.daxpay.util.PayUtil;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static cn.bootx.platform.daxpay.code.PayStatusEnum.*;

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

    private final PayChannelOrderManager payChannelOrderManager;

    /**
     * 初始化支付相关上下文
     */
    public void initPayContext(PayOrder order, PayParam payParam){
        // 初始化支付订单超时时间
        this.initExpiredTime(order,payParam);
        // 初始化通知相关上下文
        this.initNotice(payParam);
    }


    /**
     * 初始化支付订单超时时间
     * 1. 如果支付记录为空, 超时时间读取顺序 PayParam -> 平台设置
     * 2. 如果支付记录不为空, 超时时间通过支付记录进行反推
     */
    public void initExpiredTime(PayOrder order, PayParam payParam){
        // 不是异步支付，没有超时时间
        if (PayUtil.isNotSync(payParam.getPayChannels())){
            return;
        }
        AsyncPayLocal asyncPayInfo = PaymentContextLocal.get().getAsyncPayInfo();
        PlatformLocal platform = PaymentContextLocal.get().getPlatformInfo();
        // 支付订单是非为空
        if (Objects.nonNull(order)){
            asyncPayInfo.setExpiredTime(order.getExpiredTime());
            return;
        }
        // 支付参数传入
        if (Objects.nonNull(payParam.getExpiredTime())){
            asyncPayInfo.setExpiredTime(payParam.getExpiredTime());
            return;
        }
        LocalDateTime paymentExpiredTime = PayUtil.getPaymentExpiredTime(platform.getOrderTimeout());
        asyncPayInfo.setExpiredTime(paymentExpiredTime);
    }

    /**
     * 初始化通知相关上下文
     */
    private void initNotice(PayParam payParam){
        NoticeLocal noticeInfo = PaymentContextLocal.get().getNoticeInfo();
        PlatformLocal platform = PaymentContextLocal.get().getPlatformInfo();
        // 异步回调
        if (!payParam.isNotNotify()){
            noticeInfo.setNotifyUrl(payParam.getReturnUrl());
            if (StrUtil.isNotBlank(payParam.getNotifyUrl())){
                noticeInfo.setNotifyUrl(platform.getNotifyUrl());
            }
        }
        // 同步回调
        if (!payParam.isNotReturn()){
            noticeInfo.setReturnUrl(payParam.getReturnUrl());
        }
        // 退出回调地址
        noticeInfo.setQuitUrl(payParam.getQuitUrl());
    }


    /**
     * 获取异步支付参数
     */
    public PayChannelParam getAsyncPayParam(PayParam payParam, PayOrder payOrder) {
        // 查询之前的支付方式
        String asyncPayChannel = payOrder.getAsyncChannel();
        PayChannelOrder payChannelOrder = payChannelOrderManager.findByPaymentIdAndChannel(payOrder.getId(), asyncPayChannel)
                .orElseThrow(() -> new PayFailureException("支付方式数据异常"));

        // 新的异步支付方式
        PayChannelParam payChannelParam = payParam.getPayChannels()
                .stream()
                .filter(payMode -> PayChannelEnum.ASYNC_TYPE_CODE.contains(payMode.getChannel()))
                .findFirst()
                .orElseThrow(() -> new PayFailureException("支付方式数据异常"));
        // 新传入的金额是否一致
        if (!Objects.equals(payChannelOrder.getAmount(), payChannelParam.getAmount())){
            throw new PayFailureException("传入的支付金额非法！与订单金额不一致");
        }
        return payChannelParam;
    }

    /**
     * 创建支付订单/附加表/支付通道表并保存，返回支付订单
     */
    public PayOrder createPayOrder(PayParam payParam) {
        // 构建支付订单并保存
        PayOrder payOrder = PayBuilder.buildPayOrder(payParam);
        payOrderService.save(payOrder);
        // 构建支付订单扩展表并保存
        PayOrderExtra payOrderExtra = PayBuilder.buildPayOrderExtra(payParam, payOrder.getId());
        payOrderExtraManager.save(payOrderExtra);
        return payOrder;
    }

    /**
     * 更新支付订单扩展参数
     * @param payParam 支付参数
     * @param paymentId 支付订单id
     */
    public void updatePayOrderExtra(PayParam payParam,Long paymentId){
        PayOrderExtra payOrderExtra = payOrderExtraManager.findById(paymentId)
                .orElseThrow(() -> new DataNotExistException("支付订单不存在"));
        String notifyUrl = PaymentContextLocal.get().getNoticeInfo().getNotifyUrl();
        payOrderExtra.setReqTime(payParam.getReqTime())
                .setSign(payParam.getSign())
                .setNotNotify(payParam.isNotNotify())
                .setNotifyUrl(notifyUrl)
                .setAttach(payParam.getAttach())
                .setClientIp(payParam.getClientIp());
        payOrderExtraManager.updateById(payOrderExtra);
    }

    /**
     * 校验支付状态，支付成功则返回，支付失败则抛出对应的异常
     */
    public PayOrder getOrderAndCheck(String businessNo) {
        // 根据订单查询支付记录
        PayOrder payOrder = payOrderQueryService.findByBusinessNo(businessNo).orElse(null);
        if (Objects.nonNull(payOrder)) {
            // 待支付
            if (Objects.equals(payOrder.getStatus(), PROGRESS.getCode())){
                // 如果支付超时, 触发订单同步操作, 同时抛出异常
                if (Objects.nonNull(payOrder.getExpiredTime()) && LocalDateTimeUtil.ge(LocalDateTime.now(), payOrder.getExpiredTime())) {
                    paySyncService.syncPayOrder(payOrder);
                    throw new PayFailureException("支付已超时，请重新确认支付状态");
                }
                return payOrder;
            }
            // 已经支付状态
            if (SUCCESS.getCode().equals(payOrder.getStatus())) {
                throw new PayFailureException("已经支付成功，请勿重新支付");
            }
            // 支付失败类型状态
            List<String> tradesStatus = Arrays.asList(FAIL.getCode(), CLOSE.getCode());
            if (tradesStatus.contains(payOrder.getStatus())) {
                throw new PayFailureException("支付失败或已经被关闭");
            }
            // 退款类型状态
            tradesStatus = Arrays.asList(REFUNDED.getCode(), PARTIAL_REFUND.getCode(), REFUNDING.getCode());
            if (tradesStatus.contains(payOrder.getStatus())) {
                throw new PayFailureException("退款中");
            }
            // 其他状态直接抛出兜底异常
            throw new PayFailureException("订单不是待支付状态，请重新确认订单状态");
        }
        return null;
    }

}
