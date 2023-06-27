package cn.bootx.platform.daxpay.controller;

import cn.bootx.platform.common.core.rest.Res;
import cn.bootx.platform.common.core.rest.ResResult;
import cn.bootx.platform.daxpay.core.channel.wallet.service.WalletQueryService;
import cn.bootx.platform.daxpay.core.channel.wallet.service.WalletService;
import cn.bootx.platform.daxpay.dto.channel.wallet.WalletDto;
import cn.bootx.platform.starter.auth.util.SecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "钱包相关的接口")
@RestController
@RequestMapping("/wallet")
@AllArgsConstructor
public class WalletController {
    private final WalletQueryService walletQueryService;
    private final WalletService walletService;

    @Operation(summary = "根据用户查询钱包")
    @GetMapping("/findByUser")
    public ResResult<WalletDto> findByUser() {
        return Res.ok(walletQueryService.findByUser());
    }

    @Operation(summary = "开通用户钱包操作")
    @PostMapping("/createWallet")
    public ResResult<Void> createWallet() {
        Long userId = SecurityUtil.getUserId();
        walletService.createWallet(userId);
        return Res.ok();
    }
}
