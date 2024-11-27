package org.dromara.daxpay.service.controller.config;

import cn.bootx.platform.core.annotation.RequestGroup;
import cn.bootx.platform.core.annotation.RequestPath;
import cn.bootx.platform.core.rest.Res;
import cn.bootx.platform.core.rest.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.dromara.daxpay.service.param.config.cashier.CashierCodeConfigParam;
import org.dromara.daxpay.service.param.config.cashier.CashierCodeTypeConfigParam;
import org.dromara.daxpay.service.result.config.cashier.CashierCodeConfigResult;
import org.dromara.daxpay.service.result.config.cashier.CashierCodeTypeConfigResult;
import org.dromara.daxpay.service.service.config.cashier.CashierCodeConfigService;
import org.dromara.daxpay.service.service.config.cashier.CashierCodeTypeConfigService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 收银码牌配置
 * @author xxm
 * @since 2024/11/20
 */
@RequestGroup(groupCode = "CashierCodeConfig", groupName = "收银码牌配置", moduleCode = "PayConfig", moduleName = "支付配置")
@Tag(name = "收银码牌配置")
@RestController
@RequestMapping("/cashier/code/config")
@RequiredArgsConstructor
public class CashierCodeConfigController {
    private final CashierCodeConfigService codeConfigService;
    private final CashierCodeTypeConfigService codeTypeConfigService;

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

    @RequestPath("获取码牌各类型配置列表")
    @Operation(summary = "获取码牌各类型配置列表")
    @GetMapping("/type/findAll")
    public Result<List<CashierCodeTypeConfigResult>> findAllByCashierCodeId(Long cashierCodeId){
        return Res.ok(codeTypeConfigService.findByAppId(cashierCodeId));
    }

    @RequestPath("获取码牌类型配置详情")
    @Operation(summary = "获取码牌各类型配置详情")
    @GetMapping("/type/findById")
    public Result<CashierCodeTypeConfigResult> findTypeById(Long id){
        return Res.ok(codeTypeConfigService.findById(id));
    }

    @RequestPath("码牌类型配置保存")
    @Operation(summary = "码牌类型配置保存")
    @PostMapping("/type/save")
    public Result<Void> saveType(@RequestBody CashierCodeTypeConfigParam param){
        codeTypeConfigService.save(param);
        return Res.ok();
    }

    @RequestPath("码牌类型配置更新")
    @Operation(summary = "码牌类型配置更新")
    @PostMapping("/type/update")
    public Result<Void> updateType(@RequestBody CashierCodeTypeConfigParam param){
        codeTypeConfigService.update(param);
        return Res.ok();
    }

    @RequestPath("码牌类型配置删除")
    @Operation(summary = "码牌类型配置删除")
    @PostMapping("/type/delete")
    public Result<Void> deleteType(Long id){
        codeTypeConfigService.delete(id);
        return Res.ok();
    }

    @RequestPath("码牌类型是否存在")
    @Operation(summary = "码牌类型是否存在")
    @GetMapping("/type/exists")
    public Result<Boolean> existsByType(String type, Long cashierCodeId){
        return Res.ok(codeTypeConfigService.existsByType(type, cashierCodeId));
    }

    @RequestPath("码牌类型是否存在(不包括自身)")
    @Operation(summary = "码牌类型是否存在(不包括自身)")
    @GetMapping("/type/existsNotId")
    public Result<Boolean> existsByTypeNotId(String type, Long cashierCodeId, Long id){
        return Res.ok(codeTypeConfigService.existsByType(type, cashierCodeId, id));
    }

}
