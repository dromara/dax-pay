package cn.bootx.platform.daxpay.service.core.payment.repair.strategy.pay;

import cn.bootx.platform.daxpay.code.PayChannelEnum;
import cn.bootx.platform.daxpay.code.PayStatusEnum;
import cn.bootx.platform.daxpay.param.channel.WalletPayParam;
import cn.bootx.platform.daxpay.service.core.channel.wallet.entity.Wallet;
import cn.bootx.platform.daxpay.service.core.channel.wallet.service.WalletPayService;
import cn.bootx.platform.daxpay.service.core.channel.wallet.service.WalletQueryService;
import cn.bootx.platform.daxpay.service.func.AbsPayRepairStrategy;
import cn.hutool.json.JSONUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

/**
 *
 * @author xxm
 * @since 2023/12/29
 */
@Slf4j
@Scope(SCOPE_PROTOTYPE)
@Service
@RequiredArgsConstructor
public class WalletPayRepairStrategy extends AbsPayRepairStrategy {

    private final WalletPayService walletPayService;

    private final WalletQueryService walletQueryService;

    private final WalletRecordService walletRecordService;

    private Wallet wallet;


    /**
     * 策略标识
     */
    @Override
    public PayChannelEnum getChannel() {
        return PayChannelEnum.WALLET;
    }

    /**
     * 修复前处理
     */
    @Override
    public void doBeforeHandler() {
        // 从通道扩展参数中取出钱包参数
        String channelExtra = this.getChannelOrder().getChannelExtra();
        WalletPayParam walletPayParam = JSONUtil.toBean(channelExtra, WalletPayParam.class);
        this.wallet = walletQueryService.getWallet(walletPayParam);
    }

    /**
     * 关闭支付, 将订单的金额退还到钱包中
     */
    @Override
    public void doCloseLocalHandler() {
        walletPayService.close(this.getChannelOrder(),this.wallet);
        walletRecordService.payClose(this.getChannelOrder(), this.getOrder().getTitle(), this.wallet);
        this.getChannelOrder().setStatus(PayStatusEnum.CLOSE.getCode());
    }
}
