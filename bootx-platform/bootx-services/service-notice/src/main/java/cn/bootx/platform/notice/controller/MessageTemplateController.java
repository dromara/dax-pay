package cn.bootx.platform.notice.controller;

import cn.bootx.platform.common.core.rest.PageResult;
import cn.bootx.platform.common.core.rest.Res;
import cn.bootx.platform.common.core.rest.ResResult;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.notice.core.template.service.MessageTemplateService;
import cn.bootx.platform.notice.dto.template.MessageTemplateDto;
import cn.bootx.platform.notice.param.template.MessageTemplateParam;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 消息模板
 *
 * @author xxm
 * @since 2021/8/10
 */
@Tag(name = "消息模板")
@RestController
@RequestMapping("/message/template")
@RequiredArgsConstructor
public class MessageTemplateController {

    private final MessageTemplateService messageTemplateService;

    @Operation(summary = "添加")
    @PostMapping("/add")
    public ResResult<MessageTemplateDto> add(@RequestBody MessageTemplateParam param) {
        return Res.ok(messageTemplateService.add(param));
    }

    @Operation(summary = "更新")
    @PostMapping("/update")
    public ResResult<MessageTemplateDto> update(@RequestBody MessageTemplateParam param) {
        return Res.ok(messageTemplateService.update(param));
    }

    @Operation(summary = "分页")
    @GetMapping("/page")
    public ResResult<PageResult<MessageTemplateDto>> page(PageParam pageParam, MessageTemplateParam query) {
        return Res.ok(messageTemplateService.page(pageParam, query));
    }

    @Operation(summary = "获取详情")
    @GetMapping("/findById")
    public ResResult<MessageTemplateDto> findById(Long id) {
        return Res.ok(messageTemplateService.findById(id));
    }

    @Operation(summary = "删除")
    @DeleteMapping("/delete")
    public ResResult<Void> delete(Long id) {
        messageTemplateService.delete(id);
        return Res.ok();
    }

    @Operation(summary = "编码是否被使用")
    @GetMapping("/existsByCode")
    public ResResult<Boolean> existsByCode(String code) {
        return Res.ok(messageTemplateService.existsByCode(code));
    }

    @Operation(summary = "编码是否被使用(不包含自己)")
    @GetMapping("/existsByCodeNotId")
    public ResResult<Boolean> existsByCode(String code, Long id) {
        return Res.ok(messageTemplateService.existsByCode(code, id));
    }

    @Operation(summary = "渲染模板")
    @PostMapping("/rendering")
    public ResResult<String> rendering(@RequestParam String code, @RequestBody Map<String, Object> paramMap) {
        return Res.ok(messageTemplateService.rendering(code, paramMap));
    }

}
