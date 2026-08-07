package cn.daxpay.open.channel.wechat.controller.direct;

import cn.daxpay.open.channel.wechat.param.direct.WechatTransferConfigParam;
import cn.daxpay.open.channel.wechat.result.direct.WechatTransferConfigResult;
import cn.daxpay.open.channel.wechat.service.direct.WechatTransferConfigService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/// # 微信转账配置管理(运营端)
///
/// 管理通道商户的转账配置(转账场景 + 发起应用), 挂在通道商户菜单下。
///
@PermCode(menuCode = PermCodes.Channel.Merchant.MENU)
@Validated
@Tag(name = "微信转账配置管理")
@RestController
@RequestMapping("/admin/wechat/transfer-config")
@RequiredArgsConstructor
public class WechatTransferConfigController {

    private final WechatTransferConfigService wechatTransferConfigService;

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "查询通道商户的转账配置")
    @GetMapping("/find-by-channel-mch-no")
    public Result<WechatTransferConfigResult> findByChannelMchNo(
            @NotBlank(message = "{validation.field.mchNo.notBlank}") String mchNo,
            @NotBlank(message = "{validation.field.channelMerchantNo.notBlank}") String channelMchNo) {
        return Res.ok(wechatTransferConfigService.findByChannelMchNo(mchNo, channelMchNo));
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "保存或更新转账配置(一对一)")
    @PostMapping("/save")
    public Result<Void> save(@RequestBody @Validated WechatTransferConfigParam param) {
        wechatTransferConfigService.saveOrUpdate(param);
        return Res.ok();
    }
}
