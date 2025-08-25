package org.dromara.daxpay.server.handler;

import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.iam.service.client.ClientCodeService;
import org.dromara.daxpay.service.common.code.DaxPayCode;
import org.dromara.daxpay.service.merchant.local.MchContextLocal;
import org.dromara.daxpay.service.pay.common.entity.MchAppBaseEntity;
import org.dromara.daxpay.service.pay.common.entity.MchAppEditEntity;
import org.dromara.daxpay.service.pay.common.entity.MchAppRecordEntity;
import cn.hutool.core.util.ClassUtil;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import lombok.RequiredArgsConstructor;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.schema.Column;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * 租户拦截处理器, 处理代理商和商户数据隔离
 * @author xxm
 * @since 2024/6/25
 */
@Component
@RequiredArgsConstructor
public class CustomTenantLineHandler implements TenantLineHandler {
    private final ClientCodeService clientCodeService;

    /**
     * 获取租户ID
     * 商户返回商户号
     */
    @Override
    public Expression getTenantId() {
        // 获取当前用户的商户
        String mchNo = Optional.ofNullable(MchContextLocal.getMchNo()).orElse("");
        // 获取商户
        return new StringValue(mchNo);
    }

    /**
     * 租户字段
     * 商户返回商户号
     * 代理商返回代理商号
     */
    @Override
    public String getTenantIdColumn() {
        // 商户端和支付网关以及其他都使用商户号
        return "mch_no";
    }

    /**
     * 是否忽略租户拦截, 运营端不需要进行数据隔离数据隔离, 其他端各自进行处理
     */
    @Override
    public boolean ignoreTable(String tableName) {
        // 运营端不需要进行数据隔离数据隔离
        String clientCode = clientCodeService.getClientCode();
        if (Objects.equals(clientCode, DaxPayCode.Client.ADMIN)){
            return true;
        }
        // 商户端和网关端以及其他端都是用商户隔离机制
        return ignoreTableByMch(tableName);
    }

    /**
     * 忽略插入租户字段逻辑
     */
    @Override
    public boolean ignoreInsert(List<Column> columns, String tenantIdColumn) {
        // 如果租户字段已经存在不进行处理
        return TenantLineHandler.super.ignoreInsert(columns, tenantIdColumn);
    }


    /**
     * 商户权限控制
     */
    public boolean ignoreTableByMch(String tableName){
        // 读取注解, 判断是否启用商户数据隔离
        TableInfo tableInfo = MpUtil.getTableInfo(tableName);
        if (tableInfo == null){
            return true;
        }
        // 如果为 MchAppBaseEntity、MchAppEditEntity、MchAppRecordEntity 子类, 不可以忽略过滤
        boolean anyMatch = Stream.of(MchAppBaseEntity.class, MchAppEditEntity.class, MchAppRecordEntity.class)
                .anyMatch(entityClass -> ClassUtil.isAssignable(entityClass, tableInfo.getEntityType()));
        return !anyMatch;
    }
}
