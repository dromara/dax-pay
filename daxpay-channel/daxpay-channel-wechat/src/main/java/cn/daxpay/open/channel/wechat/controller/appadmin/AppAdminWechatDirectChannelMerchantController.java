package cn.daxpay.open.channel.wechat.controller.appadmin;

import cn.daxpay.open.channel.wechat.param.direct.WechatDirectChannelMerchantCreateParam;
import cn.daxpay.open.channel.wechat.param.direct.WechatDirectChannelMerchantUpdateParam;
import cn.daxpay.open.channel.wechat.result.direct.WechatDirectChannelMerchantResult;
import cn.daxpay.open.channel.wechat.result.direct.WechatTransferSceneOptionResult;
import cn.daxpay.open.channel.wechat.service.direct.WechatDirectChannelMerchantService;
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

/// # 微信直连通道商户管理(小程序管理端镜像)
///
/// 对应 admin 版 [WechatDirectChannelMerchantController], 复用同一 Service 与权限码;
/// 密钥配置相关端点不提供, 移动端引导到 Web 端操作。
@PermCode(menuCode = PermCodes.Channel.Merchant.MENU)
@Validated
@Tag(name = "小程序管理端-微信直连通道商户管理")
@RestController
@RequestMapping("/app-admin/wechat/direct/channel-merchant")
@RequiredArgsConstructor
public class AppAdminWechatDirectChannelMerchantController {

    private final WechatDirectChannelMerchantService wechatDirectChannelMerchantService;

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "根据通道商户号查询微信直连通道商户配置")
    @GetMapping("/find-by-channel-mch-no")
    public Result<WechatDirectChannelMerchantResult> findByChannelMchNo(
            @NotBlank(message = "{validation.field.channelMerchantNo.notBlank}") String channelMchNo) {
        return Res.ok(wechatDirectChannelMerchantService.findByChannelMchNo(channelMchNo));
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "创建微信直连通道商户")
    @PostMapping("/create")
    public Result<Void> create(@RequestBody @Validated WechatDirectChannelMerchantCreateParam param) {
        wechatDirectChannelMerchantService.create(param);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "更新微信直连通道商户(转账场景/微信商户号)")
    @PostMapping("/update")
    public Result<Void> update(@RequestBody @Validated WechatDirectChannelMerchantUpdateParam param) {
        wechatDirectChannelMerchantService.update(param);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "查询微信转账场景选项列表")
    @GetMapping("/scene-options")
    public Result<List<WechatTransferSceneOptionResult>> sceneOptions() {
        return Res.ok(wechatDirectChannelMerchantService.findSceneOptions());
    }
}
