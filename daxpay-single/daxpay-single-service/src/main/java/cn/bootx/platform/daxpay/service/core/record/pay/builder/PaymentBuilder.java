package cn.bootx.platform.daxpay.service.core.record.pay.builder;

import cn.bootx.platform.daxpay.code.PayChannelEnum;
import cn.bootx.platform.daxpay.code.PayStatusEnum;
import cn.bootx.platform.daxpay.service.common.context.AsyncPayLocal;
import cn.bootx.platform.daxpay.service.common.context.NoticeLocal;
import cn.bootx.platform.daxpay.service.common.context.PlatformLocal;
import cn.bootx.platform.daxpay.service.common.local.PaymentContextLocal;
import cn.bootx.platform.daxpay.service.core.record.pay.entity.PayOrder;
import cn.bootx.platform.daxpay.service.core.record.pay.entity.PayOrderChannel;
import cn.bootx.platform.daxpay.service.core.record.pay.entity.PayOrderExtra;
import cn.bootx.platform.daxpay.service.common.entity.OrderRefundableInfo;
import cn.bootx.platform.daxpay.param.pay.PayParam;
import cn.bootx.platform.daxpay.param.pay.PayChannelParam;
import cn.bootx.platform.daxpay.result.pay.PayResult;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 支付对象构建器
 *
 * @author xxm
 * @since 2021/2/25
 */
@UtilityClass
public class PaymentBuilder {

    /**
     * 构建payment记录
     */
    public PayOrder buildPayOrder(PayParam payParam) {
        // 订单超时时间
        LocalDateTime expiredTime = PaymentContextLocal.get()
                .getAsyncPayInfo()
                .getExpiredTime();
        // 可退款信息
        List<OrderRefundableInfo> refundableInfos = buildRefundableInfo(payParam.getPayChannels());
        // 计算总价
        int sumAmount = payParam.getPayChannels().stream()
                .map(PayChannelParam::getAmount)
                .filter(Objects::nonNull)
                .reduce(Integer::sum)
                .orElse(0);
        // 是否有异步支付方式
        Optional<String> asyncPay = payParam.getPayChannels().stream()
                .map(PayChannelParam::getChannel)
                .filter(PayChannelEnum.ASYNC_TYPE_CODE::contains)
                .findFirst();
        // 构建支付订单对象
        return new PayOrder()
                .setBusinessNo(payParam.getBusinessNo())
                .setTitle(payParam.getTitle())
                .setRefundableInfos(refundableInfos)
                .setStatus(PayStatusEnum.PROGRESS.getCode())
                .setAmount(sumAmount)
                .setExpiredTime(expiredTime)
                .setCombinationPay(payParam.getPayChannels().size() > 1)
                .setAsyncPay(asyncPay.isPresent())
                .setAsyncChannel(asyncPay.orElse(null))
                .setRefundableBalance(sumAmount);
    }

    /**
     * 构建支付订单的额外信息
     * @param payParam 支付参数
     * @param paymentId 支付订单id
     */
    public PayOrderExtra buildPayOrderExtra(PayParam payParam, Long paymentId) {
        PlatformLocal platform = PaymentContextLocal.get().getPlatform();
        NoticeLocal noticeInfo = PaymentContextLocal.get().getNoticeInfo();
        PayOrderExtra payOrderExtra = new PayOrderExtra()
                .setClientIp(payParam.getClientIp())
                .setDescription(payParam.getDescription())
                .setNotNotify(payParam.isNotNotify())
                .setNotifyUrl(noticeInfo.getNotifyUrl())
                .setSign(payParam.getSign())
                .setSignType(platform.getSignType())
                .setAttach(payParam.getAttach())
                .setApiVersion(payParam.getVersion())
                .setReqTime(payParam.getReqTime());
        payOrderExtra.setId(paymentId);
        return payOrderExtra;
    }

    /**
     * 构建订单关联通道信息
     */
    public List<PayOrderChannel> buildPayChannel(List<PayChannelParam> payChannelParams) {
        if (CollectionUtil.isEmpty(payChannelParams)) {
            return Collections.emptyList();
        }
        return payChannelParams.stream()
                .map(o-> new PayOrderChannel()
                        .setChannel(o.getChannel())
                        .setPayWay(o.getWay())
                        .setAmount(o.getAmount())
                        .setChannelExtra(o.getChannelExtra()))
                .collect(Collectors.toList());
    }

    /**
     * 构建支付订单的可退款信息列表
     */
    private List<OrderRefundableInfo> buildRefundableInfo(List<PayChannelParam> payChannelParamList) {
        if (CollectionUtil.isEmpty(payChannelParamList)) {
            return Collections.emptyList();
        }
        return payChannelParamList.stream()
                .map(o-> new OrderRefundableInfo()
                        .setChannel(o.getChannel())
                        .setAmount(o.getAmount()))
                .collect(Collectors.toList());
    }


    /**
     * 根据支付订单构建支付结果
     * @param payOrder 支付订单
     * @return PayResult 支付结果
     */
    public PayResult buildPayResultByPayOrder(PayOrder payOrder) {
        PayResult paymentResult;
        paymentResult = new PayResult();
        paymentResult.setPaymentId(payOrder.getId());
        paymentResult.setAsyncPay(payOrder.isAsyncPay());
        paymentResult.setAsyncPayChannel(payOrder.getAsyncChannel());
        paymentResult.setStatus(payOrder.getStatus());

        // 设置异步支付参数
        AsyncPayLocal asyncPayInfo = PaymentContextLocal.get().getAsyncPayInfo();;
        if (StrUtil.isNotBlank(asyncPayInfo.getPayBody())) {
            paymentResult.setPayBody(asyncPayInfo.getPayBody());
        }
        return paymentResult;
    }

    /**
     * 根据新的新传入的支付参数和支付订单构建出当前需要支付参数
     * 主要是针对其中的异步支付参数进行处理
     *
     * @param newPayParam 新传入的参数
     * @param payOrder 支付订单
     * @return PayParam 支付参数
     */
    public PayParam buildPayParam(PayParam newPayParam, PayOrder payOrder) {


        return null;
    }
}
