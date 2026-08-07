package cn.daxpay.open.channel.wechat.controller.callback;

import cn.daxpay.open.channel.wechat.result.direct.WechatTransferConfirmResult;
import cn.daxpay.open.channel.wechat.service.payment.transfer.WechatTransferConfirmQueryService;
import cn.daxpay.open.platform.core.annotation.IgnoreAuth;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/// # 微信转账确认收款(公开接口)
///
/// C 端收款人(无登录态)凭 transferNo 查询确认收款信息, 用于在微信内拉起收款确认页。
/// 与回调控制器同为 @IgnoreAuth, 不走 Sa-Token 认证。
@IgnoreAuth
@Validated
@Tag(name = "微信转账确认收款(公开)")
@RestController
@RequestMapping("/unipay/transfer/wechat")
@RequiredArgsConstructor
public class WechatTransferConfirmController {

    private final WechatTransferConfirmQueryService wechatTransferConfirmQueryService;

    @Operation(summary = "查询微信转账确认收款信息")
    @GetMapping("/confirm-info/{transferNo}")
    public Result<WechatTransferConfirmResult> getConfirmInfo(
            @PathVariable @NotBlank(message = "转账单号不可为空") String transferNo) {
        return Res.ok(wechatTransferConfirmQueryService.queryByTransferNo(transferNo));
    }
}
