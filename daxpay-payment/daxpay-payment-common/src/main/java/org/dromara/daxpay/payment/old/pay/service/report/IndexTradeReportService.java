package org.dromara.daxpay.payment.old.pay.service.report;

import org.dromara.daxpay.platform.common.mybatisplus.util.MpUtil;
import org.dromara.daxpay.platform.core.enums.pay.pay.PayStatusEnum;
import org.dromara.daxpay.platform.core.enums.pay.refund.RefundStatusEnum;
import org.dromara.daxpay.payment.common.service.MerchantReportQueryService;
import org.dromara.daxpay.payment.old.pay.dao.report.IndexTradeReportMapper;
import org.dromara.daxpay.payment.old.pay.entity.order.pay.PayOrder;
import org.dromara.daxpay.payment.old.pay.entity.order.refund.RefundOrder;
import org.dromara.daxpay.payment.old.pay.param.report.TradeReportQuery;
import org.dromara.daxpay.payment.old.pay.result.report.MerchantReportResult;
import org.dromara.daxpay.payment.old.pay.result.report.TradeReportResult;
import org.dromara.daxpay.payment.old.pay.result.report.TradeStatisticsReport;
import org.dromara.daxpay.platform.system.service.dict.DictItemService;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.LocalDateTimeUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/// # 交易类报表
///
@Slf4j
@Service
@RequiredArgsConstructor
public class IndexTradeReportService {
    private final IndexTradeReportMapper tradeReportMapper;
    private final MerchantReportQueryService merchantReportQueryService;
    private final DictItemService dictItemService;

    /// 支付交易统计: 笔数, 金额
    public TradeReportResult pryTradeReport(TradeReportQuery query) {
        QueryWrapper<TradeReportQuery> param = new QueryWrapper<>();
        // 获取开始时间和结束时间
        var startTime = LocalDateTimeUtil.beginOfDay(query.getStartDate());
        var endTime = LocalDateTimeUtil.endOfDay(query.getEndDate());

        param.between(MpUtil.getColumnName(PayOrder::getPayTime), startTime, endTime)
                .eq(Objects.nonNull(query.getAppId()), MpUtil.getColumnName(PayOrder::getAppId), query.getAppId())
                .eq(MpUtil.getColumnName(PayOrder::getStatus), PayStatusEnum.SUCCESS.getCode());
        return tradeReportMapper.payTradeReport(param);
    }

    /// 退款交易统计: 笔数, 金额
    public TradeReportResult refundTradeReport(TradeReportQuery query) {
        QueryWrapper<TradeReportQuery> param = new QueryWrapper<>();
        // 获取开始时间和结束时间
        var startTime = LocalDateTimeUtil.beginOfDay(query.getStartDate());
        var endTime = LocalDateTimeUtil.endOfDay(query.getEndDate());

        param.between(MpUtil.getColumnName(RefundOrder::getFinishTime), startTime, endTime)
                .eq(Objects.nonNull(query.getMchNo()), MpUtil.getColumnName(RefundOrder::getMchNo), query.getMchNo())
                .eq(Objects.nonNull(query.getAppId()), MpUtil.getColumnName(RefundOrder::getAppId), query.getAppId())
                .eq(MpUtil.getColumnName(RefundOrder::getStatus), RefundStatusEnum.SUCCESS.getCode());
        return tradeReportMapper.refundTradeReport(param);
    }

    /// 支付产品统计
    public List<TradeReportResult> payChannelReport(TradeReportQuery query) {
        QueryWrapper<TradeReportQuery> queryWrapper = new QueryWrapper<>();
        // 获取开始时间和结束时间
        var startTime = LocalDateTimeUtil.beginOfDay(query.getStartDate());
        var endTime = LocalDateTimeUtil.endOfDay(query.getEndDate());

        queryWrapper.between(MpUtil.getColumnName(PayOrder::getPayTime), startTime, endTime)
                .eq(Objects.nonNull(query.getMchNo()), MpUtil.getColumnName(PayOrder::getMchNo), query.getMchNo())
                .eq(Objects.nonNull(query.getAppId()), MpUtil.getColumnName(PayOrder::getAppId), query.getAppId())
                .eq(MpUtil.getColumnName(PayOrder::getStatus), PayStatusEnum.SUCCESS.getCode())
                .groupBy(MpUtil.getColumnName(PayOrder::getProduct));
        var list = tradeReportMapper.payChannelReport(queryWrapper);
        var dictMap = dictItemService.findEnableByDictCode("product");
        list.forEach(item -> item.setTitle(dictMap.get(item.getTitle())));
        return list;
    }

    /// 退款产品统计
    public List<TradeReportResult> refundChannelReport(TradeReportQuery query) {
        QueryWrapper<TradeReportQuery> param = new QueryWrapper<>();
        // 获取开始时间和结束时间
        var startTime = LocalDateTimeUtil.beginOfDay(query.getStartDate());
        var endTime = LocalDateTimeUtil.endOfDay(query.getEndDate());

        param.between(MpUtil.getColumnName(RefundOrder::getFinishTime), startTime, endTime)
                .groupBy(MpUtil.getColumnName(RefundOrder::getProduct))
                .eq(Objects.nonNull(query.getMchNo()), MpUtil.getColumnName(RefundOrder::getMchNo), query.getMchNo())
                .eq(Objects.nonNull(query.getAppId()), MpUtil.getColumnName(RefundOrder::getAppId), query.getAppId())
                .eq(MpUtil.getColumnName(RefundOrder::getStatus), RefundStatusEnum.SUCCESS.getCode());

        var list = tradeReportMapper.refundChannelReport(param);
        var dictMap = dictItemService.findEnableByDictCode("product");
        list.forEach(item -> item.setTitle(dictMap.get(item.getTitle())));
        return list;
    }

    /// 支付方式统计
    public List<TradeReportResult> payMethodReport(TradeReportQuery query) {
        QueryWrapper<TradeReportQuery> param = new QueryWrapper<>();
        // 获取开始时间和结束时间
        var startTime = LocalDateTimeUtil.beginOfDay(query.getStartDate());
        var endTime = LocalDateTimeUtil.endOfDay(query.getEndDate());

        param.between(MpUtil.getColumnName(PayOrder::getPayTime), startTime, endTime)
                .eq(MpUtil.getColumnName(PayOrder::getStatus), PayStatusEnum.SUCCESS.getCode())
                .eq(Objects.nonNull(query.getMchNo()), MpUtil.getColumnName(PayOrder::getMchNo), query.getMchNo())
                .eq(Objects.nonNull(query.getAppId()), MpUtil.getColumnName(PayOrder::getAppId), query.getAppId())
                .groupBy(MpUtil.getColumnName(PayOrder::getMethod));
        var list = tradeReportMapper.payMethodReport(param);
        var dictMap = dictItemService.findEnableByDictCode("pay_method");
        list.forEach(item -> item.setTitle(dictMap.get(item.getTitle())));
        return list;
    }

    /// 商户类型数量分类
    public MerchantReportResult merchantCount(TradeReportQuery query) {
        return merchantReportQueryService.merchantCount(query);
    }

    /// 交易统计信息
    public List<TradeStatisticsReport> tradeStatistics(TradeReportQuery query) {
        // 获取开始和结束时间
        var startTime = LocalDateTimeUtil.beginOfDay(query.getStartDate());
        var endTime = LocalDateTimeUtil.endOfDay(query.getEndDate());
        // 查询支付金额和笔数
        QueryWrapper<TradeReportQuery> payQueryWrapper = new QueryWrapper<>();
        QueryWrapper<TradeReportQuery> refundQueryWrapper = new QueryWrapper<>();
        payQueryWrapper.between(MpUtil.getColumnName(PayOrder::getPayTime), startTime, endTime)
                .eq(MpUtil.getColumnName(PayOrder::getStatus), PayStatusEnum.SUCCESS.getCode())
                .eq(Objects.nonNull(query.getMchNo()), MpUtil.getColumnName(PayOrder::getMchNo), query.getMchNo())
                .eq(Objects.nonNull(query.getAppId()), MpUtil.getColumnName(PayOrder::getAppId), query.getAppId());
        refundQueryWrapper.between(MpUtil.getColumnName(RefundOrder::getFinishTime), startTime, endTime)
                .eq(MpUtil.getColumnName(RefundOrder::getStatus), RefundStatusEnum.SUCCESS.getCode())
                .eq(Objects.nonNull(query.getMchNo()), MpUtil.getColumnName(RefundOrder::getMchNo), query.getMchNo())
                .eq(Objects.nonNull(query.getAppId()), MpUtil.getColumnName(RefundOrder::getAppId), query.getAppId());
        // 判断pg还是Mysql, 使用不同的SQL语法
        String payFiled, refundFiled;
        if (MpUtil.getDbType().equalsIgnoreCase("mysql")) {
            payFiled = "DATE(" + MpUtil.getColumnName(PayOrder::getPayTime) + ")";
            refundFiled = "DATE(" + MpUtil.getColumnName(RefundOrder::getFinishTime) + ")";
            payQueryWrapper.groupBy(payFiled);
            refundQueryWrapper.groupBy(refundFiled);
        } else {
            payFiled = "CAST(" + MpUtil.getColumnName(PayOrder::getPayTime) + " AS DATE)";
            refundFiled = "CAST(" + MpUtil.getColumnName(RefundOrder::getFinishTime) + " AS DATE)";
            payQueryWrapper.groupBy(payFiled);
            refundQueryWrapper.groupBy(refundFiled);
        }
        var payList = tradeReportMapper.payTradeStatistics(payQueryWrapper, payFiled);
        var refundList = tradeReportMapper.refundTradeStatistics(refundQueryWrapper, refundFiled);
        Map<String, TradeReportResult> payMap = payList.stream()
                .collect(Collectors.toMap(TradeReportResult::getTime, Function.identity()));
        Map<String, TradeReportResult> refundMap = refundList.stream()
                .collect(Collectors.toMap(TradeReportResult::getTime, Function.identity()));

        // 根据开始和结束时间生成改时间段的对象
        int daysBetween = Math.toIntExact(ChronoUnit.DAYS.between(query.getStartDate(), query.getEndDate().plusDays(1)));
        List<TradeStatisticsReport> list = new ArrayList<>(daysBetween);
        for (long i = 0; i < daysBetween; i++) {
            var tradeStatistics = new TradeStatisticsReport()
                    .setLocalDate(query.getStartDate().plusDays(i));
            // 获取支付金额和笔数
            var pay = payMap.get(LocalDateTimeUtil.format(tradeStatistics.getLocalDate(), DatePattern.NORM_DATE_PATTERN));
            tradeStatistics.setPayAmount(pay == null ? BigDecimal.valueOf(0) : pay.getTradeAmount())
                    .setPayCount(pay == null ? 0 : pay.getTradeCount());
            // 获取退款金额和笔数
            var refund = refundMap.get(LocalDateTimeUtil.format(tradeStatistics.getLocalDate(), DatePattern.NORM_DATE_PATTERN));
            tradeStatistics.setRefundAmount(refund == null ? BigDecimal.valueOf(0) : refund.getTradeAmount())
                    .setRefundCount(refund == null ? 0 : refund.getTradeCount());
            list.add(tradeStatistics);
        }
        return list;
    }

    /// 分润统计
    public TradeReportResult profitTradeReport(TradeReportQuery query) {
        return new TradeReportResult();
    }
}
