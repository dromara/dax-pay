package cn.bootx.platform.baseapi.dao.protocol;

import cn.bootx.platform.baseapi.entity.protocol.UserProtocol;
import cn.bootx.platform.baseapi.param.protocol.UserProtocolQuery;
import cn.bootx.platform.common.mybatisplus.base.MpRealDelEntity;
import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.common.mybatisplus.query.generator.QueryGenerator;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.core.rest.param.PageParam;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 用户协议管理
 * @author xxm
 * @since 2025/5/9
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class UserProtocolManager extends BaseManager<UserProtocolMapper, UserProtocol> {

    /**
     * 分页
     */
    public Page<UserProtocol> page(PageParam pageParam, UserProtocolQuery query){
        Page<UserProtocol> mpPage = MpUtil.getMpPage(pageParam, UserProtocol.class);
        QueryWrapper<UserProtocol> generator = QueryGenerator.generator(query);
        return this.page(mpPage,generator);
    }

    /**
     * 根据分类查询默认协议
     */
    public Optional<UserProtocol> findDefault(String type){
        return this.lambdaQuery()
                .eq(UserProtocol::getType,type)
                .eq(UserProtocol::getDefaultProtocol,true)
                .oneOpt();
    }

    /**
     * 清除默认协议
     */
    public void clearDefault(String type){
        this.lambdaUpdate()
                .eq(UserProtocol::getType,type)
                .set(UserProtocol::getDefaultProtocol,false)
                .setIncrBy(MpRealDelEntity::getVersion, 1)
                .update();
    }

    /**
     * 设置默认协议
     */
    public void setDefault(Long id){
        this.lambdaUpdate()
                .eq(UserProtocol::getId,id)
                .set(UserProtocol::getDefaultProtocol,true)
                .setIncrBy(MpRealDelEntity::getVersion, 1)
                .update();
    }

    /**
     * 取消默认协议
     */
    public void cancelDefault(Long id){
        this.lambdaUpdate()
                .eq(UserProtocol::getId,id)
                .set(UserProtocol::getDefaultProtocol,false)
                .setIncrBy(MpRealDelEntity::getVersion, 1)
                .update();
    }
}
