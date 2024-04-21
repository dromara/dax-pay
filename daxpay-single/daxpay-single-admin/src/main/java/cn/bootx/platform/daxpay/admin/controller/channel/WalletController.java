package cn.bootx.platform.daxpay.admin.controller.channel;

import cn.bootx.platform.common.core.rest.PageResult;
import cn.bootx.platform.common.core.rest.Res;
import cn.bootx.platform.common.core.rest.ResResult;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.core.util.ValidationUtil;
import cn.bootx.platform.daxpay.service.core.channel.wallet.service.WalletQueryService;
import cn.bootx.platform.daxpay.service.core.channel.wallet.service.WalletService;
import cn.bootx.platform.daxpay.service.dto.channel.wallet.WalletDto;
import cn.bootx.platform.daxpay.service.dto.channel.wallet.WalletRecordDto;
import cn.bootx.platform.daxpay.service.param.channel.wallet.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 钱包管理
 * @author xxm
 * @since 2024/2/17
 */
@Tag(name = "钱包管理")
@RestController
@RequestMapping("/wallet")
@RequiredArgsConstructor
public class WalletController {
    private final WalletService walletService;
    private final WalletQueryService walletQueryService;
    private final WalletRecordService walletRecordService;

    @Operation(summary = "创建钱包")
    @PostMapping("/create")
    public ResResult<Void> create(@RequestBody CreateWalletParam param){
        ValidationUtil.validateParam(param);
        walletService.create(param);
        return Res.ok();
    }

    @Operation(summary = "充值")
    @PostMapping("/recharge")
    public ResResult<Void> recharge(@RequestBody WalletRechargeParam param){
        ValidationUtil.validateParam(param);
        walletService.recharge(param);
        return Res.ok();
    }

    @Operation(summary = "扣减")
    @PostMapping("/deduct")
    public ResResult<Void> deduct(@RequestBody WalleteeDductParam param){
        ValidationUtil.validateParam(param);
        walletService.deduct(param);
        return Res.ok();
    }

    @Operation(summary = "钱包分页")
    @GetMapping("/page")
    public ResResult<PageResult<WalletDto>> page(PageParam pageParam, WalletQuery query){
        return Res.ok(walletQueryService.page(pageParam, query));
    }

    @Operation(summary = "查询钱包详情")
    @GetMapping("/findById")
    public ResResult<WalletDto> findById(Long id){
        return Res.ok(walletQueryService.findById(id));
    }

    @Operation(summary = "判断用户是否开通了钱包")
    @GetMapping("/existsByUserId")
    public ResResult<Boolean> existsByUserId(String userId){
        return Res.ok(walletQueryService.existsByUserId(userId));
    }

    @Operation(summary = "记录分页")
    @GetMapping("/record/page")
    public ResResult<PageResult<WalletRecordDto>> recordPage(PageParam pageParam, WalletRecordQuery query){
        return Res.ok(walletRecordService.page(pageParam, query));
    }

    @Operation(summary = "查询记录详情")
    @GetMapping("/record/findById")
    public ResResult<WalletRecordDto> findRecordById(Long id){
        return Res.ok(walletRecordService.findById(id));
    }
}
