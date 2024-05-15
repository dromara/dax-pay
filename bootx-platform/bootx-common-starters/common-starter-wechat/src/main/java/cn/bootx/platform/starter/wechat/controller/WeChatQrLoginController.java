package cn.bootx.platform.starter.wechat.controller;

import cn.bootx.platform.common.core.annotation.IgnoreAuth;
import cn.bootx.platform.common.core.rest.Res;
import cn.bootx.platform.common.core.rest.ResResult;
import cn.bootx.platform.starter.wechat.core.login.service.WeChatQrLoginService;
import cn.bootx.platform.starter.wechat.dto.login.WeChatLoginQrCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 微信
 *
 * @author xxm
 * @since 2022/8/4
 */
@IgnoreAuth
@Tag(name = "微信扫码登录")
@RestController
@RequestMapping("/token/wechat/qr")
@RequiredArgsConstructor
public class WeChatQrLoginController {

    private final WeChatQrLoginService weChatQrLoginService;

    @Operation(summary = "申请登录用QR码")
    @PostMapping("/applyQrCode")
    public ResResult<WeChatLoginQrCode> applyQrCode() {
         return Res.ok(weChatQrLoginService.applyQrCode());
    }

    @Operation(summary = "获取扫码状态")
    @GetMapping("/getStatus")
    public ResResult<String> getStatus(String qrCodeKey) {
         return Res.ok(weChatQrLoginService.getStatus(qrCodeKey));
    }

}
