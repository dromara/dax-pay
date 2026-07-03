package cn.daxpay.open.payment.web.merchant.controller.miniapp;

import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.dto.LabelValue;
import cn.daxpay.open.platform.core.rest.result.Result;
import cn.daxpay.open.payment.merchant.result.appinfo.MchAppInfoResult;
import cn.daxpay.open.payment.merchant.result.config.ChannelConfigResult;
import cn.daxpay.open.payment.merchant.result.info.MerchantInfoResult;
import cn.daxpay.open.payment.merchant.service.miniapp.MiniMchBasicService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/// # 小程序基础接口
///
@Validated
@Tag(name = "小程序基础接口")
@RestController
@RequestMapping("/mini/mch")
@RequiredArgsConstructor
public class MiniMchBasicController {
    private final MiniMchBasicService miniAppBasicService;

    @Operation(summary = "查询商户信息")
    @GetMapping("/find-mch-info")
    public Result<MerchantInfoResult> findMchInfo(){
        return Res.ok(miniAppBasicService.findMchInfo());
    }

    @Operation(summary = "所有商户应用下拉列表")
    @GetMapping("/dropdown-mch-app")
    public Result<List<LabelValue>> dropdownMchApp(){
        return Res.ok(miniAppBasicService.dropdownMchApp());
    }

    @Operation(summary = "查询默认应用")
    @GetMapping("/find-default-mch-app")
    public Result<MchAppInfoResult> findDefaultMchApp(){
        return Res.ok(miniAppBasicService.findDefaultMchApp());
    }

    @Operation(summary = "根据应用ID查询通道配置列表")
    @GetMapping("/all-config-by-app-id")
    public Result<List<ChannelConfigResult>> findAllConfigByAppId(@NotNull(message = "{validation.field.appId.notBlank}")  String appId){
        return Res.ok(miniAppBasicService.findAllConfigByAppId(appId));
    }
}
