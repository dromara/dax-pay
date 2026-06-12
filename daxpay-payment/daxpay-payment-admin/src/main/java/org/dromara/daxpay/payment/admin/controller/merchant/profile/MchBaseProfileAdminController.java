package org.dromara.daxpay.payment.admin.controller.merchant.profile;

import org.dromara.daxpay.platform.core.annotation.PermCode;
import org.dromara.daxpay.platform.core.rest.Res;
import org.dromara.daxpay.platform.core.rest.result.Result;
import org.dromara.daxpay.payment.merchant.param.profile.MchBaseProfileParam;
import org.dromara.daxpay.payment.merchant.result.profile.MchBaseProfileResult;
import org.dromara.daxpay.payment.merchant.service.profile.MchBaseProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/// # 商户基础资料管理控制器
///
@PermCode(menuCode = "payment:merchant")
@Validated
@Tag(name = "商户基础资料管理")
@RestController
@RequestMapping("/admin/merchant/base-profile")
@RequiredArgsConstructor
public class MchBaseProfileAdminController {

    private final MchBaseProfileService mchBaseProfileService;

    @PermCode(code = "view", nameCn = "商户查看", nameEn = "Merchant View")
    @Operation(summary = "根据商户号查询基础资料")
    @GetMapping("/get-by-mch-no")
    public Result<MchBaseProfileResult> findByMchNo(@NotBlank(message = "{validation.field.mchNo.notBlank}") String mchNo) {
        return Res.ok(mchBaseProfileService.findByMchNo(mchNo));
    }

    @PermCode(code = "edit", nameCn = "商户编辑", nameEn = "Merchant Edit")
    @Operation(summary = "保存商户基础资料")
    @PostMapping("/save")
    public Result<Void> save(@RequestBody @Validated MchBaseProfileParam param) {
        mchBaseProfileService.save(param);
        return Res.ok();
    }
}
