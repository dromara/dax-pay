package org.dromara.daxpay.service.service.report;

import cn.bootx.platform.baseapi.service.dict.DictionaryItemService;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import org.dromara.daxpay.core.enums.PayStatusEnum;
import org.dromara.daxpay.core.enums.RefundStatusEnum;
import org.dromara.daxpay.service.dao.report.IndexTradeReportMapper;
import org.dromara.daxpay.service.entity.order.pay.PayOrder;
import org.dromara.daxpay.service.entity.order.refund.RefundOrder;
import org.dromara.daxpay.service.param.report.TradeReportQuery;
import org.dromara.daxpay.service.result.report.TradeReportResult;
import cn.hutool.core.date.LocalDateTimeUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * 交易类报表
 * @author xxm
 * @since 2024/11/16
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class IndexTradeReportService {
    private final IndexTradeReportMapper tradeReportMapper;
    private final DictionaryItemService dictionaryItemService;

    /**
     * 支付交易统计: 笔数, 金额
     */
    public TradeReportResult pryTradeReport(TradeReportQuery query){
        QueryWrapper<TradeReportQuery> param = new QueryWrapper<>();
        // 获取开始时间和结束时间
        var startTime = LocalDateTimeUtil.beginOfDay(query.getStartDate());
        var endTime = LocalDateTimeUtil.endOfDay(query.getEndDate());

        param.between(MpUtil.getColumnName(PayOrder::getPayTime), startTime, endTime)
                .eq(Objects.nonNull(query.getMchNo()), MpUtil.getColumnName(PayOrder::getMchNo), query.getMchNo())
                .eq(MpUtil.getColumnName(PayOrder::getStatus), PayStatusEnum.SUCCESS.getCode());
        return tradeReportMapper.payTradeReport(param);
    }

    /**
     * 退款交易统计: 笔数, 金额
     */
    public TradeReportResult refundTradeReport(TradeReportQuery query){
        QueryWrapper<TradeReportQuery> param = new QueryWrapper<>();
        // 获取开始时间和结束时间
        var startTime = LocalDateTimeUtil.beginOfDay(query.getStartDate());
        var endTime = LocalDateTimeUtil.endOfDay(query.getEndDate());

        param.between(MpUtil.getColumnName(RefundOrder::getFinishTime),startTime, endTime)
                .eq(Objects.nonNull(query.getMchNo()), MpUtil.getColumnName(RefundOrder::getMchNo), query.getMchNo())
                .eq(MpUtil.getColumnName(RefundOrder::getStatus), RefundStatusEnum.SUCCESS.getCode());
        return tradeReportMapper.refundTradeReport(param);
    }

    /**
     * 支付通道统计
     */
    public List<TradeReportResult> payChannelReport(TradeReportQuery query){
        QueryWrapper<TradeReportQuery> queryWrapper = new QueryWrapper<>();
        // 获取开始时间和结束时间
        var startTime = LocalDateTimeUtil.beginOfDay(query.getStartDate());
        var endTime = LocalDateTimeUtil.endOfDay(query.getEndDate());

        queryWrapper.between(MpUtil.getColumnName(PayOrder::getPayTime),startTime, endTime)
                .eq(Objects.nonNull(query.getMchNo()), MpUtil.getColumnName(PayOrder::getMchNo), query.getMchNo())
                .eq(MpUtil.getColumnName(PayOrder::getStatus), PayStatusEnum.SUCCESS.getCode())
                .groupBy(MpUtil.getColumnName(PayOrder::getChannel));
        var list = tradeReportMapper.payChannelReport(queryWrapper);
        var dictMap = dictionaryItemService.findEnableByDictCode("channel");
        list.forEach(item -> item.setTitle(dictMap.get(item.getTitle())));
        return list;
    }

    /**
     * 退款通道统计
     */
    public List<TradeReportResult> refundChannelReport(TradeReportQuery query){
        QueryWrapper<TradeReportQuery> param = new QueryWrapper<>();
        // 获取开始时间和结束时间
        var startTime = LocalDateTimeUtil.beginOfDay(query.getStartDate());
        var endTime = LocalDateTimeUtil.endOfDay(query.getEndDate());

        param.between(MpUtil.getColumnName(RefundOrder::getFinishTime),startTime, endTime)
                .groupBy(MpUtil.getColumnName(RefundOrder::getChannel))
                .eq(Objects.nonNull(query.getMchNo()), MpUtil.getColumnName(RefundOrder::getMchNo), query.getMchNo())
                .eq(MpUtil.getColumnName(RefundOrder::getStatus), RefundStatusEnum.SUCCESS.getCode());

        var list = tradeReportMapper.refundChannelReport(param);
        var dictMap = dictionaryItemService.findEnableByDictCode("channel");
        list.forEach(item -> item.setTitle(dictMap.get(item.getTitle())));
        return list;
    }

    /**
     * 支付方式统计
     */
    public List<TradeReportResult> payMethodReport(TradeReportQuery query){
        QueryWrapper<TradeReportQuery> param = new QueryWrapper<>();
        // 获取开始时间和结束时间
        var startTime = LocalDateTimeUtil.beginOfDay(query.getStartDate());
        var endTime = LocalDateTimeUtil.endOfDay(query.getEndDate());

        param.between(MpUtil.getColumnName(PayOrder::getPayTime),startTime, endTime)
                .eq(MpUtil.getColumnName(PayOrder::getStatus), PayStatusEnum.SUCCESS.getCode())
                .eq(Objects.nonNull(query.getMchNo()), MpUtil.getColumnName(PayOrder::getMchNo), query.getMchNo())
                .groupBy(MpUtil.getColumnName(PayOrder::getMethod));
        var list = tradeReportMapper.payMethodReport(param);
        var dictMap = dictionaryItemService.findEnableByDictCode("pay_method");
        list.forEach(item -> item.setTitle(dictMap.get(item.getTitle())));
        return list;
    }
}
