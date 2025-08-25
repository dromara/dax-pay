package org.dromara.daxpay.controller.device.qrcode;

import cn.bootx.platform.core.annotation.ClientCode;
import cn.bootx.platform.core.annotation.RequestGroup;
import cn.bootx.platform.core.annotation.RequestPath;
import cn.bootx.platform.core.rest.Res;
import cn.bootx.platform.core.rest.param.PageParam;
import cn.bootx.platform.core.rest.result.PageResult;
import cn.bootx.platform.core.rest.result.Result;
import org.dromara.daxpay.service.common.code.DaxPayCode;
import org.dromara.daxpay.service.device.param.commom.AssignMerchantParam;
import org.dromara.daxpay.service.device.param.qrcode.info.CashierCodeBatchParam;
import org.dromara.daxpay.service.device.param.qrcode.info.CashierCodeQuery;
import org.dromara.daxpay.service.device.param.qrcode.info.CashierCodeUpdateParam;
import org.dromara.daxpay.service.device.result.qrcode.info.CashierCodeResult;
import org.dromara.daxpay.service.device.service.qrcode.CashierCodeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.dromara.core.trans.anno.TransMethodResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 收款码牌管理
 * @author xxm
 * @since 2025/7/1
 */
@Validated
@Tag(name = "收款码牌管理")
@RestController
@RequestMapping("/cashier/code")
@ClientCode({DaxPayCode.Client.ADMIN, DaxPayCode.Client.MERCHANT})
@RequestGroup(groupCode = "CashierCode", groupName = "收款码牌管理", moduleCode = "device", moduleName = "(DaxPay)设备管理")
@RequiredArgsConstructor
public class CashierCodeController {
    private final CashierCodeService cashierCodeService;

    @TransMethodResult
    @RequestPath("分页查询")
    @Operation(summary = "分页查询")
    @GetMapping("/page")
    public Result<PageResult<CashierCodeResult>> page(PageParam pageParam, CashierCodeQuery query) {
        return Res.ok(cashierCodeService.page(pageParam, query));
    }

    @TransMethodResult
    @RequestPath("查询详情")
    @Operation(summary = "查询详情")
    @GetMapping("/findById")
    public Result<CashierCodeResult> findById(@NotNull(message = "主键不可为空")Long id) {
        return Res.ok(cashierCodeService.findById(id));
    }

    @RequestPath("判断批次号是否已存在")
    @Operation(summary = "判断批次号是否已存在")
    @GetMapping("/existsByBatchNo")
    public Result<Boolean> existsByBatchNo(@NotNull(message = "批次号不可为空") String batchNo) {
        return Res.ok(cashierCodeService.existsByBatchNo(batchNo));
    }

    @RequestPath("批量创建")
    @Operation(summary = "批量创建")
    @PostMapping("/createBatch")
    public Result<Void> createBatch(@Validated @RequestBody CashierCodeBatchParam param) {
        cashierCodeService.createBatch(param);
        return Res.ok();
    }

    @RequestPath("修改")
    @Operation(summary = "修改")
    @PostMapping("/update")
    public Result<Void> update(@Validated @RequestBody CashierCodeUpdateParam param) {
        cashierCodeService.update(param);
        return Res.ok();
    }

    @ClientCode({DaxPayCode.Client.ADMIN})
    @RequestPath("绑定商户")
    @Operation(summary = "绑定商户")
    @PostMapping("/bindMerchant")
    public Result<Void> bindMerchant(@Validated @RequestBody AssignMerchantParam param) {
        cashierCodeService.bindMerchant(param);
        return Res.ok();
    }

    @ClientCode({DaxPayCode.Client.ADMIN})
    @RequestPath("解绑商户")
    @Operation(summary = "解绑商户")
    @PostMapping("/unbindMerchant")
    public Result<Void> unbindMerchant(@Validated @RequestBody AssignMerchantParam param) {
        cashierCodeService.unbindMerchant(param);
        return Res.ok();
    }


    @ClientCode(DaxPayCode.Client.MERCHANT)
    @RequestPath("绑定应用")
    @Operation(summary = "绑定应用")
    @PostMapping("/bindApp")
    public Result<Void> bindApp(@RequestBody @Validated AssignMerchantParam param){
        cashierCodeService.bindApp(param);
        return Res.ok();
    }

    @ClientCode(DaxPayCode.Client.MERCHANT)
    @RequestPath("解绑应用")
    @Operation(summary = "解绑应用")
    @PostMapping("/unbindApp")
    public Result<Void> unbindApp(@RequestBody @Validated AssignMerchantParam param){
        cashierCodeService.unbindApp(param);
        return Res.ok();
    }

    @ClientCode(DaxPayCode.Client.ADMIN)
    @Operation(summary = "删除码牌")
    @PostMapping("/delete")
    public Result<Void> delete(@NotNull(message = "主键不可为空") Long id) {
        cashierCodeService.delete(id);
        return Res.ok();
    }

    @RequestPath("获取码牌链接")
    @Operation(summary = "获取码牌链接")
    @GetMapping("/getCodeLink")
    public Result<String> getCodeLink(@NotNull(message = "码牌编号不可为空") String code) {
        return Res.ok(cashierCodeService.getCodeLink(code));
    }

}
