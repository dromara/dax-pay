package cn.daxpay.open.platform.iam.dao.permission;

import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import cn.daxpay.open.platform.iam.entity.permission.PermCodeData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

/// # 权限编码
///
@Slf4j
@Repository
@RequiredArgsConstructor
public class PermCodeManager extends BaseManager<PermCodeMapper, PermCodeData> {

    /// 根据菜单编码查询权限码
    public List<PermCodeData> findByMenuCode(String menuCode) {
        return lambdaQuery()
                .eq(PermCodeData::getMenuCode, menuCode)
                .orderByAsc(PermCodeData::getCode)
                .list();
    }
}
