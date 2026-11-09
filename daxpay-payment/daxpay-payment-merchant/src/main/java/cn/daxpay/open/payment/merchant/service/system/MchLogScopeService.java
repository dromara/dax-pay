package cn.daxpay.open.payment.merchant.service.system;

import cn.daxpay.open.platform.core.code.CommonCode;
import cn.daxpay.open.platform.core.enums.client.ClientEnum;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.iam.dao.user.UserInfoManager;
import cn.daxpay.open.platform.iam.entity.user.UserInfo;
import cn.daxpay.open.payment.common.context.PaymentContext;
import cn.daxpay.open.payment.merchant.dao.info.MerchantUserManager;
import cn.daxpay.open.payment.merchant.entity.info.MerchantUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/// # 商户日志过滤范围解析
///
/// 登录/操作日志表无 mchNo 列, 租户行隔离不生效, 越权防护全靠应用层:
/// 通过 [MerchantUser] 关联解析出当前商户可见的 userId 集与账号集, 供两日志查询拼装白名单条件。
@Service
@RequiredArgsConstructor
public class MchLogScopeService {

    private final PaymentContext paymentContext;
    private final MerchantUserManager merchantUserManager;
    private final UserInfoManager userInfoManager;

    /// 商户日志白名单范围
    ///
    /// @param userIds  本商户关联的用户 id 集(登录失败日志不写 userId, 需账号条件兜底)
    /// @param accounts 本商户在商户端身份域下的账号集(账号唯一性按端隔离)
    public record MerchantLogScope(List<Long> userIds, List<String> accounts) {
    }

    /// 解析当前商户的日志可见范围
    public MerchantLogScope resolve() {
        String mchNo = requireMchNo();
        List<MerchantUser> merchantUsers = merchantUserManager.findAllByField(MerchantUser::getMchNo, mchNo);
        if (merchantUsers.isEmpty()) {
            return new MerchantLogScope(List.of(), List.of());
        }
        List<Long> userIds = merchantUsers.stream().map(MerchantUser::getUserId).toList();
        // 只保留商户端身份域的账号, 账号唯一性按端隔离, 防跨端同名账号串数据
        List<String> accounts = userInfoManager.findAllByIds(userIds).stream()
                .filter(user -> ClientEnum.MERCHANT.getCode().equals(user.getClientCode()))
                .map(UserInfo::getAccount)
                .toList();
        return new MerchantLogScope(userIds, accounts);
    }

    private String requireMchNo() {
        String mchNo = paymentContext.getMchNo();
        if (Objects.isNull(mchNo) || mchNo.isBlank()) {
            // 商户: 数据错误未发现商户号
            throw new BizInfoException(CommonCode.FAIL_CODE, "error.payment.merchant.dataErrorNoMchNo");
        }
        return mchNo;
    }
}
