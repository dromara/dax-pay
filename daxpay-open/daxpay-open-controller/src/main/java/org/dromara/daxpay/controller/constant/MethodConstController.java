package org.dromara.daxpay.controller.constant;

import cn.bootx.platform.core.annotation.IgnoreAuth;
import cn.bootx.platform.core.rest.Res;
import cn.bootx.platform.core.rest.param.PageParam;
import cn.bootx.platform.core.rest.result.PageResult;
import cn.bootx.platform.core.rest.result.Result;
import org.dromara.daxpay.service.pay.param.constant.MethodConstQuery;
import org.dromara.daxpay.service.pay.result.constant.PayMethodConstResult;
import org.dromara.daxpay.service.pay.service.constant.MethodConstService;
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
@IgnoreAuth
@Validated
@Tag(name = "支付方式常量控制器")
@RestController
@RequestMapping("/const/method")
@RequiredArgsConstructor
public class MethodConstController {
    private final MethodConstService methodConstService;

    @Operation(summary = "支付方式分页")
    @GetMapping("/page")
    public Result<PageResult<PayMethodConstResult>> page(PageParam pageParam, MethodConstQuery query) {
        return Res.ok(methodConstService.page(pageParam, query));
    }
}
