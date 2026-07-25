package cn.daxpay.open.platform.iam.service.permission.assign;

import cn.daxpay.open.platform.core.exception.ValidationFailedException;
import cn.daxpay.open.platform.core.util.TreeBuildUtil;
import cn.daxpay.open.platform.iam.dao.permission.PermCodeManager;
import cn.daxpay.open.platform.iam.dao.permission.PermMenuManager;
import cn.daxpay.open.platform.iam.dao.role.RoleManager;
import cn.daxpay.open.platform.iam.dao.upms.RoleCodeManager;
import cn.daxpay.open.platform.iam.dao.upms.RoleMenuManager;
import cn.daxpay.open.platform.iam.entity.permission.PermCodeData;
import cn.daxpay.open.platform.iam.entity.permission.PermMenu;
import cn.daxpay.open.platform.iam.entity.role.Role;
import cn.daxpay.open.platform.iam.entity.upms.RoleCode;
import cn.daxpay.open.platform.iam.entity.upms.RoleMenu;
import cn.daxpay.open.platform.iam.exception.role.RoleNotExistedException;
import cn.daxpay.open.platform.iam.param.permission.assign.RoleUnifiedAssignParam;
import cn.daxpay.open.platform.iam.result.permission.assign.RoleUnifiedAssignResult;
import cn.daxpay.open.platform.iam.result.permission.assign.RoleUnifiedAssignTreeResult;
import cn.hutool.core.collection.CollUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/// # 角色统一授权服务
///
/// 统一授权以当前终端菜单树作为骨架，把权限码按 `menuCode` 挂载到菜单节点下展示，
/// 但菜单授权与权限码授权仍然分别写入 `role_menu`、`role_code` 两张关系表。
/// 保存时会同时校验：角色终端、菜单终端、权限码主数据存在性，以及"权限码必须依赖已选菜单"这一约束。
@Slf4j
@Service
@RequiredArgsConstructor
public class RoleUnifiedAssignService {

    private final RoleManager roleManager;
    private final PermMenuManager permMenuManager;
    private final PermCodeManager permCodeManager;
    private final RoleMenuManager roleMenuManager;
    private final RoleCodeManager roleCodeManager;

    /// 查询角色在指定终端下的统一授权数据。
    /// 这里返回的是"菜单树 + 挂载到菜单下的权限码节点"，前端基于该结果直接渲染统一授权树。
    /// 其中菜单作为树骨架节点，权限码作为菜单子节点返回，勾选态则分别由菜单 ID、权限码 ID 两组数据维护。
    /// 为避免菜单 ID 与权限码 ID 数值冲突，以及同一权限码挂到多个同 menuCode 菜单时 key 重复，
    /// 采用带类型前缀的字符串树主键进行混合构树：菜单 `menu-{menuId}`，权限码 `code-{codeId}-menu-{menuId}`。
    public RoleUnifiedAssignResult getByRole(Long roleId, String clientCode) {
        Role role = roleManager.findById(roleId).orElseThrow(RoleNotExistedException::new);
        if (!Objects.equals(role.getClientCode(), clientCode)) {
            // 权限: 角色所属终端与请求终端不一致
            throw new ValidationFailedException("error.iam.assign.roleClientMismatch");
        }

        List<PermMenu> menus = permMenuManager.findAllByClient(clientCode);
        List<PermCodeData> allCodes = permCodeManager.findAll();
        List<RoleMenu> roleMenus = roleMenuManager.findAllByRole(roleId);
        List<RoleCode> roleCodes = roleCodeManager.findAllByRole(roleId);

        // 权限码主数据不区分终端，查询时需要按 menuCode 挂载到当前终端下的菜单实例。
        Map<String, List<PermCodeData>> codeMapByMenuCode = allCodes.stream()
                .filter(item -> item.getId() != null)
                .filter(item -> item.getMenuCode() != null && !item.getMenuCode().isBlank())
                .collect(Collectors.groupingBy(PermCodeData::getMenuCode));

        // 角色菜单/权限码分配跨终端共用一张关系表，需以当前终端为白名单过滤勾选态，
        // 否则其他终端的分配会被计入，前端出现"已选数 > 总数"（如菜单 34/33）
        Set<Long> menuIdInClient = menus.stream().map(PermMenu::getId).collect(Collectors.toCollection(HashSet::new));
        // 当前终端树中实际出现的权限码：当前终端菜单的 menuCode 对应的全部权限码
        Set<Long> codeIdInTree = menus.stream()
                .map(PermMenu::getMenuCode)
                .filter(mc -> mc != null && !mc.isBlank())
                .flatMap(mc -> codeMapByMenuCode.getOrDefault(mc, List.of()).stream())
                .map(PermCodeData::getId)
                .collect(Collectors.toCollection(HashSet::new));
        Set<Long> checkedMenuIds = roleMenus.stream()
                .map(RoleMenu::getMenuId)
                .filter(menuIdInClient::contains)
                .collect(Collectors.toCollection(HashSet::new));
        Set<Long> checkedCodeIds = roleCodes.stream()
                .map(RoleCode::getCodeId)
                .filter(codeIdInTree::contains)
                .collect(Collectors.toCollection(HashSet::new));

        // 构建统一扁平节点列表：菜单节点 + 权限码节点
        List<RoleUnifiedAssignTreeResult> allNodes = new ArrayList<>();
        Map<Long, String> menuIdToTreeIdMap = new HashMap<>();

        // 生成菜单扁平节点
        for (PermMenu menu : menus) {
            String treeId = "menu-" + menu.getId();
            String treePid = menu.getPid() != null ? "menu-" + menu.getPid() : null;
            menuIdToTreeIdMap.put(menu.getId(), treeId);

            var menuNode = new RoleUnifiedAssignTreeResult()
                    .setKey(treeId)
                    .setType("menu")
                    .setTreeId(treeId)
                    .setTreePid(treePid)
                    .setId(menu.getId())
                    .setPid(menu.getPid())
                    .setMenuCode(menu.getMenuCode())
                    .setClientCode(menu.getClientCode())
                    .setI18nKey(menu.getI18nKey())
                    .setMenuType(menu.getMenuType())
                    .setSortNo(menu.getSortNo())
                    .setChildren(new ArrayList<>());
            allNodes.add(menuNode);
        }

        // 生成权限码扁平节点
        for (PermMenu menu : menus) {
            List<PermCodeData> codes = codeMapByMenuCode.getOrDefault(menu.getMenuCode(), List.of());
            String menuTreeId = menuIdToTreeIdMap.get(menu.getId());

            for (PermCodeData code : codes) {
                // 同一 code 可挂到多个同 menuCode 菜单，key 必须含所属菜单 id 才能全局唯一
                String codeTreeId = "code-" + code.getId() + "-menu-" + menu.getId();
                var codeNode = new RoleUnifiedAssignTreeResult()
                        .setKey(codeTreeId)
                        .setType("code")
                        .setTreeId(codeTreeId)
                        .setTreePid(menuTreeId)
                        .setId(code.getId())
                        .setPid(menu.getId())
                        .setCodeId(code.getId())
                        .setCode(code.getCode())
                        .setI18nKey(code.getI18nKey())
                        .setMenuCode(code.getMenuCode())
                        .setClientCode(menu.getClientCode())
                        .setSortNo(menu.getSortNo())
                        .setChildren(new ArrayList<>());
                allNodes.add(codeNode);
            }
        }

        // 基于统一扁平节点列表和专用树主键一次性构建混合树
        List<RoleUnifiedAssignTreeResult> tree = TreeBuildUtil.build(allNodes, null,
                RoleUnifiedAssignTreeResult::getTreeId,
                RoleUnifiedAssignTreeResult::getTreePid,
                RoleUnifiedAssignTreeResult::setChildren,
                Comparator.comparing(RoleUnifiedAssignTreeResult::getSortNo));

        return new RoleUnifiedAssignResult()
                .setRoleId(roleId)
                .setClientCode(clientCode)
                .setTree(tree)
                .setCheckedMenuIds(new ArrayList<>(checkedMenuIds))
                .setCheckedCodeIds(new ArrayList<>(checkedCodeIds));
    }

    /// 保存角色统一授权。
    /// 前端会同时提交菜单 ID 与权限码 ID，这里在落库前会校验权限码是否都依赖于本次已选择的菜单，
    /// 从而保证功能权限不能脱离菜单权限单独生效。
    /// 保存时会分别维护 `role_menu` 与 `role_code` 两张关系表，仅同步当前角色本次提交的数据差异。
    @Transactional(rollbackFor = Exception.class)
    public void save(RoleUnifiedAssignParam param) {
        Role role = roleManager.findById(param.getRoleId()).orElseThrow(RoleNotExistedException::new);
        if (!Objects.equals(role.getClientCode(), param.getClientCode())) {
            // 权限: 角色所属终端与请求终端不一致
            throw new ValidationFailedException("error.iam.assign.roleClientMismatch");
        }

        List<Long> menuIds = param.getMenuIds() == null ? new ArrayList<>() : param.getMenuIds().stream().filter(Objects::nonNull).distinct().toList();
        List<Long> codeIds = param.getCodeIds() == null ? new ArrayList<>() : param.getCodeIds().stream().filter(Objects::nonNull).distinct().toList();

        List<PermMenu> menus = menuIds.isEmpty() ? List.of() : permMenuManager.findAllByIds(menuIds);
        if (menus.size() != menuIds.size()) {
            // 权限: 存在无效的菜单数据
            throw new ValidationFailedException("error.iam.assign.invalidMenuData");
        }
        if (menus.stream().map(PermMenu::getClientCode).anyMatch(clientCode -> !Objects.equals(clientCode, param.getClientCode()))) {
            // 权限: 角色所属终端与菜单终端不一致
            throw new ValidationFailedException("error.iam.assign.roleMenuClientMismatch");
        }

        List<PermCodeData> codes = codeIds.isEmpty() ? List.of() : permCodeManager.findAllByIds(codeIds);
        if (codes.size() != codeIds.size()) {
            // 权限: 存在无效的权限码数据
            throw new ValidationFailedException("error.iam.assign.invalidPermCodeData");
        }
        // 只有当权限码归属的 menuCode 出现在本次已选菜单中，权限码才允许被保存。
        Set<String> menuCodeSet = menus.stream().map(PermMenu::getMenuCode).filter(Objects::nonNull).collect(Collectors.toSet());
        List<String> invalidCodeList = codes.stream()
                .filter(item -> !menuCodeSet.contains(item.getMenuCode()))
                .map(PermCodeData::getCode)
                .sorted()
                .toList();
        if (CollUtil.isNotEmpty(invalidCodeList)) {
            // 权限: 存在未挂载到已选菜单的权限码: {0}
            throw new ValidationFailedException("error.iam.assign.permCodeNotOnSelectedMenus", String.join(", ", invalidCodeList));
        }

        List<RoleMenu> existedRoleMenus = roleMenuManager.findAllByRole(param.getRoleId());
        Set<Long> existedMenuIdSet = existedRoleMenus.stream().map(RoleMenu::getMenuId).collect(Collectors.toSet());
        List<Long> deleteRoleMenuIds = existedRoleMenus.stream()
                .filter(item -> !menuIds.contains(item.getMenuId()))
                .map(RoleMenu::getId)
                .toList();
        roleMenuManager.deleteByIds(deleteRoleMenuIds);
        List<RoleMenu> addRoleMenus = menuIds.stream()
                .filter(menuId -> !existedMenuIdSet.contains(menuId))
                .map(menuId -> new RoleMenu(param.getRoleId(), menuId))
                .toList();
        roleMenuManager.saveAll(addRoleMenus);

        List<RoleCode> existedRoleCodes = roleCodeManager.findAllByRole(param.getRoleId());
        Set<Long> existedCodeIdSet = existedRoleCodes.stream().map(RoleCode::getCodeId).collect(Collectors.toSet());
        List<Long> deleteRoleCodeIds = existedRoleCodes.stream()
                .filter(item -> !codeIds.contains(item.getCodeId()))
                .map(RoleCode::getId)
                .toList();
        roleCodeManager.deleteByIds(deleteRoleCodeIds);
        List<RoleCode> addRoleCodes = codeIds.stream()
                .filter(codeId -> !existedCodeIdSet.contains(codeId))
                .map(codeId -> new RoleCode(param.getRoleId(), codeId))
                .toList();
        roleCodeManager.saveAll(addRoleCodes);
    }
}

