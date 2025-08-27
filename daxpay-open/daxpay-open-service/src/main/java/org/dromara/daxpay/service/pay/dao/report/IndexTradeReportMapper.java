package org.dromara.daxpay.service.pay.dao.report;

import org.dromara.daxpay.service.pay.param.report.TradeReportQuery;
import org.dromara.daxpay.service.pay.result.report.TradeReportResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 首页报表数据库查询接口
 * @author xxm
 * @since 2024/11/17
 */
@Mapper
public interface IndexTradeReportMapper {

    /**
     * 支付订单
     */
    @Select("""
        select
            sum(amount) as tradeAmount,
            count(amount) as tradeCount,
            max(amount) as maxAmount,
            avg(amount) as avgAmount
        from pay_order
        ${ew.customSqlSegment}
    """)
    TradeReportResult payTradeReport(@Param(Constants.WRAPPER) QueryWrapper<TradeReportQuery> param);

    /**
     * 退款订单
     */
    @Select("""
        select
            sum(amount) as tradeAmount,
            count(amount) as tradeCount,
            max(amount) as maxAmount,
            avg(amount) as avgAmount
        from pay_refund_order
        ${ew.customSqlSegment}
    """)
    TradeReportResult refundTradeReport(@Param(Constants.WRAPPER) QueryWrapper<TradeReportQuery> param);

    /**
     * 支付通道分布
     */
    @Select("""
        select
            channel as title,
            sum(amount) as tradeAmount,
            count(amount) as tradeCount,
            max(amount) as maxAmount,
            avg(amount) as avgAmount
        from pay_order
        ${ew.customSqlSegment}
    """)
    List<TradeReportResult> payChannelReport(@Param(Constants.WRAPPER) QueryWrapper<TradeReportQuery> param);

    /**
     * 退款通道分布
     */
    @Select("""
        select
            channel as title,
            sum(amount) as tradeAmount,
            count(amount) as tradeCount,
            max(amount) as maxAmount,
            avg(amount) as avgAmount
        from pay_refund_order
        ${ew.customSqlSegment}
    """)
    List<TradeReportResult> refundChannelReport(@Param(Constants.WRAPPER) QueryWrapper<TradeReportQuery> param);

    /**
     * 支付方式统计
     */
    @Select("""
        select
            method as title,
            sum(amount) as tradeAmount,
            count(amount) as tradeCount,
            max(amount) as maxAmount,
            avg(amount) as avgAmount
        from pay_order
        ${ew.customSqlSegment}
    """)
    List<TradeReportResult> payMethodReport(@Param(Constants.WRAPPER) QueryWrapper<TradeReportQuery> param);

    /**
     * 支付统计
     */
    @Select("""
        select
            ${dateField} as time,
            sum(amount) as tradeAmount,
            count(amount) as tradeCount
        from pay_order
        ${ew.customSqlSegment}
    """)
    List<TradeReportResult> payTradeStatistics(@Param(Constants.WRAPPER) QueryWrapper<TradeReportQuery> param, @Param("dateField") String dateField);

    /**
     * 退款统计
     */
    @Select("""
        select
            ${dateField} as time,
            sum(amount) as tradeAmount,
            count(amount) as tradeCount
        from pay_refund_order
        ${ew.customSqlSegment}
    """)
    List<TradeReportResult> refundTradeStatistics(@Param(Constants.WRAPPER) QueryWrapper<TradeReportQuery> param, @Param("dateField") String dateField);

}
