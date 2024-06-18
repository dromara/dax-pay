package cn.daxpay.single.service.core.payment.transfer.strategy;

import cn.daxpay.single.core.code.PayChannelEnum;
import cn.daxpay.single.core.param.payment.transfer.TransferParam;
import cn.daxpay.single.service.core.channel.wechat.entity.WeChatPayConfig;
import cn.daxpay.single.service.core.channel.wechat.service.WeChatPayConfigService;
import cn.daxpay.single.service.core.channel.wechat.service.WeChatPayTransferService;
import cn.daxpay.single.service.func.AbsTransferStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static cn.daxpay.single.core.code.TransferPayeeTypeEnum.WX_PERSONAL;
import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

/**
 * 微信转账策略
 * @author xxm
 * @since 2024/6/14
 */
@Slf4j
@Service
@Scope(SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class WeChatTransferStrategy extends AbsTransferStrategy {

    private final WeChatPayConfigService weChatPayConfigService;

    private final WeChatPayTransferService weChatPayTransferService;

    private WeChatPayConfig weChatPayConfig;

    @Override
    public String getChannel() {
        return PayChannelEnum.WECHAT.getCode();
    }


    /**
     * 校验参数
     */
    @Override
    public void doValidateParam(TransferParam transferParam) {
        // 转账接收方类型校验
        String payeeType = transferParam.getPayeeType();
        if (!Objects.equals(WX_PERSONAL.getCode(), payeeType)){
            throw new PayFailureException("支付宝不支持该类型收款人");
        }
    }

    /**
     * 转账前操作
     */
    @Override
    public void doBeforeHandler() {
        this.weChatPayConfig = weChatPayConfigService.getConfig();
    }

    /**
     * 转账操作
     */
    @Override
    public void doTransferHandler() {
        weChatPayTransferService.transfer(this.getTransferOrder(), weChatPayConfig);
    }

}
