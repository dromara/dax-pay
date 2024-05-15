package cn.bootx.platform.iam.handler;

import cn.bootx.platform.common.core.annotation.PermCode;
import cn.bootx.platform.common.core.entity.UserDetail;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.starter.auth.util.SecurityUtil;
import cn.bootx.platform.starter.data.perm.select.SelectFieldPermHandler;
import cn.bootx.platform.iam.core.upms.service.RolePermService;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.statement.select.SelectItem;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 数据字段权限业务逻辑实现
 *
 * @author xxm
 * @since 2023/1/22
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SelectFieldPermHandlerImpl implements SelectFieldPermHandler {

    @Lazy
    private final RolePermService rolePermService;

    /**
     * 将没有权限的SQL查询字段给过滤掉
     * @param selectItems SQL查询字段
     */
    @Override
    public List<SelectItem> filterFields(List<SelectItem> selectItems, String tableName) {
        TableInfo tableInfo = MpUtil.getTableInfo(tableName);
        // 未被MybatisPlus管理
        if (Objects.isNull(tableInfo)) {
            return selectItems;
        }
        Optional<UserDetail> currentUser = SecurityUtil.getCurrentUser();
        // 管理员直接拥有所有字段的权限
        if (currentUser.map(UserDetail::isAdmin).orElse(false)) {
            return selectItems;
        }

        // 获取类注解
        List<String> userPermCodes = currentUser.map(UserDetail::getId)
            .map(rolePermService::findEffectPermCodesByUserId)
            .orElse(new ArrayList<>(0));

        Class<?> entityType = tableInfo.getEntityType();
        PermCode classPermCode = entityType.getAnnotation(PermCode.class);
        if (!Objects.isNull(classPermCode)) {
            boolean b = Arrays.stream(classPermCode.value()).anyMatch(userPermCodes::contains);
            if (b) {
                return selectItems;
            }
        }
        // 处理字段注解
        return selectItems.stream().filter(selectItem -> {
            TableFieldInfo tableField = getTableFieldByColumn(tableInfo.getFieldList(), selectItem.toString());
            // 字段未被管理,不进行权限处理
            if (Objects.isNull(tableField)) {
                return true;
            }
            PermCode permCode = tableField.getField().getAnnotation(PermCode.class);
            if (Objects.nonNull(permCode)) {
                // 用户没有对应的权限码时, 过滤掉这个字段
                return Arrays.stream(permCode.value()).anyMatch(userPermCodes::contains);
            }
            else {
                return true;
            }
        }).collect(Collectors.toList());
    }

    /**
     * 获取关联的字段配置
     */
    private TableFieldInfo getTableFieldByColumn(List<TableFieldInfo> permCodeFields, String column) {
        return permCodeFields.stream().filter(o -> o.getColumn().equalsIgnoreCase(column)).findFirst().orElse(null);
    }

}
