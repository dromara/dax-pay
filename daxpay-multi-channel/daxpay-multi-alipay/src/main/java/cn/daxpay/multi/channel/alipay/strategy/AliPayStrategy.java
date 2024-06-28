package cn.daxpay.multi.channel.alipay.strategy;

import cn.bootx.platform.core.exception.ValidationFailedException;
import cn.daxpay.multi.channel.alipay.param.pay.AliPayParam;
import cn.daxpay.multi.channel.alipay.service.pay.AliPayService;
import cn.daxpay.multi.core.enums.ChannelEnum;
import cn.daxpay.multi.service.strategy.AbsPayStrategy;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONException;
import cn.hutool.json.JSONUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

/**
 * 支付宝支付
 * 注意: channelOrder对象需要单独处理, 直接获取会空指针
 * @author xxm
 * @since 2021/2/27
 */
@Scope(SCOPE_PROTOTYPE)
@Component
@RequiredArgsConstructor
public class AliPayStrategy extends AbsPayStrategy {

    private final AliPayService aliPayService;

    private AliPayParam aliPayParam;

     @Override
    public String getChannel() {
        return ChannelEnum.ALI.getCode();
    }

    /**
     * 支付前操作
     */
    @Override
    public void doBeforePayHandler() {
        try {
            // 支付宝参数验证
            String channelParam = this.getPayParam().getExtraParam();
            if (StrUtil.isNotBlank(channelParam)) {
                this.aliPayParam = JSONUtil.toBean(channelParam, AliPayParam.class);
            }
            else {
                this.aliPayParam = new AliPayParam();
            }
        }
        catch (JSONException e) {
            throw new ValidationFailedException("支付参数错误");
        }
        // 支付宝相关校验
        aliPayService.validation(this.getPayParam());
    }

    /**
     * 发起支付操作
     */
    @Override
    public void doPayHandler() {
        aliPayService.pay(this.getOrder(), this.aliPayParam);
    }

}
