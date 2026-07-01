package cn.daxpay.open.channel.wechat.controller.isv;

import cn.daxpay.open.channel.wechat.param.isv.WechatIsvAppCapabilityBatchParam;
import cn.daxpay.open.channel.wechat.result.WechatCapabilityOption;
import cn.daxpay.open.channel.wechat.result.isv.WechatIsvAppCapabilityResult;
import cn.daxpay.open.channel.wechat.service.isv.WechatIsvAppCapabilityService;
import cn.daxpay.open.platform.core.annotation.PermCode;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/// # 微信服务商应用支付能力关联管理
///
/// 提供全局维度下「支付能力 → 服务商应用」绑定关系的查询、批量保存及能力候选查询。
///
@PermCode(menuCode = "payment:wechat:isv")
@Validated
@Tag(name = "微信服务商应用支付能力关联管理")
@RestController
@RequestMapping("/admin/wechat/isv-app/capability")
@RequiredArgsConstructor
public class WechatIsvAppCapabilityController {

    private final WechatIsvAppCapabilityService wechatIsvAppCapabilityService;

    @PermCode(code = "view", nameCn = "微信服务商查看", nameEn = "WeChat ISV View")
    @Operation(summary = "查询能力应用关联列表")
    @GetMapping("/list-all")
    public Result<List<WechatIsvAppCapabilityResult>> listAll() {
        return Res.ok(wechatIsvAppCapabilityService.listAll());
    }

    @PermCode(code = "manage", nameCn = "微信服务商管理", nameEn = "WeChat ISV Manage")
    @Operation(summary = "全量保存能力应用关联")
    @PostMapping("/save-batch")
    public Result<Void> saveBatch(@RequestBody @Validated WechatIsvAppCapabilityBatchParam param) {
        wechatIsvAppCapabilityService.saveBatch(param);
        return Res.ok();
    }

    @PermCode(code = "view", nameCn = "微信服务商查看", nameEn = "WeChat ISV View")
    @Operation(summary = "查询微信服务商支持的支付能力候选")
    @GetMapping("/list-supported-capabilities")
    public Result<List<WechatCapabilityOption>> listSupportedCapabilities() {
        return Res.ok(wechatIsvAppCapabilityService.listSupportedCapabilities());
    }
}
