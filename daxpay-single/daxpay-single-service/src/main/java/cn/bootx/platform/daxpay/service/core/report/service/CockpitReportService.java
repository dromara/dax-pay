package cn.bootx.platform.daxpay.service.core.report.service;

import cn.bootx.platform.common.core.function.CollectorsFunction;
import cn.bootx.platform.daxpay.code.PayChannelEnum;
import cn.bootx.platform.daxpay.service.core.report.dao.CockpitReportMapper;
import cn.bootx.platform.daxpay.service.core.report.entity.ChannelOrderLine;
import cn.bootx.platform.daxpay.service.dto.report.ChannelLineReport;
import cn.bootx.platform.daxpay.service.param.report.CockpitReportQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 驾驶舱接口
 * @author xxm
 * @since 2024/3/15
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CockpitReportService {

    private final CockpitReportMapper cockpitReportMapper;

    /**
     * 支付金额(分)
     */
    public Integer getPayAmount(CockpitReportQuery query){
        // 获取支付成功的订单
        return cockpitReportMapper.getPayAmount(query).orElse(0);
    }

    /**
     * 退款金额(分)
     */
    public Integer getRefundAmount(CockpitReportQuery query){
        return cockpitReportMapper.getRefundAmount(query).orElse(0);
    }

    /**
     * 支付订单数量
     */
    public Integer getPayOrderCount(CockpitReportQuery query){
        return cockpitReportMapper.getPayOrderCount(query);
    }

    /**
     * 退款订单数量
     */
    public Integer getRefundOrderCount(CockpitReportQuery query){
        return cockpitReportMapper.getRefundOrderCount(query);
    }

    /**
     * (折线图/占比图)显示各通道支付分为支付金额和订单数
     */
    public List<ChannelLineReport> getPayChannelInfo(CockpitReportQuery query){
        Map<String, ChannelOrderLine> lineMap = cockpitReportMapper.getPayChannelInfo(query)
                .stream()
                .collect(Collectors.toMap(ChannelOrderLine::getChannel, Function.identity(), CollectorsFunction::retainLatest));
        // 根据系统中有的通道编码,获取对应的通道名称
        return Arrays.stream(PayChannelEnum.values())
                .map(e->{
                    ChannelOrderLine channelOrderLine = lineMap.get(e.getCode());
                    ChannelLineReport channelLineReport = new ChannelLineReport()
                            .setChannelCode(e.getCode())
                            .setChannelName(e.getName());
                    if (Objects.isNull(channelOrderLine)){
                        channelLineReport.setOrderAmount(0).setOrderCount(0);
                    } else {
                        channelLineReport.setOrderAmount(channelOrderLine.getSum())
                                .setOrderCount(channelOrderLine.getCount());
                    }
                    return channelLineReport;
                }).collect(Collectors.toList());
    }

    /**
     * (折线图/占比图)显示各通道退款金额和订单数,
     */
    public List<ChannelLineReport> getRefundChannelInfo(CockpitReportQuery query){
        Map<String, ChannelOrderLine> lineMap = cockpitReportMapper.getRefundChannelInfo(query)
                .stream()
                .collect(Collectors.toMap(ChannelOrderLine::getChannel, Function.identity(), CollectorsFunction::retainLatest));
        // 根据系统中有的通道编码,获取对应的通道名称
        return Arrays.stream(PayChannelEnum.values())
                .map(e->{
                    ChannelOrderLine channelOrderLine = lineMap.get(e.getCode());
                    ChannelLineReport channelLineReport = new ChannelLineReport()
                            .setChannelCode(e.getCode())
                            .setChannelName(e.getName());
                    if (Objects.isNull(channelOrderLine)){
                        channelLineReport.setOrderAmount(0).setOrderCount(0);
                    } else {
                        channelLineReport.setOrderAmount(channelOrderLine.getSum())
                                .setOrderCount(channelOrderLine.getCount());
                    }
                    return channelLineReport;
                }).collect(Collectors.toList());
    }
}
