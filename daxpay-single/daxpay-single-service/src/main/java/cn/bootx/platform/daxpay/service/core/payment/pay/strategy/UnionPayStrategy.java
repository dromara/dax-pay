package cn.bootx.platform.daxpay.service.core.payment.pay.strategy;

import cn.bootx.platform.daxpay.code.PayChannelEnum;
import cn.bootx.platform.daxpay.exception.pay.PayAmountAbnormalException;
import cn.bootx.platform.daxpay.exception.pay.PayFailureException;
import cn.bootx.platform.daxpay.param.channel.UnionPayParam;
import cn.bootx.platform.daxpay.param.pay.PayChannelParam;
import cn.bootx.platform.daxpay.service.common.context.PayLocal;
import cn.bootx.platform.daxpay.service.common.local.PaymentContextLocal;
import cn.bootx.platform.daxpay.service.core.channel.union.entity.UnionPayConfig;
import cn.bootx.platform.daxpay.service.core.channel.union.service.UnionPayConfigService;
import cn.bootx.platform.daxpay.service.core.channel.union.service.UnionPayRecordService;
import cn.bootx.platform.daxpay.service.core.channel.union.service.UnionPayService;
import cn.bootx.platform.daxpay.service.core.order.pay.entity.PayChannelOrder;
import cn.bootx.platform.daxpay.service.core.order.pay.service.PayChannelOrderService;
import cn.bootx.platform.daxpay.service.func.AbsPayStrategy;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.Map;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

/**
 * 云闪付
 *
 * @author xxm
 * @since 2022/3/8
 */
@Slf4j
@Scope(SCOPE_PROTOTYPE)
@Service
@RequiredArgsConstructor
public class UnionPayStrategy extends AbsPayStrategy {

    private final PayChannelOrderService channelOrderService;

    private final UnionPayService unionPayService;

    private final UnionPayConfigService unionPayConfigService;

    private final UnionPayRecordService unionPayRecordService;

    private UnionPayParam unionPayParam;

    private UnionPayConfig unionPayConfig;

    @Override
    public PayChannelEnum getChannel() {
        return PayChannelEnum.UNION_PAY;
    }
    /**
     * 支付前操作
     */
    @Override
    public void doBeforePayHandler() {
        try {
            // 云闪付参数验证
            Map<String, Object> channelParam = this.getPayChannelParam().getChannelParam();
            if (CollUtil.isNotEmpty(channelParam)) {
                this.unionPayParam = BeanUtil.toBean(channelParam, UnionPayParam.class);
            }
            else {
                this.unionPayParam = new UnionPayParam();
            }
        }
        catch (JSONException e) {
            throw new PayFailureException("支付参数错误");
        }
        // 检查金额
        PayChannelParam payMode = this.getPayChannelParam();
        if (payMode.getAmount() <= 0) {
            throw new PayAmountAbnormalException();
        }
        // 检查并获取云闪付支付配置
        this.unionPayConfig = unionPayConfigService.getAndCheckConfig();
        unionPayService.validation(this.getPayChannelParam(), unionPayConfig);
    }

    /**
     * 发起支付操作
     */
    @Override
    public void doPayHandler() {
        unionPayService.pay(this.getOrder(), this.getPayChannelParam(), this.unionPayParam, this.unionPayConfig);
    }

    /**
     * 不使用默认的生成通道支付单方法, 异步支付通道的支付订单自己管理
     * channelOrderService.switchAsyncPayChannel 进行切换
     */
    @Override
    public void generateChannelOrder() {
    }

    /**
     * 支付调起成功, 保存或更新通道支付订单
     */
    @Override
    public void doSuccessHandler() {
        PayLocal asyncPayInfo = PaymentContextLocal.get().getPayInfo();
        PayChannelOrder payChannelOrder = channelOrderService.switchAsyncPayChannel(this.getOrder(), this.getPayChannelParam());
        // 支付完成, 保存记录
        if (asyncPayInfo.isPayComplete()) {
            unionPayRecordService.pay(this.getOrder(), payChannelOrder);
        }
    }


}
