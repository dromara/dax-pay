package cn.daxpay.open.payment.admin.controller.wx;

import cn.daxpay.open.payment.wx.param.channel.WxChannelAppCapabilityBatchParam;
import cn.daxpay.open.payment.wx.result.channel.WxChannelAppCapabilityResult;
import cn.daxpay.open.payment.wx.service.channel.WxChannelAppCapabilityService;
import cn.daxpay.open.platform.core.annotation.PermCode;
import cn.daxpay.open.platform.core.code.PermCodes;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/// # 通道商户微信应用能力绑定（运营端）
///
@PermCode(menuCode = PermCodes.Payment.Wx.MchApp.MENU)
@Validated
@Tag(name = "通道商户微信应用能力绑定")
@RestController
@RequestMapping("/admin/wx/channel-app-capability")
@RequiredArgsConstructor
public class WxChannelAppCapabilityController {

    private final WxChannelAppCapabilityService wxChannelAppCapabilityService;

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "按通道商户号查询能力绑定")
    @GetMapping("/list-by-channel-mch-no")
    public Result<List<WxChannelAppCapabilityResult>> listByChannelMchNo(
            @NotBlank(message = "{validation.field.channelMchNo.notBlank}") String channelMchNo) {
        return Res.ok(wxChannelAppCapabilityService.listByChannelMchNo(channelMchNo));
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "全量保存能力绑定")
    @PostMapping("/save-batch")
    public Result<Void> saveBatch(@RequestBody @Validated WxChannelAppCapabilityBatchParam param) {
        wxChannelAppCapabilityService.saveBatch(param.getMchNo(), param.getChannelMchNo(), param.getItems());
        return Res.ok();
    }
}
