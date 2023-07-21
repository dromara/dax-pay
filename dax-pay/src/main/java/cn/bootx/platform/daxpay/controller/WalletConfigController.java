package cn.bootx.platform.daxpay.controller;

import cn.bootx.platform.common.core.rest.Res;
import cn.bootx.platform.common.core.rest.ResResult;
import cn.bootx.platform.daxpay.core.channel.wallet.service.WalletConfigService;
import cn.bootx.platform.daxpay.dto.channel.wallet.WalletConfigDto;
import cn.bootx.platform.daxpay.param.channel.wechat.WalletConfigParam;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 钱包配置
 * @author xxm
 * @since 2023/7/20
 */
@Tag(name = "钱包配置")
@RestController
@RequestMapping("/wallet/config")
@RequiredArgsConstructor
public class WalletConfigController {
    private final WalletConfigService walletConfigService;

    @Operation(summary = "获取钱包支付配置")
    @GetMapping("/findByMchCode")
    public ResResult<WalletConfigDto> findByMchCode(String mchCode){
        return Res.ok(walletConfigService.findByMchCode(mchCode));
    }

    @Operation(summary = "添加")
    @PostMapping("/add")
    public ResResult<Void> add(@RequestBody WalletConfigParam param) {
        walletConfigService.add(param);
        return Res.ok();
    }

    @Operation(summary = "更新")
    @PostMapping("/update")
    public ResResult<Void> update(@RequestBody WalletConfigParam param) {
        walletConfigService.update(param);
        return Res.ok();
    }
}
