package cn.bootx.platform.daxpay.service.core.payment.reconcile.strategy;

import cn.bootx.platform.common.core.function.CollectorsFunction;
import cn.bootx.platform.common.core.util.CollUtil;
import cn.bootx.platform.common.core.util.LocalDateTimeUtil;
import cn.bootx.platform.common.sequence.func.Sequence;
import cn.bootx.platform.daxpay.code.PayChannelEnum;
import cn.bootx.platform.daxpay.code.PayReconcileTradeEnum;
import cn.bootx.platform.daxpay.service.code.AliPayRecordTypeEnum;
import cn.bootx.platform.daxpay.service.core.channel.alipay.dao.AliPayRecordManager;
import cn.bootx.platform.daxpay.service.core.channel.alipay.entity.AliPayConfig;
import cn.bootx.platform.daxpay.service.core.channel.alipay.entity.AliPayRecord;
import cn.bootx.platform.daxpay.service.core.channel.alipay.service.AliPayConfigService;
import cn.bootx.platform.daxpay.service.core.channel.alipay.service.AliPayReconcileService;
import cn.bootx.platform.daxpay.service.core.order.reconcile.entity.PayReconcileDetail;
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
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
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
     * 比对生成对账差异单
     * 1. 远程有, 本地无  补单(追加回订单/记录差异表)
     * 2. 远程无, 本地有  记录差错表
     * 3. 远程有, 本地有, 但状态不一致 记录差错表
     */
    @Override
    public void compare() {
        List<PayReconcileDetail> details = this.getReconcileDetails();
        if (CollUtil.isEmpty(details)){
            return;
        }
        Map<String, PayReconcileDetail> detailMap = details.stream()
                .collect(Collectors.toMap(PayReconcileDetail::getOrderId, Function.identity(), CollectorsFunction::retainLatest));

        // 对哪天进行对账
        LocalDate date = this.getRecordOrder().getDate();

        // 查询流水
        LocalDateTime localDateTime = LocalDateTimeUtil.date2DateTime(date);
        LocalDateTime start = LocalDateTimeUtil.beginOfDay(localDateTime);
        LocalDateTime end = LocalDateTimeUtil.endOfDay(localDateTime);
        List<AliPayRecord> records = recordManager.findByDate(start, end);
        Map<Long, AliPayRecord> recordMap = records.stream()
                .collect(Collectors.toMap(AliPayRecord::getOrderId, Function.identity(), CollectorsFunction::retainLatest));

        // 对账与流水比对
        for (PayReconcileDetail detail : details) {
            // 判断本地有没有记录
            AliPayRecord record = recordMap.get(Long.valueOf(detail.getOrderId()));
            if (Objects.isNull(record)){
                log.info("本地订单不存在: {}", detail.getOrderId());
                continue;
            }
            System.out.println(detail.getId());
            // 交易类型 支付/退款
            if (Objects.equals(detail.getType(), PayReconcileTradeEnum.PAY.getCode())){
                // 判断类型是否存在差异
                if (!Objects.equals(record.getType(), AliPayRecordTypeEnum.PAY.getCode())){
                    log.info("本地订单类型不正常: {}", detail.getOrderId());
                    continue;
                }
            } else {
                // 判断类型是否存在差异
                if (!Objects.equals(record.getType(), AliPayRecordTypeEnum.REFUND.getCode())){
                    log.info("本地订单类型不正常: {}", detail.getOrderId());
                    continue;
                }
            }
            // 判断是否存在差异 金额, 状态
            if (!Objects.equals(record.getAmount(), detail.getAmount())){
                log.info("本地订单金额不正常: {}", detail.getOrderId());
                continue;
            }
        }
        // 流水与对账单比对, 找出本地有, 远程没有的记录
        for (AliPayRecord record : records) {
            PayReconcileDetail detail = detailMap.get(String.valueOf(record.getOrderId()));
            if (Objects.isNull(detail)){
                log.info("远程订单不存在: {}", record.getOrderId());
                continue;
            }
        }
    }
}
