package cn.daxpay.single.admin.controller.extra;

import cn.bootx.platform.common.core.annotation.IgnoreAuth;
import cn.bootx.platform.common.core.rest.Res;
import cn.bootx.platform.common.core.rest.ResResult;
import cn.daxpay.single.service.core.extra.WeChatOpenIdService;
import cn.daxpay.single.service.dto.extra.WeChatAuthUrlResult;
import cn.daxpay.single.service.dto.extra.WeChatOpenIdResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author xxm
 * @since 2024/6/15
 */
@IgnoreAuth
@Tag(name = "微信OpenId服务类")
@RestController
@RequestMapping("/wechat/openId/")
@RequiredArgsConstructor
public class WechatOpenIdController {
    private final WeChatOpenIdService wechatOpenIdService;

    @Operation(summary = "返回获取OpenId授权页面地址和标识码")
    @PostMapping("/generateAuthUrl")
    public ResResult<WeChatAuthUrlResult> generateAuthUrl(){
        return Res.ok(wechatOpenIdService.generateAuthUrl());
    }


    @Operation(summary = "根据标识码查询OpenId")
    @GetMapping("/queryOpenId")
    public ResResult<WeChatOpenIdResult> queryOpenId(String code){
        return Res.ok(wechatOpenIdService.queryOpenId(code));
    }
}
