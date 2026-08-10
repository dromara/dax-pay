package cn.daxpay.open.channel.douyin.strategy.direct.alloc;

import cn.daxpay.open.channel.douyin.client.credential.DouyinSdkCredential;
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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
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
        }
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
        return douyinDirectConfigAssembler.buildConfig(
                context.getMchNo(), context.getChannelMchNo(),
                context.getAllocOrder().getCapability());
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
