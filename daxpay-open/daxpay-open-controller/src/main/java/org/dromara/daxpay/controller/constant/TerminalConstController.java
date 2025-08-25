package org.dromara.daxpay.controller.constant;

import cn.bootx.platform.core.annotation.IgnoreAuth;
import cn.bootx.platform.core.rest.Res;
import cn.bootx.platform.core.rest.param.PageParam;
import cn.bootx.platform.core.rest.result.PageResult;
import cn.bootx.platform.core.rest.result.Result;
import org.dromara.daxpay.service.pay.param.constant.TerminalConstQuery;
import org.dromara.daxpay.service.pay.result.constant.TerminalConstResult;
import org.dromara.daxpay.service.pay.service.constant.TerminalConstService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 终端报送方式常量控制器
 * @author xxm
 * @since 2025/3/12
 */
@Validated
@IgnoreAuth
@Tag(name = "终端报送方式常量控制器")
@RestController
@RequestMapping("/const/terminal")
@RequiredArgsConstructor
public class TerminalConstController {
    private final TerminalConstService terminalConstService;

    @Operation(summary = "支付方式分页")
    @GetMapping("/page")
    public Result<PageResult<TerminalConstResult>> page(PageParam pageParam, TerminalConstQuery query) {
        return Res.ok(terminalConstService.page(pageParam, query));
    }
}
