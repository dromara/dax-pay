package cn.daxpay.open.channel.wechat.strategy.direct.alloc;

import cn.daxpay.open.channel.wechat.client.credential.WechatSdkCredential;
import cn.daxpay.open.channel.wechat.service.direct.WechatDirectConfigAssembler;
import cn.daxpay.open.channel.wechat.service.payment.alloc.WechatAllocService;
import cn.daxpay.open.payment.strategy.alloc.AbsAllocStrategy;
import cn.daxpay.open.payment.strategy.alloc.AllocStrategyContext;
import cn.daxpay.open.payment.trade.alloc.bo.AllocResultBo;
import cn.daxpay.open.payment.trade.alloc.entity.AllocDetail;
import cn.daxpay.open.payment.trade.alloc.enums.AllocReceiverTypeEnum;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/// # 微信分账策略
///
/// 按 channel=wechat 注册。凭证组装继承原支付 capability + channelAppId。
///
/// 微信分账接口:
/// - 发起 V3 profitsharing/orders(unfreeze_unsplit=true 自动解冻剩余)
/// - 同步 profitsharing/orders/{out_order_no}
/// 纯查询式, 无异步回调。接收方类型: MERCHANT_ID / PERSONAL_OPENID。
@Slf4j
@Service
@RequiredArgsConstructor
public class WechatAllocStrategy extends AbsAllocStrategy {

    /// 微信分账支持的接收方类型
    private static final Set<String> SUPPORTED_RECEIVER_TYPES = Set.of(
            AllocReceiverTypeEnum.MERCHANT_ID.getCode(),
            AllocReceiverTypeEnum.PERSONAL_OPENID.getCode());

    private final WechatAllocService wechatAllocService;
    private final WechatDirectConfigAssembler wechatDirectConfigAssembler;

    @Override
    public String getChannel() {
        return "wechat";
    }

    @Override
    public void doValidateParam(AllocStrategyContext context) {
        for (AllocDetail detail : context.getDetails()) {
            if (!SUPPORTED_RECEIVER_TYPES.contains(detail.getReceiverType())) {
                throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                        "error.channel.wechat.allocReceiverTypeInvalid", detail.getReceiverType());
            }
            if (StrUtil.isBlank(detail.getReceiverAccount())) {
                throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                        "error.channel.wechat.allocReceiverAccountRequired");
            }
            // 个人 openid 必填姓名
            if (AllocReceiverTypeEnum.PERSONAL_OPENID.getCode().equals(detail.getReceiverType())
                    && StrUtil.isBlank(detail.getReceiverName())) {
                throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                        "error.channel.wechat.allocPersonalNameRequired");
            }
        }
    }

    @Override
    public AllocResultBo doAlloc(AllocStrategyContext context) {
        WechatSdkCredential credential = buildCredential(context);
        List<WechatAllocService.AllocDetailInfo> details = buildDetailInfos(context);
        return wechatAllocService.alloc(details,
                context.getAllocOrder().getAllocNo(),
                context.getOutOrderNo(),
                credential);
    }

    @Override
    public AllocResultBo doSync(AllocStrategyContext context) {
        WechatSdkCredential credential = buildCredential(context);
        return wechatAllocService.sync(context.getAllocOrder().getAllocNo(),
                context.getOutOrderNo(), credential);
    }

    /// 组装通道调用凭证(继承原支付 capability + channelAppId)
    private WechatSdkCredential buildCredential(AllocStrategyContext context) {
        return wechatDirectConfigAssembler.buildConfig(
                context.getMchNo(),
                context.getChannelMchNo(),
                context.getAllocOrder().getCapability(),
                context.getChannelAppId());
    }

    private List<WechatAllocService.AllocDetailInfo> buildDetailInfos(AllocStrategyContext context) {
        List<WechatAllocService.AllocDetailInfo> result = new ArrayList<>();
        for (AllocDetail detail : context.getDetails()) {
            result.add(new WechatAllocService.AllocDetailInfo(
                    detail.getReceiverType(),
                    detail.getReceiverAccount(),
                    detail.getReceiverName(),
                    detail.getAmount()));
        }
        return result;
    }
}
