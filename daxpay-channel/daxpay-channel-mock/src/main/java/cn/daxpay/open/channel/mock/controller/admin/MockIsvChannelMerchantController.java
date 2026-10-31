package cn.daxpay.open.channel.mock.controller.admin;

import cn.daxpay.open.channel.mock.param.isv.MockIsvChannelMerchantCreateParam;
import cn.daxpay.open.channel.mock.result.isv.MockIsvChannelMerchantResult;
import cn.daxpay.open.channel.mock.service.isv.MockIsvChannelMerchantService;
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

/// # Mock 模拟服务商通道商户管理
///
/// 服务商形态通道商户(mock_isv 产品)建档入口(双表建档: 通用主表 + 服务商归属绑定表),
/// 权限用法与 [MockChannelMerchantController] 同款(复用通道商户通用菜单 + 动作码)。
/// 随 [cn.daxpay.open.channel.mock.MockChannelApp] 仅 DEV 装配, PROD 下端点不存在。
@PermCode(menuCode = PermCodes.Channel.Merchant.MENU)
@Validated
@Tag(name = "Mock模拟服务商通道商户管理")
@RestController
@RequestMapping("/admin/mock/isv-channel-merchant")
@RequiredArgsConstructor
public class MockIsvChannelMerchantController {

    private final MockIsvChannelMerchantService mockIsvChannelMerchantService;

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "创建Mock模拟服务商通道商户")
    @PostMapping("/create")
    public Result<Void> create(@RequestBody @Validated MockIsvChannelMerchantCreateParam param) {
        mockIsvChannelMerchantService.create(param);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "查询服务商通道商户绑定(详情工作台基本信息展示, 带出归属服务商)")
    @GetMapping("/find")
    public Result<MockIsvChannelMerchantResult> find(
            @NotBlank(message = "{validation.field.channelMchNo.notBlank}") String channelMchNo) {
        return Res.ok(mockIsvChannelMerchantService.findByChannelMchNo(channelMchNo));
    }
}
