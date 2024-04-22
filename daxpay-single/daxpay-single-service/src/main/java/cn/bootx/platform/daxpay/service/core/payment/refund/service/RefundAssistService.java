package cn.bootx.platform.daxpay.service.core.payment.refund.service;

import cn.bootx.platform.common.core.exception.DataNotExistException;
import cn.bootx.platform.daxpay.code.PayStatusEnum;
import cn.bootx.platform.daxpay.code.RefundStatusEnum;
import cn.bootx.platform.daxpay.exception.pay.PayFailureException;
import cn.bootx.platform.daxpay.param.payment.refund.RefundParam;
import cn.bootx.platform.daxpay.service.common.context.ApiInfoLocal;
import cn.bootx.platform.daxpay.service.common.context.NoticeLocal;
import cn.bootx.platform.daxpay.service.common.context.PlatformLocal;
import cn.bootx.platform.daxpay.service.common.context.RefundLocal;
import cn.bootx.platform.daxpay.service.common.local.PaymentContextLocal;
import cn.bootx.platform.daxpay.service.core.order.pay.entity.PayOrder;
import cn.bootx.platform.daxpay.service.core.order.pay.service.PayOrderQueryService;
import cn.bootx.platform.daxpay.service.core.order.refund.dao.RefundOrderExtraManager;
import cn.bootx.platform.daxpay.service.core.order.refund.dao.RefundOrderManager;
import cn.bootx.platform.daxpay.service.core.order.refund.entity.RefundOrder;
import cn.bootx.platform.daxpay.service.core.order.refund.entity.RefundOrderExtra;
import cn.bootx.platform.daxpay.util.OrderNoGenerateUtil;
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

import static cn.bootx.platform.daxpay.code.RefundStatusEnum.SUCCESS;

/**
 * 支付退款支撑服务
 * @author xxm
 * @since 2023/12/26
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RefundAssistService {
    private final PayOrderQueryService payOrderQueryService;

    private final RefundOrderManager refundOrderManager;

    private final RefundOrderExtraManager refundOrderExtraManager;

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
        ApiInfoLocal apiInfo = PaymentContextLocal.get().getApiInfo();
        PlatformLocal platform = PaymentContextLocal.get().getPlatformInfo();
        // 异步回调为开启状态
        if (!param.isNotNotify() && apiInfo.isNotice()){
            // 首先读取请求参数
            noticeInfo.setNotifyUrl(param.getNotifyUrl());
            // 读取接口配置
            if (StrUtil.isBlank(noticeInfo.getNotifyUrl())){
                noticeInfo.setNotifyUrl(apiInfo.getNoticeUrl());
            }
            // 读取平台配置
            if (StrUtil.isBlank(noticeInfo.getNotifyUrl())){
                noticeInfo.setNotifyUrl(platform.getNotifyUrl());
            }
        }
    }

    /**
     * 根据退款参数获取退款订单
     */
    public RefundOrder getRefundOrder(RefundParam param){
        // 查询退款单
        RefundOrder refundOrder = null;
        if (Objects.nonNull(param.getrefun())){
            refundOrder = refundOrderManager.findById(param.getRefundNo())
                    .orElseThrow(() -> new DataNotExistException("未查询到支付订单"));
        }
        if (Objects.isNull(refundOrder)){
            refundOrder = refundOrderManager.findByRefundNo(param.getRefundNo())
                    .orElseThrow(() -> new DataNotExistException("未查询到支付订单"));
        }
        return refundOrder;
    }

    /**
     * 根据退款参数获取支付订单
     */
    public PayOrder getPayOrder(RefundParam param){
        PayOrder payOrder = null;
        if (Objects.nonNull(param.getOrderNo())){
            payOrder = payOrderQueryService.findByOrderNo(param.getOrderNo())
                    .orElseThrow(() -> new PayFailureException("未查询到支付订单"));
        }
        if (Objects.isNull(payOrder)){
            payOrder = payOrderQueryService.findByBizOrderNo(param.getBizOrderNo())
                    .orElseThrow(() -> new PayFailureException("未查询到支付订单"));
        }
        return payOrder;
    }

    /**
     * 检查并处理退款参数
     */
    public void checkAndParam(RefundParam param, PayOrder payOrder){

        // 状态判断, 支付中/失败/取消等不能进行退款
        List<String> tradesStatus = Arrays.asList(
                PayStatusEnum.PROGRESS.getCode(),
                PayStatusEnum.CLOSE.getCode(),
                PayStatusEnum.REFUNDED.getCode(),
                PayStatusEnum.REFUNDING.getCode(),
                PayStatusEnum.FAIL.getCode());
        if (tradesStatus.contains(payOrder.getStatus())) {
            PayStatusEnum statusEnum = PayStatusEnum.findByCode(payOrder.getStatus());
            throw new PayFailureException("当前订单状态["+statusEnum.getName()+"]不允许发起退款操作");
        }

        // 退款号唯一校验
        if (StrUtil.isNotBlank(param.getBizRefundNo()) && refundOrderManager.existsByRefundNo(param.getBizRefundNo())){
            throw new PayFailureException("退款单号已存在");
        }
    }

    /**
     * 预先创建退款相关订单并保存, 使用新事务, 防止丢单
     */
    @Transactional(rollbackFor = Exception.class)
    public RefundOrder createOrder(RefundParam refundParam, PayOrder payOrder) {
        // 生成退款订单
        RefundOrder refundOrder = new RefundOrder()
                .setOrderId(payOrder.getId())
                .setOrderNo(payOrder.getOrderNo())
                .setBizOrderNo(payOrder.getBizOrderNo())
                .setRefundNo(OrderNoGenerateUtil.refund())
                .setBizRefundNo(refundParam.getBizRefundNo())
                .setStatus(RefundStatusEnum.PROGRESS.getCode())
                .setOrderAmount(payOrder.getAmount())
                .setAmount(refundParam.getAmount())
                .setTitle(payOrder.getTitle())
                .setReason(refundParam.getReason());
        refundOrderManager.save(refundOrder);
        // 生成退款扩展订单
        NoticeLocal notice = PaymentContextLocal.get().getNoticeInfo();
        RefundOrderExtra orderExtra = new RefundOrderExtra()
                .setClientIp(refundParam.getClientIp())
                .setReqTime(refundParam.getReqTime())
                .setAttach(refundParam.getAttach())
                .setNotifyUrl(notice.getNotifyUrl());
        orderExtra.setId(refundOrder.getId());

        refundOrderExtraManager.save(orderExtra);
        return refundOrder;
    }

    /**
     * 更新退款成功信息
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateOrder(RefundOrder refundOrder){
        RefundLocal asyncRefundInfo = PaymentContextLocal.get().getRefundInfo();
        refundOrder.setStatus(asyncRefundInfo.getStatus().getCode())
                .setRefundNo(asyncRefundInfo.getOutRefundNo());
        // 退款成功更新退款时间
        if (Objects.equals(refundOrder.getStatus(), SUCCESS.getCode())){
            refundOrder.setRefundTime(LocalDateTime.now());
        }
        refundOrderManager.updateById(refundOrder);
    }

    /**
     * 更新退款错误信息
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void updateOrderByError(RefundOrder refundOrder){
        RefundLocal refundInfo = PaymentContextLocal.get().getRefundInfo();
        refundOrder.setErrorCode(refundInfo.getErrorCode());
        refundOrder.setErrorMsg(refundInfo.getErrorMsg());
        // 退款失败不保存剩余可退余额, 否则数据看起开会产生困惑
        refundOrder.setRefundableBalance(null);
        refundOrderManager.updateById(refundOrder);
    }

}
