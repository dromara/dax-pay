package cn.daxpay.open.platform.system.dao.protocol;

import cn.daxpay.open.platform.system.entity.protocol.UserProtocol;
import cn.daxpay.open.platform.system.param.protocol.UserProtocolQuery;
import cn.daxpay.open.platform.common.mybatisplus.base.MpRealDelEntity;
import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import cn.daxpay.open.platform.common.mybatisplus.query.generator.QueryGenerator;
import cn.daxpay.open.platform.common.mybatisplus.util.MpUtil;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/// # 用户协议管理
///
@Slf4j
@Repository
@RequiredArgsConstructor
public class UserProtocolManager extends BaseManager<UserProtocolMapper, UserProtocol> {

    /// 分页
    public Page<UserProtocol> page(PageParam pageParam, UserProtocolQuery query){
        Page<UserProtocol> mpPage = MpUtil.getMpPage(pageParam, UserProtocol.class);
        QueryWrapper<UserProtocol> generator = QueryGenerator.generator(query);
        return this.page(mpPage,generator);
    }

    /// 根据分类查询默认协议
    public Optional<UserProtocol> findDefault(String type, String clientType){
        return this.lambdaQuery()
                .eq(UserProtocol::getType,type)
                .eq(UserProtocol::getClientType, clientType)
                .eq(UserProtocol::getDefaultProtocol,true)
                .oneOpt();
    }

    /// 清除默认协议
    public void clearDefault(String type, String clientType){
        this.lambdaUpdate()
                .eq(UserProtocol::getType,type)
                .eq(UserProtocol::getClientType, clientType)
                .set(UserProtocol::getDefaultProtocol,false)
                .setIncrBy(MpRealDelEntity::getVersion, 1)
                .update();
    }

    /// 设置默认协议
    public void setDefault(Long id){
        this.lambdaUpdate()
                .eq(UserProtocol::getId,id)
                .set(UserProtocol::getDefaultProtocol,true)
                .setIncrBy(MpRealDelEntity::getVersion, 1)
                .update();
    }

    /// 取消默认协议
    public void cancelDefault(Long id){
        this.lambdaUpdate()
                .eq(UserProtocol::getId,id)
                .set(UserProtocol::getDefaultProtocol,false)
                .setIncrBy(MpRealDelEntity::getVersion, 1)
                .update();
    }
}
