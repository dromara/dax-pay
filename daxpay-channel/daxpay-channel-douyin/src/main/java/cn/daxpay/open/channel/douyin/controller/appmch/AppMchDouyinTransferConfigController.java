package cn.daxpay.open.channel.douyin.controller.appmch;

import cn.daxpay.open.channel.douyin.param.direct.DouyinTransferConfigParam;
import cn.daxpay.open.channel.douyin.result.direct.DouyinTransferConfigResult;
import cn.daxpay.open.channel.douyin.result.direct.DouyinTransferSceneOptionResult;
import cn.daxpay.open.channel.douyin.service.direct.DouyinDirectChannelMerchantService;
import cn.daxpay.open.channel.douyin.service.direct.DouyinTransferConfigService;
import cn.daxpay.open.payment.common.context.PaymentContext;
import cn.daxpay.open.platform.core.annotation.PermCode;
import cn.daxpay.open.platform.core.code.CommonCode;
import cn.daxpay.open.platform.core.code.PermCodes;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.result.Result;
import cn.daxpay.open.platform.core.util.ValidationUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

/// # 抖音转账配置管理(小程序商户端镜像)
///
/// 对照 admin 版 [cn.daxpay.open.channel.douyin.controller.direct.DouyinTransferConfigController]。
/// 商户号一律取自 [PaymentContext], 忽略客户端传入, 防越权;
/// 通道商户归属校验由 Service 内 assertChannelMerchant 兜底。
/// 另附转账场景选项端点(app-mch 无 direct-channel-merchant 镜像, 场景选项并入本类)。
@PermCode(menuCode = PermCodes.Channel.Merchant.MENU)
@Validated
@Tag(name = "小程序商户端-抖音转账配置管理")
@RestController
@RequestMapping("/app-mch/douyin/transfer-config")
@RequiredArgsConstructor
public class AppMchDouyinTransferConfigController {

    private final DouyinTransferConfigService douyinTransferConfigService;
    private final DouyinDirectChannelMerchantService douyinDirectChannelMerchantService;
    private final PaymentContext paymentContext;

    /// 当前登录商户号(上下文必有;缺则视为会话异常)
    private String requireMchNo() {
        String mchNo = paymentContext.getMchNo();
        if (Objects.isNull(mchNo) || mchNo.isBlank()) {
            // 商户上下文缺失
            throw new BizInfoException(CommonCode.FAIL_CODE, "pay.error.assist.mchContextMissing");
        }
        return mchNo;
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "查询通道商户的转账配置")
    @GetMapping("/find-by-channel-mch-no")
    public Result<DouyinTransferConfigResult> findByChannelMchNo(
            @NotBlank(message = "{validation.field.channelMerchantNo.notBlank}") String channelMchNo) {
        return Res.ok(douyinTransferConfigService.findByChannelMchNo(requireMchNo(), channelMchNo));
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "查询抖音转账场景选项列表(主数据枚举)")
    @GetMapping("/scene-options")
    public Result<List<DouyinTransferSceneOptionResult>> sceneOptions() {
        return Res.ok(douyinDirectChannelMerchantService.findSceneOptions());
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "保存或更新转账配置(一对一)")
    @PostMapping("/save")
    public Result<Void> save(@RequestBody DouyinTransferConfigParam param) {
        // 强制当前商户号, 忽略客户端传入(防越权)
        param.setMchNo(requireMchNo());
        ValidationUtil.validateParam(param);
        douyinTransferConfigService.saveOrUpdate(param);
        return Res.ok();
    }
}
