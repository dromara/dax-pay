package org.dromara.daxpay.channel.alipay.strategy;

import cn.bootx.platform.core.exception.ValidationFailedException;
import org.dromara.daxpay.channel.alipay.entity.config.AliPayConfig;
import org.dromara.daxpay.channel.alipay.service.config.AliPayConfigService;
import org.dromara.daxpay.channel.alipay.service.transfer.AliPayTransferService;
import org.dromara.daxpay.core.enums.ChannelEnum;
import org.dromara.daxpay.core.param.trade.transfer.TransferParam;
import org.dromara.daxpay.service.bo.trade.TransferResultBo;
import org.dromara.daxpay.service.strategy.AbsTransferStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.dromara.daxpay.core.enums.TransferPayeeTypeEnum.*;
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
        return ChannelEnum.ALI.getCode();
    }

    @Override
    public void doValidateParam(TransferParam transferParam) {
        // 转账接收方类型校验
        String payeeType = transferParam.getPayeeType();
        if (!List.of(ALI_USER_ID.getCode(), ALI_OPEN_ID.getCode(), ALI_LOGIN_NAME.getCode()).contains(payeeType)){
            throw new ValidationFailedException("支付宝不支持该类型收款人");
        }
    }

    /**
     * 转账操作
     */
    @Override
    public TransferResultBo doTransferHandler() {
        return aliPayTransferService.transfer(this.getTransferOrder());
    }
}
