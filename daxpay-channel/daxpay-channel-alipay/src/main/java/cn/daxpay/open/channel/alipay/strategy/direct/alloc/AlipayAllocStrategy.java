package cn.daxpay.open.channel.alipay.strategy.direct.alloc;

import cn.daxpay.open.channel.alipay.client.credential.AlipaySdkCredential;
import cn.daxpay.open.channel.alipay.service.direct.AlipayDirectConfigAssembler;
import cn.daxpay.open.channel.alipay.service.payment.alloc.AlipayAllocService;
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

/// # 支付宝分账策略
///
/// 按 channel=alipay 注册, 与 [cn.daxpay.open.channel.alipay.strategy.direct.transfer.AlipayTransferStrategy] 同级。
/// 凭证组装继承原支付 capability(分账不二次路由), 用 [AlipayDirectConfigAssembler#buildConfig]。
///
/// 支付宝分账接口:
/// - 发起 alipay.trade.order.settle(royalty_mode=async)
/// - 同步 alipay.trade.order.settle.query
/// 纯查询式, 无异步回调。
@Slf4j
@Service
@RequiredArgsConstructor
public class AlipayAllocStrategy extends AbsAllocStrategy {

    /// 支付宝分账支持的接收方类型(userId / loginName)
    private static final Set<String> SUPPORTED_RECEIVER_TYPES = Set.of(
            AllocReceiverTypeEnum.USER_ID.getCode(),
            AllocReceiverTypeEnum.LOGIN_NAME.getCode());

    private final AlipayAllocService alipayAllocService;
    private final AlipayDirectConfigAssembler alipayDirectConfigAssembler;

    @Override
    public String getChannel() {
        return "alipay";
    }

    /// 通道特有参数校验(支付宝仅支持 userId / loginName)
    @Override
    public void doValidateParam(AllocStrategyContext context) {
        for (AllocDetail detail : context.getDetails()) {
            if (!SUPPORTED_RECEIVER_TYPES.contains(detail.getReceiverType())) {
                throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                        "error.channel.alipay.allocReceiverTypeInvalid", detail.getReceiverType());
            }
            if (StrUtil.isBlank(detail.getReceiverAccount())) {
                throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                        "error.channel.alipay.allocReceiverAccountRequired");
            }
        }
    }

    @Override
    public AllocResultBo doAlloc(AllocStrategyContext context) {
        AlipaySdkCredential credential = buildCredential(context);
        List<AlipayAllocService.AllocDetailInfo> details = buildDetailInfos(context);
        return alipayAllocService.alloc(details,
                context.getAllocOrder().getAllocNo(),
                context.getOutOrderNo(),
                credential);
    }

    @Override
    public AllocResultBo doSync(AllocStrategyContext context) {
        AlipaySdkCredential credential = buildCredential(context);
        List<String> receiverAccounts = context.getDetails().stream()
                .map(AllocDetail::getReceiverAccount)
                .toList();
        return alipayAllocService.sync(context.getAllocOrder().getAllocNo(),
                context.getOutOrderNo(), receiverAccounts, credential);
    }

    /// 组装通道调用凭证(继承原支付 capability, 不二次路由;
    /// channelAppId 为分账单快照(继承原支付单), 存量订单为空时回退能力关联解析)
    private AlipaySdkCredential buildCredential(AllocStrategyContext context) {
        String capability = context.getAllocOrder().getCapability();
        return alipayDirectConfigAssembler.buildConfig(
                context.getMchNo(), context.getChannelMchNo(), capability, context.getChannelAppId());
    }

    /// 构建分账明细信息(映射接收方类型为支付宝原生 userId / loginName)
    private List<AlipayAllocService.AllocDetailInfo> buildDetailInfos(AllocStrategyContext context) {
        List<AlipayAllocService.AllocDetailInfo> result = new ArrayList<>();
        for (AllocDetail detail : context.getDetails()) {
            // 映射接收方类型: USER_ID → userId, LOGIN_NAME → loginName
            String transInType = mapTransInType(detail.getReceiverType());
            result.add(new AlipayAllocService.AllocDetailInfo(
                    transInType, detail.getReceiverAccount(), detail.getAmount()));
        }
        return result;
    }

    /// 映射接收方类型为支付宝原生编码
    private String mapTransInType(String receiverType) {
        if (AllocReceiverTypeEnum.USER_ID.getCode().equals(receiverType)) {
            return "userId";
        }
        if (AllocReceiverTypeEnum.LOGIN_NAME.getCode().equals(receiverType)) {
            return "loginName";
        }
        return receiverType;
    }
}
