package cn.daxpay.open.payment.merchant.result.info;

import cn.daxpay.open.platform.iam.result.user.UserWholeInfoResult;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 商户用户详情结果
///
/// 在 iam 全量用户信息(含登录与密码安全信息)基础上补充单角色信息,
/// 数据由 [cn.daxpay.open.platform.iam.service.user.UserAdminService#findDetail] 联查后拷贝填充。
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class MerchantUserDetailResult extends UserWholeInfoResult {

    /// 角色名称(单角色模型, 未分配时为空)
    private String roleName;

    /// 角色编码
    private String roleCode;
}
