package org.dromara.daxpay.payment.merchant.controller.miniapp;

import org.dromara.daxpay.platform.core.rest.Res;
import org.dromara.daxpay.platform.core.rest.result.Result;
import org.dromara.daxpay.payment.common.context.PaymentContext;
import org.dromara.daxpay.payment.merchant.param.profile.MchBaseProfileParam;
import org.dromara.daxpay.payment.merchant.param.profile.MchBankCardProfileParam;
import org.dromara.daxpay.payment.merchant.param.profile.MchCardHolderProfileParam;
import org.dromara.daxpay.payment.merchant.param.profile.MchLegalProfileParam;
import org.dromara.daxpay.payment.merchant.param.profile.MchLicenseProfileParam;
import org.dromara.daxpay.payment.merchant.param.profile.MchShopProfileParam;
import org.dromara.daxpay.payment.merchant.result.profile.MchBaseProfileResult;
import org.dromara.daxpay.payment.merchant.result.profile.MchBankCardProfileResult;
import org.dromara.daxpay.payment.merchant.result.profile.MchCardHolderProfileResult;
import org.dromara.daxpay.payment.merchant.result.profile.MchLegalProfileResult;
import org.dromara.daxpay.payment.merchant.result.profile.MchLicenseProfileResult;
import org.dromara.daxpay.payment.merchant.result.profile.MchShopProfileResult;
import org.dromara.daxpay.payment.merchant.service.profile.MchBaseProfileService;
import org.dromara.daxpay.payment.merchant.service.profile.MchBankCardProfileService;
import org.dromara.daxpay.payment.merchant.service.profile.MchCardHolderProfileService;
import org.dromara.daxpay.payment.merchant.service.profile.MchLegalProfileService;
import org.dromara.daxpay.payment.merchant.service.profile.MchLicenseProfileService;
import org.dromara.daxpay.payment.merchant.service.profile.MchShopProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/// # 商户资料信息控制器（小程序端）
///
@Validated
@Tag(name = "商户资料信息")
@RestController
@RequestMapping("/mini/mch/profile")
@RequiredArgsConstructor
public class MiniMchProfileController {

    private final MchBaseProfileService baseProfileService;
    private final MchLegalProfileService legalProfileService;
    private final MchLicenseProfileService licenseProfileService;
    private final MchBankCardProfileService bankCardProfileService;
    private final MchCardHolderProfileService cardHolderProfileService;
    private final MchShopProfileService shopProfileService;
    private final PaymentContext apiContext;

    @Operation(summary = "查询基础资料")
    @GetMapping("/base/get-by-mch-no")
    public Result<MchBaseProfileResult> findBaseProfile() {
        return Res.ok(baseProfileService.findByMchNo(apiContext.getTradeInfo().getMchNo()));
    }

    @Operation(summary = "保存基础资料")
    @PostMapping("/base/save")
    public Result<Void> saveBaseProfile(@RequestBody @Validated MchBaseProfileParam param) {
        param.setMchNo(apiContext.getTradeInfo().getMchNo());
        baseProfileService.save(param);
        return Res.ok();
    }

    @Operation(summary = "查询法人信息")
    @GetMapping("/legal/get-by-mch-no")
    public Result<MchLegalProfileResult> findLegalProfile() {
        return Res.ok(legalProfileService.findByMchNo(apiContext.getTradeInfo().getMchNo()));
    }

    @Operation(summary = "保存法人信息")
    @PostMapping("/legal/save")
    public Result<Void> saveLegalProfile(@RequestBody @Validated MchLegalProfileParam param) {
        param.setMchNo(apiContext.getTradeInfo().getMchNo());
        legalProfileService.saveOrUpdate(param);
        return Res.ok();
    }

    @Operation(summary = "查询营业执照信息")
    @GetMapping("/license/get-by-mch-no")
    public Result<MchLicenseProfileResult> findLicenseProfile() {
        return Res.ok(licenseProfileService.findByMchNo(apiContext.getTradeInfo().getMchNo()));
    }

    @Operation(summary = "保存营业执照信息")
    @PostMapping("/license/save")
    public Result<Void> saveLicenseProfile(@RequestBody @Validated MchLicenseProfileParam param) {
        param.setMchNo(apiContext.getTradeInfo().getMchNo());
        licenseProfileService.saveOrUpdate(param);
        return Res.ok();
    }

    @Operation(summary = "查询银行卡信息")
    @GetMapping("/bank-card/get-by-mch-no")
    public Result<MchBankCardProfileResult> findBankCardProfile() {
        return Res.ok(bankCardProfileService.findByMchNo(apiContext.getTradeInfo().getMchNo()));
    }

    @Operation(summary = "保存银行卡信息")
    @PostMapping("/bank-card/save")
    public Result<Void> saveBankCardProfile(@RequestBody @Validated MchBankCardProfileParam param) {
        param.setMchNo(apiContext.getTradeInfo().getMchNo());
        bankCardProfileService.saveOrUpdate(param);
        return Res.ok();
    }

    @Operation(summary = "查询持卡人信息")
    @GetMapping("/card-holder/get-by-mch-no")
    public Result<MchCardHolderProfileResult> findCardHolderProfile() {
        return Res.ok(cardHolderProfileService.findByMchNo(apiContext.getTradeInfo().getMchNo()));
    }

    @Operation(summary = "保存持卡人信息")
    @PostMapping("/card-holder/save")
    public Result<Void> saveCardHolderProfile(@RequestBody @Validated MchCardHolderProfileParam param) {
        param.setMchNo(apiContext.getTradeInfo().getMchNo());
        cardHolderProfileService.saveOrUpdate(param);
        return Res.ok();
    }

    @Operation(summary = "查询经营场所信息")
    @GetMapping("/shop/get-by-mch-no")
    public Result<MchShopProfileResult> findShopProfile() {
        return Res.ok(shopProfileService.findByMchNo(apiContext.getTradeInfo().getMchNo()));
    }

    @Operation(summary = "保存经营场所信息")
    @PostMapping("/shop/save")
    public Result<Void> saveShopProfile(@RequestBody @Validated MchShopProfileParam param) {
        param.setMchNo(apiContext.getTradeInfo().getMchNo());
        shopProfileService.saveOrUpdate(param);
        return Res.ok();
    }
}
