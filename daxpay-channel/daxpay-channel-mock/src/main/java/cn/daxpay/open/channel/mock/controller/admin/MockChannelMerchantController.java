package cn.daxpay.open.channel.mock.controller.admin;

import cn.daxpay.open.channel.mock.param.MockChannelMerchantCreateParam;
import cn.daxpay.open.channel.mock.service.merchant.MockChannelMerchantService;
import cn.daxpay.open.platform.core.annotation.PermCode;
import cn.daxpay.open.platform.core.code.PermCodes;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/// # Mock 模拟通道商户管理
///
/// 提供模拟通道商户创建(纯本地登记: 商户档案在模拟通道控制台创建, 此处登记其通道商户号),
/// 不调 mock 子应用(独立通道, 无对接接口); 直连商户级接入密钥由密钥端点后置维护。
/// 列表/详情统一走通用通道商户链路(全局列表 + 详情工作台), 模块内不再单独提供分页查询。
/// 复用通道商户通用权限码(菜单 channel:merchant + 动作 manage)。
/// 随 [cn.daxpay.open.channel.mock.MockChannelApp] 仅 DEV 装配, PROD 下端点不存在。
@PermCode(menuCode = PermCodes.Channel.Merchant.MENU)
@Validated
@Tag(name = "Mock模拟通道商户管理")
@RestController
@RequestMapping("/admin/mock/channel-merchant")
@RequiredArgsConstructor
public class MockChannelMerchantController {

    private final MockChannelMerchantService mockChannelMerchantService;

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "创建Mock模拟通道商户")
    @PostMapping("/create")
    public Result<Void> create(@RequestBody @Validated MockChannelMerchantCreateParam param) {
        mockChannelMerchantService.create(param);
        return Res.ok();
    }
}
