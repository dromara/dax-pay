package cn.daxpay.single.service.core.payment.refund.service;

import cn.daxpay.single.core.code.PayOrderRefundStatusEnum;
import cn.daxpay.single.core.code.PaySignTypeEnum;
import cn.daxpay.single.core.code.PayStatusEnum;
import cn.daxpay.single.core.code.RefundStatusEnum;
import cn.bootx.platform.common.core.exception.ValidationFailedException;
import cn.daxpay.single.core.exception.TradeStatusErrorException;
import cn.daxpay.single.core.param.payment.refund.RefundParam;
import cn.daxpay.single.core.result.pay.RefundResult;
import cn.daxpay.single.core.util.TradeNoGenerateUtil;
import cn.daxpay.single.core.util.PaySignUtil;
import cn.daxpay.single.service.common.context.ErrorInfoLocal;
import cn.daxpay.single.service.common.context.PlatformLocal;
import cn.daxpay.single.service.common.context.RefundLocal;
import cn.daxpay.single.service.common.local.PaymentContextLocal;
import cn.daxpay.single.service.core.order.pay.entity.PayOrder;
import cn.daxpay.single.service.core.order.refund.dao.RefundOrderManager;
import cn.daxpay.single.service.core.order.refund.entity.RefundOrder;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static cn.daxpay.single.core.code.RefundStatusEnum.SUCCESS;

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

    /**
     * 检查并处理退款参数
     */
    public void checkAndParam(RefundParam param, PayOrder payOrder){
        // 非支付完成的不能进行退款
        if (!Objects.equals(SUCCESS.getCode(), payOrder.getStatus())) {
            PayStatusEnum statusEnum = PayStatusEnum.findByCode(payOrder.getStatus());
            throw new TradeStatusErrorException("当前支付单订状态["+statusEnum.getName()+"]不允许发起退款操作");
        }
        // 退款中和退款完成不能退款
        List<String> tradesStatus = Arrays.asList(
                PayOrderRefundStatusEnum.REFUNDED.getCode(),
                PayOrderRefundStatusEnum.REFUNDING.getCode());
        if (tradesStatus.contains(payOrder.getRefundStatus())){
            val statusEnum = PayOrderRefundStatusEnum.findByCode(payOrder.getRefundStatus());
            throw new TradeStatusErrorException("当前支付单退款状态["+statusEnum.getName()+"]不允许发起退款操作");
        }
        // 退款号唯一校验
        if (StrUtil.isNotBlank(param.getBizRefundNo()) && refundOrderManager.existsByRefundNo(param.getBizRefundNo())){
            throw new ValidationFailedException("退款单号已存在");
        }

        // 金额判断
        if (param.getAmount() > payOrder.getRefundableBalance()){
            throw new ValidationFailedException("退款金额不能大于支付金额");
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
                .setRefundNo(TradeNoGenerateUtil.refund())
                .setBizRefundNo(refundParam.getBizRefundNo())
                .setChannel(payOrder.getChannel())
                .setStatus(RefundStatusEnum.PROGRESS.getCode())
                .setOrderAmount(payOrder.getAmount())
                .setAmount(refundParam.getAmount())
                .setTitle(payOrder.getTitle())
                .setReason(refundParam.getReason())
                .setClientIp(refundParam.getClientIp())
                .setReqTime(refundParam.getReqTime())
                .setAttach(refundParam.getAttach())
                .setNotifyUrl(refundParam.getNotifyUrl());;
        refundOrderManager.save(refundOrder);
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
            // 读取网关返回的退款时间和完成时间
            refundOrder.setFinishTime(refundInfo.getFinishTime());
        }
        refundOrderManager.updateById(refundOrder);
    }

    /**
     * 更新退款错误信息
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void updateOrderByError(RefundOrder refundOrder){
        RefundLocal refundInfo = PaymentContextLocal.get().getRefundInfo();
        ErrorInfoLocal errorInfo = PaymentContextLocal.get().getErrorInfo();
        refundOrder.setErrorCode(String.valueOf(errorInfo.getErrorCode()));
        refundOrder.setErrorMsg(errorInfo.getErrorMsg());
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
        refundResult.setMsg(refundOrder.getErrorMsg());

        // 进行签名
        String signType = platformInfo.getSignType();
        if (Objects.equals(PaySignTypeEnum.HMAC_SHA256.getCode(), signType)){
            refundResult.setSign(PaySignUtil.hmacSha256Sign(refundOrder, platformInfo.getSignSecret()));
        } else if (Objects.equals(PaySignTypeEnum.MD5.getCode(), signType)){
            refundResult.setSign(PaySignUtil.md5Sign(refundOrder, platformInfo.getSignSecret()));
        } else {
            throw new ValidationFailedException("未获取到签名方式，请检查");
        }
        return refundResult;
    }

}
