package org.dromara.daxpay.channel.wechat.strategy.merchant;

import org.dromara.daxpay.channel.wechat.code.WechatPayCode;
import org.dromara.daxpay.channel.wechat.entity.config.WechatPayConfig;
import org.dromara.daxpay.channel.wechat.service.allocation.receiver.WechatPayAllocReceiverV2Service;
import org.dromara.daxpay.channel.wechat.service.allocation.receiver.WechatPayAllocReceiverV3Service;
import org.dromara.daxpay.channel.wechat.service.config.WechatPayConfigService;
import org.dromara.daxpay.core.enums.AllocReceiverTypeEnum;
import org.dromara.daxpay.core.enums.ChannelEnum;
import org.dromara.daxpay.service.strategy.AbsAllocReceiverStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

/**
 * 微信分账接收策略
 * @author xxm
 * @since 2024/4/1
 */
@Slf4j
@Service
@Scope(SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class WechatAllocReceiverStrategy extends AbsAllocReceiverStrategy {

    private final WechatPayAllocReceiverV2Service receiverV2Service;

    private final WechatPayAllocReceiverV3Service receiverV3Service;

    private final WechatPayConfigService wechatPayConfigService;

    private WechatPayConfig config;

    /**
     * 策略标识
     */
    @Override
    public String getChannel() {
        return ChannelEnum.WECHAT.getCode();
    }

    @Override
    public List<AllocReceiverTypeEnum> getSupportReceiverTypes() {
        return List.of(AllocReceiverTypeEnum.OPEN_ID, AllocReceiverTypeEnum.MERCHANT_NO);
    }

    /**
     * 校验方法
     */
    @Override
    public boolean validation(){
        List<String> list = Arrays.asList(AllocReceiverTypeEnum.OPEN_ID.getCode(), AllocReceiverTypeEnum.MERCHANT_NO.getCode());
        String receiverType = this.getAllocReceiver().getReceiverType();
        return list.contains(receiverType);
    }

    @Override
    public void doBeforeHandler(){
        this.config = wechatPayConfigService.getAndCheckConfig(false);
    }


    /**
     * 添加到支付系统中
     */
    @Override
    public void bind() {
        if (Objects.equals(config.getApiVersion(), WechatPayCode.API_V2)){
            receiverV2Service.bind(getAllocReceiver(), this.config);
        } else {
            receiverV3Service.bind(getAllocReceiver(), this.config);
        }
    }

    /**
     * 从三方支付系统中删除
     */
    @Override
    public void unbind() {
        if (Objects.equals(config.getApiVersion(), WechatPayCode.API_V2)){
            receiverV2Service.unbind(getAllocReceiver(), this.config);
        } else {
            receiverV3Service.unbind(getAllocReceiver(), this.config);
        }
    }
}
