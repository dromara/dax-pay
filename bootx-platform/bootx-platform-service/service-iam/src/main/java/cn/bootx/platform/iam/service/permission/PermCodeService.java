package cn.bootx.platform.iam.service.permission;

import cn.bootx.platform.core.exception.BizException;
import cn.bootx.platform.core.exception.DataNotExistException;
import cn.bootx.platform.core.util.TreeBuildUtil;
import cn.bootx.platform.iam.dao.permission.PermCodeManager;
import cn.bootx.platform.iam.dao.upms.RoleCodeManager;
import cn.bootx.platform.iam.entity.permission.PermCode;
import cn.bootx.platform.iam.entity.upms.RoleCode;
import cn.bootx.platform.iam.param.permission.PermCodeParam;
import cn.bootx.platform.iam.result.permission.PermCodeResult;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * 权限码管理
 *
 * @author xxm
 * @since 2023/1/23
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PermCodeService {
    private final PermCodeManager permCodeManager;
    private final RoleCodeManager roleCodeManager;

    /**
     * 添加权限码
     */
    @Transactional(rollbackFor = Exception.class)
    public void add(PermCodeParam param) {
        PermCode permCode = PermCode.init(param);
        // 如果是权限码, 判断是否存在
        if (param.isLeaf()) {
            if (permCodeManager.existedByField(PermCode::getCode, param.getCode())){
                throw new BizException("权限码已存在");
            }
        }
        permCodeManager.save(permCode);
    }

    /**
     * 更新
     */
    @Transactional(rollbackFor = Exception.class)
    public void update(PermCodeParam param) {
        PermCode permCode = permCodeManager.findById(param.getId()).orElseThrow(() -> new BizException("权限码信息不存在"));
        if (permCode.isLeaf()){
            if (permCodeManager.existedByField(PermCode::getCode, param.getCode(), param.getId())){
                throw new BizException("权限码已存在");
            }
        }
        String oldCode = permCode.getCode();
        String newCode = param.getCode();
        BeanUtil.copyProperties(param, permCode, CopyOptions.create().ignoreNullValue());
        permCodeManager.updateById(permCode);

        // 如果编码值变了进行级联更新角色关联关系
        if (Objects.equals(oldCode,newCode)){
            roleCodeManager.updateCodes(oldCode,newCode);
        }
    }

    /**
     * 根据id查询
     */
    public PermCodeResult findById(Long id) {
        return permCodeManager.findById(id).map(PermCode::toResult).orElseThrow(DataNotExistException::new);
    }

    /**
     * 获取全部权限码
     */
    public List<String> findAllCode(){
        return permCodeManager.findAll().stream()
                .map(PermCode::getCode)
                .toList();
    }

    /**
     * 删除
     */
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        PermCode permCode = permCodeManager.findById(id).orElseThrow(() -> new BizException("权限码信息不存在"));
        // 有子菜单不可以删除
        if (permCode.isLeaf()){
            if (permCodeManager.existedByField(PermCode::getPid, id)) {
                throw new BizException("目录下有权限码数据不允许删除");
            }
        }
        roleCodeManager.deleteByField(RoleCode::getCode, permCode.getCode());
        permCodeManager.deleteById(id);
    }

    /**
     * 权限码树
     */
    public List<PermCodeResult> tree() {
        List<PermCodeResult> list = permCodeManager.findAll()
                .stream()
                .map(PermCode::toResult)
                .toList();
        return TreeBuildUtil.build(list, null, PermCodeResult::getId, PermCodeResult::getPid, PermCodeResult::setChildren);
    }

    /**
     * 权限码是否存在
     */
    public boolean existsByCode(String permCode) {
        return permCodeManager.existedByField(PermCode::getCode, permCode);
    }

    /**
     * 权限码是否存在
     */
    public Boolean existsByPermCode(String permCode, Long id) {
        return permCodeManager.existedByField(PermCode::getCode, permCode, id);
    }
}
