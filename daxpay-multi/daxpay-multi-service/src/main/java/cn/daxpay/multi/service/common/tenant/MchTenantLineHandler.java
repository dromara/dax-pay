package cn.daxpay.multi.service.common.tenant;

import cn.bootx.platform.common.config.BootxConfigProperties;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.daxpay.multi.service.common.local.MchContextLocal;
import cn.daxpay.multi.service.common.entity.MchEntity;
import cn.hutool.core.util.ClassUtil;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import lombok.RequiredArgsConstructor;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.StringValue;
import org.springframework.stereotype.Component;

/**
 * 租户拦截处理器, 用商户号进行区分
 * @author xxm
 * @since 2024/6/25
 */
@Component
@RequiredArgsConstructor
public class MchTenantLineHandler  implements TenantLineHandler {
    private final BootxConfigProperties bootxConfigProperties;


    /**
     * 获取租户ID
     */
    @Override
    public Expression getTenantId() {
        // 从中获取当前用户的商户
        String mchNo = MchContextLocal.getMchNo();
        return new StringValue(mchNo);
    }

    /**
     * 租户字段
     */
    @Override
    public String getTenantIdColumn() {
        // 商户号字段
        return "mch_no";
    }

    /**
     * 是否忽略租户拦截
     * 1. 不是继承 MchEntity 的实体类，默认忽略
     * 2. 方法或类上添加了忽略注解的, 忽略拦截, 通过注解自动实现
     * 3. 未被MP管理的实体类，默认忽略
     * 4. 管理端运营人员不需要隔离数据
     */
    @Override
    public boolean ignoreTable(String tableName) {
        // 管理端运营人员不需要隔离数据
        if (bootxConfigProperties.getClientCode().equals("dax-pay-admin")){
            return true;
        }

        // 读取注解, 判断是否启用商户数据隔离
        TableInfo tableInfo = MpUtil.getTableInfo(tableName);
        if (tableInfo == null){
            return true;
        }
        // 判断实体类上是否为 MchEntity 子类
        if (!ClassUtil.isAssignable(MchEntity.class, tableInfo.getEntityType())){
            return true;
        }

        return false;
    }
}
