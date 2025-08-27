package org.dromara.daxpay.channel.wechat.controller.isv;

import cn.bootx.platform.core.annotation.ClientCode;
import cn.bootx.platform.core.annotation.OperateLog;
import cn.bootx.platform.core.annotation.RequestGroup;
import cn.bootx.platform.core.annotation.RequestPath;
import cn.bootx.platform.core.rest.Res;
import cn.bootx.platform.core.rest.result.Result;
import cn.bootx.platform.core.util.ValidationUtil;
import org.dromara.daxpay.channel.wechat.param.config.WechatIsvConfigParam;
import org.dromara.daxpay.channel.wechat.result.config.WechatIsvConfigResult;
import org.dromara.daxpay.channel.wechat.service.isv.WechatIsvConfigService;
import org.dromara.daxpay.service.common.code.DaxPayCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 微信支付配置
 * @author xxm
 * @since 2021/3/19
 */
@ClientCode({DaxPayCode.Client.ADMIN})
@Validated
@Tag(name = "微信服务商配置")
@RestController
@RequestMapping("/isv/wechat/config")
@RequestGroup(groupCode = "WechatIsvConfig", groupName = "微信服务商配置", moduleCode = "wechatPay", moduleName = "(DaxPay通道)微信支付")
@AllArgsConstructor
public class WechatIsvConfigController {

    private final WechatIsvConfigService wechatPayConfigService;

    @RequestPath("获取配置")
    @Operation(summary = "获取配置")
    @GetMapping("/findById")
    public Result<WechatIsvConfigResult> findById(@NotNull(message = "ID不可为空") Long id) {
        return Res.ok(wechatPayConfigService.findById(id));
    }

    @RequestPath("新增或更新")
    @Operation(summary = "新增或更新")
    @PostMapping("/saveOrUpdate")
    @OperateLog(title = "新增或更新微信支付配置", businessType = OperateLog.BusinessType.UPDATE, saveParam = true)
    public Result<Void> saveOrUpdate(@RequestBody @Validated WechatIsvConfigParam param) {
        ValidationUtil.validateParam(param);
        wechatPayConfigService.saveOrUpdate(param);
        return Res.ok();
    }

}
