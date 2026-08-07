package cn.daxpay.open.payment.admin.controller.trade;

import cn.daxpay.open.platform.core.annotation.PermCode;
import cn.daxpay.open.platform.core.code.PermCodes;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import cn.daxpay.open.platform.core.rest.result.Result;
import cn.daxpay.open.payment.admin.service.trade.TransferAdminService;
import cn.daxpay.open.payment.trade.transfer.param.AlipayTransferOrderQuery;
import cn.daxpay.open.payment.trade.transfer.param.DouyinTransferOrderQuery;
import cn.daxpay.open.payment.trade.transfer.param.TransferParam;
import cn.daxpay.open.payment.trade.transfer.param.TransferTradeQuery;
import cn.daxpay.open.payment.trade.transfer.param.WechatTransferOrderQuery;
import cn.daxpay.open.payment.trade.transfer.result.AlipayTransferOrderResult;
import cn.daxpay.open.payment.trade.transfer.result.DouyinTransferOrderResult;
import cn.daxpay.open.payment.trade.transfer.result.TransferCreateResult;
import cn.daxpay.open.payment.trade.transfer.result.TransferTradeResult;
import cn.daxpay.open.payment.trade.transfer.result.WechatTransferOrderResult;
import cn.daxpay.open.platform.system.service.config.infra.PlatformUrlConfigService;
import cn.hutool.core.util.StrUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/// # 转账单(管理)
///
/// 面向运营后台的转账单管理。按通道独立列表/详情(独立表存储), 运营端可代商户发起转账。
@PermCode(menuCode = PermCodes.Trade.Transfer.MENU)
@Validated
@Tag(name = "转账单(管理)")
@RestController
@RequestMapping("/admin/transfer")
@RequiredArgsConstructor
public class TransferAdminController {

    private final TransferAdminService transferAdminService;
    private final PlatformUrlConfigService platformUrlConfigService;

    // ===== 微信转账 =====

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "微信转账单分页")
    @GetMapping("/wechat/page")
    public Result<PageResult<WechatTransferOrderResult>> wechatPage(PageParam pageParam, WechatTransferOrderQuery query) {
        return Res.ok(transferAdminService.wechatPage(pageParam, query));
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "微信转账单详情")
    @GetMapping("/wechat/get-by-id")
    public Result<WechatTransferOrderResult> wechatFindById(
            @NotNull(message = "{validation.field.id.notNull}") Long id) {
        return Res.ok(transferAdminService.wechatFindById(id));
    }

    // ===== 支付宝转账 =====

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "支付宝转账单分页")
    @GetMapping("/alipay/page")
    public Result<PageResult<AlipayTransferOrderResult>> alipayPage(PageParam pageParam, AlipayTransferOrderQuery query) {
        return Res.ok(transferAdminService.alipayPage(pageParam, query));
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "支付宝转账单详情")
    @GetMapping("/alipay/get-by-id")
    public Result<AlipayTransferOrderResult> alipayFindById(
            @NotNull(message = "{validation.field.id.notNull}") Long id) {
        return Res.ok(transferAdminService.alipayFindById(id));
    }

    // ===== 抖音转账 =====

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "抖音转账单分页")
    @GetMapping("/douyin/page")
    public Result<PageResult<DouyinTransferOrderResult>> douyinPage(PageParam pageParam, DouyinTransferOrderQuery query) {
        return Res.ok(transferAdminService.douyinPage(pageParam, query));
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "抖音转账单详情")
    @GetMapping("/douyin/get-by-id")
    public Result<DouyinTransferOrderResult> douyinFindById(
            @NotNull(message = "{validation.field.id.notNull}") Long id) {
        return Res.ok(transferAdminService.douyinFindById(id));
    }

    // ===== 转账公共操作 =====

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "发起微信转账(运营端代发, 传 mchNo; FAIL 单复用原单号即重试)")
    @PostMapping("/wechat/create")
    public Result<TransferCreateResult> wechatCreate(@Valid @RequestBody TransferParam param) {
        String transferNo = transferAdminService.create("wechat", param);
        // 生成确认收款链接(供商户发给收款人在微信内打开)
        String confirmUrl = buildConfirmUrl(transferNo);
        return Res.ok(new TransferCreateResult().setTransferNo(transferNo).setConfirmUrl(confirmUrl));
    }

    /// 生成微信转账确认收款链接
    private String buildConfirmUrl(String transferNo) {
        String base = platformUrlConfigService.getUrlConfig().getPaymentGatewayBaseUrl();
        if (StrUtil.isBlank(base)) {
            return null;
        }
        return StrUtil.format("{}/transfer-confirm/{}", base, transferNo);
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "发起支付宝转账(运营端代发, 传 mchNo; FAIL 单复用原单号即重试)")
    @PostMapping("/alipay/create")
    public Result<Void> alipayCreate(@Valid @RequestBody TransferParam param) {
        transferAdminService.create("alipay", param);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "发起抖音转账(运营端代发, 传 mchNo; FAIL 单复用原单号即重试)")
    @PostMapping("/douyin/create")
    public Result<Void> douyinCreate(@Valid @RequestBody TransferParam param) {
        transferAdminService.create("douyin", param);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "同步转账状态")
    @PostMapping("/sync")
    public Result<Void> sync(
            @NotNull(message = "{validation.field.channel.notNull}") String channel,
            @NotNull(message = "{validation.field.id.notNull}") Long id) {
        transferAdminService.sync(channel, id);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "关闭转账(仅通道支持场景有效)")
    @PostMapping("/close")
    public Result<Void> close(
            @NotNull(message = "{validation.field.channel.notNull}") String channel,
            @NotNull(message = "{validation.field.id.notNull}") Long id) {
        transferAdminService.close(channel, id);
        return Res.ok();
    }

    // ===== 转账记录(公共资金凭证) =====

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "转账记录分页(跨通道)")
    @GetMapping("/trade/page")
    public Result<PageResult<TransferTradeResult>> tradePage(PageParam pageParam, TransferTradeQuery query) {
        return Res.ok(transferAdminService.tradePage(pageParam, query));
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "转账记录详情")
    @GetMapping("/trade/get-by-id")
    public Result<TransferTradeResult> tradeFindById(
            @NotNull(message = "{validation.field.id.notNull}") Long id) {
        return Res.ok(transferAdminService.tradeFindById(id));
    }
}
