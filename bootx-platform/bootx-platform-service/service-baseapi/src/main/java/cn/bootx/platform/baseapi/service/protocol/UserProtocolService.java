package cn.bootx.platform.baseapi.service.protocol;

import cn.bootx.platform.baseapi.dao.protocol.UserProtocolManager;
import cn.bootx.platform.baseapi.entity.protocol.UserProtocol;
import cn.bootx.platform.baseapi.param.protocol.UserProtocolParam;
import cn.bootx.platform.baseapi.param.protocol.UserProtocolQuery;
import cn.bootx.platform.baseapi.result.protocol.UserProtocolResult;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.core.exception.BizException;
import cn.bootx.platform.core.exception.DataNotExistException;
import cn.bootx.platform.core.rest.param.PageParam;
import cn.bootx.platform.core.rest.result.PageResult;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户协议管理服务
 * @author xxm
 * @since 2025/5/9
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserProtocolService {
    private final UserProtocolManager userProtocolManager;

    /**
     * 分页
     */
    public PageResult<UserProtocolResult> page(PageParam pageParam, UserProtocolQuery query){
        return MpUtil.toPageResult(userProtocolManager.page(pageParam,query));

    }

    /**
     * 添加
     */
    public void add(UserProtocolParam param){
        var userProtocol = UserProtocol.init(param);
        userProtocolManager.save(userProtocol);
    }

    /**
     * 更新
     */
    public void update(UserProtocolParam param){
        var userProtocol = userProtocolManager.findById(param.getId())
                .orElseThrow(() -> new DataNotExistException("用户协议不存在"));
        BeanUtil.copyProperties(param, userProtocol, CopyOptions.create().ignoreNullValue());
        userProtocolManager.updateById(userProtocol);
    }

    /**
     * 删除
     */
    public void delete(Long id){
        // 默认不可被删除
        var userProtocol = userProtocolManager.findById(id)
                .orElseThrow(() -> new DataNotExistException("用户协议不存在"));
        if (userProtocol.getDefaultProtocol()){
            throw new BizException("默认协议不可删除");
        }
        userProtocolManager.deleteById(id);
    }

    /**
     * 根据ID查询
     */
    public UserProtocolResult findById(Long id){
        return userProtocolManager.findById(id)
                .map(UserProtocol::toResult)
                .orElseThrow(() -> new DataNotExistException("用户协议不存在"));
    }

    /**
     * 根据分类查询默认协议
     */
    public UserProtocolResult findDefault(String type){
        return userProtocolManager.findDefault(type)
                .map(UserProtocol::toResult)
                .orElseThrow(() -> new DataNotExistException("用户协议不存在"));
    }

    /**
     * 设置默认协议
     */
    @Transactional(rollbackFor = Exception.class)
    public void setDefault(Long id){
        var userProtocol = userProtocolManager.findById(id)
                .orElseThrow(() -> new DataNotExistException("用户协议不存在"));
        userProtocolManager.clearDefault(userProtocol.getType());
        userProtocolManager.setDefault(id);
    }

    /**
     * 取消默认协议
     */
    public void cancelDefault(Long id){
        var userProtocol = userProtocolManager.findById(id)
                .orElseThrow(() -> new DataNotExistException("用户协议不存在"));
        userProtocolManager.cancelDefault(id);
    }
}
