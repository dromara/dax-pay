package org.dromara.daxpay.channel.wechat.strategy.merchant;

import cn.bootx.platform.core.exception.ValidationFailedException;
import cn.bootx.platform.core.util.BigDecimalUtil;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.daxpay.channel.wechat.entity.config.WechatPayConfig;
import org.dromara.daxpay.channel.wechat.service.config.WechatPayConfigService;
import org.dromara.daxpay.channel.wechat.service.transfer.WechatTransferV3Service;
import org.dromara.daxpay.core.enums.ChannelEnum;
import org.dromara.daxpay.core.enums.TransferPayeeTypeEnum;
import org.dromara.daxpay.core.param.trade.transfer.TransferParam;
import org.dromara.daxpay.service.bo.trade.TransferResultBo;
import org.dromara.daxpay.service.strategy.AbsTransferStrategy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Objects;

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
public class WechatTransferStrategy extends AbsTransferStrategy {

    private final WechatPayConfigService wechatPayConfigService;

    private final WechatTransferV3Service transferV3Service;

    private WechatPayConfig weChatPayConfig;

    @Override
    public String getChannel() {
        return ChannelEnum.WECHAT.getCode();
    }

    /**
     * 校验参数
     */
    @Override
    public void doValidateParam(TransferParam transferParam) {
        // 转账接收方类型校验
        String payeeType = transferParam.getPayeeType();
        if (!Objects.equals(TransferPayeeTypeEnum.OPEN_ID.getCode(), payeeType)){
            throw new ValidationFailedException("微信不支持该类型收款人");
        }
        // 收款方真实姓名。支持标准RSA算法和国密算法，公钥由微信侧提供
        // 明细转账金额<0.3元时，不允许填写收款用户姓名
        // 明细转账金额 >= 2,000元时，该笔明细必须填写收款用户姓名
        String name = transferParam.getPayeeName();
        BigDecimal amount = transferParam.getAmount();
        if (StrUtil.isBlank(name)){
            if (BigDecimalUtil.isGreaterAndEqualThan(amount, BigDecimal.valueOf(2000))){
                throw new ValidationFailedException("微信转账金额 >= 2,000元时，该笔明细必须填写收款用户姓名");
            }
        } else {
            if (BigDecimalUtil.isLessThan(amount, BigDecimal.valueOf(0.3))){
                throw new ValidationFailedException("微信转账金额<0.3元时，不允许填写收款用户姓名");
            }
        }

    }

    /**
     * 转账前操作
     */
    @Override
    public void doBeforeHandler() {
        this.weChatPayConfig = wechatPayConfigService.getAndCheckConfig(true);
    }

    /**
     * 转账操作
     */
    @Override
    public TransferResultBo doTransferHandler() {
        return transferV3Service.transfer(this.getTransferOrder(), weChatPayConfig);
    }
}
