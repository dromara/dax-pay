package cn.bootx.platform.daxpay.service.core.payment.reconcile.strategy;

import cn.bootx.platform.common.core.util.LocalDateTimeUtil;
import cn.bootx.platform.common.sequence.func.Sequence;
import cn.bootx.platform.daxpay.code.PayChannelEnum;
import cn.bootx.platform.daxpay.service.core.channel.wechat.convert.WeChatConvert;
import cn.bootx.platform.daxpay.service.core.channel.wechat.dao.WeChatPayRecordManager;
import cn.bootx.platform.daxpay.service.core.channel.wechat.entity.WeChatPayConfig;
import cn.bootx.platform.daxpay.service.core.channel.wechat.entity.WeChatPayRecord;
import cn.bootx.platform.daxpay.service.core.channel.wechat.service.WeChatPayConfigService;
import cn.bootx.platform.daxpay.service.core.channel.wechat.service.WechatPayReconcileService;
import cn.bootx.platform.daxpay.service.core.payment.reconcile.domain.GeneralReconcileRecord;
import cn.bootx.platform.daxpay.service.func.AbsReconcileStrategy;
import cn.bootx.platform.daxpay.service.handler.sequence.DaxPaySequenceHandler;
import cn.hutool.core.date.DatePattern;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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

    private final DaxPaySequenceHandler daxPaySequenceHandler;

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
     * 生成对账序列号
     * 生成批次号
     * 规则：通道简称 + yyyyMMdd + 两位流水号
     * 例子：wx2024012001、ali2024012002
     */
    @Override
    public String generateSequence(LocalDate date) {
        String prefix = getChannel().getReconcilePrefix();
        String dateStr = LocalDateTimeUtil.format(date, DatePattern.PURE_DATE_PATTERN);
        Sequence sequence = daxPaySequenceHandler.wechatReconcileSequence(dateStr);
        String key = String.format("%02d", sequence.next());
        return prefix + dateStr + key;
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

    /**
     * 获取通用对账对象, 将流水记录转换为对账对象
     */
    @Override
    public List<GeneralReconcileRecord> getGeneralReconcileRecord() {
        // 查询流水
        LocalDateTime localDateTime = LocalDateTimeUtil.date2DateTime(this.getRecordOrder().getDate());
        LocalDateTime start = LocalDateTimeUtil.beginOfDay(localDateTime);
        LocalDateTime end = LocalDateTimeUtil.endOfDay(localDateTime);
        return null;
    }

}
