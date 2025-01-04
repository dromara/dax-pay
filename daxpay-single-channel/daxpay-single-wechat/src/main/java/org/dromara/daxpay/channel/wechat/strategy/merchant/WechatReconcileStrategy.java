package org.dromara.daxpay.channel.wechat.strategy.merchant;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.LocalDateTimeUtil;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.dromara.daxpay.channel.wechat.service.reconcile.WechatReconcileService;
import org.dromara.daxpay.core.enums.ChannelEnum;
import org.dromara.daxpay.service.bo.reconcile.ReconcileResolveResultBo;
import org.dromara.daxpay.service.enums.ReconcileFileTypeEnum;
import org.dromara.daxpay.service.strategy.AbsReconcileStrategy;
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
public class WechatReconcileStrategy extends AbsReconcileStrategy {

    private final WechatReconcileService reconcileService;

    /**
     * 策略标识
     *
     * @see ChannelEnum
     */
    @Override
    public String getChannel() {
        return ChannelEnum.WECHAT.getCode();
    }

    /**
     * 上传对账单解析并保存
     */
    @SneakyThrows
    @Override
    public ReconcileResolveResultBo uploadAndResolve(MultipartFile file, ReconcileFileTypeEnum fileType) {
//        WechatPayConfig wechatPayConfig = wechatPayConfigService.getAndCheckConfig();
//        return reconcileService.uploadBill(file.getBytes(),wechatPayConfig);
        return null;
    }

    /**
     * 下载对账单
     */
    @Override
    public ReconcileResolveResultBo downAndResolve() {
        String date = LocalDateTimeUtil.format(this.getStatement().getDate(), DatePattern.NORM_DATE_PATTERN);
        return reconcileService.downAndResolve(this.getStatement(), date);
    }

}
