package org.dromara.daxpay.platform.iam.service.permission.resource;

import org.dromara.daxpay.platform.core.exception.DataNotExistException;
import org.dromara.daxpay.platform.iam.dao.permission.PermCodeManager;
import org.dromara.daxpay.platform.iam.dao.permission.PermMenuManager;
import org.dromara.daxpay.platform.iam.entity.permission.PermCodeData;
import org.dromara.daxpay.platform.iam.entity.permission.PermMenu;
import org.dromara.daxpay.platform.iam.result.permission.resource.MenuPermCodeItemResult;
import org.dromara.daxpay.platform.iam.result.permission.resource.PermCodeResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/// # 权限码管理
///
@Slf4j
@Service
@RequiredArgsConstructor
public class PermCodeService {
    private final PermCodeManager permCodeManager;
    private final PermMenuManager permMenuManager;

    /// 获取全部权限码
    public List<String> findAllCode(){
        return permCodeManager.findAll()
                .stream()
                .map(PermCodeData::getCode)
                .toList();
    }

    /// 根据菜单查询权限码列表
    public List<MenuPermCodeItemResult> findByMenu(Long menuId) {
        PermMenu menu = permMenuManager.findById(menuId).orElseThrow(() -> new DataNotExistException("error.iam.menu.notExist"));
        if (menu.getMenuCode() == null || menu.getMenuCode().isBlank()) {
            return List.of();
        }
        return permCodeManager.findByMenuCode(menu.getMenuCode())
                .stream()
                .map(item -> new MenuPermCodeItemResult()
                        .setId(item.getId())
                        .setCode(item.getCode())
                        .setNameCn(item.getNameCn())
                        .setNameEn(item.getNameEn())
                        .setMenuCode(item.getMenuCode())
                        .setInternal(item.isInternal())
                        .setRemark(item.getRemark()))
                .toList();
    }
}
