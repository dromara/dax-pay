package cn.daxpay.open.channel.leshua.controller.isv;

import cn.daxpay.open.channel.leshua.convert.isv.LeshuaIsvKeyConfigConvert;
import cn.daxpay.open.channel.leshua.param.isv.LeshuaIsvKeyConfigParam;
import cn.daxpay.open.channel.leshua.result.isv.LeshuaIsvKeyConfigResult;
import cn.daxpay.open.channel.leshua.service.isv.LeshuaIsvKeyConfigService;
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

/// # 乐刷服务商密钥配置
///
@PermCode(menuCode = "payment:leshua:isv")
@Validated
@Tag(name = "乐刷服务商密钥配置")
@RestController
@RequestMapping("/admin/leshua/isv-key-config")
@RequiredArgsConstructor
public class LeshuaIsvKeyConfigController {

    private final LeshuaIsvKeyConfigService leshuaIsvKeyConfigService;

    @PermCode(code = "view", nameCn = "乐刷服务商查看", nameEn = "Leshua ISV View")
    @Operation(summary = "查询乐刷服务商密钥配置")
    @GetMapping("/find-config")
    public Result<LeshuaIsvKeyConfigResult> findConfig(
            @NotBlank(message = "{validation.field.product.notBlank}") String product,
            boolean sandbox) {
        return Res.ok(LeshuaIsvKeyConfigConvert.CONVERT.toResult(leshuaIsvKeyConfigService.findByProduct(product, sandbox)));
    }

    @PermCode(code = "manage", nameCn = "乐刷服务商管理", nameEn = "Leshua ISV Manage")
    @Operation(summary = "保存乐刷服务商密钥配置")
    @PostMapping("/save-config")
    public Result<Void> saveConfig(@RequestBody @Validated LeshuaIsvKeyConfigParam param) {
        leshuaIsvKeyConfigService.saveConfig(param);
        return Res.ok();
    }
}
