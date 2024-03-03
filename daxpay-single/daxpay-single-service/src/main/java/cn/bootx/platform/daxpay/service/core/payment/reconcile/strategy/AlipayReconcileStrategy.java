package cn.bootx.platform.daxpay.service.core.payment.reconcile.strategy;

import cn.bootx.platform.common.core.util.LocalDateTimeUtil;
import cn.bootx.platform.common.sequence.func.Sequence;
import cn.bootx.platform.daxpay.code.PayChannelEnum;
import cn.bootx.platform.daxpay.service.core.channel.alipay.convert.AlipayConvert;
import cn.bootx.platform.daxpay.service.core.channel.alipay.dao.AliPayRecordManager;
import cn.bootx.platform.daxpay.service.core.channel.alipay.entity.AliPayConfig;
import cn.bootx.platform.daxpay.service.core.channel.alipay.entity.AliPayRecord;
import cn.bootx.platform.daxpay.service.core.channel.alipay.service.AliPayConfigService;
import cn.bootx.platform.daxpay.service.core.channel.alipay.service.AliPayReconcileService;
import cn.bootx.platform.daxpay.service.core.payment.reconcile.domain.GeneralReconcileRecord;
import cn.bootx.platform.daxpay.service.func.AbsReconcileStrategy;
import cn.hutool.core.date.DatePattern;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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
public class AlipayReconcileStrategy extends AbsReconcileStrategy {

    private final AliPayReconcileService reconcileService;

    private final AliPayRecordManager recordManager;

    private final AliPayConfigService configService;

    @Getter
    @Qualifier("alipayReconcileSequence")
    private final Sequence sequence;

    private AliPayConfig config;


    /**
     * 策略标识
     *
     * @see PayChannelEnum
     */
    @Override
    public PayChannelEnum getChannel() {
        return PayChannelEnum.ALI;
    }

    /**
     * 生成对账序列号
     * 规则：通道简称 + yyyyMMdd + 两位流水号
     * 例子：wx2024012001、ali2024012002
     */
    @Override
    public String generateSequence(LocalDate date) {
        String prefix = getChannel().getReconcilePrefix();
        String dateStr = LocalDateTimeUtil.format(date, DatePattern.PURE_DATE_PATTERN);
        String key = String.format("%02d", sequence.next());
        return prefix + dateStr + key;
    }

    /**
     * 对账前处理, 主要是初始化支付SDK配置
     */
    @Override
    public void doBeforeHandler() {
        this.config = configService.getConfig();
        configService.initConfig(this.config);
    }

    /**
     * 下载和保存对账单
     */
    @Override
    public void downAndSave() {
        String date = LocalDateTimeUtil.format(this.getRecordOrder().getDate(), DatePattern.NORM_DATE_PATTERN);
        reconcileService.downAndSave(date,this.getRecordOrder().getId());
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
        List<AliPayRecord> records = recordManager.findByDate(start, end);
        return records.stream().map(AlipayConvert.CONVERT::convertReconcileRecord).collect(Collectors.toList());
    }
}
