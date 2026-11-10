package cn.daxpay.open.channel.mock.controller.admin;

import cn.daxpay.open.channel.mock.result.isv.MockIsvInfoResult;
import cn.daxpay.open.channel.mock.service.isv.MockIsvInfoService;
import cn.daxpay.open.platform.core.annotation.PermCode;
import cn.daxpay.open.platform.core.code.PermCodes;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/// # Mock 模拟服务商信息管理(全局唯一)
///
/// 多服务商模式取消后仅保留单例服务商的查询、编号编辑与支付开关:
/// 服务商编号可运营端修改(须与 mock 网关控制台商户档案 isv_no 一致), 名称固定;
/// 建档由 Service 内部 get-or-create 完成, 运营端不提供新增/删除入口。
/// 权限用法与 [MockChannelMerchantController] 同款(复用通道商户通用菜单 + 动作码)。
/// 随 [cn.daxpay.open.channel.mock.MockChannelApp] 仅 DEV 装配, PROD 下端点不存在。
@PermCode(menuCode = PermCodes.Channel.Merchant.MENU)
@Tag(name = "Mock模拟服务商信息管理")
@RestController
@RequestMapping("/admin/mock/isv-info")
@RequiredArgsConstructor
@Validated
public class MockIsvInfoController {

    private final MockIsvInfoService mockIsvInfoService;

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "查询当前模拟服务商信息")
    @GetMapping("/current")
    public Result<MockIsvInfoResult> current() {
        return Res.ok(mockIsvInfoService.current());
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "修改模拟服务商编号")
    @PostMapping("/update-isv-no")
    public Result<MockIsvInfoResult> updateIsvNo(
            @RequestParam("isvNo")
            @NotBlank(message = "{validation.field.isvNo.notBlank}")
            @Size(max = 32, message = "{validation.field.isvNo.size}") String isvNo) {
        return Res.ok(mockIsvInfoService.updateIsvNo(isvNo));
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "切换模拟服务商支付开关")
    @PostMapping("/switch-pay")
    public Result<Void> switchPay(
            @RequestParam("payEnable") @NotNull(message = "{validation.field.enable.notNull}") Boolean payEnable) {
        mockIsvInfoService.switchPay(payEnable);
        return Res.ok();
    }
}
