package cn.daxpay.open.platform.system.service.protocol;

import cn.daxpay.open.platform.system.convert.protocol.UserProtocolConvert;
import cn.daxpay.open.platform.system.dao.protocol.UserProtocolItemManager;
import cn.daxpay.open.platform.system.dao.protocol.UserProtocolManager;
import cn.daxpay.open.platform.system.entity.protocol.UserProtocolItem;
import cn.daxpay.open.platform.system.param.protocol.UserProtocolItemParam;
import cn.daxpay.open.platform.system.param.protocol.UserProtocolItemQuery;
import cn.daxpay.open.platform.system.result.protocol.UserProtocolItemResult;
import cn.daxpay.open.platform.common.mybatisplus.util.MpUtil;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/// # 用户协议项管理服务
///
@Slf4j
@Service
@RequiredArgsConstructor
public class UserProtocolItemService {
    private final UserProtocolItemManager userProtocolItemManager;
    private final UserProtocolManager userProtocolManager;

    /// 分页
    public PageResult<UserProtocolItemResult> page(PageParam pageParam, UserProtocolItemQuery query){
        return MpUtil.toPageResult(userProtocolItemManager.page(pageParam,query));
    }

    /// 创建协议项
    public void add(UserProtocolItemParam param){
        var userProtocolItem = UserProtocolItem.init(param);
        userProtocolItemManager.save(userProtocolItem);
    }

    /// 更新协议项
    public void update(UserProtocolItemParam param){
        var userProtocolItem = userProtocolItemManager.findById(param.getId())
                // 系统: 协议项不存在
                .orElseThrow(() -> new DataNotExistException("error.system.protocol.itemNotExist"));
        UserProtocolConvert.CONVERT.copy(param, userProtocolItem);
        userProtocolItemManager.updateById(userProtocolItem);
    }

    /// 删除
    public void delete(Long id){
        userProtocolItemManager.deleteById(id);
    }

    /// 根据ID查询
    public UserProtocolItemResult findById(Long id){
        return userProtocolItemManager.findById(id)
                .map(UserProtocolItem::toResult)
                // 系统: 协议项不存在
                .orElseThrow(() -> new DataNotExistException("error.system.protocol.itemNotExist"));
    }

    /// 根据协议ID查询协议项列表
    public PageResult<UserProtocolItemResult> page(PageParam pageParam, Long protocolId){
        UserProtocolItemQuery query = new UserProtocolItemQuery();
        query.setProtocolId(protocolId);
        return MpUtil.toPageResult(userProtocolItemManager.page(pageParam, query));
    }

    /// 根据类型查询默认协议，然后根据协议code查询明细，根据排序字段进行排序
    public List<UserProtocolItemResult> findByProtocolType(String type, String clientType) {
        // 根据类型查询默认协议
        var defaultProtocol = userProtocolManager.findDefault(type, clientType)
                // 系统: 默认协议项不存在
                .orElseThrow(() -> new DataNotExistException("error.system.protocol.defaultItemNotExist"));
        var protocolItems = userProtocolItemManager.findAllByProtocolIdOrderBySortNo(defaultProtocol.getId());
        return MpUtil.toListResult(protocolItems);
    }

}
