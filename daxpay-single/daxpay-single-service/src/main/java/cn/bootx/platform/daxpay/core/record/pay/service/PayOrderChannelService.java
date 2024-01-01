package cn.bootx.platform.daxpay.core.record.pay.service;

import cn.bootx.platform.daxpay.code.PayChannelEnum;
import cn.bootx.platform.daxpay.core.record.pay.dao.PayOrderChannelManager;
import cn.bootx.platform.daxpay.core.record.pay.entity.PayOrder;
import cn.bootx.platform.daxpay.core.record.pay.entity.PayOrderChannel;
import cn.bootx.platform.daxpay.param.pay.PayWayParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * 支付订单关联通道服务
 * @author xxm
 * @since 2023/12/31
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PayOrderChannelService {
    private final PayOrderChannelManager payOrderChannelManager;

    /**
     * 更新支付订单的通道信息
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateChannel(PayWayParam payWayParam, PayOrder payOrder){
        Optional<PayOrderChannel> payOrderChannelOpt = payOrderChannelManager.findByPaymentIdAndChannel(payOrder.getId(), PayChannelEnum.WECHAT.getCode());
        if (!payOrderChannelOpt.isPresent()){
            payOrderChannelManager.deleteByPaymentIdAndAsync(payOrder.getId());
            payOrderChannelManager.save(new PayOrderChannel()
                    .setPaymentId(payOrder.getId())
                    .setChannel(PayChannelEnum.ALI.getCode())
                    .setAmount(payWayParam.getAmount())
                    .setPayWay(payWayParam.getWay())
                    .setChannelExtra(payWayParam.getChannelExtra())
                    .setAsync(true)
            );
        } else {
            payOrderChannelOpt.get()
                    .setChannelExtra(payWayParam.getChannelExtra())
                    .setPayWay(payWayParam.getWay());
            payOrderChannelManager.updateById(payOrderChannelOpt.get());
        }
    }
}
