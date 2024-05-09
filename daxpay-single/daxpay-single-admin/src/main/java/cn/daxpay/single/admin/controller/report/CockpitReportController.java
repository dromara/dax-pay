package cn.daxpay.single.admin.controller.report;

import cn.bootx.platform.common.core.annotation.IgnoreAuth;
import cn.bootx.platform.common.core.rest.Res;
import cn.bootx.platform.common.core.rest.ResResult;
import cn.bootx.platform.common.core.util.ValidationUtil;
import cn.daxpay.single.service.core.report.service.CockpitReportService;
import cn.daxpay.single.service.dto.report.ChannelLineReport;
import cn.daxpay.single.service.param.report.CockpitReportQuery;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 驾驶舱接口
 * @author xxm
 * @since 2024/3/17
 */
@IgnoreAuth// TODO 测试用, 后续删除
@Tag(name = "驾驶舱接口")
@RestController
@RequestMapping("/report/cockpit")
@RequiredArgsConstructor
public class CockpitReportController {
    private final CockpitReportService cockpitReportService;

    @Operation(summary = "支付金额(分)")
    @GetMapping("/getPayAmount")
    public ResResult<Integer> getPayAmount(@ParameterObject CockpitReportQuery query){
        ValidationUtil.validateParam(query);
        return Res.ok(cockpitReportService.getPayAmount(query));
    }

    @Operation(summary = "退款金额(分)")
    @GetMapping("/getRefundAmount")
    public ResResult<Integer> getRefundAmount(@ParameterObject CockpitReportQuery query){
        ValidationUtil.validateParam(query);
        return Res.ok(cockpitReportService.getRefundAmount(query));
    }

    @Operation(summary = "支付订单数量")
    @GetMapping("/getPayOrderCount")
    public ResResult<Integer> getPayOrderCount(@ParameterObject CockpitReportQuery query){
        ValidationUtil.validateParam(query);
        return Res.ok(cockpitReportService.getPayOrderCount(query));
    }

    @Operation(summary = "退款订单数量")
    @GetMapping("/getRefundOrderCount")
    public ResResult<Integer> getRefundOrderCount(@ParameterObject CockpitReportQuery query){
        ValidationUtil.validateParam(query);
        return Res.ok(cockpitReportService.getRefundOrderCount(query));
    }

    @Operation(summary = "显示通道支付订单金额和订单数")
    @GetMapping("/getPayChannelInfo")
    public ResResult<List<ChannelLineReport>> getPayChannelInfo(@ParameterObject CockpitReportQuery query){
        ValidationUtil.validateParam(query);
        return Res.ok(cockpitReportService.getPayChannelInfo(query));
    }

    @Operation(summary = "显示通道退款订单金额和订单数")
    @GetMapping("/getRefundChannelInfo")
    public ResResult<List<ChannelLineReport>> getRefundChannelInfo(@ParameterObject CockpitReportQuery query){
        ValidationUtil.validateParam(query);
        return Res.ok(cockpitReportService.getRefundChannelInfo(query));
    }

}
