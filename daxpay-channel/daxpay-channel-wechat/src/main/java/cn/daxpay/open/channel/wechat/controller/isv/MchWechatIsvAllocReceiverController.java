package cn.daxpay.open.channel.wechat.controller.isv;

import cn.daxpay.open.channel.wechat.dao.isv.WechatIsvAllocReceiverManager;
import cn.daxpay.open.channel.wechat.entity.isv.WechatIsvAllocReceiver;
import cn.daxpay.open.channel.wechat.param.isv.WechatIsvAllocReceiverBindParam;
import cn.daxpay.open.channel.wechat.param.isv.WechatIsvAllocReceiverCreateParam;
import cn.daxpay.open.channel.wechat.param.isv.WechatIsvAllocReceiverQuery;
import cn.daxpay.open.channel.wechat.result.isv.WechatIsvAllocReceiverResult;
import cn.daxpay.open.channel.wechat.service.isv.WechatIsvAllocReceiverService;
import cn.daxpay.open.payment.common.context.PaymentContext;
import cn.daxpay.open.platform.core.annotation.PermCode;
import cn.daxpay.open.platform.core.code.CommonCode;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.code.PermCodes;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import cn.daxpay.open.platform.core.rest.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/// # 微信服务商分账接收方管理（商户端）
///
/// 对照运营端 [WechatIsvAllocReceiverController]，路径前缀 `/mch/wechat/isv-alloc-receiver`。
/// 商户号一律取自 [PaymentContext]，忽略请求中的 mchNo，防越权。
@PermCode(menuCode = PermCodes.Channel.Merchant.MENU)
@Validated
@Tag(name = "微信服务商分账接收方管理(商户端)")
@RestController
@RequestMapping("/mch/wechat/isv-alloc-receiver")
@RequiredArgsConstructor
public class MchWechatIsvAllocReceiverController {

    private final WechatIsvAllocReceiverService allocReceiverService;
    private final WechatIsvAllocReceiverManager allocReceiverManager;
    private final PaymentContext paymentContext;

    /// 当前登录商户号（上下文必有；缺则视为会话异常）
    private String requireMchNo() {
        String mchNo = paymentContext.getMchNo();
        if (mchNo == null || mchNo.isBlank()) {
            // 商户上下文缺失
            throw new BizInfoException(CommonCode.FAIL_CODE, "pay.error.assist.mchContextMissing");
        }
        return mchNo;
    }

    /// 校验接收方记录归属当前商户（TenantLine 兜底之外的显式防越权）
    private WechatIsvAllocReceiver loadOwned(Long id) {
        WechatIsvAllocReceiver entity = allocReceiverManager.findById(id)
                .orElseThrow(() -> new BizInfoException(CommonErrorCode.DATA_NOT_EXIST,
                        "error.common.dataNotExist", id));
        if (!Objects.equals(entity.getMchNo(), this.requireMchNo())) {
            // 记录不属于当前商户，按不存在处理避免信息泄露
            throw new BizInfoException(CommonErrorCode.DATA_NOT_EXIST,
                    "error.common.dataNotExist", id);
        }
        return entity;
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "分页查询")
    @GetMapping("/page")
    public Result<PageResult<WechatIsvAllocReceiverResult>> page(PageParam pageParam,
                                                                    WechatIsvAllocReceiverQuery query) {
        // 强制当前商户，忽略客户端传入的 mchNo
        query.setMchNo(requireMchNo());
        return Res.ok(allocReceiverService.page(pageParam, query));
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "新增并绑定接收方(同步调通道, 失败记录保留)")
    @PostMapping("/create")
    public Result<Void> create(@RequestBody @Validated WechatIsvAllocReceiverCreateParam param) {
        // 强制当前商户；服务内部校验通道商户归属与该商户号一致
        param.setMchNo(requireMchNo());
        allocReceiverService.create(param);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "重新绑定(绑定失败/已解绑状态, 可更换绑定所用应用)")
    @PostMapping("/bind")
    public Result<Void> bind(@RequestBody @Validated WechatIsvAllocReceiverBindParam param) {
        this.loadOwned(param.getId());
        allocReceiverService.bind(param);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "解绑(已绑定状态, 保留记录)")
    @PostMapping("/unbind")
    public Result<Void> unbind(@NotNull(message = "{validation.field.id.notNull}") Long id) {
        this.loadOwned(id);
        allocReceiverService.unbind(id);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "删除(仅绑定失败/已解绑状态)")
    @PostMapping("/delete")
    public Result<Void> delete(@NotNull(message = "{validation.field.id.notNull}") Long id) {
        this.loadOwned(id);
        allocReceiverService.delete(id);
        return Res.ok();
    }
}
