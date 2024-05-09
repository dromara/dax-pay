package cn.bootx.platform.baseapi.controller;

import cn.bootx.platform.baseapi.core.template.service.GeneralTemplateService;
import cn.bootx.platform.baseapi.dto.template.GeneralTemplateDto;
import cn.bootx.platform.baseapi.param.template.GeneralTemplateParam;
import cn.bootx.platform.common.core.rest.PageResult;
import cn.bootx.platform.common.core.rest.Res;
import cn.bootx.platform.common.core.rest.ResResult;
import cn.bootx.platform.common.core.rest.param.PageParam;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 通用模板管理
 * @author xxm
 * @since 2023-08-12
 */
@Tag(name ="通用模板管理")
@RestController
@RequestMapping("/general/template")
@RequiredArgsConstructor
public class GeneralTemplateController {
    private final GeneralTemplateService generalTemplateService;

    @Operation( summary = "添加")
    @PostMapping(value = "/add")
    public ResResult<Void> add(@RequestBody GeneralTemplateParam param){
        generalTemplateService.add(param);
        return Res.ok();
    }

    @Operation( summary = "修改")
    @PostMapping(value = "/update")
    public ResResult<Void> update(@RequestBody GeneralTemplateParam param){
        generalTemplateService.update(param);
        return Res.ok();
    }

    @Operation( summary = "删除")
    @DeleteMapping(value = "/delete")
    public ResResult<Void> delete(Long id){
        generalTemplateService.delete(id);
        return Res.ok();
    }

    @Operation(summary = "批量删除")
    @DeleteMapping("/deleteBatch")
    public ResResult<Void> deleteBatch(@RequestBody List<Long> ids) {
        generalTemplateService.deleteBatch(ids);
        return Res.ok();
    }

    @Operation( summary = "通过ID查询")
    @GetMapping(value = "/findById")
    public ResResult<GeneralTemplateDto> findById(Long id){
        return Res.ok(generalTemplateService.findById(id));
    }

    @Operation( summary = "通过Code查询")
    @GetMapping(value = "/findByCode")
    public ResResult<GeneralTemplateDto> findByCode(String code){
        return Res.ok(generalTemplateService.findByCode(code));
    }


    @Operation(summary = "编码是否被使用")
    @GetMapping("/existsByCode")
    public ResResult<Boolean> existsByCode(String code) {
        return Res.ok(generalTemplateService.existsByCode(code));
    }

    @Operation(summary = "编码是否被使用(不包含自己)")
    @GetMapping("/existsByCodeNotId")
    public ResResult<Boolean> existsByCode(String code, Long id) {
        return Res.ok(generalTemplateService.existsByCode(code, id));
    }


    @Operation( summary = "查询所有")
    @GetMapping(value = "/findAll")
    public ResResult<List<GeneralTemplateDto>> findAll(){
        return Res.ok(generalTemplateService.findAll());
    }

    @Operation( summary = "分页查询")
    @GetMapping(value = "/page")
    public ResResult<PageResult<GeneralTemplateDto>> page(PageParam pageParam, GeneralTemplateParam query){
        return Res.ok(generalTemplateService.page(pageParam,query));
    }
}
