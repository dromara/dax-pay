package cn.daxpay.multi.admin.controller.record;

import cn.bootx.platform.core.annotation.RequestGroup;
import cn.bootx.platform.core.annotation.RequestPath;
import cn.bootx.platform.core.rest.Res;
import cn.bootx.platform.core.rest.param.PageParam;
import cn.bootx.platform.core.rest.result.PageResult;
import cn.bootx.platform.core.rest.result.Result;
import cn.daxpay.multi.service.param.record.PayCloseRecordQuery;
import cn.daxpay.multi.service.result.record.close.PayCloseRecordResult;
import cn.daxpay.multi.service.service.record.close.PayCloseRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 支付订单关闭记录
 * @author xxm
 * @since 2024/1/9
 */
@Tag(name = "支付订单关闭记录")
@RestController
@RequestMapping("/record/close")
@RequestGroup(moduleCode = "TradeRecord", moduleName = "交易记录", groupCode = "CloseRecord", groupName = "支付关闭记录")
@RequiredArgsConstructor
public class PayCloseRecordController {
    private final PayCloseRecordService service;

    @RequestPath("分页查询")
    @Operation(summary = "分页查询")
    @GetMapping("/page")
    public Result<PageResult<PayCloseRecordResult>> page(PageParam pageParam, PayCloseRecordQuery query){
        return Res.ok(service.page(pageParam, query));
    }

    @RequestPath("查询单条")
    @Operation(summary = "查询单条")
    @GetMapping("/findById")
    public Result<PayCloseRecordResult> findById(Long id){
        return Res.ok(service.findById(id));
    }

}
