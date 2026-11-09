package cn.daxpay.open.payment.merchant.service.system;

import cn.daxpay.open.platform.capability.audit.log.entity.LoginLogDb;
import cn.daxpay.open.platform.capability.audit.log.param.LoginLogQuery;
import cn.daxpay.open.platform.capability.audit.log.result.LoginLogResult;
import cn.daxpay.open.platform.common.mybatisplus.util.MpUtil;
import cn.daxpay.open.platform.core.enums.client.ClientEnum;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import cn.daxpay.open.payment.merchant.dao.system.MchLoginLogManager;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

/// # 登录日志(商户端)
///
/// 只读查询本商户用户的登录活动; 按 [MchLogScopeService] 白名单强制过滤, 不开放清理。
@Service
@RequiredArgsConstructor
public class MchLoginLogService {

    private final MchLoginLogManager mchLoginLogManager;
    private final MchLogScopeService mchLogScopeService;

    /// 分页查询
    public PageResult<LoginLogResult> page(PageParam pageParam, LoginLogQuery query) {
        // 强制商户端身份域, 防止按其他端过滤; userId 维度由白名单统一管控, 忽略入参
        query.setClient(ClientEnum.MERCHANT.getCode());
        query.setUserId(null);
        MchLogScopeService.MerchantLogScope scope = mchLogScopeService.resolve();
        if (scope.userIds().isEmpty() && scope.accounts().isEmpty()) {
            // 白名单为空直接返回空页, 禁止退化全表
            return new PageResult<>();
        }
        Page<LoginLogDb> page = mchLoginLogManager.pageByScope(pageParam, query, scope.userIds(), scope.accounts());
        PageResult<LoginLogResult> pageResult = MpUtil.toPageResult(page);
        pageResult.getRecords().forEach(this::sanitizeForMerchant);
        return pageResult;
    }

    /// 详情
    public LoginLogResult findById(Long id) {
        MchLogScopeService.MerchantLogScope scope = mchLogScopeService.resolve();
        LoginLogResult result = mchLoginLogManager.findById(id)
                .map(LoginLogDb::toResult)
                .orElseThrow(DataNotExistException::new);
        if (!inScope(result, scope)) {
            // 非本商户数据按不存在处理, 防止越权探测
            throw new DataNotExistException();
        }
        return sanitizeForMerchant(result);
    }

    /// 记录是否属于当前商户白名单(与分页过滤条件语义一致: 商户端身份域 + userId 或 account 命中)
    private boolean inScope(LoginLogResult result, MchLogScopeService.MerchantLogScope scope) {
        if (!ClientEnum.MERCHANT.getCode().equals(result.getClient())) {
            return false;
        }
        boolean userIdHit = Objects.nonNull(result.getUserId()) && scope.userIds().contains(result.getUserId());
        boolean accountHit = Objects.nonNull(result.getAccount()) && scope.accounts().contains(result.getAccount());
        return userIdHit || accountHit;
    }

    /// 商户端字段裁剪: userId 对商户无意义不下发, account 保留供辨认操作人(最小披露原则)
    private LoginLogResult sanitizeForMerchant(LoginLogResult result) {
        result.setUserId(null);
        return result;
    }
}
