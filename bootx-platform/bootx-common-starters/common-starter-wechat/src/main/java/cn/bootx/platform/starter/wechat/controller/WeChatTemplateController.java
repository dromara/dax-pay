package cn.bootx.platform.starter.wechat.controller;

import cn.bootx.platform.common.core.rest.PageResult;
import cn.bootx.platform.common.core.rest.Res;
import cn.bootx.platform.common.core.rest.ResResult;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.starter.auth.util.SecurityUtil;
import cn.bootx.platform.starter.wechat.core.notice.service.WeChatTemplateService;
import cn.bootx.platform.starter.wechat.dto.notice.WeChatTemplateDto;
import cn.bootx.platform.starter.wechat.param.notice.WeChatTemplateParam;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * @author xxm
 * @since 2022/7/16
 */
@Tag(name = "微信模板消息")
@RestController
@RequestMapping("/wechat/template")
@RequiredArgsConstructor
public class WeChatTemplateController {

    private final WeChatTemplateService weChatTemplateService;

    @Operation(summary = "修改")
    @PostMapping(value = "/update")
    public ResResult<Void> update(@RequestBody WeChatTemplateParam param) {
        weChatTemplateService.update(param);
        return Res.ok();
    }

    @Operation(summary = "通过ID查询")
    @GetMapping(value = "/findById")
    public ResResult<WeChatTemplateDto> findById(Long id) {
        return Res.ok(weChatTemplateService.findById(id));
    }

    @Operation(summary = "分页查询")
    @GetMapping(value = "/page")
    public ResResult<PageResult<WeChatTemplateDto>> page(PageParam pageParam, WeChatTemplateParam weChatTemplateParam) {
        return Res.ok(weChatTemplateService.page(pageParam, weChatTemplateParam));
    }

    @Operation(summary = "编码是否被使用(不包含自己)")
    @GetMapping("/existsByCodeNotId")
    public ResResult<Boolean> existsByCode(String code, Long id) {
        return Res.ok(weChatTemplateService.existsByCode(code, id));
    }

    @Operation(summary = "同步消息模板数据")
    @PostMapping("/sync")
    public ResResult<Void> sync() {
        // 为了获取用户生效, 测试用
        SecurityUtil.getUserId();
        weChatTemplateService.sync();
        return Res.ok();
    }

}
