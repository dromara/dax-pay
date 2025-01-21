package cn.bootx.platform.iam.service.permission;

import cn.bootx.platform.core.exception.DataNotExistException;
import cn.bootx.platform.core.util.TreeBuildUtil;
import cn.bootx.platform.iam.dao.permission.PermPathManager;
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

    /**
     * 获取单条
     */
    public PermPathResult findById(Long id) {
        return permPathManager.findById(id).map(PermPath::toResult).orElseThrow(DataNotExistException::new);
    }

    public List<PermPathResult> tree(String clientCode) {
        List<PermPathResult> permPaths = permPathManager.findAllByField(PermPath::getClientCode, clientCode).stream()
                .map(PermPath::toResult)
                .toList();
        return TreeBuildUtil.build(permPaths,null, PermPathResult::getCode, PermPathResult::getParentCode, PermPathResult::setChildren);
    }
}
