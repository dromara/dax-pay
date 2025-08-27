package org.dromara.daxpay.controller.constant;

import cn.bootx.platform.core.annotation.IgnoreAuth;
import cn.bootx.platform.core.rest.Res;
import cn.bootx.platform.core.rest.param.PageParam;
import cn.bootx.platform.core.rest.result.PageResult;
import cn.bootx.platform.core.rest.result.Result;
import org.dromara.daxpay.service.pay.param.constant.ApiConstQuery;
import org.dromara.daxpay.service.pay.result.constant.ApiConstResult;
import org.dromara.daxpay.service.pay.service.constant.ApiConstService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 支付开放接口控制器
 * @author xxm
 * @since 2024/7/14
 */
@IgnoreAuth
@Validated
@Tag(name = "支付开放接口控制器")
@RestController
@RequestMapping("/const/api")
@RequiredArgsConstructor
public class ApiConstController {
    private final ApiConstService apiConstService;

    @Operation(summary = "支付开放接口分页")
    @GetMapping("/page")
    public Result<PageResult<ApiConstResult>> page(PageParam pageParam, ApiConstQuery query) {
        return Res.ok(apiConstService.page(pageParam, query));
    }
}
