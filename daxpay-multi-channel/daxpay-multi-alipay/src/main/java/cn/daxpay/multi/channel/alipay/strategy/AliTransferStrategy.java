package cn.daxpay.multi.channel.alipay.strategy;

import cn.bootx.platform.core.exception.ValidationFailedException;
import cn.daxpay.multi.channel.alipay.entity.config.AliPayConfig;
import cn.daxpay.multi.channel.alipay.service.config.AliPayConfigService;
import cn.daxpay.multi.channel.alipay.service.transfer.AliTransferService;
import cn.daxpay.multi.core.enums.ChannelEnum;
import cn.daxpay.multi.service.bo.trade.TransferResultBo;
import cn.daxpay.multi.service.param.order.transfer.TransferParam;
import cn.daxpay.multi.service.strategy.AbsTransferStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.Arrays;

import static cn.daxpay.multi.core.enums.TransferPayeeTypeEnum.*;
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
public class AliTransferStrategy extends AbsTransferStrategy {

    private final AliPayConfigService payConfigService;

    private final AliTransferService aliTransferService;

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
        if (!Arrays.asList(ALI_USER_ID.getCode(), ALI_OPEN_ID.getCode(), ALI_LOGIN_NAME.getCode()).contains(payeeType)){
            throw new ValidationFailedException("支付宝不支持该类型收款人");
        }
    }

    /**
     * 转账前操作
     */
    @Override
    public void doBeforeHandler() {
        this.config = payConfigService.getAliPayConfig();
    }

    /**
     * 转账操作
     */
    @Override
    public TransferResultBo doTransferHandler() {
        return aliTransferService.transfer(this.getTransferOrder(), this.config);
    }
}
