package cn.daxpay.open.channel.douyin.controller.appadmin;

import cn.daxpay.open.channel.douyin.param.direct.DouyinDirectChannelMerchantCreateParam;
import cn.daxpay.open.channel.douyin.result.direct.DouyinDirectChannelMerchantResult;
import cn.daxpay.open.channel.douyin.result.direct.DouyinTransferSceneOptionResult;
import cn.daxpay.open.channel.douyin.service.direct.DouyinDirectChannelMerchantService;
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

/// # 抖音直连通道商户管理(小程序管理端镜像)
///
/// 对应 admin 版 [DouyinDirectChannelMerchantController], 复用同一 Service 与权限码;
/// 密钥配置相关端点不提供, 移动端引导到 Web 端操作。
@PermCode(menuCode = PermCodes.Channel.Merchant.MENU)
@Validated
@Tag(name = "小程序管理端-抖音直连通道商户管理")
@RestController
@RequestMapping("/app-admin/douyin/direct/channel-merchant")
@RequiredArgsConstructor
public class AppAdminDouyinDirectChannelMerchantController {

    private final DouyinDirectChannelMerchantService douyinDirectChannelMerchantService;

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "根据通道商户号查询抖音直连通道商户配置")
    @GetMapping("/find-by-channel-mch-no")
    public Result<DouyinDirectChannelMerchantResult> findByChannelMchNo(
            @NotBlank(message = "{validation.field.channelMerchantNo.notBlank}") String channelMchNo) {
        return Res.ok(douyinDirectChannelMerchantService.findByChannelMchNo(channelMchNo));
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "创建抖音直连通道商户")
    @PostMapping("/create")
    public Result<Void> create(@RequestBody @Validated DouyinDirectChannelMerchantCreateParam param) {
        douyinDirectChannelMerchantService.create(param);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "查询抖音转账场景选项列表(主数据枚举)")
    @GetMapping("/scene-options")
    public Result<List<DouyinTransferSceneOptionResult>> sceneOptions() {
        return Res.ok(douyinDirectChannelMerchantService.findSceneOptions());
    }
}
