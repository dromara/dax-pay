package cn.bootx.platform.daxpay.service.core.order.pay.service;

import cn.bootx.platform.common.core.util.ResultConvertUtil;
import cn.bootx.platform.daxpay.code.PayChannelEnum;
import cn.bootx.platform.daxpay.param.pay.PayChannelParam;
import cn.bootx.platform.daxpay.service.core.order.pay.dao.PayChannelOrderManager;
import cn.bootx.platform.daxpay.service.core.order.pay.entity.PayChannelOrder;
import cn.bootx.platform.daxpay.service.core.order.pay.entity.PayOrder;
import cn.bootx.platform.daxpay.service.dto.order.pay.PayChannelOrderDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
    private final PayChannelOrderManager payChannelOrderManager;

    /**
     * 根据支付ID查询列表
     */
    public List<PayChannelOrderDto> findAllByPaymentId(Long paymentId){
        return ResultConvertUtil.dtoListConvert(payChannelOrderManager.findAllByPaymentId(paymentId));
    }

    /**
     * 更新支付订单的异步通道信息
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateAsyncChannelOrder(PayOrder payOrder, PayChannelParam payChannelParam){
        Optional<PayChannelOrder> payOrderChannelOpt = payChannelOrderManager.findByPaymentIdAndChannel(payOrder.getId(), PayChannelEnum.WECHAT.getCode());
        if (!payOrderChannelOpt.isPresent()){
            payChannelOrderManager.deleteByPaymentIdAndAsync(payOrder.getId());
            payChannelOrderManager.save(new PayChannelOrder()
                    .setPaymentId(payOrder.getId())
                    .setChannel(PayChannelEnum.ALI.getCode())
                    .setAmount(payChannelParam.getAmount())
                    .setRefundableBalance(payChannelParam.getAmount())
                    .setPayWay(payChannelParam.getWay())
                    .setChannelExtra(payChannelParam.getChannelExtra())
                    .setAsync(true)
            );
        } else {
            payOrderChannelOpt.get()
                    .setChannelExtra(payChannelParam.getChannelExtra())
                    .setPayWay(payChannelParam.getWay());
            payChannelOrderManager.updateById(payOrderChannelOpt.get());
        }
    }

    /**
     * 支付调起成功 更新payment中异步支付类型信息, 如果支付完成, 创建支付宝支付单
     */
    @Transactional(rollbackFor = Exception.class)
    public void updatePaySuccess(PayOrder payOrder, PayChannelParam payChannelParam) {
        // 更新支付宝异步支付类型信息
        this.updateAsyncChannelOrder(payOrder, payChannelParam);
    }

}
