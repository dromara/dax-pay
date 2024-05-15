package cn.bootx.platform.iam.controller;

import cn.bootx.platform.common.core.annotation.IgnoreAuth;
import cn.bootx.platform.common.core.rest.PageResult;
import cn.bootx.platform.common.core.rest.Res;
import cn.bootx.platform.common.core.rest.ResResult;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.core.util.ValidationUtil;
import cn.bootx.platform.iam.core.third.service.UserThirdBindService;
import cn.bootx.platform.iam.core.third.service.UserThirdQueryService;
import cn.bootx.platform.iam.dto.user.UserThirdBindInfo;
import cn.bootx.platform.iam.dto.user.UserThirdDto;
import cn.bootx.platform.iam.param.user.UserBindThirdParam;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;

/**
 * 用户三方登录管理
 *
 * @author xxm
 * @since 2021/8/4
 */
@Validated
@Tag(name = "用户三方登录管理")
@RestController
@RequestMapping("/user/third")
@AllArgsConstructor
public class UserThirdController {

    private final UserThirdBindService userThirdBindService;

    private final UserThirdQueryService userThirdQueryService;

    @Operation(summary = "分页")
    @GetMapping("/page")
    public ResResult<PageResult<UserThirdDto>> page(PageParam pageParam) {
        return Res.ok(userThirdQueryService.page(pageParam));
    }

    @Operation(summary = "获取详情")
    @PostMapping("/findById")
    public ResResult<UserThirdDto> findById(Long id) {
        return Res.ok(userThirdQueryService.findById(id));
    }

    @IgnoreAuth
    @Operation(summary = "获取绑定详情")
    @GetMapping("/getThirdBindInfo")
    public ResResult<UserThirdBindInfo> getThirdBindInfo() {
        return Res.ok(userThirdQueryService.getThirdBindInfo());
    }

    @IgnoreAuth
    @Operation(summary = "绑定第三方账号")
    @PostMapping("/bind")
    public ResResult<Void> bind(@RequestBody UserBindThirdParam param) {
        ValidationUtil.validateParam(param);
        userThirdBindService.bind(param.getAuthCode(), param.getLoginType(), param.getState());
        return Res.ok();
    }

    @IgnoreAuth
    @Operation(summary = "解绑第三方账号")
    @PostMapping("/unbind")
    public ResResult<Void> unbind(@NotBlank(message = "终端代码不可为空") String clientCode) {
        userThirdBindService.unbind(clientCode);
        return Res.ok();
    }

}
