package cn.daxpay.open.platform.common.mybatisplus.util;

import cn.daxpay.open.platform.common.mybatisplus.function.ToResult;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.plugins.IgnoreStrategy;
import com.baomidou.mybatisplus.core.plugins.InterceptorIgnoreHelper;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.core.toolkit.LambdaUtils;
import com.baomidou.mybatisplus.core.toolkit.support.ColumnCache;
import com.baomidou.mybatisplus.core.toolkit.support.LambdaMeta;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.experimental.UtilityClass;
import org.apache.ibatis.reflection.property.PropertyNamer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/// # MP工具类
///
@UtilityClass
public class MpUtil {

    private static final Logger log = LoggerFactory.getLogger(MpUtil.class);

    /// 当前线程 @IgnoreTenant 嵌套深度（null/0 = 未忽略）
    private static final ThreadLocal<Integer> IGNORE_TENANT_DEPTH = new ThreadLocal<>();

    /// 忽略租户行拦截策略（tenantLine = true）
    private static final IgnoreStrategy IGNORE_TENANT_STRATEGY =
            IgnoreStrategy.builder().tenantLine(true).build();

    /// 获取分页对象 MyBatis-Plus
    public <T> Page<T> getMpPage(PageParam page) {
        return Page.of(page.getCurrent(), page.getSize());
    }
    /// 获取分页对象 MyBatis-Plus
    public <T> Page<T> getMpPage(PageParam page, Class<T> clazz) {
        return Page.of(page.getCurrent(), page.getSize());
    }

    /// 获取行名称
    /// @param function 对象字段对应的读取方法的Lambda表达式
    /// @return 字段名
    public <T> String getColumnName(SFunction<T, ?> function) {
        LambdaMeta meta = LambdaUtils.extract(function);
        Map<String, ColumnCache> columnMap = LambdaUtils.getColumnMap(meta.getInstantiatedClass());
        Assert.notEmpty(columnMap, "错误:无法执行.因为无法获取到实体类的表对应缓存!");
        String fieldName = PropertyNamer.methodToProperty(meta.getImplMethodName());
        ColumnCache columnCache = columnMap.get(LambdaUtils.formatKey(fieldName));
        return columnCache.getColumn();
    }

    /// mp page转换为 PageResult 同时进行dto转换
    public <T> PageResult<T> toPageResult(Page<? extends ToResult<T>> page) {
        if (Objects.isNull(page)) {
            return new PageResult<>();
        }
        List<T> collect = page.getRecords().stream().map(ToResult::toResult).toList();
        // 构造 PageResult 对象
        return new PageResult<T>().setSize(page.getSize())
                .setCurrent(page.getCurrent())
                .setTotal(page.getTotal())
                .setRecords(collect);
    }

    /// mp page转换为 PageResult 同时进行dto转换
    public <T> List<T> toListResult(List<? extends ToResult<T>> list) {
        return list.stream().map(ToResult::toResult).toList();
    }

    /// 获取关联的 TableInfo
    public TableInfo getTableInfo(String tableName) {
        for (TableInfo tableInfo : TableInfoHelper.getTableInfos()) {
            if (tableName.equalsIgnoreCase(tableInfo.getTableName())) {
                return tableInfo;
            }
        }
        return null;
    }

    /// 获取主键名称
    public String getKeyProperty(Class<?> clazz) {
        TableInfo tableInfo = TableInfoHelper.getTableInfo(clazz);
        Assert.notNull(tableInfo, "错误:无法执行.因为找不到实体的 TableInfo 缓存!");
        String keyProperty = tableInfo.getKeyProperty();
        Assert.notEmpty(keyProperty, "错误:无法执行.因为无法从实体中找到主键的列!");
        return keyProperty;
    }

    /// 进入忽略租户作用域（可重入，与 [#clearIgnoreTenant] 成对使用）
    ///
    /// 基于线程内引用计数：仅 depth 从 0→1 时真正打开 MP 忽略策略；
    /// 嵌套调用不会互相 clear，直到最外层 exit 才恢复租户过滤。
    public void ignoreTenant() {
        int depth = Optional.ofNullable(IGNORE_TENANT_DEPTH.get()).orElse(0) + 1;
        IGNORE_TENANT_DEPTH.set(depth);
        if (depth == 1) {
            InterceptorIgnoreHelper.handle(IGNORE_TENANT_STRATEGY);
        }
    }

    /// 退出忽略租户作用域（可重入；depth 归零时才真正 clear）
    ///
    /// 不配对的 exit（depth 已为 0）会强制清空并打错误日志，避免脏状态。
    public void clearIgnoreTenant() {
        Integer cur = IGNORE_TENANT_DEPTH.get();
        if (cur == null || cur <= 0) {
            // 不配对 exit：强制清干净，避免脏状态
            IGNORE_TENANT_DEPTH.remove();
            InterceptorIgnoreHelper.clearIgnoreStrategy();
            log.error("clearIgnoreTenant without matching ignoreTenant (depth underflow), force cleared");
            return;
        }
        int depth = cur - 1;
        if (depth == 0) {
            IGNORE_TENANT_DEPTH.remove();
            InterceptorIgnoreHelper.clearIgnoreStrategy();
        } else {
            IGNORE_TENANT_DEPTH.set(depth);
            // 防御：depth>0 但 MP 标志被硬 clear 时补回
            if (!InterceptorIgnoreHelper.hasIgnoreStrategy()) {
                InterceptorIgnoreHelper.handle(IGNORE_TENANT_STRATEGY);
                log.warn("IgnoreStrategy missing while ignoreTenant depth={}, re-applied", depth);
            }
        }
    }

    /// 当前线程忽略租户嵌套深度（0 表示未忽略；测试 / 巡检用）
    public int getIgnoreTenantDepth() {
        return Optional.ofNullable(IGNORE_TENANT_DEPTH.get()).orElse(0);
    }

    /// 强制清空 depth 与 MP 忽略标志
    ///
    /// 仅用于请求结束兜底（如 Filter finally），业务中间勿调用，以免打断外层 ignore 作用域。
    public void forceClearIgnoreTenant() {
        IGNORE_TENANT_DEPTH.remove();
        InterceptorIgnoreHelper.clearIgnoreStrategy();
    }
}

