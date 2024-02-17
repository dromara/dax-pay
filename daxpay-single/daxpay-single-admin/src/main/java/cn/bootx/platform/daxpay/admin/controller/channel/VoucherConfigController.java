package cn.bootx.platform.daxpay.admin.controller.channel;

import cn.bootx.platform.common.core.rest.Res;
import cn.bootx.platform.common.core.rest.ResResult;
import cn.bootx.platform.common.core.rest.dto.LabelValue;
import cn.bootx.platform.daxpay.service.core.channel.voucher.service.VoucherConfigService;
import cn.bootx.platform.daxpay.service.dto.channel.voucher.VoucherConfigDto;
import cn.bootx.platform.daxpay.service.param.channel.wechat.WalletConfigParam;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 储值卡支付配置
 * @author xxm
 * @since 2024/2/17
 */
@Tag(name = "储值卡支付配置")
@RestController
@RequestMapping("/voucher/config")
@RequiredArgsConstructor
public class VoucherConfigController {
    private final VoucherConfigService service;

    @Operation(summary = "获取配置")
    @GetMapping("/getConfig")
    public ResResult<VoucherConfigDto> getConfig() {
        return Res.ok(service.getConfig().toDto());
    }

    @Operation(summary = "更新")
    @PostMapping("/update")
    public ResResult<Void> update(@RequestBody WalletConfigParam param) {
        service.update(param);
        return Res.ok();
    }

    @Operation(summary = "支付宝支持支付方式")
    @GetMapping("/findPayWays")
    public ResResult<List<LabelValue>> findPayWays() {
        return Res.ok(service.findPayWays());
    }
}
