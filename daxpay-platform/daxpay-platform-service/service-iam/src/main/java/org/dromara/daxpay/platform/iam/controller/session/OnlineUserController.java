package org.dromara.daxpay.platform.iam.controller.session;

import org.dromara.daxpay.platform.core.annotation.PermCode;
import org.dromara.daxpay.platform.core.rest.Res;
import org.dromara.daxpay.platform.core.rest.param.PageParam;
import org.dromara.daxpay.platform.core.rest.result.PageResult;
import org.dromara.daxpay.platform.core.rest.result.Result;
import org.dromara.daxpay.platform.iam.param.session.OnlineUserQuery;
import org.dromara.daxpay.platform.iam.result.session.OnlineUserResult;
import org.dromara.daxpay.platform.iam.service.session.OnlineUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/// # 在线用户管理
///
@PermCode(menuCode = "iam:online:user")
@Validated
@Tag(name = "在线用户管理")
@RestController
@RequestMapping("/online")
@RequiredArgsConstructor
public class OnlineUserController {

    private final OnlineUserService onlineUserService;

    @PermCode(code = "view", nameCn = "在线用户查看", nameEn = "Online User View")
    @Operation(summary = "在线用户分页")
    @GetMapping("/page")
    public Result<PageResult<OnlineUserResult>> page(PageParam pageParam, OnlineUserQuery query) {
        return Res.ok(onlineUserService.page(pageParam, query));
    }

    @PermCode(code = "kickout", nameCn = "强制下线", nameEn = "Kickout")
    @Operation(summary = "强制用户下线")
    @PostMapping("/kickout")
    public Result<Void> kickout(@RequestParam String sessionId) {
        onlineUserService.kickout(sessionId);
        return Res.ok();
    }

    @PermCode(code = "kickout", nameCn = "强制下线", nameEn = "Kickout")
    @Operation(summary = "批量强制用户下线")
    @PostMapping("/kickout-batch")
    public Result<Void> kickoutBatch(@RequestBody @NotEmpty(message = "{validation.field.sessionIds.notEmpty}") List<String> sessionIds) {
        onlineUserService.kickoutBatch(sessionIds);
        return Res.ok();
    }
}
