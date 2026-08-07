package cn.daxpay.open.channel.douyin.strategy.transfer;

import cn.daxpay.open.channel.douyin.client.credential.DouyinSdkCredential;
import cn.daxpay.open.channel.douyin.dao.direct.DouyinTransferConfigManager;
import cn.daxpay.open.channel.douyin.entity.direct.DouyinTransferConfig;
import cn.daxpay.open.channel.douyin.enums.DouyinTransferSceneEnum;
import cn.daxpay.open.channel.douyin.service.direct.DouyinDirectConfigAssembler;
import cn.daxpay.open.channel.douyin.service.payment.transfer.DouyinTransferService;
import cn.daxpay.open.payment.strategy.transfer.AbsTransferStrategy;
import cn.daxpay.open.payment.strategy.transfer.TransferStrategyContext;
import cn.daxpay.open.payment.trade.transfer.bo.TransferResultBo;
import cn.daxpay.open.payment.trade.transfer.enums.TransferPayeeTypeEnum;
import cn.daxpay.open.payment.trade.transfer.param.TransferParam;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.exception.config.ConfigErrorException;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Set;

/// # 抖音直连转账策略
///
/// 抖音商家转账的通道策略。
/// 通道差异:
/// - 收款人支持 openid / phone(手机号) 两种([TransferPayeeTypeEnum])，
///   手机号需与抖音支付实名一致, 由子应用证书加密上送;
///   收款人 openId 由「转账发起应用」(网站应用)承接 H5 授权
/// - transfer_scene_id 为主数据枚举(1001-1007), 发起转账时由前端选择传入, 无需预配置
/// - 转账发起应用由通道商户的转账配置([DouyinTransferConfig])显式指定
/// - 金额大于等于 2000 元必填收款人姓名(子应用加密上送)
/// - 商户转账号(out_bill_no)规则: 6-32位, 仅数字/大小写字母/_/-/*
@Slf4j
@Service
@RequiredArgsConstructor
public class DouyinTransferStrategy extends AbsTransferStrategy {

    /// 大额档位(分): 大于等于该金额必填姓名
    private static final long LARGE_AMOUNT_LIMIT = 200_000L;

    /// 商户转账号规则(数字/大小写字母/_-*)
    private static final String OUT_BILL_NO_PATTERN = "^[0-9A-Za-z_\\-*]{6,32}$";

    /// 抖音支持的收款人类型
    private static final Set<String> SUPPORTED_PAYEE_TYPES = Set.of(
            TransferPayeeTypeEnum.OPENID.getCode(),
            TransferPayeeTypeEnum.PHONE.getCode());

    private final DouyinTransferService douyinTransferService;
    private final DouyinDirectConfigAssembler douyinDirectConfigAssembler;
    private final DouyinTransferConfigManager douyinTransferConfigManager;

    @Override
    public String getChannel() {
        return "douyin";
    }

    /// 通道特有参数校验
    @Override
    public void doValidateParam(TransferParam param) {
        // 抖音仅支持 openid / phone(手机号) 收款
        if (!SUPPORTED_PAYEE_TYPES.contains(param.getPayeeType())) {
            // 抖音: 不支持的收款人账号类型
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "error.channel.douyin.transferPayeeTypeInvalid", param.getPayeeType());
        }
        // 手机号模式: 11位数字且与抖音支付实名一致
        if (TransferPayeeTypeEnum.PHONE.getCode().equals(param.getPayeeType())
                && !param.getPayeeAccount().matches("^\\d{11}$")) {
            // 抖音: 收款手机号格式不正确(需为11位数字)
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "error.channel.douyin.transferPhoneInvalid");
        }
        long amountFen = param.getAmount().movePointRight(2).longValue();
        if (amountFen >= LARGE_AMOUNT_LIMIT && StrUtil.isBlank(param.getPayeeName())) {
            // 抖音: 大于等于2000元必须填收款人姓名
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "error.channel.douyin.transferNameRequired");
        }
        // 商户转账号规则(数字/大小写字母/_-*, 6-32位)
        if (!param.getBizTransferNo().matches(OUT_BILL_NO_PATTERN)) {
            // 抖音: 商户转账号格式不正确(6-32位, 仅数字/大小写字母/_/-/*)
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "error.channel.douyin.transferOutBillNoInvalid");
        }
        // 转账场景ID必填且必须为合法枚举
        if (StrUtil.isBlank(param.getTransferScene())) {
            // 抖音: 转账场景ID必填
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "error.channel.douyin.transferSceneIdRequired");
        }
        if (DouyinTransferSceneEnum.findByCode(param.getTransferScene()) == null) {
            // 抖音: 不支持的转账场景ID[{0}]
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "error.channel.douyin.transferSceneNameInvalid", param.getTransferScene());
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

    /// 组装通道调用凭证
    ///
    /// 转账场景(transfer_scene_id)来自请求参数(前端选择的主数据枚举), 已在 [#doValidateParam] 校验合法性。
    /// 转账发起应用(决定转出主体与收款人 openId 来源)由通道商户的转账配置显式指定,
    /// 读取 [DouyinTransferConfig#getTransferAppRefId] 组装凭证。
    private DouyinSdkCredential buildCredential(TransferStrategyContext context) {
        DouyinTransferConfig transferConfig = douyinTransferConfigManager
                .findByChannelMchNo(context.getChannelMchNo())
                .orElseThrow(() -> new ConfigErrorException("error.channel.douyin.transferAppNotConfigured"));
        if (transferConfig.getTransferAppRefId() == null) {
            // 抖音: 转账发起应用未配置
            throw new ConfigErrorException("error.channel.douyin.transferAppNotConfigured");
        }
        return douyinDirectConfigAssembler.buildTransferConfig(
                context.getMchNo(), context.getChannelMchNo(), transferConfig.getTransferAppRefId());
    }
}
