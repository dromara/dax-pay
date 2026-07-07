package cn.daxpay.open.channel.hkrt.controller.isv;

import cn.daxpay.open.channel.hkrt.convert.isv.HkrtIsvKeyConfigConvert;
import cn.daxpay.open.channel.hkrt.param.isv.HkrtIsvKeyConfigParam;
import cn.daxpay.open.channel.hkrt.result.isv.HkrtIsvKeyConfigResult;
import cn.daxpay.open.channel.hkrt.service.isv.HkrtIsvKeyConfigService;
import cn.daxpay.open.platform.core.annotation.PermCode;
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

/// # 海科融通服务商密钥配置
///
@PermCode(menuCode = "payment:hkrt:isv")
@Validated
@Tag(name = "海科融通服务商密钥配置")
@RestController
@RequestMapping("/admin/hkrt/isv-key-config")
@RequiredArgsConstructor
public class HkrtIsvKeyConfigController {

    private final HkrtIsvKeyConfigService hkrtIsvKeyConfigService;

    @PermCode(code = "view", nameCn = "海科融通服务商查看", nameEn = "Hkrt ISV View")
    @Operation(summary = "查询海科融通服务商密钥配置")
    @GetMapping("/find-config")
    public Result<HkrtIsvKeyConfigResult> findConfig(
            @NotBlank(message = "{validation.field.product.notBlank}") String product) {
        return Res.ok(HkrtIsvKeyConfigConvert.CONVERT.toResult(hkrtIsvKeyConfigService.findByProduct(product)));
    }

    @PermCode(code = "manage", nameCn = "海科融通服务商管理", nameEn = "Hkrt ISV Manage")
    @Operation(summary = "保存海科融通服务商密钥配置")
    @PostMapping("/save-config")
    public Result<Void> saveConfig(@RequestBody @Validated HkrtIsvKeyConfigParam param) {
        hkrtIsvKeyConfigService.saveConfig(param);
        return Res.ok();
    }
}
