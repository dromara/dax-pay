package cn.daxpay.open.channel.wechat.strategy.direct.transfer;

import cn.daxpay.open.channel.wechat.client.credential.WechatSdkCredential;
import cn.daxpay.open.channel.wechat.dao.direct.WechatDirectChannelMerchantManager;
import cn.daxpay.open.channel.wechat.entity.direct.WechatDirectChannelMerchant;
import cn.daxpay.open.channel.wechat.service.direct.WechatDirectConfigAssembler;
import cn.daxpay.open.channel.wechat.service.payment.transfer.WechatTransferService;
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

/// # 微信直连转账策略
///
/// 微信商家转账到零钱(V3)的通道策略。
/// 通道差异:
/// - 仅支持 openid 收款人([TransferPayeeTypeEnum#OPENID])
/// - 金额档位姓名校验: 小于 0.3 元禁填姓名, 大于等于 2000 元必填姓名
/// - transfer_scene 取自通道商户配置, 未配置时报错
@Slf4j
@Service
@RequiredArgsConstructor
public class WechatTransferStrategy extends AbsTransferStrategy {

    /// 小额档位(分): 小于该金额禁填姓名
    private static final long SMALL_AMOUNT_LIMIT = 30L;
    /// 大额档位(分): 大于等于该金额必填姓名
    private static final long LARGE_AMOUNT_LIMIT = 200_000L;

    private final WechatTransferService wechatTransferService;
    private final WechatDirectConfigAssembler wechatDirectConfigAssembler;
    private final WechatDirectChannelMerchantManager wechatDirectChannelMerchantManager;

    @Override
    public String getChannel() {
        return "wechat";
    }

    /// 通道特有参数校验
    @Override
    public void doValidateParam(TransferParam param) {
        // 微信仅支持 openid 收款
        if (!StrUtil.equals(param.getPayeeType(), TransferPayeeTypeEnum.OPENID.getCode())) {
            // 微信: 仅支持 openid 收款人
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "error.channel.wechat.transferOnlyOpenid");
        }
        long amountFen = param.getAmount().movePointRight(2).longValue();
        String payeeName = param.getPayeeName();
        if (amountFen < SMALL_AMOUNT_LIMIT && StrUtil.isNotBlank(payeeName)) {
            // 微信: 小于0.3元不允许填收款人姓名
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "error.channel.wechat.transferNameForbidden");
        }
        if (amountFen >= LARGE_AMOUNT_LIMIT && StrUtil.isBlank(payeeName)) {
            // 微信: 大于等于2000元必须填收款人姓名
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "error.channel.wechat.transferNameRequired");
        }
    }

    /// 发起转账
    @Override
    public TransferResultBo doTransfer(TransferStrategyContext context) {
        WechatSdkCredential credential = this.buildCredential(context);
        return wechatTransferService.transfer(context, credential);
    }

    /// 同步查询
    @Override
    public TransferResultBo doSync(TransferStrategyContext context) {
        WechatSdkCredential credential = this.buildCredential(context);
        return wechatTransferService.sync(context, credential);
    }

    /// 组装通道调用凭证并注入转账场景
    ///
    /// 转账场景(transfer_scene)从通道商户配置读取, 经上下文回写, 由编排层在"处理中"镜像落库。
    private WechatSdkCredential buildCredential(TransferStrategyContext context) {
        WechatDirectChannelMerchant channelMerchant = wechatDirectChannelMerchantManager.lambdaQuery()
                .eq(WechatDirectChannelMerchant::getChannelMchNo, context.getChannelMchNo())
                .oneOpt()
                .orElseThrow(() -> new BizInfoException(DaxPayErrorCode.CONFIG_NOT_EXIST,
                        "error.payment.channel.channelMerchantNotExist"));
        if (StrUtil.isBlank(channelMerchant.getTransferScene())) {
            // 微信: 转账场景未配置
            throw new BizInfoException(DaxPayErrorCode.CONFIG_NOT_EXIST,
                    "error.channel.wechat.transferSceneNotConfigured");
        }
        context.setTransferScene(channelMerchant.getTransferScene());
        return wechatDirectConfigAssembler.buildConfig(
                context.getMchNo(), context.getChannelMchNo(), null, null);
    }
}
