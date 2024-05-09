package cn.daxpay.single.service.core.payment.refund.service;

import cn.daxpay.single.code.PaySignTypeEnum;
import cn.daxpay.single.code.PayStatusEnum;
import cn.daxpay.single.code.RefundStatusEnum;
import cn.daxpay.single.exception.pay.PayFailureException;
import cn.daxpay.single.param.payment.refund.RefundParam;
import cn.daxpay.single.result.pay.RefundResult;
import cn.daxpay.single.service.common.context.ApiInfoLocal;
import cn.daxpay.single.service.common.context.NoticeLocal;
import cn.daxpay.single.service.common.context.PlatformLocal;
import cn.daxpay.single.service.common.context.RefundLocal;
import cn.daxpay.single.service.common.local.PaymentContextLocal;
import cn.daxpay.single.service.core.order.pay.entity.PayOrder;
import cn.daxpay.single.service.core.order.refund.dao.RefundOrderExtraManager;
import cn.daxpay.single.service.core.order.refund.dao.RefundOrderManager;
import cn.daxpay.single.service.core.order.refund.entity.RefundOrder;
import cn.daxpay.single.service.core.order.refund.entity.RefundOrderExtra;
import cn.daxpay.single.util.OrderNoGenerateUtil;
import cn.daxpay.single.util.PaySignUtil;
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

import static cn.daxpay.single.code.RefundStatusEnum.SUCCESS;

/**
 * 支付退款支撑服务
 * @author xxm
 * @since 2023/12/26
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RefundAssistService {
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
     * 初始化退款通知相关上下文
     */
    private void initNotice(RefundParam param) {
        NoticeLocal noticeInfo = PaymentContextLocal.get().getNoticeInfo();
        ApiInfoLocal apiInfo = PaymentContextLocal.get().getApiInfo();
        PlatformLocal platform = PaymentContextLocal.get().getPlatformInfo();
        // 异步回调为开启状态
        if (!Objects.equals(param.getNotNotify(), false) && apiInfo.isNotice()){
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
                .setOutOrderNo(payOrder.getOutOrderNo())
                .setRefundNo(OrderNoGenerateUtil.refund())
                .setBizRefundNo(refundParam.getBizRefundNo())
                .setChannel(payOrder.getChannel())
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
        RefundLocal refundInfo = PaymentContextLocal.get().getRefundInfo();
        refundOrder.setStatus(refundInfo.getStatus().getCode())
                .setOutRefundNo(refundInfo.getOutRefundNo());
        // 退款成功更新退款时间
        if (Objects.equals(refundOrder.getStatus(), SUCCESS.getCode())){
            // TODO 读取网关返回的退款时间和完成时间
            refundOrder.setFinishTime(LocalDateTime.now());
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
        refundOrder.setStatus(refundInfo.getStatus().getCode());
        refundOrderManager.updateById(refundOrder);
    }

    /**
     * 根据退款订单信息构建出返回结果
     */
    public RefundResult buildResult(RefundOrder refundOrder){
        PlatformLocal platformInfo = PaymentContextLocal.get()
                .getPlatformInfo();

        RefundResult refundResult = new RefundResult();
        refundResult.setRefundNo(refundOrder.getRefundNo())
                .setBizRefundNo(refundOrder.getBizRefundNo());
        refundResult.setCode(refundOrder.getStatus())
                .setMsg(refundOrder.getErrorMsg())
                .setCode(refundOrder.getErrorCode());

        // 进行签名
        String signType = platformInfo.getSignType();
        if (Objects.equals(PaySignTypeEnum.HMAC_SHA256.getCode(), signType)){
            refundResult.setSign(PaySignUtil.hmacSha256Sign(refundOrder, platformInfo.getSignSecret()));
        } else if (Objects.equals(PaySignTypeEnum.MD5.getCode(), signType)){
            refundResult.setSign(PaySignUtil.md5Sign(refundOrder, platformInfo.getSignSecret()));
        } else {
            throw new PayFailureException("未获取到签名方式，请检查");
        }
        return refundResult;
    }

}
