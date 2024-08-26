package cn.daxpay.multi.merchant.controller.merchant;

import cn.bootx.platform.core.annotation.RequestGroup;
import cn.bootx.platform.core.annotation.RequestPath;
import cn.bootx.platform.core.rest.Res;
import cn.bootx.platform.core.rest.result.Result;
import cn.bootx.platform.core.validation.ValidationGroup;
import cn.daxpay.multi.service.param.merchant.MerchantParam;
import cn.daxpay.multi.service.service.merchant.MerchantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    private final MerchantService merchantService;

    @RequestPath("修改商户信息")
    @Operation(summary = "修改商户")
    @PostMapping("/update")
    public Result<Void> update(@RequestBody @Validated(ValidationGroup.edit.class) MerchantParam param){
        merchantService.update(param);
        return Res.ok();
    }

}
