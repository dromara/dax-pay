package cn.daxpay.open.channel.douyin.strategy.direct.alloc;

import cn.daxpay.open.channel.douyin.client.credential.DouyinSdkCredential;
import cn.daxpay.open.channel.douyin.dao.direct.DouyinDirectAllocReceiverManager;
import cn.daxpay.open.channel.douyin.service.direct.DouyinDirectConfigAssembler;
import cn.daxpay.open.channel.douyin.service.payment.alloc.DouyinAllocService;
import cn.daxpay.open.platform.system.service.config.infra.PlatformUrlConfigService;
import cn.daxpay.open.payment.strategy.alloc.AbsAllocStrategy;
import cn.daxpay.open.payment.strategy.alloc.AllocStrategyContext;
import cn.daxpay.open.payment.trade.alloc.bo.AllocResultBo;
import cn.daxpay.open.payment.trade.alloc.entity.AllocDetail;
import cn.daxpay.open.payment.trade.alloc.enums.AllocReceiverTypeEnum;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.code.DaxPayErrorCode;
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

/// # 抖音分账策略
///
/// 按 channel=douyin 注册。凭证组装继承原支付 capability。
/// 抖音分账接口: splitFund + querySplitFund + 异步回调。
/// 接收方类型: MERCHANT_ID / PERSONAL_OPENID。
@Slf4j
@Service
@RequiredArgsConstructor
public class DouyinAllocStrategy extends AbsAllocStrategy {

    private static final Set<String> SUPPORTED_RECEIVER_TYPES = Set.of(
            AllocReceiverTypeEnum.MERCHANT_ID.getCode(),
            AllocReceiverTypeEnum.PERSONAL_OPENID.getCode());

    private final DouyinAllocService douyinAllocService;
    private final DouyinDirectConfigAssembler douyinDirectConfigAssembler;
    private final DouyinDirectAllocReceiverManager douyinDirectAllocReceiverManager;
    private final PlatformUrlConfigService platformUrlConfigService;

    @Override
    public String getChannel() {
        return "douyin";
    }

    @Override
    public void doValidateParam(AllocStrategyContext context) {
        for (AllocDetail detail : context.getDetails()) {
            if (!SUPPORTED_RECEIVER_TYPES.contains(detail.getReceiverType())) {
                throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                        "error.channel.douyin.allocReceiverTypeInvalid", detail.getReceiverType());
            }
            if (StrUtil.isBlank(detail.getReceiverAccount())) {
                throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                        "error.channel.douyin.allocReceiverAccountRequired");
            }
            // 个人 openid 必填姓名
            if (AllocReceiverTypeEnum.PERSONAL_OPENID.getCode().equals(detail.getReceiverType())
                    && StrUtil.isBlank(detail.getReceiverName())) {
                throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                        "error.channel.douyin.allocPersonalNameRequired");
            }
            // openid 与发起应用一致性: openid 是 appid 维度账号, 绑定应用与分账发起应用不一致会被抖音拒绝
            if (AllocReceiverTypeEnum.PERSONAL_OPENID.getCode().equals(detail.getReceiverType())) {
                this.validateReceiverAppMatch(context, detail);
            }
        }
    }

    /// 校验 openid 接收方绑定时所用应用与分账发起应用一致
    ///
    /// 仅对系统内已绑定(bound)档案校验; 查不到档案(调用方自行在通道侧绑定)或非 bound 状态放行。
    /// 发起应用优先取分账单快照(继承原支付单, 见 [AllocStrategyContext#getChannelAppId]);
    /// 存量订单快照为空时回退按 capability 路由解析。
    private void validateReceiverAppMatch(AllocStrategyContext context, AllocDetail detail) {
        String resolved = context.getChannelAppId();
        if (StrUtil.isBlank(resolved)) {
            // 存量订单无快照: 回退按能力绑/推导解析
            DouyinSdkCredential credential = douyinDirectConfigAssembler.buildConfig(
                    context.getMchNo(), context.getChannelMchNo(), context.getAllocOrder().getCapability(), null);
            resolved = credential.getDouyinAppId();
        }
        final String allocAppId = resolved;
        String accountHash = SecureUtil.sha256(detail.getReceiverAccount());
        douyinDirectAllocReceiverManager.findBoundByChannelMchNoAndTypeAndHash(
                        context.getChannelMchNo(), detail.getReceiverType(), accountHash)
                .ifPresent(receiver -> {
                    if (!Objects.equals(receiver.getChannelAppId(), allocAppId)) {
                        // openid 接收方绑定于其他应用, 与分账发起应用不一致
                        throw new BizInfoException(CommonErrorCode.UN_SUPPORTED_OPERATE,
                                "error.channel.douyin.allocReceiverAppMismatch", receiver.getChannelAppId());
                    }
                });
    }

    @Override
    public AllocResultBo doAlloc(AllocStrategyContext context) {
        DouyinSdkCredential credential = buildCredential(context);
        List<DouyinAllocService.AllocDetailInfo> details = buildDetailInfos(context);
        String notifyUrl = buildNotifyUrl(context);
        return douyinAllocService.alloc(details,
                context.getAllocOrder().getAllocNo(),
                context.getOutOrderNo(),
                notifyUrl, credential);
    }

    @Override
    public AllocResultBo doSync(AllocStrategyContext context) {
        DouyinSdkCredential credential = buildCredential(context);
        return douyinAllocService.sync(context.getAllocOrder().getAllocNo(),
                context.getOutOrderNo(), credential);
    }

    private DouyinSdkCredential buildCredential(AllocStrategyContext context) {
        // channelAppId 为分账单快照(继承原支付单); 存量订单为空时回退按能力绑/推导解析
        return douyinDirectConfigAssembler.buildConfig(
                context.getMchNo(), context.getChannelMchNo(),
                context.getAllocOrder().getCapability(), context.getChannelAppId());
    }

    private List<DouyinAllocService.AllocDetailInfo> buildDetailInfos(AllocStrategyContext context) {
        List<DouyinAllocService.AllocDetailInfo> result = new ArrayList<>();
        for (AllocDetail detail : context.getDetails()) {
            result.add(new DouyinAllocService.AllocDetailInfo(
                    detail.getReceiverType(),
                    detail.getReceiverAccount(),
                    detail.getReceiverName(),
                    detail.getAmount()));
        }
        return result;
    }

    /// 生成抖音分账异步通知地址(抖音→平台)
    private String buildNotifyUrl(AllocStrategyContext context) {
        String base = platformUrlConfigService.getUrlConfig().getBackendBaseUrl();
        if (StrUtil.isBlank(base)) {
            throw new BizInfoException(DaxPayErrorCode.CONFIG_ERROR, "error.common.backendBaseUrlNotConfigured");
        }
        return StrUtil.format("{}/unipay/callback/{}/{}/douyin/alloc",
                base, context.getMchNo(), context.getChannelMchNo());
    }
}
