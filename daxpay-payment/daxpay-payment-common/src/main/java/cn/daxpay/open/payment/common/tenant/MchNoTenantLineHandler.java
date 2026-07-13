package cn.daxpay.open.payment.common.tenant;

import cn.daxpay.open.platform.common.mybatisplus.util.MpUtil;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.enums.client.ClientEnum;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.iam.service.client.ClientCodeService;
import cn.daxpay.open.payment.common.context.PaymentContext;
import cn.daxpay.open.payment.common.context.TradeActor;
import cn.daxpay.open.payment.common.entity.MchBaseEntity;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import lombok.RequiredArgsConstructor;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.schema.Column;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

/// # mch_no 行级数据隔离内核（MyBatis-Plus TenantLine）
///
/// 跨端共用：商户端 / 开放 API / 网关 / 回调等，只要访问 [MchBaseEntity] 表即拼 `mch_no`。
/// **不感知身份从哪来**——由各端 Bootstrap（登录 Filter / 验签切面 / 回调 path / 订单引导装载）
/// 写入 [PaymentContext]，本类只读。
///
/// 运营端（[ClientEnum#ADMIN]）全局忽略。
/// 非运营端且上下文无有效 mchNo 时 **fail-closed**（禁止静默 `mch_no = ''`）。
///
/// 详见 `_doc/design/mch-no-tenant-isolation.md`。
@Component
@RequiredArgsConstructor
public class MchNoTenantLineHandler implements TenantLineHandler {
    private final ClientCodeService clientCodeService;
    private final PaymentContext paymentContext;

    /// 获取租户 ID（当前线程商户号）
    @Override
    public Expression getTenantId() {
        String mchNo = paymentContext.currentActor()
                .map(TradeActor::getMchNo)
                .filter(StrUtil::isNotBlank)
                .orElseThrow(() -> new BizInfoException(
                        CommonErrorCode.SYSTEM_ERROR, "pay.error.assist.mchContextMissing"));
        return new StringValue(mchNo);
    }

    /// 租户字段：统一 mch_no
    @Override
    public String getTenantIdColumn() {
        return "mch_no";
    }

    /// 是否忽略租户拦截：运营端不隔离，其余端按实体类型
    @Override
    public boolean ignoreTable(String tableName) {
        String clientCode = clientCodeService.getClientCode();
        if (Objects.equals(clientCode, ClientEnum.ADMIN.getCode())) {
            return true;
        }
        return ignoreTableByMch(tableName);
    }

    /// 忽略插入租户字段：列已存在则不重复处理
    @Override
    public boolean ignoreInsert(List<Column> columns, String tenantIdColumn) {
        return TenantLineHandler.super.ignoreInsert(columns, tenantIdColumn);
    }

    /// 仅对 [MchBaseEntity] 子类启用 mch_no 过滤
    public boolean ignoreTableByMch(String tableName) {
        TableInfo tableInfo = MpUtil.getTableInfo(tableName);
        if (tableInfo == null) {
            return true;
        }
        boolean anyMatch = Stream.of(MchBaseEntity.class)
                .anyMatch(entityClass -> ClassUtil.isAssignable(entityClass, tableInfo.getEntityType()));
        return !anyMatch;
    }
}
