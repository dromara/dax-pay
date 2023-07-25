package cn.bootx.platform.daxpay.controller;

import cn.bootx.platform.common.core.rest.PageResult;
import cn.bootx.platform.common.core.rest.Res;
import cn.bootx.platform.common.core.rest.ResResult;
import cn.bootx.platform.common.core.rest.dto.LabelValue;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.daxpay.core.merchant.service.MchAppPayConfigService;
import cn.bootx.platform.daxpay.core.merchant.service.MchAppService;
import cn.bootx.platform.daxpay.dto.merchant.MchAppPayConfigResult;
import cn.bootx.platform.daxpay.dto.merchant.MchApplicationDto;
import cn.bootx.platform.daxpay.param.merchant.MchApplicationParam;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 商户应用
 *
 * @author xxm
 * @since 2023-05-23
 */
@Tag(name = "商户应用")
@RestController
@RequestMapping("/mch/app")
@RequiredArgsConstructor
public class MchApplicationController {

    private final MchAppService applicationService;

    private final MchAppPayConfigService appPayConfigService;

    @Operation(summary = "添加")
    @PostMapping(value = "/add")
    public ResResult<Void> add(@RequestBody MchApplicationParam param) {
        applicationService.add(param);
        return Res.ok();
    }

    @Operation(summary = "修改")
    @PostMapping(value = "/update")
    public ResResult<Void> update(@RequestBody MchApplicationParam param) {
        applicationService.update(param);
        return Res.ok();
    }

    @Operation(summary = "删除")
    @DeleteMapping(value = "/delete")
    public ResResult<Void> delete(Long id) {
        applicationService.delete(id);
        return Res.ok();
    }

    @Operation(summary = "通过ID查询")
    @GetMapping(value = "/findById")
    public ResResult<MchApplicationDto> findById(Long id) {
        return Res.ok(applicationService.findById(id));
    }

    @Operation(summary = "下拉列表")
    @GetMapping("/dropdown")
    public ResResult<List<LabelValue>> dropdown(String mchCode) {
        return Res.ok(applicationService.dropdown(mchCode));
    }

    @Operation(summary = "查询所有")
    @GetMapping(value = "/findAll")
    public ResResult<List<MchApplicationDto>> findAll() {
        return Res.ok(applicationService.findAll());
    }

    @Operation(summary = "分页查询")
    @GetMapping(value = "/page")
    public ResResult<PageResult<MchApplicationDto>> page(PageParam pageParam, MchApplicationParam mchApplicationParam) {
        return Res.ok(applicationService.page(pageParam, mchApplicationParam));
    }

    @Operation(summary = "关联支付配置列表")
    @GetMapping("/findAllConfig")
    public ResResult<List<MchAppPayConfigResult>> findAllConfig(String appCode) {
        return Res.ok(appPayConfigService.ListByAppId(appCode));
    }

}
