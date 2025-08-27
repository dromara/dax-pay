package org.dromara.daxpay.server.interceptor;

import com.baomidou.mybatisplus.extension.plugins.handler.MultiDataPermissionHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.schema.Table;
import org.springframework.stereotype.Service;

/**
 * 支持多表的数据权限处理器, 每个表都会追加条件
 * @author xxm
 * @since 2025/2/2
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MchDataScopeHandler implements MultiDataPermissionHandler {

    /**
     * 语句拼装
     * @param table             所执行的数据库表信息，可以通过此参数获取表名和表别名
     * @param where             原有的 where 条件信息
     * @param mappedStatementId Mybatis MappedStatement Id 根据该参数可以判断具体执行方法
     * @return 追加的 where 条件信息
     */
    @Override
    public Expression getSqlSegment(Table table, Expression where, String mappedStatementId) {
        return null;
        // AND (hello = 1) 格式的SQL语句
//        return new ParenthesedExpressionList<>(new EqualsTo(new Column("1"), new LongValue(1L)));
    }
}
