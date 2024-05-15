package cn.bootx.platform.iam.core.dept.service;

import cn.bootx.platform.common.core.exception.BizException;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.iam.core.dept.dao.DeptManager;
import cn.bootx.platform.iam.core.dept.entity.Dept;
import cn.bootx.platform.iam.dto.dept.DeptTreeResult;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.lock.annotation.Lock4j;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 部门规则工具类
 *
 * @author xxm
 * @since 2020/5/10 15:01
 */
@Service
@RequiredArgsConstructor
public class DeptRuleService {

    private final DeptManager deptManager;

    /**
     * 生成机构代码 根机构_子机构_子子机构 使用分布式锁
     */
    @Lock4j(keys = "#parentId")
    public String generateOrgCode(Long parentId) {
        // 顶级机构
        if (Objects.isNull(parentId)) {
            Dept dept = MpUtil
                .findOne(deptManager.lambdaQuery().isNull(Dept::getParentId).orderByDesc(Dept::getOrgCode))
                .orElse(null);
            if (Objects.isNull(dept)) {
                return "1";
            }
            else {
                return this.getNextCode(dept.getOrgCode());
            }
        }
        else {
            // 父亲
            Dept parenDept = deptManager.findById(parentId).orElseThrow(() -> new BizException("父机构不存在"));
            // 最新的兄弟
            Dept dept = MpUtil
                .findOne(deptManager.lambdaQuery()
                    .eq(Dept::getParentId, parenDept.getId())
                    .orderByDesc(Dept::getOrgCategory))
                .orElse(null);
            if (Objects.isNull(dept)) {
                return parenDept.getOrgCode() + "_1";
            }
            else {
                return this.getNextCode(dept.getOrgCode());
            }
        }
    }

    /**
     * 根据前一个code，获取同级下一个code 例如:当前最大code为1_2，下一个code为：1_3
     */
    public String getNextCode(String code) {
        // 没有分隔符, 纯数字
        if (!StrUtil.contains(code, "_")) {
            int i = Integer.parseInt(code);
            return String.valueOf(i + 1);
        }
        else {
            int start = code.lastIndexOf("_");
            String sub = StrUtil.sub(code, start + 1, code.length());
            int i = Integer.parseInt(sub);
            // 拼接新的 前缀+ '_'+新编号
            return StrUtil.sub(code, 0, start + 1) + (i + 1);
        }

    }

    /**
     * 构造部门树状结构
     */
    public List<DeptTreeResult> buildTreeList(List<Dept> recordList) {
        // 排序
        recordList.sort(Comparator.comparing(Dept::getSortNo));
        List<DeptTreeResult> tree = new ArrayList<>();
        for (Dept dept : recordList) {
            // 查出没有父级的部门
            if (Objects.isNull(dept.getParentId())) {
                DeptTreeResult deptTreeResult = new DeptTreeResult();
                BeanUtil.copyProperties(dept, deptTreeResult);
                this.findChildren(deptTreeResult, recordList);
                tree.add(deptTreeResult);
            }
        }
        return tree;
    }

    /**
     * 递归查找子节点
     */
    private void findChildren(DeptTreeResult treeNode, List<Dept> categories) {
        for (Dept category : categories) {
            if (treeNode.getId().equals(category.getParentId())) {
                if (treeNode.getChildren() == null) {
                    treeNode.setChildren(new ArrayList<>());
                }
                DeptTreeResult childNode = new DeptTreeResult();
                BeanUtil.copyProperties(category, childNode);
                findChildren(childNode, categories);
                // 子节点
                treeNode.getChildren().add(childNode);
            }
        }
    }

}
