package cn.bootx.platform.iam.service.permission;

import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.core.exception.DataNotExistException;
import cn.bootx.platform.iam.dao.permission.PermPathManager;
import cn.bootx.platform.iam.dao.upms.RolePathManager;
import cn.bootx.platform.iam.entity.permission.PermPath;
import cn.bootx.platform.iam.result.permission.PermPathResult;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 请求权限
 *
 * @author xxm
 * @since 2020/5/10 23:20
 */
@Service
@AllArgsConstructor
public class PermPathService {

    private final PermPathManager permPathManager;

    private final RolePathManager rolePathManager;

    /**
     * 获取单条
     */
    public PermPathResult findById(Long id) {
        return permPathManager.findById(id).map(PermPath::toResult).orElseThrow(DataNotExistException::new);
    }

    /**
     * 根据ids查询权限信息
     */
    public List<PermPathResult> findByIds(List<Long> ids) {
        return MpUtil.toListResult(permPathManager.findAllByIds(ids));
    }

    /**
     * 全部列表
     */
    public List<PermPathResult> findAll() {
        return MpUtil.toListResult(permPathManager.findAll());
    }

    /**
     * 根据
     */


}
