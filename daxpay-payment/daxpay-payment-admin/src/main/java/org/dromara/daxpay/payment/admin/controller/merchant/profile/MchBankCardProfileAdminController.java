package org.dromara.daxpay.payment.admin.controller.merchant.profile;

import org.dromara.daxpay.platform.core.annotation.PermCode;
import org.dromara.daxpay.platform.core.rest.Res;
import org.dromara.daxpay.platform.core.rest.result.Result;
import org.dromara.daxpay.payment.merchant.param.profile.MchBankCardProfileParam;
import org.dromara.daxpay.payment.merchant.result.profile.MchBankCardProfileResult;
import org.dromara.daxpay.payment.merchant.service.profile.MchBankCardProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/// # 商户银行卡信息管理控制器
///
@PermCode(menuCode = "payment:merchant")
@Validated
@Tag(name = "商户银行卡信息管理")
@RestController
@RequestMapping("/admin/merchant/bank-card-profile")
@RequiredArgsConstructor
public class MchBankCardProfileAdminController {

    private final MchBankCardProfileService bankCardProfileService;

    @PermCode(code = "view", nameCn = "商户查看", nameEn = "Merchant View")
    @Operation(summary = "根据商户号查询银行卡信息")
    @GetMapping("/get-by-mch-no")
    public Result<MchBankCardProfileResult> findByMchNo(@NotBlank(message = "{validation.field.mchNo.notBlank}") String mchNo) {
        return Res.ok(bankCardProfileService.findByMchNo(mchNo));
    }

    @PermCode(code = "edit", nameCn = "商户编辑", nameEn = "Merchant Edit")
    @Operation(summary = "保存银行卡信息")
    @PostMapping("/save")
    public Result<Void> save(@RequestBody @Validated MchBankCardProfileParam param) {
        bankCardProfileService.saveOrUpdate(param);
        return Res.ok();
    }
}
