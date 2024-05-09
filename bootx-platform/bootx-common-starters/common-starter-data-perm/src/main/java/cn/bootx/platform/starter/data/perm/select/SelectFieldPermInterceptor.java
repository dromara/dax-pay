package cn.bootx.platform.starter.data.perm.select;

import cn.bootx.platform.starter.data.perm.configuration.DataPermProperties;
import cn.bootx.platform.starter.data.perm.exception.NotLoginPermException;
import cn.bootx.platform.common.core.annotation.NestedPermission;
import cn.bootx.platform.common.core.annotation.Permission;
import cn.bootx.platform.common.core.entity.UserDetail;
import cn.bootx.platform.common.core.util.CollUtil;
import cn.bootx.platform.starter.data.perm.local.DataPermContextHolder;
import com.baomidou.mybatisplus.core.toolkit.PluginUtils;
import com.baomidou.mybatisplus.extension.parser.JsqlParserSupport;
import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectBody;
import net.sf.jsqlparser.statement.select.SelectItem;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

/**
 * 查询字段权限处理器
 *
 * @author xxm
 * @since 2022/12/29
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SelectFieldPermInterceptor extends JsqlParserSupport implements InnerInterceptor {

    private final DataPermProperties dataPermProperties;

    private final SelectFieldPermHandler selectFieldPermHandler;

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
     * 查询处理
     */
    @Override
    protected void processSelect(Select select, int index, String sql, Object obj) {
        SelectBody selectBody = select.getSelectBody();

        if (selectBody instanceof PlainSelect) {
            PlainSelect plainSelect = (PlainSelect) selectBody;
            // 关联表直接排除
            if (CollUtil.isNotEmpty(plainSelect.getJoins())) {
                return;
            }
            String tableName = plainSelect.getFromItem().toString();
            List<SelectItem> selectItems = plainSelect.getSelectItems();
            // 过滤掉没有权限查看的字段
            List<SelectItem> newSelectItems = selectFieldPermHandler.filterFields(selectItems, tableName);
            plainSelect.setSelectItems(newSelectItems);
        }
    }

    /**
     * 判断是否需要进行数据权限控制
     */
    protected boolean checkPermission() {
        // 配置是否开启了权限控制
        if (!dataPermProperties.isEnableSelectFieldPerm()) {
            return false;
        }
        // 判断是否在嵌套执行环境中
        NestedPermission nestedPermission = DataPermContextHolder.getNestedPermission();
        if (Objects.nonNull(nestedPermission) && !nestedPermission.selectField()) {
            return false;
        }
        // 是否添加了对应的注解来开启数据权限控制
        Permission permission = DataPermContextHolder.getPermission();
        if (Objects.isNull(permission) || !permission.selectField()) {
            return false;
        }
        // 检查是否已经登录和是否是超级管理员, 管理员跳过权限控制
        return !DataPermContextHolder.getUserDetail().map(UserDetail::isAdmin).orElseThrow(NotLoginPermException::new);
    }

}
