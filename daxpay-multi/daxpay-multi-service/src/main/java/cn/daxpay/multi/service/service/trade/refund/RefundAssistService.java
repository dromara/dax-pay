package cn.daxpay.multi.service.service.trade.refund;

import cn.bootx.platform.core.exception.ValidationFailedException;
import cn.daxpay.multi.core.enums.PayRefundStatusEnum;
import cn.daxpay.multi.core.enums.PayStatusEnum;
import cn.daxpay.multi.core.enums.RefundStatusEnum;
import cn.daxpay.multi.core.exception.TradeStatusErrorException;
import cn.daxpay.multi.core.param.trade.refund.RefundParam;
import cn.daxpay.multi.core.result.trade.RefundResult;
import cn.daxpay.multi.core.util.PayUtil;
import cn.daxpay.multi.core.util.TradeNoGenerateUtil;
import cn.daxpay.multi.service.common.context.ErrorInfoLocal;
import cn.daxpay.multi.service.common.context.RefundLocal;
import cn.daxpay.multi.service.common.local.PaymentContextLocal;
import cn.daxpay.multi.service.dao.order.refund.RefundOrderManager;
import cn.daxpay.multi.service.entity.order.pay.PayOrder;
import cn.daxpay.multi.service.entity.order.refund.RefundOrder;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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
public class RefundAssistService {
    private final RefundOrderManager refundOrderManager;

    /**
     * 检查并处理退款参数
     */
    public void checkAndParam(RefundParam param, PayOrder payOrder){
        // 非支付完成的不能进行退款
        if (!Objects.equals(RefundStatusEnum.SUCCESS.getCode(), payOrder.getStatus())) {
            PayStatusEnum statusEnum = PayStatusEnum.findByCode(payOrder.getStatus());
            throw new TradeStatusErrorException("当前支付单订状态["+statusEnum.getName()+"]不允许发起退款操作");
        }
        // 退款中和退款完成不能退款
        List<String> tradesStatus = Arrays.asList(
                PayRefundStatusEnum.REFUNDED.getCode(),
                PayRefundStatusEnum.REFUNDING.getCode());
        if (tradesStatus.contains(payOrder.getRefundStatus())){
            var statusEnum = PayRefundStatusEnum.findByCode(payOrder.getRefundStatus());
            throw new TradeStatusErrorException("当前支付单退款状态["+statusEnum.getName()+"]不允许发起退款操作");
        }
        // 退款号唯一校验
        if (StrUtil.isNotBlank(param.getBizRefundNo()) && refundOrderManager.existsByRefundNo(param.getBizRefundNo())){
            throw new ValidationFailedException("退款单号已存在");
        }

        // 金额判断
        if (PayUtil.isGreaterThan(param.getAmount(),payOrder.getRefundableBalance())){
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
        if (Objects.equals(refundOrder.getStatus(), RefundStatusEnum.SUCCESS.getCode())){
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
        return new RefundResult()
                .setStatus(refundOrder.getStatus())
                .setRefundNo(refundOrder.getRefundNo())
                .setBizRefundNo(refundOrder.getBizRefundNo());
    }
}
