package cn.bootx.platform.iam.controller;

import cn.bootx.platform.common.core.rest.PageResult;
import cn.bootx.platform.common.core.rest.Res;
import cn.bootx.platform.common.core.rest.ResResult;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.iam.core.online.service.OnlineUserService;
import cn.bootx.platform.iam.dto.online.OnlineUserInfoDto;
import cn.bootx.platform.iam.dto.online.OnlineUserSessionDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 在线用户
 * @author xxm
 * @since 2023/12/4
 */
@Tag(name = "在线用户")
@RestController
@RequestMapping("/online/user")
@RequiredArgsConstructor
public class OnlineUserController {
    private final OnlineUserService onlineUserService;

    @Operation(summary = "分页")
    @GetMapping("/page")
    public ResResult<PageResult<OnlineUserInfoDto>> page(PageParam pageParam){
        return Res.ok(onlineUserService.page(pageParam));
    }

    @Operation(summary = "获取用户链接信息")
    @GetMapping("/getSessionByUserId")
    public ResResult<List<OnlineUserSessionDto>> getSessionByUserId(String userId){
        return Res.ok(onlineUserService.getSessionByUserId(userId));
    }

    @Operation(summary = "登录用户分页")
    @GetMapping("/pageByLogin")
    public ResResult<Void> pageByLogin(PageParam pageParam){
        return Res.ok();
    }

    @Operation(summary = "踢出用户")
    @GetMapping("/kickOut")
    public ResResult<Void> kickOut(String userId){
        return Res.ok();
    }
}
