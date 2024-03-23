package cn.bootx.platform.daxpay.service.core.order.pay.service;

import cn.bootx.platform.common.core.exception.DataNotExistException;
import cn.bootx.platform.common.core.util.ResultConvertUtil;
import cn.bootx.platform.daxpay.code.PayStatusEnum;
import cn.bootx.platform.daxpay.code.RefundStatusEnum;
import cn.bootx.platform.daxpay.param.pay.PayChannelParam;
import cn.bootx.platform.daxpay.service.common.context.PayLocal;
import cn.bootx.platform.daxpay.service.common.local.PaymentContextLocal;
import cn.bootx.platform.daxpay.service.core.order.pay.dao.PayChannelOrderManager;
import cn.bootx.platform.daxpay.service.core.order.pay.dao.PayOrderManager;
import cn.bootx.platform.daxpay.service.core.order.pay.entity.PayChannelOrder;
import cn.bootx.platform.daxpay.service.core.order.pay.entity.PayOrder;
import cn.bootx.platform.daxpay.service.core.order.refund.entity.RefundChannelOrder;
import cn.bootx.platform.daxpay.service.dto.order.pay.PayChannelOrderDto;
import cn.hutool.json.JSONUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * 支付订单关联通道服务
 * @author xxm
 * @since 2023/12/31
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PayChannelOrderService {
    private final PayChannelOrderManager channelOrderManager;

    private final PayOrderManager payOrderManager;

    /**
     * 根据支付ID查询列表
     */
    public List<PayChannelOrderDto> findAllByPaymentId(Long paymentId){
        return ResultConvertUtil.dtoListConvert(channelOrderManager.findAllByPaymentId(paymentId));
    }

    /**
     * 查询单条
     */
    public PayChannelOrderDto findById(Long id){
        return channelOrderManager.findById(id).map(PayChannelOrder::toDto).orElseThrow(() -> new DataNotExistException("通道支付订单未查到"));
    }

    /**
     * 切换支付订单关联的异步支付通道, 设置是否支付完成状态, 并返回通道订单
     * 同时会更新支付订单上的异步通道信息
     */
    @Transactional(rollbackFor = Exception.class)
    public PayChannelOrder switchAsyncPayChannel(PayOrder payOrder, PayChannelParam payChannelParam){
        PayLocal payInfo = PaymentContextLocal.get().getPayInfo();
        // 是否支付完成
        PayStatusEnum payStatus = payInfo.isPayComplete() ? PayStatusEnum.SUCCESS : PayStatusEnum.PROGRESS;
        Optional<PayChannelOrder> payOrderChannelOpt = channelOrderManager.findByPaymentIdAndChannel(payOrder.getId(), payChannelParam.getChannel());
        // 扩展信息处理
        Map<String, Object> channelParam = payChannelParam.getChannelParam();
        String channelParamStr = null;
        if (Objects.nonNull(channelParam)){
            channelParamStr = JSONUtil.toJsonStr(channelParam);
        }
        PayChannelOrder payChannelOrder;
        if (!payOrderChannelOpt.isPresent()){
            payChannelOrder = new PayChannelOrder();
            // 替换原有的的支付通道信息
            payChannelOrder.setPayWay(payChannelParam.getWay())
                    .setPaymentId(payOrder.getId())
                    .setAsync(true)
                    .setChannel(payChannelParam.getChannel())
                    .setPayWay(payChannelParam.getWay())
                    .setAmount(payChannelParam.getAmount())
                    .setRefundableBalance(payChannelParam.getAmount())
                    .setChannelExtra(channelParamStr)
                    .setStatus(payStatus.getCode());
            channelOrderManager.deleteByPaymentIdAndAsync(payOrder.getId());
            channelOrderManager.save(payChannelOrder);
            payInfo.getPayChannelOrders().add(payChannelOrder);
        } else {
            // 更新支付通道信息
            payChannelOrder = payOrderChannelOpt.get();
            payChannelOrder.setPayWay(payChannelParam.getWay())
                    .setChannelExtra(channelParamStr)
                    .setStatus(payStatus.getCode());
            channelOrderManager.updateById(payChannelOrder);
        }
        // 对上下文中的通道支付订单进行替换
        List<PayChannelOrder> payChannelOrders = payInfo.getPayChannelOrders();
        payChannelOrders.removeIf(o->Objects.equals(o.getChannel(),payChannelOrder.getChannel()));
        payChannelOrders.add(payChannelOrder);
        // 更新支付订单中的异步通道信息
        payOrder.setAsyncChannel(payChannelOrder.getChannel());
        payOrderManager.updateById(payOrder);
        return payChannelOrder;
    }

    /**
     * 更新异步支付通道退款余额和状态
     */
    public void updateAsyncPayRefund(PayChannelOrder payChannelOrder, RefundChannelOrder refundChannelOrder){
        // 支付通道订单状态
        if (Objects.equals(refundChannelOrder.getStatus(), RefundStatusEnum.SUCCESS.getCode())){
            // 如果可退金额为0说明已经全部退款
            PayStatusEnum status = payChannelOrder.getRefundableBalance() == 0 ? PayStatusEnum.REFUNDED : PayStatusEnum.PARTIAL_REFUND;
            payChannelOrder.setStatus(status.getCode());
            refundChannelOrder.setRefundTime(LocalDateTime.now());
        } else {
            payChannelOrder.setStatus(PayStatusEnum.REFUNDING.getCode());
        }
    }

}
