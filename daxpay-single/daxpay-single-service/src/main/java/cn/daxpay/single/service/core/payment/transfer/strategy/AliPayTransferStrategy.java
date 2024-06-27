package cn.daxpay.single.service.core.payment.transfer.strategy;

import cn.daxpay.single.core.code.PayChannelEnum;
import cn.bootx.platform.common.core.exception.ValidationFailedException;
import cn.daxpay.single.core.param.payment.transfer.TransferParam;
import cn.daxpay.single.service.core.channel.alipay.entity.AliPayConfig;
import cn.daxpay.single.service.core.channel.alipay.service.AliPayConfigService;
import cn.daxpay.single.service.core.channel.alipay.service.AliPayTransferService;
import cn.daxpay.single.service.func.AbsTransferStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.Arrays;

import static cn.daxpay.single.core.code.TransferPayeeTypeEnum.*;
import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

/**
 * 支付宝转账策略
 * @author xxm
 * @since 2024/3/21
 */
@Slf4j
@Service
@Scope(SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class AliPayTransferStrategy extends AbsTransferStrategy {

    private final AliPayConfigService payConfigService;

    private final AliPayTransferService aliPayTransferService;

    private AliPayConfig config;

    /**
     * 策略标识
     */
     @Override
    public String getChannel() {
        return PayChannelEnum.ALI.getCode();
    }

    @Override
    public void doValidateParam(TransferParam transferParam) {
        // 转账接收方类型校验
        String payeeType = transferParam.getPayeeType();
        if (!Arrays.asList(ALI_USER_ID.getCode(), ALI_OPEN_ID.getCode(), ALI_LOGIN_NAME.getCode()).contains(payeeType)){
            throw new ValidationFailedException("支付宝不支持该类型收款人");
        }
    }

    /**
     * 转账前操作
     */
    @Override
    public void doBeforeHandler() {
        this.config = payConfigService.getAndCheckConfig();
    }

    /**
     * 转账操作
     */
    @Override
    public void doTransferHandler() {
        aliPayTransferService.transfer(this.getTransferOrder(), this.config);
    }
}
