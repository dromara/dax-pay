package cn.bootx.platform.daxpay.service.core.payment.reconcile.strategy;

import cn.bootx.platform.common.core.util.LocalDateTimeUtil;
import cn.bootx.platform.daxpay.code.PayChannelEnum;
import cn.bootx.platform.daxpay.service.core.channel.wechat.entity.WeChatPayConfig;
import cn.bootx.platform.daxpay.service.core.channel.wechat.service.WeChatPayConfigService;
import cn.bootx.platform.daxpay.service.core.channel.wechat.service.WechatPayReconcileService;
import cn.bootx.platform.daxpay.service.core.order.pay.dao.PayOrderManager;
import cn.bootx.platform.daxpay.service.func.AbsReconcileStrategy;
import cn.hutool.core.date.DatePattern;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

/**
 * 微信支付对账策略
 * @author xxm
 * @since 2024/1/17
 */
@Slf4j
@Scope(SCOPE_PROTOTYPE)
@Service
@RequiredArgsConstructor
public class WechatPayReconcileStrategy extends AbsReconcileStrategy {

    private final WechatPayReconcileService reconcileService;

    private final WeChatPayConfigService weChatPayConfigService;

    private final PayOrderManager payOrderManager;

    private WeChatPayConfig config;

    /**
     * 策略标识
     *
     * @see PayChannelEnum
     */
    @Override
    public PayChannelEnum getChannel() {
        return PayChannelEnum.WECHAT;
    }

    /**
     * 对账前处理, 主要是初始化支付SDK配置
     */
    @Override
    public void doBeforeHandler() {
        this.config = weChatPayConfigService.getConfig();
    }

    /**
     * 上传对账单解析并保存
     */
    @SneakyThrows
    @Override
    public void upload(MultipartFile file) {
        reconcileService.uploadBill(file.getBytes(),this.config);
    }

    /**
     * 下载对账单
     */
    @Override
    public void downAndSave() {
        String format = LocalDateTimeUtil.format(this.getRecordOrder().getDate(), DatePattern.PURE_DATE_PATTERN);
        reconcileService.downAndSave(format, this.config);
    }

}
