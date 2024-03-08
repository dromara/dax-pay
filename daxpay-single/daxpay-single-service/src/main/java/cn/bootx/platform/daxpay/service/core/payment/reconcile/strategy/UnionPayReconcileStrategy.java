package cn.bootx.platform.daxpay.service.core.payment.reconcile.strategy;

import cn.bootx.platform.common.core.util.LocalDateTimeUtil;
import cn.bootx.platform.common.sequence.func.Sequence;
import cn.bootx.platform.daxpay.code.PayChannelEnum;
import cn.bootx.platform.daxpay.service.core.channel.union.convert.UnionPayConvert;
import cn.bootx.platform.daxpay.service.core.channel.union.dao.UnionPayRecordManager;
import cn.bootx.platform.daxpay.service.core.channel.union.entity.UnionPayConfig;
import cn.bootx.platform.daxpay.service.core.channel.union.entity.UnionPayRecord;
import cn.bootx.platform.daxpay.service.core.channel.union.service.UnionPayConfigService;
import cn.bootx.platform.daxpay.service.core.channel.union.service.UnionPayReconcileService;
import cn.bootx.platform.daxpay.service.core.payment.reconcile.domain.GeneralReconcileRecord;
import cn.bootx.platform.daxpay.service.func.AbsReconcileStrategy;
import cn.bootx.platform.daxpay.service.sdk.union.api.UnionPayKit;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

/**
 *
 * @author xxm
 * @since 2024/3/8
 */
@Slf4j
@Service
@Scope(SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class UnionPayReconcileStrategy extends AbsReconcileStrategy {

    private final UnionPayConfigService configService;

    private final UnionPayReconcileService reconcileService;

    private final UnionPayRecordManager recordManager;

    @Qualifier("unionPayReconcileSequence")
    private final Sequence sequence;

    private UnionPayKit unionPayKit;

    /**
     * 生成对账序列号
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
        UnionPayConfig config = configService.getConfig();
        this.unionPayKit = configService.initPayService(config);
    }

    /**
     * 下载对账单到本地进行保存
     */
    @Override
    public void downAndSave() {
        Date date = DateUtil.date(this.getRecordOrder().getDate());
        reconcileService.downAndSave(date, this.getRecordOrder().getId(), this.unionPayKit);
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
        List<UnionPayRecord> records = recordManager.findByDate(start, end);
        return records.stream().map(UnionPayConvert.CONVERT::convertReconcileRecord).collect(Collectors.toList());
    }

    /**
     * 策略标识
     */
    @Override
    public PayChannelEnum getChannel() {
        return PayChannelEnum.UNION_PAY;
    }
}
