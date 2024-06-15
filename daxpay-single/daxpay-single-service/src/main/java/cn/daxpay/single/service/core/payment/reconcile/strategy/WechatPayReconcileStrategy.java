package cn.daxpay.single.service.core.payment.reconcile.strategy;

import cn.bootx.platform.common.core.util.LocalDateTimeUtil;
import cn.daxpay.single.code.PayChannelEnum;
import cn.daxpay.single.service.code.ReconcileFileTypeEnum;
import cn.daxpay.single.service.core.channel.wechat.entity.WeChatPayConfig;
import cn.daxpay.single.service.core.channel.wechat.service.WeChatPayConfigService;
import cn.daxpay.single.service.core.channel.wechat.service.WeChatPayReconcileService;
import cn.daxpay.single.service.func.AbsReconcileStrategy;
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

    private final WeChatPayReconcileService reconcileService;

    private final WeChatPayConfigService weChatPayConfigService;

    private WeChatPayConfig config;

    /**
     * 策略标识
     *
     * @see PayChannelEnum
     */
    @Override
    public String getChannel() {
        return PayChannelEnum.WECHAT.getCode();
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
    public void upload(MultipartFile file, ReconcileFileTypeEnum fileType) {
        reconcileService.uploadBill(file.getBytes(),this.config);
    }

    /**
     * 下载对账单
     */
    @Override
    public void downAndSave() {
        String date = LocalDateTimeUtil.format(this.getRecordOrder().getDate(), DatePattern.PURE_DATE_PATTERN);
        reconcileService.downAndSave(this.getRecordOrder(), date, this.config);
    }

}
