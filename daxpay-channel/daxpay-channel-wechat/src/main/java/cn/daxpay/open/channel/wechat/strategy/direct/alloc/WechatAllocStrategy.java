package cn.daxpay.open.channel.wechat.strategy.direct.alloc;

import cn.daxpay.open.channel.wechat.client.credential.WechatSdkCredential;
import cn.daxpay.open.channel.wechat.dao.direct.WechatDirectAllocReceiverManager;
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
import cn.hutool.crypto.SecureUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
    private final WechatDirectAllocReceiverManager wechatDirectAllocReceiverManager;

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
            // openid 与发起应用一致性: openid 是 appid 维度账号, 绑定应用与分账发起应用不一致会被微信拒绝
            if (AllocReceiverTypeEnum.PERSONAL_OPENID.getCode().equals(detail.getReceiverType())) {
                this.validateReceiverAppMatch(context, detail);
            }
        }
    }

    /// 校验 openid 接收方绑定时所用应用与分账发起应用一致
    ///
    /// 仅对系统内已绑定(bound)档案校验; 查不到档案(调用方自行在通道侧绑定)或非 bound 状态放行。
    private void validateReceiverAppMatch(AllocStrategyContext context, AllocDetail detail) {
        String accountHash = SecureUtil.sha256(detail.getReceiverAccount());
        wechatDirectAllocReceiverManager.findBoundByChannelMchNoAndTypeAndHash(
                        context.getChannelMchNo(), detail.getReceiverType(), accountHash)
                .ifPresent(receiver -> {
                    if (!Objects.equals(receiver.getChannelAppId(), context.getChannelAppId())) {
                        // openid 接收方绑定于其他应用, 与分账发起应用不一致
                        throw new BizInfoException(CommonErrorCode.UN_SUPPORTED_OPERATE,
                                "error.channel.wechat.allocReceiverAppMismatch", receiver.getChannelAppId());
                    }
                });
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
