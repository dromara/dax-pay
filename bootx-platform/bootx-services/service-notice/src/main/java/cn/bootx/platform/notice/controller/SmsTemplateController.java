package cn.bootx.platform.notice.controller;

import cn.bootx.platform.common.core.rest.PageResult;
import cn.bootx.platform.common.core.rest.Res;
import cn.bootx.platform.common.core.rest.ResResult;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.notice.core.sms.service.SmsTemplateService;
import cn.bootx.platform.notice.dto.sms.SmsTemplateDto;
import cn.bootx.platform.notice.param.sms.SmsTemplateParam;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 短信模板配置
 * @author xxm
 * @since 2023-08-03
 */
@Tag(name ="短信模板配置")
@RestController
@RequestMapping("/sms/template")
@RequiredArgsConstructor
public class SmsTemplateController {
    private final SmsTemplateService smsTemplateService;

    @Operation( summary = "添加")
    @PostMapping(value = "/add")
    public ResResult<Void> add(@RequestBody SmsTemplateParam param){
        smsTemplateService.add(param);
        return Res.ok();
    }

    @Operation( summary = "修改")
    @PostMapping(value = "/update")
    public ResResult<Void> update(@RequestBody SmsTemplateParam param){
        smsTemplateService.update(param);
        return Res.ok();
    }

    @Operation( summary = "删除")
    @DeleteMapping(value = "/delete")
    public ResResult<Void> delete(Long id){
        smsTemplateService.delete(id);
        return Res.ok();
    }

    @Operation( summary = "通过ID查询")
    @GetMapping(value = "/findById")
    public ResResult<SmsTemplateDto> findById(Long id){
        return Res.ok(smsTemplateService.findById(id));
    }

    @Operation( summary = "查询所有")
    @GetMapping(value = "/findAll")
    public ResResult<List<SmsTemplateDto>> findAll(){
        return Res.ok(smsTemplateService.findAll());
    }

    @Operation( summary = "分页查询")
    @GetMapping(value = "/page")
    public ResResult<PageResult<SmsTemplateDto>> page(PageParam pageParam, SmsTemplateParam smsTemplateParam){
        return Res.ok(smsTemplateService.page(pageParam,smsTemplateParam));
    }
}
