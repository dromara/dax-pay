package cn.daxpay.open.channel.alipay.strategy.direct.transfer;

import cn.daxpay.open.channel.alipay.client.credential.AlipaySdkCredential;
import cn.daxpay.open.channel.alipay.dao.direct.AlipayTransferConfigManager;
import cn.daxpay.open.channel.alipay.entity.direct.AlipayTransferConfig;
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

import java.math.BigDecimal;
import java.util.Set;

/// # 支付宝直连转账策略
///
/// 支付宝单笔转账(uni.transfer)的通道策略。
/// 通道差异:
/// - 支持两种收款人类型([TransferPayeeTypeEnum]: user_id/login_name)
/// - 收款人姓名: 登录号(login_name)转账必填, 其余场景可选(填了则校验姓名一致性)
/// - 转账金额最低 0.1 元(文档 trans_amount 取值范围)
/// - 转账金额达到 50000 元时, 付款理由(remark)必填(监管要求)
@Slf4j
@Service
@RequiredArgsConstructor
public class AlipayTransferStrategy extends AbsTransferStrategy {

    /// 支付宝支持的收款人类型(仅会员ID/登录号, 不支持开放ID)
    private static final Set<String> SUPPORTED_PAYEE_TYPES = Set.of(
            TransferPayeeTypeEnum.USER_ID.getCode(),
            TransferPayeeTypeEnum.LOGIN_NAME.getCode());

    /// 转账金额下限(元, 文档 trans_amount 取值范围 [0.1, 100000000])
    private static final BigDecimal AMOUNT_MIN = new BigDecimal("0.1");

    /// 大额档位(元): 达到该金额后付款理由必填(错误码 MEMO_REQUIRED_IN_TRANSFER_ERROR)
    private static final BigDecimal LARGE_AMOUNT_LIMIT = new BigDecimal("50000");

    private final AlipayTransferService alipayTransferService;
    private final AlipayDirectConfigAssembler alipayDirectConfigAssembler;
    private final AlipayTransferConfigManager alipayTransferConfigManager;

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
        if (StrUtil.isBlank(param.getTitle())) {
            // 支付宝: 转账标题必填(order_title 支付宝要求必选)
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "error.channel.alipay.transferTitleRequired");
        }
        // 登录号(login_name)转账时姓名必填(文档 payee_info.name 条件必填)
        if (TransferPayeeTypeEnum.LOGIN_NAME.getCode().equals(param.getPayeeType())
                && StrUtil.isBlank(param.getPayeeName())) {
            // 支付宝: 登录号收款时必须填写收款人姓名
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "error.channel.alipay.transferLogonNameRequired");
        }
        // 转账金额不可低于 0.1 元
        if (param.getAmount().compareTo(AMOUNT_MIN) < 0) {
            // 支付宝: 转账金额不可低于0.1元
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "error.channel.alipay.transferAmountMin");
        }
        // 转账金额达到 50000 元时, 付款理由必填(监管要求, 错误码 MEMO_REQUIRED_IN_TRANSFER_ERROR)
        if (param.getAmount().compareTo(LARGE_AMOUNT_LIMIT) >= 0 && StrUtil.isBlank(param.getReason())) {
            // 支付宝: 转账金额达到50000元时, 必须填写付款理由
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "error.channel.alipay.transferRemarkRequired");
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

    /// 组装通道调用凭证(转账无能力维度, 按转账配置显式绑定应用解析)
    private AlipaySdkCredential buildCredential(TransferStrategyContext context) {
        // 读取转账配置(一对一绑定转出应用, 未绑定不允许发起)
        AlipayTransferConfig transferConfig = alipayTransferConfigManager
                .findByChannelMchNo(context.getChannelMchNo())
                .orElseThrow(() -> new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                        "error.channel.alipay.transferAppNotConfigured"));
        if (transferConfig.getTransferAppRefId() == null) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "error.channel.alipay.transferAppNotConfigured");
        }
        return alipayDirectConfigAssembler.buildTransferConfig(
                context.getMchNo(), context.getChannelMchNo(), transferConfig.getTransferAppRefId());
    }
}
