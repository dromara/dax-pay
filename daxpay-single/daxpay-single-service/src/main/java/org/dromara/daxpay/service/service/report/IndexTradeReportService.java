package org.dromara.daxpay.service.service.report;

import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.daxpay.core.enums.PayStatusEnum;
import org.dromara.daxpay.core.enums.RefundStatusEnum;
import org.dromara.daxpay.service.dao.report.IndexTradeReportMapper;
import org.dromara.daxpay.service.entity.order.pay.PayOrder;
import org.dromara.daxpay.service.entity.order.refund.RefundOrder;
import org.dromara.daxpay.service.param.report.TradeReportQuery;
import org.dromara.daxpay.service.result.report.TradeReportResult;
import org.springframework.stereotype.Service;

import java.util.List;

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

    /**
     * 支付交易统计: 笔数, 金额
     */
    public TradeReportResult pryTradeReport(TradeReportQuery query){
        QueryWrapper<TradeReportQuery> param = new QueryWrapper<>();
        // 获取开始时间和结束时间
        var startTime = LocalDateTimeUtil.beginOfDay(query.getStartDate());
        var endTime = LocalDateTimeUtil.endOfDay(query.getEndDate());

        param.between(MpUtil.getColumnName(PayOrder::getPayTime), startTime, endTime)
                .eq(MpUtil.getColumnName(PayOrder::getStatus), PayStatusEnum.SUCCESS);
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
                .eq(MpUtil.getColumnName(RefundOrder::getStatus), RefundStatusEnum.SUCCESS);
        return tradeReportMapper.refundTradeReport(param);
    }

    /**
     * 支付通道统计
     */
    public List<TradeReportResult> payChannelReport(TradeReportQuery query){
        QueryWrapper<TradeReportQuery> param = new QueryWrapper<>();
        // 获取开始时间和结束时间
        var startTime = LocalDateTimeUtil.beginOfDay(query.getStartDate());
        var endTime = LocalDateTimeUtil.endOfDay(query.getEndDate());

        param.between(MpUtil.getColumnName(PayOrder::getPayTime),startTime, endTime)
                .eq(MpUtil.getColumnName(PayOrder::getStatus), PayStatusEnum.SUCCESS)
                .groupBy(MpUtil.getColumnName(PayOrder::getChannel));
        return tradeReportMapper.payChannelReport(param);
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
                .eq(MpUtil.getColumnName(RefundOrder::getStatus), RefundStatusEnum.SUCCESS);
        return tradeReportMapper.refundChannelReport(param);
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
                .eq(MpUtil.getColumnName(PayOrder::getStatus), PayStatusEnum.SUCCESS)
                .groupBy(MpUtil.getColumnName(PayOrder::getMethod));
        return tradeReportMapper.payMethodReport(param);
    }
}
