package org.dromara.daxpay.channel.alipay.strategy.isv;

import org.dromara.daxpay.channel.alipay.entity.AliPayConfig;
import org.dromara.daxpay.channel.alipay.service.config.AlipayConfigService;
import org.dromara.daxpay.channel.alipay.service.reconcile.AlipayReconcileService;
import org.dromara.daxpay.core.enums.ChannelEnum;
import org.dromara.daxpay.service.bo.reconcile.ReconcileResolveResultBo;
import org.dromara.daxpay.service.enums.ReconcileFileTypeEnum;
import org.dromara.daxpay.service.strategy.AbsReconcileStrategy;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.LocalDateTimeUtil;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

/**
 * 支付宝对账策略
 * @author xxm
 * @since 2024/1/17
 */
@Slf4j
@Service
@Scope(SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class AlipayIsvReconcileStrategy extends AbsReconcileStrategy {

    private final AlipayReconcileService reconcileService;
    private final AlipayConfigService aliPayConfigService;

    /**
     * 策略标识, 可以自行进行扩展
     *
     * @see ChannelEnum
     */
    @Override
    public String getChannel() {
        return ChannelEnum.ALIPAY_ISV.getCode();
    }

    /**
     * 上传对账单解析并保存
     *
     */
    @SneakyThrows
    @Override
    public ReconcileResolveResultBo uploadAndResolve(MultipartFile file, ReconcileFileTypeEnum fileType) {
        return reconcileService.upload(this.getStatement(), file.getBytes());
    }

    /**
     * 下载对账单到本地进行保存
     */
    @Override
    public ReconcileResolveResultBo downAndResolve() {
        AliPayConfig aliPayConfig = aliPayConfigService.getAliPayConfig(true);
        String date = LocalDateTimeUtil.format(this.getStatement().getDate(), DatePattern.NORM_DATE_PATTERN);
        return reconcileService.downAndResolve(date, this.getStatement(),aliPayConfig);
    }
}
