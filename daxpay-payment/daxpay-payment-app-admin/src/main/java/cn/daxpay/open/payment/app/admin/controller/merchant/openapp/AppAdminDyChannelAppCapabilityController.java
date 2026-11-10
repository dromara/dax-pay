package cn.daxpay.open.payment.app.admin.controller.merchant.openapp;

import cn.daxpay.open.payment.douyin.param.channel.DyChannelAppCapabilityBatchParam;
import cn.daxpay.open.payment.douyin.result.channel.DyChannelAppCapabilityResult;
import cn.daxpay.open.payment.douyin.service.channel.DyChannelAppCapabilityService;
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

import java.util.List;

/// # 小程序管理端-通道商户抖音应用能力绑定
///
/// 镜像自 admin 版 `DyChannelAppCapabilityController`(路径 /admin/douyin/channel-app-capability),
/// 端点语义与权限码完全一致, 供小程序端商户支付应用(抖音)的通道能力绑定配置使用;
/// 与微信侧 [AppAdminWxChannelAppCapabilityController] 同构。
@PermCode(menuCode = PermCodes.Payment.Douyin.MchApp.MENU)
@Validated
@Tag(name = "小程序管理端-通道商户抖音应用能力绑定")
@RestController
@RequestMapping("/app-admin/douyin/channel-app-capability")
@RequiredArgsConstructor
public class AppAdminDyChannelAppCapabilityController {

    private final DyChannelAppCapabilityService dyChannelAppCapabilityService;

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "按通道商户号查询能力绑定")
    @GetMapping("/list-by-channel-mch-no")
    public Result<List<DyChannelAppCapabilityResult>> listByChannelMchNo(
            @NotBlank(message = "{validation.field.channelMchNo.notBlank}") String channelMchNo) {
        return Res.ok(dyChannelAppCapabilityService.listByChannelMchNo(channelMchNo));
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "全量保存能力绑定")
    @PostMapping("/save-batch")
    public Result<Void> saveBatch(@RequestBody @Validated DyChannelAppCapabilityBatchParam param) {
        dyChannelAppCapabilityService.saveBatch(param.getMchNo(), param.getChannelMchNo(), param.getItems());
        return Res.ok();
    }
}
