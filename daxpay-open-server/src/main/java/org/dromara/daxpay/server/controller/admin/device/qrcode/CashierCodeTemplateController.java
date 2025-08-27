package org.dromara.daxpay.server.controller.admin.device.qrcode;

import cn.bootx.platform.core.annotation.ClientCode;
import cn.bootx.platform.core.annotation.RequestGroup;
import cn.bootx.platform.core.annotation.RequestPath;
import cn.bootx.platform.core.rest.Res;
import cn.bootx.platform.core.rest.param.PageParam;
import cn.bootx.platform.core.rest.result.PageResult;
import cn.bootx.platform.core.rest.result.Result;
import org.dromara.daxpay.service.common.code.DaxPayCode;
import org.dromara.daxpay.service.device.param.qrcode.template.CashierCodeTemplateParam;
import org.dromara.daxpay.service.device.param.qrcode.template.CashierCodeTemplateQuery;
import org.dromara.daxpay.service.device.result.qrcode.template.CashierCodeTemplateResult;
import org.dromara.daxpay.service.device.service.qrcode.CashierCodeTemplateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@ClientCode(DaxPayCode.Client.ADMIN)
@Tag(name = "收银码牌模板")
@RestController
@RequestMapping("/admin/cashier/code/template")
@RequestGroup(groupCode = "CashierCodeTemplate", groupName = "收银码牌模板", moduleCode = "device")
@RequiredArgsConstructor
public class CashierCodeTemplateController {
    private final CashierCodeTemplateService cashierCodeTemplateService;

    @RequestPath("分页查询")
    @Operation(summary = "分页查询")
    @GetMapping("/page")
    public Result<PageResult<CashierCodeTemplateResult>> page(PageParam pageParam, CashierCodeTemplateQuery query){
        return Res.ok(cashierCodeTemplateService.page(pageParam, query));
    }

    @RequestPath("查询详情")
    @Operation(summary = "查询详情")
    @GetMapping("/findById")
    public Result<CashierCodeTemplateResult> findById(@NotNull(message = "id不能为空") Long id){
        return Res.ok(cashierCodeTemplateService.findById(id));
    }

    @RequestPath("生成预览图片")
    @Operation(summary = "生成预览图片")
    @PostMapping("/generatePreviewImg")
    public Result<String> generatePreviewImg(@Validated CashierCodeTemplateParam param){
        return Res.ok(cashierCodeTemplateService.generatePreviewImg(param));
    }

    @RequestPath("创建")
    @Operation(summary = "创建")
    @PostMapping("/create")
    public Result<Void> create(@Validated CashierCodeTemplateParam param){
        cashierCodeTemplateService.create(param);
        return Res.ok();
    }

    @RequestPath("更新")
    @Operation(summary = "更新")
    @PostMapping("/update")
    public Result<Void> update(@Validated CashierCodeTemplateParam param){
        cashierCodeTemplateService.update(param);
        return Res.ok();
    }

}
