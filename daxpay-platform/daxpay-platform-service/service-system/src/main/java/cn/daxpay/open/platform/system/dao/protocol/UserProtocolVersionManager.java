package cn.daxpay.open.platform.system.dao.protocol;

import cn.daxpay.open.platform.common.mybatisplus.base.MpRealDelEntity;
import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import cn.daxpay.open.platform.common.mybatisplus.query.generator.QueryGenerator;
import cn.daxpay.open.platform.common.mybatisplus.util.MpUtil;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.system.entity.protocol.UserProtocolVersion;
import cn.daxpay.open.platform.system.param.protocol.UserProtocolVersionQuery;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

/// # 用户协议版本管理
///
@Slf4j
@Repository
@RequiredArgsConstructor
public class UserProtocolVersionManager extends BaseManager<UserProtocolVersionMapper, UserProtocolVersion> {

    /// 分页
    public Page<UserProtocolVersion> page(PageParam pageParam, UserProtocolVersionQuery query){
        Page<UserProtocolVersion> mpPage = MpUtil.getMpPage(pageParam, UserProtocolVersion.class);
        QueryWrapper<UserProtocolVersion> generator = QueryGenerator.generator(query);
        return this.page(mpPage,generator);
    }

    /// 查询当前生效版本(已发布)
    public Optional<UserProtocolVersion> findPublished(Long protocolId, String language){
        return this.lambdaQuery()
                .eq(UserProtocolVersion::getProtocolId,protocolId)
                .eq(UserProtocolVersion::getLanguage,language)
                .eq(UserProtocolVersion::getStatus, "PUBLISHED")
                .oneOpt();
    }

    /// 查询可继承的源版本: 同协议同语言已发布优先, 否则取 versionNo 最大的归档版
    public Optional<UserProtocolVersion> findLatestForInherit(Long protocolId, String language){
        Optional<UserProtocolVersion> published = this.findPublished(protocolId, language);
        if (published.isPresent()) {
            return published;
        }
        return firstOpt(q -> q
                .eq(UserProtocolVersion::getProtocolId, protocolId)
                .eq(UserProtocolVersion::getLanguage, language)
                .eq(UserProtocolVersion::getStatus, "ARCHIVED")
                .orderByDesc(UserProtocolVersion::getVersionNo));
    }

    /// 查询某协议下所有版本(级联删除/复制用)
    public List<UserProtocolVersion> findAllByProtocol(Long protocolId){
        return this.lambdaQuery()
                .eq(UserProtocolVersion::getProtocolId,protocolId)
                .list();
    }

    /// 获取下一个版本号(同协议同语言自增)
    public Integer nextVersionNo(Long protocolId, String language){
        Long count = this.lambdaQuery()
                .eq(UserProtocolVersion::getProtocolId,protocolId)
                .eq(UserProtocolVersion::getLanguage,language)
                .count();
        return count.intValue() + 1;
    }

    /// 将同协议同语言的原已发布版本归档
    public void archivePublished(Long protocolId, String language){
        this.lambdaUpdate()
                .eq(UserProtocolVersion::getProtocolId,protocolId)
                .eq(UserProtocolVersion::getLanguage,language)
                .eq(UserProtocolVersion::getStatus, "PUBLISHED")
                .set(UserProtocolVersion::getStatus, "ARCHIVED")
                .setIncrBy(MpRealDelEntity::getVersion, 1)
                .update();
    }

    /// 发布版本
    public void publish(Long id, Integer versionNo){
        this.lambdaUpdate()
                .eq(UserProtocolVersion::getId,id)
                .set(UserProtocolVersion::getStatus, "PUBLISHED")
                .set(UserProtocolVersion::getVersionNo, versionNo)
                .set(UserProtocolVersion::getEffectiveTime, OffsetDateTime.now())
                .setIncrBy(MpRealDelEntity::getVersion, 1)
                .update();
    }

    /// 归档版本
    public void archive(Long id){
        this.lambdaUpdate()
                .eq(UserProtocolVersion::getId,id)
                .set(UserProtocolVersion::getStatus, "ARCHIVED")
                .setIncrBy(MpRealDelEntity::getVersion, 1)
                .update();
    }

    /// 根据协议ID删除全部版本(级联)
    public void deleteByProtocolId(Long protocolId){
        this.lambdaUpdate()
                .eq(UserProtocolVersion::getProtocolId,protocolId)
                .remove();
    }
}
