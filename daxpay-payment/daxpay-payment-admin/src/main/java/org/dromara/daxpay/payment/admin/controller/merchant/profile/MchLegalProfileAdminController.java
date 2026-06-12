package org.dromara.daxpay.payment.admin.controller.merchant.profile;

import org.dromara.daxpay.platform.core.annotation.PermCode;
import org.dromara.daxpay.platform.core.rest.Res;
import org.dromara.daxpay.platform.core.rest.result.Result;
import org.dromara.daxpay.payment.merchant.param.profile.MchLegalProfileParam;
import org.dromara.daxpay.payment.merchant.result.profile.MchLegalProfileResult;
import org.dromara.daxpay.payment.merchant.service.profile.MchLegalProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/// # 商户法人信息管理控制器
///
@PermCode(menuCode = "payment:merchant")
@Validated
@Tag(name = "商户法人信息管理")
@RestController
@RequestMapping("/admin/merchant/legal-profile")
@RequiredArgsConstructor
public class MchLegalProfileAdminController {

    private final MchLegalProfileService legalProfileService;

    @PermCode(code = "view", nameCn = "商户查看", nameEn = "Merchant View")
    @Operation(summary = "根据商户号查询法人信息")
    @GetMapping("/get-by-mch-no")
    public Result<MchLegalProfileResult> findByMchNo(@NotBlank(message = "{validation.field.mchNo.notBlank}") String mchNo) {
        return Res.ok(legalProfileService.findByMchNo(mchNo));
    }

    @PermCode(code = "edit", nameCn = "商户编辑", nameEn = "Merchant Edit")
    @Operation(summary = "保存法人信息")
    @PostMapping("/save")
    public Result<Void> save(@RequestBody @Validated MchLegalProfileParam param) {
        legalProfileService.saveOrUpdate(param);
        return Res.ok();
    }
}
