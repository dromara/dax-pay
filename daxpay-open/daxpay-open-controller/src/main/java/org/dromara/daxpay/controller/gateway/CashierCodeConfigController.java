package org.dromara.daxpay.controller.gateway;

import cn.bootx.platform.core.annotation.RequestGroup;
import cn.bootx.platform.core.annotation.RequestPath;
import cn.bootx.platform.core.rest.Res;
import cn.bootx.platform.core.rest.result.Result;
import org.dromara.daxpay.service.param.gateway.CashierCodeConfigParam;
import org.dromara.daxpay.service.param.gateway.CashierCodeItemConfigParam;
import org.dromara.daxpay.service.result.gateway.config.CashierCodeConfigResult;
import org.dromara.daxpay.service.result.gateway.config.CashierCodeItemConfigResult;
import org.dromara.daxpay.service.service.gateway.config.CashierCodeConfigService;
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
@RequestGroup(groupCode = "CashierCodeConfig", groupName = "收银码牌配置", moduleCode = "PayConfig", moduleName = "(DaxPay)支付配置")
@Tag(name = "收银码牌配置")
@RestController
@RequestMapping("/cashier/code/config")
@RequiredArgsConstructor
public class CashierCodeConfigController {
    private final CashierCodeConfigService codeConfigService;

    @RequestPath("根据AppId查询码牌列表")
    @Operation(summary = "根据AppId查询码牌列表")
    @GetMapping("/findAllByAppId")
    public Result<List<CashierCodeConfigResult>> findAllByAppId(String appId){
        return Res.ok(codeConfigService.findAllByAppId(appId));
    }

    @RequestPath("根据id查询码牌信息")
    @Operation(summary = "根据id查询码牌信息")
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

    @RequestPath("获取码牌地址")
    @Operation(summary = "获取码牌地址")
    @GetMapping("/getCashierCodeUrl")
    public Result<String> getCashierCodeUrl(Long id){
        return Res.ok(codeConfigService.getCashierCodeUrl(id));
    }

    @RequestPath("获取码牌各配置明细列表")
    @Operation(summary = "获取码牌各配置明细列表")
    @GetMapping("/item/findAll")
    public Result<List<CashierCodeItemConfigResult>> findAllByCodeId(Long codeId){
        return Res.ok(codeConfigService.findItemByCodeId(codeId));
    }

    @RequestPath("获取码牌配置明细详情")
    @Operation(summary = "获取码牌各配置明细详情")
    @GetMapping("/item/findById")
    public Result<CashierCodeItemConfigResult> findItemById(Long id){
        return Res.ok(codeConfigService.findItemById(id));
    }

    @RequestPath("码牌配置明细保存")
    @Operation(summary = "码牌配置明细保存")
    @PostMapping("/item/save")
    public Result<Void> saveItem(@RequestBody CashierCodeItemConfigParam param){
        codeConfigService.saveItem(param);
        return Res.ok();
    }

    @RequestPath("码牌配置明细更新")
    @Operation(summary = "码牌配置明细更新")
    @PostMapping("/item/update")
    public Result<Void> updateItem(@RequestBody CashierCodeItemConfigParam param){
        codeConfigService.updateItem(param);
        return Res.ok();
    }

    @RequestPath("码牌配置明细删除")
    @Operation(summary = "码牌配置明细删除")
    @PostMapping("/item/delete")
    public Result<Void> deleteItem(Long id){
        codeConfigService.deleteItem(id);
        return Res.ok();
    }

    @RequestPath("码牌配置明细是否存在")
    @Operation(summary = "码牌配置明细是否存在")
    @GetMapping("/item/exists")
    public Result<Boolean> existsByItem(String type, Long codeId){
        return Res.ok(codeConfigService.existsByTypeItem(type, codeId));
    }

    @RequestPath("码牌配置明细是否存在(不包括自身)")
    @Operation(summary = "码牌配置明细是否存在(不包括自身)")
    @GetMapping("/item/existsNotId")
    public Result<Boolean> existsByItemNotId(String type, Long codeId, Long id){
        return Res.ok(codeConfigService.existsByTypeItem(type, codeId, id));
    }

}
