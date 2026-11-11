package cn.daxpay.open.payment.app.merchant.controller.store;

import cn.daxpay.open.payment.app.merchant.service.store.AppMerchantStoreInfoService;
import cn.daxpay.open.payment.merchant.param.store.MchStoreInfoParam;
import cn.daxpay.open.payment.merchant.param.store.MchStoreInfoQuery;
import cn.daxpay.open.payment.merchant.result.store.MchStoreInfoResult;
import cn.daxpay.open.payment.merchant.result.store.MchStoreOverviewResult;
import cn.daxpay.open.platform.core.annotation.PermCode;
import cn.daxpay.open.platform.core.code.PermCodes;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import cn.daxpay.open.platform.core.rest.result.Result;
import cn.daxpay.open.platform.core.validation.ValidationGroup;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/// # 门店信息管理(商户移动端)
///
/// 面向商户移动端的门店管理。业务编排委托 [AppMerchantStoreInfoService]。
/// list-all / overview 两个端点镜像 Web 商户端
/// [cn.daxpay.open.payment.merchant.controller.store.MchStoreInfoController] 的同名列表与概况聚合
/// (Web 端列表路径为 `/list`, 此处取 `/list-all` 对齐 WxMchApp/DyMchApp 等移动端既有列表命名), 均复用同一核心 Service, 无业务逻辑。
@PermCode(menuCode = PermCodes.Merchant.Store.MENU)
@Validated
@Tag(name = "门店信息管理(商户移动端)")
@RestController
@RequestMapping("/app-mch/store")
@RequiredArgsConstructor
public class AppMerchantStoreInfoController {

    private final AppMerchantStoreInfoService storeInfoService;

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "门店分页")
    @GetMapping("/page")
    public Result<PageResult<MchStoreInfoResult>> page(PageParam pageParam, MchStoreInfoQuery query) {
        return Res.ok(storeInfoService.page(pageParam, query));
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "门店列表(当前商户全部门店, 不分页)")
    @GetMapping("/list-all")
    public Result<List<MchStoreInfoResult>> listAll() {
        return Res.ok(storeInfoService.list());
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "根据id查询门店")
    @GetMapping("/get")
    public Result<MchStoreInfoResult> findById(@NotNull(message = "{validation.field.id.notNull}") Long id) {
        return Res.ok(storeInfoService.findById(id));
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "门店概况聚合(门店工作台)")
    @GetMapping("/overview")
    public Result<MchStoreOverviewResult> overview(@NotNull(message = "{validation.field.id.notNull}") Long id) {
        return Res.ok(storeInfoService.overview(id));
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "新增门店")
    @PostMapping("/add")
    public Result<Void> add(@RequestBody @Validated(ValidationGroup.add.class) MchStoreInfoParam param) {
        storeInfoService.add(param);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "修改门店")
    @PostMapping("/update")
    public Result<Void> update(@RequestBody @Validated(ValidationGroup.edit.class) MchStoreInfoParam param) {
        storeInfoService.update(param);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "删除门店")
    @PostMapping("/delete")
    public Result<Void> delete(@NotNull(message = "{validation.field.id.notNull}") Long id) {
        storeInfoService.delete(id);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "设为默认门店")
    @PostMapping("/set-default")
    public Result<Void> setDefault(@NotNull(message = "{validation.field.id.notNull}") Long id) {
        storeInfoService.setDefault(id);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "取消默认门店")
    @PostMapping("/clear-default")
    public Result<Void> clearDefault(@NotNull(message = "{validation.field.id.notNull}") Long id) {
        storeInfoService.clearDefault(id);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "设置/清空门店默认应用(空=跟随商户默认)")
    @PostMapping("/set-app")
    public Result<Void> setApp(@NotNull(message = "{validation.field.id.notNull}") Long id, String appId) {
        storeInfoService.setApp(id, appId);
        return Res.ok();
    }
}
