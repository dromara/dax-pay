package cn.daxpay.open.channel.vbill.controller.isv;

import cn.daxpay.open.channel.vbill.convert.isv.VbillIsvKeyConfigConvert;
import cn.daxpay.open.channel.vbill.param.isv.VbillIsvKeyConfigParam;
import cn.daxpay.open.channel.vbill.result.isv.VbillIsvKeyConfigResult;
import cn.daxpay.open.channel.vbill.service.isv.VbillIsvKeyConfigService;
import cn.daxpay.open.platform.core.annotation.PermCode;
import cn.daxpay.open.platform.core.code.PermCodes;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/// # 随行付服务商密钥配置
@PermCode(menuCode = PermCodes.Payment.Vbill.MENU)
@Validated
@Tag(name = "随行付服务商密钥配置")
@RestController
@RequestMapping("/admin/vbill/isv-key-config")
@RequiredArgsConstructor
public class VbillIsvKeyConfigController {

    private final VbillIsvKeyConfigService vbillIsvKeyConfigService;

    @PermCode(code = PermCodes.Action.VIEW, nameCn = "随行付服务商查看", nameEn = "VBill ISV View")
    @Operation(summary = "查询随行付服务商密钥配置")
    @GetMapping("/find-config")
    public Result<VbillIsvKeyConfigResult> findConfig(
            @NotBlank(message = "{validation.field.product.notBlank}") String product,
            boolean sandbox) {
        return Res.ok(VbillIsvKeyConfigConvert.CONVERT.toResult(vbillIsvKeyConfigService.findByProduct(product, sandbox)));
    }

    @PermCode(code = PermCodes.Action.MANAGE, nameCn = "随行付服务商管理", nameEn = "VBill ISV Manage")
    @Operation(summary = "保存随行付服务商密钥配置")
    @PostMapping("/save-config")
    public Result<Void> saveConfig(@RequestBody @Validated VbillIsvKeyConfigParam param) {
        vbillIsvKeyConfigService.saveConfig(param);
        return Res.ok();
    }
}
