package cn.daxpay.open.channel.alipay.strategy.direct.transfer;

import cn.daxpay.open.channel.alipay.client.credential.AlipaySdkCredential;
import cn.daxpay.open.channel.alipay.service.direct.AlipayDirectConfigAssembler;
import cn.daxpay.open.channel.alipay.service.payment.transfer.AlipayTransferService;
import cn.daxpay.open.payment.strategy.transfer.AbsTransferStrategy;
import cn.daxpay.open.payment.strategy.transfer.TransferStrategyContext;
import cn.daxpay.open.payment.trade.transfer.bo.TransferResultBo;
import cn.daxpay.open.payment.trade.transfer.enums.TransferPayeeTypeEnum;
import cn.daxpay.open.payment.trade.transfer.param.TransferParam;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Set;

/// # 支付宝直连转账策略
///
/// 支付宝单笔转账(uni.transfer)的通道策略。
/// 通道差异:
/// - 支持三种收款人类型([TransferPayeeTypeEnum]: user_id/open_id/login_name)
/// - 收款人姓名非必填, 但部分场景(转账到他人账号)支付宝要求姓名校验
@Slf4j
@Service
@RequiredArgsConstructor
public class AlipayTransferStrategy extends AbsTransferStrategy {

    /// 支付宝支持的收款人类型
    private static final Set<String> SUPPORTED_PAYEE_TYPES = Set.of(
            TransferPayeeTypeEnum.USER_ID.getCode(),
            TransferPayeeTypeEnum.OPEN_ID.getCode(),
            TransferPayeeTypeEnum.LOGIN_NAME.getCode());

    private final AlipayTransferService alipayTransferService;
    private final AlipayDirectConfigAssembler alipayDirectConfigAssembler;

    @Override
    public String getChannel() {
        return "alipay";
    }

    /// 通道特有参数校验
    @Override
    public void doValidateParam(TransferParam param) {
        if (!SUPPORTED_PAYEE_TYPES.contains(param.getPayeeType())) {
            // 支付宝: 不支持的收款人账号类型
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "error.channel.alipay.transferPayeeTypeInvalid", param.getPayeeType());
        }
        if (StrUtil.isBlank(param.getPayeeAccount())) {
            // 支付宝: 收款人账号必填
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "error.channel.alipay.transferPayeeAccountRequired");
        }
    }

    /// 发起转账
    @Override
    public TransferResultBo doTransfer(TransferStrategyContext context) {
        AlipaySdkCredential credential = this.buildCredential(context);
        return alipayTransferService.transfer(context, credential);
    }

    /// 同步查询
    @Override
    public TransferResultBo doSync(TransferStrategyContext context) {
        AlipaySdkCredential credential = this.buildCredential(context);
        return alipayTransferService.sync(context, credential);
    }

    /// 组装通道调用凭证(转账无能力维度, 走直连密钥配置)
    private AlipaySdkCredential buildCredential(TransferStrategyContext context) {
        return alipayDirectConfigAssembler.buildConfig(
                context.getMchNo(), context.getChannelMchNo(), null);
    }
}
