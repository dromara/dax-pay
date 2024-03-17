package cn.bootx.platform.daxpay.service.core.report.dao;

import cn.bootx.platform.daxpay.service.core.report.entity.ChannelOrderLine;
import cn.bootx.platform.daxpay.service.param.report.CockpitReportQuery;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

/**
 * 查询报表
 * @author xxm
 * @since 2024/3/17
 */
@Mapper
public interface CockpitReportMapper {

    /**
     * 获取支付金额
     */
    Optional<Integer> getPayAmount(CockpitReportQuery query);

    /**
     * 获取退款金额
     */
    Optional<Integer> getRefundAmount(CockpitReportQuery query);

    /**
     * 获取支付订单数量
     */
    Integer getPayOrderCount(CockpitReportQuery query);

    /**
     * 获取退款订单数量
     */
    Integer getRefundOrderCount(CockpitReportQuery query);

    /**
     * 支付通道订单折线图
     */
    List<ChannelOrderLine> getPayChannelInfo(CockpitReportQuery query);

    /**
     * 退款通道订单折线图
     */
    List<ChannelOrderLine> getRefundChannelInfo(CockpitReportQuery query);


}
