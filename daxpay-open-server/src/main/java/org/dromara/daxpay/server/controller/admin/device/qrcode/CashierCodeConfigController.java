package org.dromara.daxpay.server.controller.admin.device.qrcode;

import cn.bootx.platform.core.annotation.ClientCode;
import cn.bootx.platform.core.annotation.RequestGroup;
import cn.bootx.platform.core.annotation.RequestPath;
import cn.bootx.platform.core.rest.Res;
import cn.bootx.platform.core.rest.dto.LabelValue;
import cn.bootx.platform.core.rest.param.PageParam;
import cn.bootx.platform.core.rest.result.PageResult;
import cn.bootx.platform.core.rest.result.Result;
import org.dromara.daxpay.service.common.code.DaxPayCode;
import org.dromara.daxpay.service.device.param.qrcode.config.CashierCodeConfigParam;
import org.dromara.daxpay.service.device.param.qrcode.config.CashierCodeConfigQuery;
import org.dromara.daxpay.service.device.param.qrcode.config.CashierCodeSceneConfigParam;
import org.dromara.daxpay.service.device.result.qrcode.config.CashierCodeConfigResult;
import org.dromara.daxpay.service.device.result.qrcode.config.CashierCodeSceneConfigResult;
import org.dromara.daxpay.service.device.service.qrcode.CashierCodeConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 收银码牌配置
 * @author xxm
 * @since 2024/11/20
 */
@Validated
@ClientCode(DaxPayCode.Client.ADMIN)
@Tag(name = "收银码牌配置")
@RestController
@RequestMapping("/admin/cashier/code/config")
@RequestGroup(groupCode = "CashierCodeConfig", groupName = "收银码牌配置", moduleCode = "device")
@RequiredArgsConstructor
public class CashierCodeConfigController {
    private final CashierCodeConfigService codeConfigService;

    @RequestPath("分页查询")
    @GetMapping("/page")
    public Result<PageResult<CashierCodeConfigResult>> page(PageParam pageParam, CashierCodeConfigQuery query) {
        return Res.ok(codeConfigService.page(pageParam, query));
    }

    @RequestPath("根据id查询码牌配置")
    @Operation(summary = "根据id查询码牌配置")
    @GetMapping("/findById")
    public Result<CashierCodeConfigResult> findById(Long id){
        return Res.ok(codeConfigService.findById(id));
    }

    @RequestPath("码牌配置保存")
    @Operation(summary = "码牌配置保存")
    @PostMapping("/save")
    public Result<Void> save(@RequestBody CashierCodeConfigParam param) {
        codeConfigService.save(param);
        return Res.ok();
    }

    @RequestPath("码牌配置更新")
    @Operation(summary = "码牌配置更新")
    @PostMapping("/update")
    public Result<Void> update(@RequestBody CashierCodeConfigParam param) {
        codeConfigService.update(param);
        return Res.ok();
    }

    @RequestPath("码牌配置删除")
    @Operation(summary = "码牌配置删除")
    @PostMapping("/delete")
    public Result<Void> delete(Long id){
        codeConfigService.delete(id);
        return Res.ok();
    }

    @RequestPath("获取码牌支付场景配置列表")
    @Operation(summary = "获取码牌支付场景配置列表")
    @GetMapping("/scene/findAll")
    public Result<List<CashierCodeSceneConfigResult>> findSceneByConfigId(Long configId){
        return Res.ok(codeConfigService.findSceneByConfigId(configId));
    }

    @RequestPath("获取码牌支付场景配置详情")
    @Operation(summary = "获取码牌各支付场景配置详情")
    @GetMapping("/scene/findById")
    public Result<CashierCodeSceneConfigResult> findSceneById(Long id){
        return Res.ok(codeConfigService.findItemById(id));
    }

    @RequestPath("码牌支付场景配置保存")
    @Operation(summary = "码牌支付场景配置保存")
    @PostMapping("/scene/save")
    public Result<Void> saveScene(@RequestBody CashierCodeSceneConfigParam param){
        codeConfigService.saveScene(param);
        return Res.ok();
    }

    @RequestPath("码牌支付场景配置更新")
    @Operation(summary = "码牌支付场景配置更新")
    @PostMapping("/scene/update")
    public Result<Void> updateScene(@RequestBody CashierCodeSceneConfigParam param){
        codeConfigService.updateScene(param);
        return Res.ok();
    }

    @RequestPath("码牌支付场景配置删除")
    @Operation(summary = "码牌支付场景配置删除")
    @PostMapping("/scene/delete")
    public Result<Void> deleteScene(Long id){
        codeConfigService.deleteScene(id);
        return Res.ok();
    }

    @RequestPath("码牌支付场景配置是否存在")
    @Operation(summary = "码牌支付场景配置是否存在")
    @GetMapping("/scene/exists")
    public Result<Boolean> existsByScene(String scene, Long configId){
        return Res.ok(codeConfigService.existsByScene(scene, configId));
    }

    @RequestPath("码牌支付场景配置是否存在(不包括自身)")
    @Operation(summary = "码牌支付场景配置是否存在(不包括自身)")
    @GetMapping("/scene/existsNotId")
    public Result<Boolean> existsBySceneNotId(String scene, Long configId, Long id){
        return Res.ok(codeConfigService.existsByScene(scene, configId, id));
    }

    @ClientCode({DaxPayCode.Client.ADMIN, DaxPayCode.Client.MERCHANT})
    @RequestPath("码牌配置下拉")
    @Operation(summary = "码牌配置下拉")
    @GetMapping("/dropdown")
    public Result<List<LabelValue>> dropdown(){
        return Res.ok(codeConfigService.dropdown());
    }

}
