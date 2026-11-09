package cn.daxpay.open.payment.merchant.service.system;

import cn.daxpay.open.platform.capability.audit.log.entity.OperateLogDb;
import cn.daxpay.open.platform.capability.audit.log.param.OperateLogQuery;
import cn.daxpay.open.platform.capability.audit.log.result.OperateLogResult;
import cn.daxpay.open.platform.common.mybatisplus.util.MpUtil;
import cn.daxpay.open.platform.core.enums.client.ClientEnum;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import cn.daxpay.open.payment.merchant.dao.system.MchOperateLogManager;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

/// # 操作日志(商户端)
///
/// 只读查询本商户用户的操作记录; 按 [MchLogScopeService] 白名单强制过滤, 不开放清理。
@Service
@RequiredArgsConstructor
public class MchOperateLogService {

    private final MchOperateLogManager mchOperateLogManager;
    private final MchLogScopeService mchLogScopeService;

    /// 分页查询
    public PageResult<OperateLogResult> page(PageParam pageParam, OperateLogQuery query) {
        // 强制商户端身份域, 防止按其他端过滤
        query.setClient(ClientEnum.MERCHANT.getCode());
        MchLogScopeService.MerchantLogScope scope = mchLogScopeService.resolve();
        if (scope.userIds().isEmpty() && scope.accounts().isEmpty()) {
            // 白名单为空直接返回空页, 禁止退化全表
            return new PageResult<>();
        }
        Page<OperateLogDb> page = mchOperateLogManager.pageByScope(pageParam, query, scope.userIds(), scope.accounts());
        PageResult<OperateLogResult> pageResult = MpUtil.toPageResult(page);
        pageResult.getRecords().forEach(this::sanitizeForMerchant);
        return pageResult;
    }

    /// 详情
    public OperateLogResult findById(Long id) {
        MchLogScopeService.MerchantLogScope scope = mchLogScopeService.resolve();
        OperateLogResult result = mchOperateLogManager.findById(id)
                .map(OperateLogDb::toResult)
                .orElseThrow(DataNotExistException::new);
        if (!inScope(result, scope)) {
            // 非本商户数据按不存在处理, 防止越权探测
            throw new DataNotExistException();
        }
        return sanitizeForMerchant(result);
    }

    /// 记录是否属于当前商户白名单(与分页过滤条件语义一致: 商户端身份域 + operateId 或 account 命中)
    private boolean inScope(OperateLogResult result, MchLogScopeService.MerchantLogScope scope) {
        if (!ClientEnum.MERCHANT.getCode().equals(result.getClient())) {
            return false;
        }
        boolean operateIdHit = Objects.nonNull(result.getOperateId()) && scope.userIds().contains(result.getOperateId());
        boolean accountHit = Objects.nonNull(result.getAccount()) && scope.accounts().contains(result.getAccount());
        return operateIdHit || accountHit;
    }

    /// 商户端字段裁剪: method(类名#方法名)暴露平台内部结构不下发;
    /// operateParam/operateReturn 写入时已脱敏(见平台 AuditLogMaskService), 保留展示
    private OperateLogResult sanitizeForMerchant(OperateLogResult result) {
        result.setMethod(null);
        return result;
    }
}
