package cn.bootx.platform.daxpay.service.core.payment.pay.strategy;

import cn.bootx.platform.daxpay.code.PayChannelEnum;
import cn.bootx.platform.daxpay.exception.pay.PayAmountAbnormalException;
import cn.bootx.platform.daxpay.exception.pay.PayFailureException;
import cn.bootx.platform.daxpay.param.pay.PayChannelParam;
import cn.bootx.platform.daxpay.service.common.exception.ExceptionInfo;
import cn.bootx.platform.daxpay.service.core.channel.wechat.entity.WeChatPayConfig;
import cn.bootx.platform.daxpay.service.core.channel.wechat.service.WeChatPayCloseService;
import cn.bootx.platform.daxpay.service.core.channel.wechat.service.WeChatPayConfigService;
import cn.bootx.platform.daxpay.service.core.channel.wechat.service.WeChatPayOrderService;
import cn.bootx.platform.daxpay.service.core.channel.wechat.service.WeChatPayService;
import cn.bootx.platform.daxpay.service.func.AbsPayStrategy;
import cn.bootx.platform.daxpay.service.param.channel.wechat.WeChatPayParam;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONException;
import cn.hutool.json.JSONUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

/**
 * 微信支付
 *
 * @author xxm
 * @since 2021/4/5
 */
@Scope(SCOPE_PROTOTYPE)
@Component
@RequiredArgsConstructor
public class WeChatPayStrategy extends AbsPayStrategy {

    private final WeChatPayConfigService weChatPayConfigService;

    private final WeChatPayService weChatPayService;

    private final WeChatPayOrderService weChatPayOrderService;

    private final WeChatPayCloseService weChatPayCloseService;

    private WeChatPayConfig weChatPayConfig;

    private WeChatPayParam weChatPayParam;

    /**
     * 类型
     */
    @Override
    public PayChannelEnum getType() {
        return PayChannelEnum.WECHAT;
    }

    /**
     * 支付前操作
     */
    @Override
    public void doBeforePayHandler() {
        this.initWeChatPayConfig();
        try {
            // 微信参数验证
            String extraParamsJson = this.getPayChannelParam().getChannelExtra();
            if (StrUtil.isNotBlank(extraParamsJson)) {
                this.weChatPayParam = JSONUtil.toBean(extraParamsJson, WeChatPayParam.class);
            }
            else {
                this.weChatPayParam = new WeChatPayParam();
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

        // 检查并获取微信支付配置
        this.initWeChatPayConfig();
        weChatPayService.validation(this.getPayChannelParam(), weChatPayConfig);
    }

    /**
     * 发起支付
     */
    @Override
    public void doPayHandler() {
        weChatPayService.pay(this.getOrder(), this.weChatPayParam, this.getPayChannelParam(), this.weChatPayConfig);
    }

    /**
     * 支付调起成功
     */
    @Override
    public void doSuccessHandler() {
        weChatPayOrderService.updatePaySuccess(this.getOrder(), this.getPayChannelParam());
    }

    /**
     * 错误处理
     */
    @Override
    public void doErrorHandler(ExceptionInfo exceptionInfo) {
        this.doCloseHandler();
    }

    /**
     * 关闭本地支付记录
     */
    @Override
    public void doCloseHandler() {
        weChatPayCloseService.close(this.getOrder(), weChatPayConfig);
        weChatPayOrderService.updateClose(this.getOrder().getId());
    }

    /**
     * 初始化微信支付
     */
    private void initWeChatPayConfig() {
        this.weChatPayConfig = weChatPayConfigService.getConfig();
    }

}
