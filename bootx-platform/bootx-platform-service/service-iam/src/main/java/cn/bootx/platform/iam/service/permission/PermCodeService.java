package cn.bootx.platform.iam.service.permission;

import cn.bootx.platform.core.exception.BizException;
import cn.bootx.platform.core.exception.BizInfoException;
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
import cn.hutool.core.collection.CollUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
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
            // 判断上级是否是目录
            if (Objects.nonNull(param.getPid())){
                PermCode code = permCodeManager.findById(param.getPid()).orElseThrow(() -> new DataNotExistException("上级权限码目录不存在"));
                if (code.isLeaf()){
                    throw new BizInfoException("上级节点不是目录");
                }
            }
            // 权限码是否存在
            if (permCodeManager.existedByField(PermCode::getCode, param.getCode())){
                throw new BizException("权限码已存在");
            }
        } else {
            permCode.setCode(null);
        }
        permCodeManager.save(permCode);
    }

    /**
     * 更新
     */
    @CacheEvict(value = "cache:permCode", allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void update(PermCodeParam param) {
        PermCode permCode = permCodeManager.findById(param.getId()).orElseThrow(() -> new BizException("权限码信息不存在"));
        if (permCode.isLeaf()){
            // 判断上级是否是目录
            if (Objects.nonNull(param.getPid())){
                PermCode code = permCodeManager.findById(param.getPid()).orElseThrow(() -> new DataNotExistException("上级权限码目录不存在"));
                if (code.isLeaf()){
                    throw new BizInfoException("上级节点不是目录");
                }
            }
            // 权限码是否存在
            if (permCodeManager.existedByField(PermCode::getCode, param.getCode(), param.getId())){
                throw new BizException("权限码已存在");
            }
        } else {
            permCode.setCode(null);
        }
        BeanUtil.copyProperties(param, permCode, CopyOptions.create().ignoreNullValue());
        permCodeManager.updateById(permCode);
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
        return permCodeManager.findAllByLeaf(true)
                .stream()
                .map(PermCode::getCode)
                .toList();
    }

    /**
     * 删除
     */
    @CacheEvict(value = "cache:permCode", allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        PermCode permCode = permCodeManager.findById(id).orElseThrow(() -> new BizException("权限码信息不存在"));
        // 有子权限码不可以删除
        if (!permCode.isLeaf()){
            if (permCodeManager.existedByField(PermCode::getPid, id)) {
                throw new BizException("目录下有权限码数据不允许删除");
            }
        }
        roleCodeManager.deleteByField(RoleCode::getCodeId, permCode.getId());
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
     * 权限目录树
     */
    public List<PermCodeResult> catalogTree() {
        List<PermCodeResult> list = permCodeManager.findAllByLeaf(false)
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


    /**
     * 生成树
     */
    public List<PermCodeResult> buildTree(List<PermCodeResult> permCodes){
        // 生成树
        List<PermCodeResult> tree = TreeBuildUtil.build(permCodes, null, PermCodeResult::getId, PermCodeResult::getPid, PermCodeResult::setChildren);
        // 平铺树并过滤掉没有子节点的目录
        List<PermCodeResult> codeResultList = TreeBuildUtil.unfold(tree, PermCodeResult::getChildren)
                .stream()
                .filter(this::isOrHasLeaf)
                .toList();
        // 重新生成树
        return TreeBuildUtil.build(codeResultList, null, PermCodeResult::getId, PermCodeResult::getPid, PermCodeResult::setChildren);
    }

    /**
     * 判断自己和子孙节点中是否有叶子节点
     */
    private boolean isOrHasLeaf(PermCodeResult permCode) {
        List<PermCodeResult> children = permCode.getChildren();
        if (CollUtil.isEmpty(children)) {
            return permCode.isLeaf();
        }
        for (PermCodeResult codeResult : children) {
            if (CollUtil.isNotEmpty(codeResult.getChildren())) {
                // 如果有下级元素, 进行递归判断
                boolean hasLeaf = this.isOrHasLeaf(codeResult);
                // 如果存在子节点, 直接返回
                if (hasLeaf) {
                    return true;
                }
            }
            // 如果当前节点是子节点, 返回true
            if (codeResult.isLeaf()) {
                return true;
            }
        }
        return false;
    }
}
