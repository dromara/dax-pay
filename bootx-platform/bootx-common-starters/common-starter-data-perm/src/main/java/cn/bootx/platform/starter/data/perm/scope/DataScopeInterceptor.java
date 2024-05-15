package cn.bootx.platform.starter.data.perm.scope;

import cn.bootx.platform.starter.data.perm.configuration.DataPermProperties;
import cn.bootx.platform.starter.data.perm.exception.NotLoginPermException;
import cn.bootx.platform.common.core.annotation.NestedPermission;
import cn.bootx.platform.common.core.annotation.Permission;
import cn.bootx.platform.common.core.code.CommonCode;
import cn.bootx.platform.common.core.entity.UserDetail;
import cn.bootx.platform.common.core.exception.BizException;
import cn.bootx.platform.common.core.util.CollUtil;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.starter.data.perm.code.DataScopeEnum;
import cn.bootx.platform.starter.data.perm.local.DataPermContextHolder;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.toolkit.PluginUtils;
import com.baomidou.mybatisplus.extension.parser.JsqlParserSupport;
import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.Parenthesis;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.select.*;
import net.sf.jsqlparser.statement.update.Update;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 数据权限处理器
 *
 * @author xxm
 * @since 2021/12/22
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DataScopeInterceptor extends JsqlParserSupport implements InnerInterceptor {

    private final DataPermProperties dataPermProperties;

    private final DataPermScopeHandler dataPermScopeHandler;

    /**
     * 查询语句判断
     */
    @Override
    public void beforeQuery(Executor executor, MappedStatement ms, Object parameter, RowBounds rowBounds,
            ResultHandler resultHandler, BoundSql boundSql) {
        if (!this.checkPermission()) {
            return;
        }
        PluginUtils.MPBoundSql mpBs = PluginUtils.mpBoundSql(boundSql);
        mpBs.sql(this.parserSingle(mpBs.sql(), ms.getId()));
    }

    /**
     * 更新语句判断
     */
    @Override
    public void beforeUpdate(Executor executor, MappedStatement ms, Object parameter) {
        if (!this.checkPermission()) {
            return;
        }
        // 插入操作不处理
        if (ms.getSqlCommandType() == SqlCommandType.INSERT) {
            return;
        }
        // 更新处理 和 删除处理
        BoundSql boundSql = ms.getBoundSql(parameter);
        PluginUtils.MPBoundSql mpBs = PluginUtils.mpBoundSql(boundSql);
        mpBs.sql(this.parserSingle(mpBs.sql(), ms.getId()));
    }

    /**
     * 查询处理
     */
    @Override
    protected void processSelect(Select select, int index, String sql, Object obj) {
        SelectBody selectBody = select.getSelectBody();
        if (selectBody instanceof PlainSelect) {
            this.setWhere((PlainSelect) selectBody);
        }
        else if (selectBody instanceof SetOperationList) {
            SetOperationList setOperationList = (SetOperationList) selectBody;
            List<SelectBody> selectBodyList = setOperationList.getSelects();
            selectBodyList.forEach(s -> this.setWhere((PlainSelect) s));
        }
    }

    /**
     * update 语句处理
     */
    @Override
    protected void processUpdate(Update update, int index, String sql, Object obj) {
        String tableName = update.getTable().getName();
        if (!checkTableCreator(tableName)) {
            return;
        }
        Expression where = update.getWhere();
        final Expression sqlSegment = this.dataScope(where, null);
        if (null != sqlSegment) {
            update.setWhere(sqlSegment);
        }
    }

    /**
     * delete 语句处理
     */
    @Override
    protected void processDelete(Delete delete, int index, String sql, Object obj) {
        String tableName = delete.getTable().getName();
        if (!checkTableCreator(tableName)) {
            return;
        }
        final Expression sqlSegment = this.dataScope(delete.getWhere(), null);
        if (null != sqlSegment) {
            delete.setWhere(sqlSegment);
        }
    }

    /**
     * 设置 where 条件
     * @param plainSelect 查询对象
     */
    protected void setWhere(PlainSelect plainSelect) {
        String tableName = plainSelect.getFromItem().toString();
        if (!checkTableCreator(tableName)) {
            return;
        }
        // 如果不是多表联查, 不需要传表名
        if (CollUtil.isEmpty(plainSelect.getJoins())) {
            tableName = null;
        }
        Expression sqlSegment = this.dataScope(plainSelect.getWhere(), tableName);
        if (null != sqlSegment) {
            plainSelect.setWhere(sqlSegment);
        }
    }

    /**
     * 数据范围权限sql处理
     * @param where 条件表达式
     * @param mainTableName 数据表名称, 多表join时传主表
     * @return 新的表达式
     */
    protected Expression dataScope(Expression where, String mainTableName) {
        DataPermScope dataPermScope = dataPermScopeHandler.getDataPermScope();
        Expression queryExpression;
        DataScopeEnum scopeType = dataPermScope.getScopeType();
        switch (scopeType) {
            case SELF: {
                queryExpression = this.selfScope(mainTableName);
                break;
            }
            case DEPT_SCOPE_AND_SUB:
            case DEPT_SCOPE: {
                Expression deptScopeExpression = this.deptScope(dataPermScope.getDeptScopeIds(), mainTableName);
                // 追加查询自身的数据
                queryExpression = new OrExpression(deptScopeExpression, this.selfScope(mainTableName));
                break;
            }
            case USER_SCOPE: {
                // 包含自身的数据
                queryExpression = this.userScope(dataPermScope.getUserScopeIds(), mainTableName);
                break;
            }
            case DEPT_AND_USER_SCOPE: {
                // 包含自身的数据
                queryExpression = this.deptAndUserScope(dataPermScope.getDeptScopeIds(),
                        dataPermScope.getUserScopeIds(), mainTableName);
                break;
            }
            case SELF_DEPT:
            case SELF_DEPT_AND_SUB: {
                queryExpression = this.deptScope(dataPermScope.getDeptScopeIds(), mainTableName);
                break;
            }
            case ALL_SCOPE:
                return where;
            default: {
                throw new BizException("代码有问题");
            }
        }
        // 将查询条件与追加条件结合
        if (Objects.nonNull(where)) {
            return new AndExpression(new Parenthesis(queryExpression), where);
        }
        return new Parenthesis(queryExpression);
    }

    /**
     * 查询自己的数据
     */
    protected Expression selfScope(String mainTableName) {
        Long userId = DataPermContextHolder.getUserDetail()
            .map(UserDetail::getId)
            .orElseThrow(NotLoginPermException::new);
        return new EqualsTo(new Column(this.getPermColumn(mainTableName)), new LongValue(userId));
    }

    /**
     * 查询用户范围的数据
     */
    protected Expression userScope(Set<Long> userScopeIds, String mainTableName) {
        Long userId = DataPermContextHolder.getUserDetail()
            .map(UserDetail::getId)
            .orElseThrow(NotLoginPermException::new);
        List<Expression> userExpressions = Optional.ofNullable(userScopeIds)
            .orElse(new HashSet<>())
            .stream()
            .map(LongValue::new)
            .collect(Collectors.toList());
        // 追加自身
        userExpressions.add(new LongValue(userId));
        return new InExpression(new Column(this.getPermColumn(mainTableName)), new ExpressionList(userExpressions));
    }

    /**
     * 查询部门范围的数据
     */
    protected Expression deptScope(Set<Long> deptIds, String mainTableName) {
        DataPermProperties.DataPerm deptDataPerm = dataPermProperties.getDeptDataPerm();
        // 创建嵌套子查询
        PlainSelect plainSelect = new PlainSelect();
        // 设置查询字段
        SelectExpressionItem selectItem = new SelectExpressionItem();
        selectItem.setExpression(new Column(deptDataPerm.getQueryField()));
        plainSelect.addSelectItems(selectItem);
        // 过滤重复的子查询结果
        plainSelect.setDistinct(new Distinct());
        // 设置所查询表
        plainSelect.setFromItem(new Table(deptDataPerm.getTable()));

        // 构建查询条件
        List<Expression> deptExpressions = Optional.ofNullable(deptIds)
            .orElse(new HashSet<>())
            .stream()
            .map(LongValue::new)
            .collect(Collectors.toList());
        // 构造空查询
        if (deptExpressions.isEmpty()) {
            deptExpressions.add(null);
        }
        // 设置查询条件
        plainSelect
            .setWhere(new InExpression(new Column(deptDataPerm.getWhereField()), new ExpressionList(deptExpressions)));

        // 拼接子查询
        SubSelect subSelect = new SubSelect();
        subSelect.setSelectBody(plainSelect);
        return new InExpression(new Column(this.getPermColumn(mainTableName)), subSelect);
    }

    /**
     * 查询部门和用户范围的数据
     */
    protected Expression deptAndUserScope(Set<Long> deptScopeIds, Set<Long> userScopeIds, String mainTableName) {
        Expression deptScopeExpression = this.deptScope(deptScopeIds, mainTableName);
        Expression userScopeExpression = this.userScope(userScopeIds, mainTableName);
        return new OrExpression(deptScopeExpression, userScopeExpression);
    }

    /**
     * 判断是否需要进行数据权限控制
     */
    protected boolean checkPermission() {
        // 配置是否开启了权限控制
        if (!dataPermProperties.isEnableDataPerm()) {
            return false;
        }
        // 判断是否在嵌套执行环境中
        NestedPermission nestedPermission = DataPermContextHolder.getNestedPermission();
        if (Objects.nonNull(nestedPermission) && !nestedPermission.dataScope()) {
            return false;
        }
        // 是否添加了对应的注解来开启数据权限控制
        Permission permission = DataPermContextHolder.getPermission();
        if (Objects.isNull(permission) || !permission.dataScope()) {
            return false;
        }
        // 检查是否已经登录和是否是超级管理员, 管理员跳过权限控制
        return !DataPermContextHolder.getUserDetail().map(UserDetail::isAdmin).orElseThrow(NotLoginPermException::new);
    }

    /**
     * 语句中是否有 创建人字段 creator , 没有的话为了不影响系统执行, 将不进行权限控制, 只报警告
     */
    protected boolean checkTableCreator(String tableName) {
        TableInfo tableInfo = MpUtil.getTableInfo(tableName);
        if (tableInfo == null) {
            log.warn("'{}' 数据表未找到对应的MybatisPlus实体类，将不会启用数据权限控制，请检查配置", tableName);
            return false;
        }
        // 查看实体类上是否关闭了数据权限控制
        Permission permission = tableInfo.getEntityType().getAnnotation(Permission.class);
        if (Objects.nonNull(permission) && permission.dataScope()) {
            return false;
        }
        boolean b = tableInfo.getFieldList()
            .stream()
            .map(TableFieldInfo::getColumn)
            .anyMatch(CommonCode.CREATOR::equalsIgnoreCase);
        if (!b) {
            log.warn("'{}' 数据表未找到权限控制字段 'creator' ，将不会启用数据权限控制，请检查配置", tableName);
        }

        return b;
    }

    /**
     * 获取权限字段名称
     * @param mainTableName 主表名称, 表联合查询的时候需要传
     */
    protected String getPermColumn(String mainTableName) {
        // 权限字段名
        String column;
        if (StrUtil.isNotBlank(mainTableName)) {
            column = mainTableName + "." + CommonCode.CREATOR;
        }
        else {
            column = CommonCode.CREATOR;
        }
        return column;
    }

}
