package org.dromara.daxpay.channel.union.strategy;

import cn.bootx.platform.core.exception.ValidationFailedException;
import cn.bootx.platform.core.util.JsonUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.daxpay.channel.union.entity.config.UnionPayConfig;
import org.dromara.daxpay.channel.union.param.pay.UnionPayParam;
import org.dromara.daxpay.channel.union.sdk.api.UnionPayKit;
import org.dromara.daxpay.channel.union.service.config.UnionPayConfigService;
import org.dromara.daxpay.channel.union.service.pay.UnionPayService;
import org.dromara.daxpay.core.enums.ChannelEnum;
import org.dromara.daxpay.service.bo.trade.PayResultBo;
import org.dromara.daxpay.service.strategy.AbsPayStrategy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

/**
 * 云闪付支付
 *
 * @author xxm
 * @since 2022/3/8
 */
@Slf4j
@Scope(SCOPE_PROTOTYPE)
@Service
@RequiredArgsConstructor
public class UnionPayStrategy extends AbsPayStrategy {

    private final UnionPayService unionPayService;

    private final UnionPayConfigService unionPayConfigService;

    private UnionPayParam unionPayParam;

    private UnionPayConfig unionPayConfig;

    @Override
    public String getChannel() {
        return ChannelEnum.UNION_PAY.getCode();
    }
    /**
     * 支付前操作
     */
    @Override
    public void doBeforePayHandler() {
        try {
            // 云闪付参数验证
            var channelParam = this.getPayParam().getExtraParam();
            if (StrUtil.isNotBlank(channelParam)) {
                this.unionPayParam = JsonUtil.toBean(channelParam, UnionPayParam.class);
            }
            else {
                this.unionPayParam = new UnionPayParam();
            }
        }
        catch (JSONException e) {
            throw new ValidationFailedException("支付参数错误");
        }
        // 检查并获取云闪付支付配置
        this.unionPayConfig = unionPayConfigService.getAndCheckConfig();
        unionPayService.validation(this.getPayParam(), unionPayConfig);
    }

    /**
     * 发起支付操作
     */
    @Override
    public PayResultBo doPayHandler() {
        UnionPayKit unionPayKit = unionPayConfigService.initPayKit(unionPayConfig);
        return unionPayService.pay(this.getOrder(), this.unionPayParam, unionPayKit);
    }
}

