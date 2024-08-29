package cn.daxpay.multi.merchant.controller.merchant;

import cn.bootx.platform.core.annotation.RequestGroup;
import cn.bootx.platform.core.annotation.RequestPath;
import cn.bootx.platform.core.rest.Res;
import cn.bootx.platform.core.rest.dto.LabelValue;
import cn.bootx.platform.core.rest.result.Result;
import cn.bootx.platform.core.validation.ValidationGroup;
import cn.daxpay.multi.merchant.service.merchant.MerchantInfoService;
import cn.daxpay.multi.service.param.merchant.MerchantParam;
import cn.daxpay.multi.service.result.merchant.MerchantResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 商户控制器
 * @author xxm
 * @since 2024/6/25
 */
@Validated
@Tag(name = "商户控制器")
@RestController
@RequestMapping("/merchant")
@RequestGroup(groupCode = "merchant", moduleCode = "PayConfig")
@RequiredArgsConstructor
public class MerchantController {
    private final MerchantInfoService merchantInfoService;

    @RequestPath("商户下拉列表")
    @Operation(summary = "商户下拉列表")
    @GetMapping("/dropdown")
    public Result<List<LabelValue>> dropdown(){
        return Res.ok(merchantInfoService.dropdown());
    }

    @RequestPath("获取商户信息")
    @Operation(summary = "获取商户信息")
    @GetMapping("/get")
    public Result<MerchantResult> getMerchant(){
        return Res.ok(merchantInfoService.getMerchant());
    }

    @RequestPath("修改商户信息")
    @Operation(summary = "修改商户")
    @PostMapping("/update")
    public Result<Void> update(@RequestBody @Validated(ValidationGroup.edit.class) MerchantParam param){
        merchantInfoService.update(param);
        return Res.ok();
    }

}
