package org.dromara.daxpay.controller.constant;

import cn.bootx.platform.core.annotation.RequestGroup;
import cn.bootx.platform.core.annotation.RequestPath;
import cn.bootx.platform.core.rest.Res;
import cn.bootx.platform.core.rest.param.PageParam;
import cn.bootx.platform.core.rest.result.PageResult;
import cn.bootx.platform.core.rest.result.Result;
import org.dromara.daxpay.service.param.constant.MethodConstQuery;
import org.dromara.daxpay.service.result.constant.PayMethodConstResult;
import org.dromara.daxpay.service.service.constant.MethodConstService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 支付方式常量控制器
 * @author xxm
 * @since 2024/7/14
 */
@Validated
@Tag(name = "支付方式常量控制器")
@RestController
@RequestMapping("/const/method")
@RequiredArgsConstructor
@RequestGroup(groupCode = "PayConst", moduleCode = "PayConfig")
public class MethodConstController {
    private final MethodConstService methodConstService;

    @RequestPath("支付方式分页")
    @Operation(summary = "支付方式分页")
    @GetMapping("/page")
    public Result<PageResult<PayMethodConstResult>> page(PageParam pageParam, MethodConstQuery query) {
        return Res.ok(methodConstService.page(pageParam, query));
    }
}
