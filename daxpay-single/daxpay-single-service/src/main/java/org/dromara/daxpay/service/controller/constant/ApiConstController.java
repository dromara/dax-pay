package org.dromara.daxpay.service.controller.constant;

import cn.bootx.platform.core.annotation.RequestGroup;
import cn.bootx.platform.core.annotation.RequestPath;
import cn.bootx.platform.core.rest.Res;
import cn.bootx.platform.core.rest.param.PageParam;
import cn.bootx.platform.core.rest.result.PageResult;
import cn.bootx.platform.core.rest.result.Result;
import org.dromara.daxpay.service.param.constant.ApiConstQuery;
import org.dromara.daxpay.service.result.constant.ApiConstResult;
import org.dromara.daxpay.service.service.constant.ApiConstService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 支付开放接口控制器
 * @author xxm
 * @since 2024/7/14
 */
@Tag(name = "支付开放接口控制器")
@RestController
@RequestMapping("/const/api")
@RequiredArgsConstructor
@RequestGroup(groupCode = "PayConst", groupName = "支付常量", moduleCode = "PayConfig")
public class ApiConstController {
    private final ApiConstService apiConstService;

    @RequestPath("支付开放接口分页")
    @Operation(summary = "支付开放接口分页")
    @GetMapping("/page")
    public Result<PageResult<ApiConstResult>> page(PageParam pageParam, ApiConstQuery query) {
        return Res.ok(apiConstService.page(pageParam, query));
    }
}
