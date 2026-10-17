package cn.daxpay.open.channel.ysep.controller.admin;

import cn.daxpay.open.channel.ysep.param.YsepKeyConfigParam;
import cn.daxpay.open.channel.ysep.result.YsepKeyConfigResult;
import cn.daxpay.open.channel.ysep.service.config.YsepKeyConfigService;
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

import java.util.Objects;

/// # 银盛通道商户密钥配置
///
/// 提供通道商户维度的银盛直连密钥配置管理(一通道两产品共用一张表, 行内 product 表达归属)。
/// 银盛无沙箱环境, 每通道商户仅一条配置(无环境维度参数)。
@PermCode(menuCode = PermCodes.Channel.Merchant.MENU)
@Validated
@Tag(name = "银盛通道商户密钥配置")
@RestController
@RequestMapping("/admin/ysep/key-config")
@RequiredArgsConstructor
public class YsepKeyConfigController {

    private final YsepKeyConfigService ysepKeyConfigService;

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "根据通道商户号查询银盛密钥配置")
    @GetMapping("/find-config")
    public Result<YsepKeyConfigResult> findConfig(
            @NotBlank(message = "{validation.field.channelMerchantNo.notBlank}") String channelMchNo) {
        var config = ysepKeyConfigService.findByChannelMchNo(channelMchNo);
        var result = config.toResult();
        result.setMerchantCertPfxConfigured(Objects.nonNull(config.getMerchantCertPfx()));
        result.setMerchantCertPasswordConfigured(Objects.nonNull(config.getMerchantCertPassword()));
        result.setYsepayPublicCertConfigured(Objects.nonNull(config.getYsepayPublicCert()));
        return Res.ok(result);
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "保存银盛密钥配置")
    @PostMapping("/save-config")
    public Result<Void> saveConfig(@RequestBody @Validated YsepKeyConfigParam param) {
        ysepKeyConfigService.save(param);
        return Res.ok();
    }
}
