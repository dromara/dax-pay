package cn.daxpay.open.channel.douyin.strategy.transfer;

import cn.daxpay.open.channel.douyin.client.credential.DouyinSdkCredential;
import cn.daxpay.open.channel.douyin.dao.direct.DouyinDirectChannelMerchantManager;
import cn.daxpay.open.channel.douyin.entity.direct.DouyinDirectChannelMerchant;
import cn.daxpay.open.channel.douyin.service.direct.DouyinDirectConfigAssembler;
import cn.daxpay.open.channel.douyin.service.payment.transfer.DouyinTransferService;
import cn.daxpay.open.payment.strategy.transfer.AbsTransferStrategy;
import cn.daxpay.open.payment.strategy.transfer.TransferStrategyContext;
import cn.daxpay.open.payment.trade.transfer.bo.TransferResultBo;
import cn.daxpay.open.payment.trade.transfer.enums.TransferPayeeTypeEnum;
import cn.daxpay.open.payment.trade.transfer.param.TransferParam;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.code.DaxPayErrorCode;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 抖音直连转账策略
///
/// 抖音商家转账的通道策略。
/// 通道差异:
/// - openid 收款人([TransferPayeeTypeEnum#OPENID])
/// - transfer_scene_id 取自通道商户配置, 未配置时报错
/// - 金额大于等于 2000 元必填收款人姓名(子应用加密上送)
@Slf4j
@Service
@RequiredArgsConstructor
public class DouyinTransferStrategy extends AbsTransferStrategy {

    /// 大额档位(分): 大于等于该金额必填姓名
    private static final long LARGE_AMOUNT_LIMIT = 200_000L;

    private final DouyinTransferService douyinTransferService;
    private final DouyinDirectConfigAssembler douyinDirectConfigAssembler;
    private final DouyinDirectChannelMerchantManager douyinDirectChannelMerchantManager;

    @Override
    public String getChannel() {
        return "douyin";
    }

    /// 通道特有参数校验
    @Override
    public void doValidateParam(TransferParam param) {
        // 抖音仅支持 openid 收款
        if (!StrUtil.equals(param.getPayeeType(), TransferPayeeTypeEnum.OPENID.getCode())) {
            // 抖音: 仅支持 openid 收款人
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "error.channel.douyin.transferOnlyOpenid");
        }
        long amountFen = param.getAmount().movePointRight(2).longValue();
        if (amountFen >= LARGE_AMOUNT_LIMIT && StrUtil.isBlank(param.getPayeeName())) {
            // 抖音: 大于等于2000元必须填收款人姓名
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "error.channel.douyin.transferNameRequired");
        }
    }

    /// 发起转账
    @Override
    public TransferResultBo doTransfer(TransferStrategyContext context) {
        DouyinSdkCredential credential = this.buildCredential(context);
        return douyinTransferService.transfer(context, credential);
    }

    /// 同步查询
    @Override
    public TransferResultBo doSync(TransferStrategyContext context) {
        DouyinSdkCredential credential = this.buildCredential(context);
        return douyinTransferService.sync(context, credential);
    }

    /// 组装通道调用凭证并注入转账场景
    ///
    /// 转账场景(transfer_scene_id)从通道商户配置读取, 经上下文回写, 由编排层在"处理中"镜像落库。
    private DouyinSdkCredential buildCredential(TransferStrategyContext context) {
        DouyinDirectChannelMerchant channelMerchant = douyinDirectChannelMerchantManager.lambdaQuery()
                .eq(DouyinDirectChannelMerchant::getChannelMchNo, context.getChannelMchNo())
                .oneOpt()
                .orElseThrow(() -> new BizInfoException(DaxPayErrorCode.CONFIG_NOT_EXIST,
                        "error.payment.channel.channelMerchantNotExist"));
        if (StrUtil.isBlank(channelMerchant.getTransferScene())) {
            // 抖音: 转账场景未配置
            throw new BizInfoException(DaxPayErrorCode.CONFIG_NOT_EXIST,
                    "error.channel.douyin.transferSceneNotConfigured");
        }
        context.setTransferScene(channelMerchant.getTransferScene());
        return douyinDirectConfigAssembler.buildConfig(
                context.getMchNo(), context.getChannelMchNo(), null);
    }
}

