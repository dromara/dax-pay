package cn.daxpay.open.payment.merchant.controller.douyin;

import cn.daxpay.open.payment.common.context.PaymentContext;
import cn.daxpay.open.payment.douyin.param.merchant.DyMchAppParam;
import cn.daxpay.open.payment.douyin.result.merchant.DyMchAppResult;
import cn.daxpay.open.payment.douyin.service.merchant.DyMchAppService;
import cn.daxpay.open.platform.core.annotation.PermCode;
import cn.daxpay.open.platform.core.code.CommonCode;
import cn.daxpay.open.platform.core.code.PermCodes;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.exception.config.ConfigErrorException;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.result.Result;
import cn.daxpay.open.platform.core.util.ValidationUtil;
import cn.daxpay.open.platform.core.validation.ValidationGroup;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

/// # 商户抖音应用管理（商户端）
///
/// 对照运营端 [cn.daxpay.open.payment.admin.controller.douyin.DyMchAppController]，路径 `/mch/douyin/mch-app`。
/// 商户号一律取自 [PaymentContext]，写操作覆盖请求中的 mchNo，防越权。
@PermCode(menuCode = PermCodes.Payment.Douyin.MchApp.MENU)
@Validated
@Tag(name = "商户抖音应用管理(商户端)")
@RestController
@RequestMapping("/mch/douyin/mch-app")
@RequiredArgsConstructor
public class MchDyMchAppController {

    private final DyMchAppService dyMchAppService;
    private final PaymentContext paymentContext;

    /// 当前登录商户号
    private String requireMchNo() {
        String mchNo = paymentContext.getMchNo();
        if (mchNo == null || mchNo.isBlank()) {
            // 商户上下文缺失
            throw new BizInfoException(CommonCode.FAIL_CODE, "pay.error.assist.mchContextMissing");
        }
        return mchNo;
    }

    /// 校验资源归属当前商户
    private void assertOwned(DyMchAppResult result) {
        if (!Objects.equals(result.getMchNo(), this.requireMchNo())) {
            // 商户抖音应用不属于当前商户
            throw new ConfigErrorException("error.payment.douyin.mchAppNotFound");
        }
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "当前商户抖音应用列表")
    @GetMapping("/list-all")
    public Result<List<DyMchAppResult>> listAll() {
        return Res.ok(dyMchAppService.listByMchNo(this.requireMchNo()));
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "查询应用详情")
    @GetMapping("/find-by-id")
    public Result<DyMchAppResult> findById(
            @NotNull(message = "{validation.field.id.notNull}") Long id) {
        DyMchAppResult result = dyMchAppService.findById(id);
        this.assertOwned(result);
        return Res.ok(result);
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "抖音应用AppId是否已存在")
    @GetMapping("/exists-douyin-app-id")
    public Result<Boolean> existsDouyinAppId(
            @NotBlank(message = "{validation.field.douyinAppId.notBlank}") String douyinAppId) {
        return Res.ok(dyMchAppService.existsDouyinAppId(this.requireMchNo(), douyinAppId, null));
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "抖音应用AppId是否已存在(排除自身)")
    @GetMapping("/exists-douyin-app-id-not-id")
    public Result<Boolean> existsDouyinAppIdNotId(
            @NotBlank(message = "{validation.field.douyinAppId.notBlank}") String douyinAppId,
            @NotNull(message = "{validation.field.id.notNull}") Long id) {
        return Res.ok(dyMchAppService.existsDouyinAppId(this.requireMchNo(), douyinAppId, id));
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "新增商户抖音应用")
    @PostMapping("/add")
    public Result<Void> add(@RequestBody DyMchAppParam param) {
        // 强制当前商户号，忽略客户端传入（防越权）
        param.setMchNo(this.requireMchNo());
        ValidationUtil.validateParam(param, ValidationGroup.add.class);
        dyMchAppService.add(param);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "修改商户抖音应用")
    @PostMapping("/update")
    public Result<Void> update(@RequestBody DyMchAppParam param) {
        this.assertOwned(dyMchAppService.findById(param.getId()));
        // 强制当前商户号，忽略客户端传入（防越权）
        param.setMchNo(this.requireMchNo());
        ValidationUtil.validateParam(param, ValidationGroup.edit.class);
        dyMchAppService.update(param);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "删除商户抖音应用")
    @PostMapping("/delete")
    public Result<Void> delete(@NotNull(message = "{validation.field.id.notNull}") Long id) {
        this.assertOwned(dyMchAppService.findById(id));
        dyMchAppService.delete(id);
        return Res.ok();
    }

}
