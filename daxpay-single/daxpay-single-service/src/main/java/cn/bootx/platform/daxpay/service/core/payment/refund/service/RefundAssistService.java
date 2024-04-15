package cn.bootx.platform.daxpay.service.core.payment.refund.service;

import cn.bootx.platform.common.core.exception.ValidationFailedException;
import cn.bootx.platform.common.core.util.CollUtil;
import cn.bootx.platform.daxpay.code.PayChannelEnum;
import cn.bootx.platform.daxpay.code.PayStatusEnum;
import cn.bootx.platform.daxpay.code.RefundStatusEnum;
import cn.bootx.platform.daxpay.exception.pay.PayFailureException;
import cn.bootx.platform.daxpay.param.pay.RefundChannelParam;
import cn.bootx.platform.daxpay.param.pay.RefundParam;
import cn.bootx.platform.daxpay.service.common.context.ApiInfoLocal;
import cn.bootx.platform.daxpay.service.common.context.NoticeLocal;
import cn.bootx.platform.daxpay.service.common.context.PlatformLocal;
import cn.bootx.platform.daxpay.service.common.context.RefundLocal;
import cn.bootx.platform.daxpay.service.common.local.PaymentContextLocal;
import cn.bootx.platform.daxpay.service.core.order.pay.entity.PayOrder;
import cn.bootx.platform.daxpay.service.core.order.pay.service.PayOrderQueryService;
import cn.bootx.platform.daxpay.service.core.order.refund.dao.RefundChannelOrderManager;
import cn.bootx.platform.daxpay.service.core.order.refund.dao.RefundOrderExtraManager;
import cn.bootx.platform.daxpay.service.core.order.refund.dao.RefundOrderManager;
import cn.bootx.platform.daxpay.service.core.order.refund.entity.RefundChannelOrder;
import cn.bootx.platform.daxpay.service.core.order.refund.entity.RefundOrder;
import cn.bootx.platform.daxpay.service.core.order.refund.entity.RefundOrderExtra;
import cn.bootx.platform.daxpay.util.OrderNoGenerateUtil;
import cn.hutool.core.util.IdUtil;
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
import java.util.stream.Collectors;

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

    private final RefundChannelOrderManager payRefundChannelOrderManager;

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
     * 根据退款参数获取支付订单
     */
    public PayOrder getPayOrder(RefundParam param){
        PayOrder payOrder = null;
        if (Objects.nonNull(param.getPaymentId())){
            payOrder = payOrderQueryService.findById(param.getPaymentId())
                    .orElseThrow(() -> new PayFailureException("未查询到支付订单"));
        }
        if (Objects.isNull(payOrder)){
            payOrder = payOrderQueryService.findByBusinessNo(param.getBusinessNo())
                    .orElseThrow(() -> new PayFailureException("未查询到支付订单"));
        }
        return payOrder;
    }

    /**
     * 检查并处理退款参数
     */
    public void checkAndDisposeParam(RefundParam param, PayOrder payOrder){
        // 全额退款和部分退款校验
        if (!param.isRefundAll()) {
            if (CollUtil.isEmpty(param.getRefundChannels())) {
                throw new ValidationFailedException("退款通道参数不能为空");
            }
        }

        // 简单退款校验
        if (payOrder.isCombinationPay()){
            throw new PayFailureException("组合支付不可以使用简单退款方式");
        }

        // 状态判断, 支付中/失败/取消等不能进行退款
        List<String> tradesStatus = Arrays.asList(
                PayStatusEnum.PROGRESS.getCode(),
                PayStatusEnum.CLOSE.getCode(),
                PayStatusEnum.REFUNDED.getCode(),
                PayStatusEnum.REFUNDING.getCode(),
                PayStatusEnum.FAIL.getCode());
        if (tradesStatus.contains(payOrder.getStatus())) {
            PayStatusEnum statusEnum = PayStatusEnum.findByCode(payOrder.getStatus());
            throw new PayFailureException("当前状态["+statusEnum.getName()+"]不允许发起退款操作");
        }

        // 过滤掉金额为空和0的退款参数
        if (!param.isRefundAll()) {
            List<RefundChannelParam> channelParams = param.getRefundChannels()
                    .stream()
                    .filter(r -> Objects.nonNull(r.getAmount()))
                    .filter(r -> r.getAmount() > 0)
                    .collect(Collectors.toList());
            param.setRefundChannels(channelParams);
        }

        // 退款号唯一校验
        if (StrUtil.isNotBlank(param.getRefundNo())
                && refundOrderManager.existsByRefundNo(param.getRefundNo())){
            throw new PayFailureException("退款单号已存在");
        }
    }

    /**
     * 预先创建退款相关订单并保存, 使用新事务, 防止丢单
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public RefundOrder createOrderAndChannel(RefundParam refundParam, PayOrder payOrder, List<RefundChannelOrder> refundChannelOrders) {
        // 此次的总退款金额
        Integer amount = refundParam.getRefundChannels()
                .stream()
                .map(RefundChannelParam::getAmount)
                .reduce(0, Integer::sum);
        int refundableBalance = payOrder.getRefundableBalance();

        // 生成退款订单
        RefundOrder refundOrder = new RefundOrder()
                .setOrderNo(payOrder.getOrderNo())
                .setStatus(RefundStatusEnum.PROGRESS.getCode())
                .setBusinessNo(payOrder.getBusinessNo())
                .setRefundNo(OrderNoGenerateUtil.refund())
                .setRefundBusinessNo(refundParam.getRefundNo())
                .setOrderAmount(payOrder.getAmount())
                .setAmount(amount)
                .setRefundableBalance(refundableBalance)
                .setTitle(payOrder.getTitle())
                .setReason(refundParam.getReason());

        // 退款参数中是否存在异步通道
        RefundChannelParam asyncChannel = refundParam.getRefundChannels()
                .stream()
                .filter(r -> PayChannelEnum.ASYNC_TYPE_CODE.contains(r.getChannel()))
                .findFirst()
                .orElse(null);
        if (Objects.nonNull(asyncChannel)){
            refundOrder.setAsyncChannel(asyncChannel.getChannel());
            refundOrder.setAsyncPay(true);
        }

        // 主键使用预先生成的ID, 如果有异步通道, 关联的退款号就是这个ID
        refundOrder.setId(IdUtil.getSnowflakeNextId());


        RefundOrderExtra refundOrderExtra = this.createRefundOrderExtra(refundParam, refundOrder.getId());
        refundChannelOrders.forEach(r->r.setRefundId(refundOrder.getId()));
        refundOrderExtraManager.save(refundOrderExtra);
        payRefundChannelOrderManager.saveAll(refundChannelOrders);
        return refundOrderManager.save(refundOrder);
    }

    /**
     * 创建退款扩展订单
     */
    public RefundOrderExtra createRefundOrderExtra(RefundParam refundParam, Long refundOrderId){
        PlatformLocal platform = PaymentContextLocal.get().getPlatformInfo();
        ApiInfoLocal apiInfo = PaymentContextLocal.get().getApiInfo();
        NoticeLocal notice = PaymentContextLocal.get().getNoticeInfo();
        String reqId = PaymentContextLocal.get()
                .getRequestInfo()
                .getReqId();
        RefundOrderExtra orderExtra = new RefundOrderExtra()
                .setClientIp(refundParam.getClientIp())
                .setReqId(reqId)
                .setReqTime(refundParam.getReqTime())
                .setAttach(refundParam.getAttach())
                .setReqSign(refundParam.getSign())
                .setReqSignType(platform.getSignType())
                .setNoticeSign(apiInfo.isNoticeSign())
                .setNotifyUrl(notice.getNotifyUrl());
        orderExtra.setId(refundOrderId);
        return orderExtra;
    }

    /**
     * 更新退款成功信息
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateOrderAndChannel(RefundOrder refundOrder, List<RefundChannelOrder> refundChannelOrders){
        RefundLocal asyncRefundInfo = PaymentContextLocal.get().getRefundInfo();
        refundOrder.setStatus(asyncRefundInfo.getStatus().getCode())
                .setGatewayOrderNo(asyncRefundInfo.getGatewayOrderNo());
        // 退款成功更新退款时间
        if (Objects.equals(refundOrder.getStatus(), SUCCESS.getCode())){
            refundOrder.setRefundTime(LocalDateTime.now());
        }
        refundOrderManager.updateById(refundOrder);
        payRefundChannelOrderManager.updateAllById(refundChannelOrders);
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
