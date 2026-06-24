package cn.daxpay.open.platform.system.service.protocol;

import cn.daxpay.open.platform.common.mybatisplus.util.MpUtil;
import cn.daxpay.open.platform.core.code.CommonCode;
import cn.daxpay.open.platform.core.exception.BizException;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import cn.daxpay.open.platform.system.convert.protocol.UserProtocolVersionConvert;
import cn.daxpay.open.platform.system.dao.protocol.UserProtocolVersionManager;
import cn.daxpay.open.platform.system.entity.protocol.UserProtocolVersion;
import cn.daxpay.open.platform.system.enums.UserProtocolContentFormatEnum;
import cn.daxpay.open.platform.system.enums.UserProtocolVersionStatusEnum;
import cn.daxpay.open.platform.system.param.protocol.UserProtocolVersionParam;
import cn.daxpay.open.platform.system.param.protocol.UserProtocolVersionQuery;
import cn.daxpay.open.platform.system.result.protocol.UserProtocolVersionResult;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/// # 用户协议版本管理服务
///
@Slf4j
@Service
@RequiredArgsConstructor
public class UserProtocolVersionService {
    private final UserProtocolVersionManager userProtocolVersionManager;

    /// 分页
    public PageResult<UserProtocolVersionResult> page(PageParam pageParam, UserProtocolVersionQuery query){
        return MpUtil.toPageResult(userProtocolVersionManager.page(pageParam,query));
    }

    /// 创建草稿
    public void add(UserProtocolVersionParam param){
        var version = UserProtocolVersion.init(param);
        version.setContentFormat(this.getContentFormat(param.getContentFormat()));
        version.setStatus(UserProtocolVersionStatusEnum.DRAFT.getCode());
        userProtocolVersionManager.save(version);
    }

    /// 更新草稿内容(仅草稿可编辑)
    public void update(UserProtocolVersionParam param){
        var version = userProtocolVersionManager.findById(param.getId())
                // 系统: 协议版本不存在
                .orElseThrow(() -> new DataNotExistException("error.system.protocol.versionNotExist"));
        if (!UserProtocolVersionStatusEnum.DRAFT.getCode().equals(version.getStatus())) {
            // 系统: 仅草稿状态可编辑
            throw new BizException(CommonCode.FAIL_CODE, "error.system.protocol.draftOnlyCanEdit");
        }
        UserProtocolVersionConvert.CONVERT.copy(param, version);
        version.setContentFormat(this.getContentFormat(param.getContentFormat()));
        userProtocolVersionManager.updateById(version);
    }

    /// 删除(仅草稿可删除)
    public void delete(Long id){
        var version = userProtocolVersionManager.findById(id)
                // 系统: 协议版本不存在
                .orElseThrow(() -> new DataNotExistException("error.system.protocol.versionNotExist"));
        if (!UserProtocolVersionStatusEnum.DRAFT.getCode().equals(version.getStatus())) {
            // 系统: 仅草稿状态可删除
            throw new BizException(CommonCode.FAIL_CODE, "error.system.protocol.draftOnlyCanDelete");
        }
        userProtocolVersionManager.deleteById(id);
    }

    /// 详情
    public UserProtocolVersionResult findById(Long id){
        return userProtocolVersionManager.findById(id)
                .map(UserProtocolVersion::toResult)
                // 系统: 协议版本不存在
                .orElseThrow(() -> new DataNotExistException("error.system.protocol.versionNotExist"));
    }

    /// 发布版本: 草稿 -> 已发布, 同协议同语言原已发布 -> 归档
    @Transactional(rollbackFor = Exception.class)
    public void publish(Long id){
        var version = userProtocolVersionManager.findById(id)
                // 系统: 协议版本不存在
                .orElseThrow(() -> new DataNotExistException("error.system.protocol.versionNotExist"));
        if (!UserProtocolVersionStatusEnum.DRAFT.getCode().equals(version.getStatus())) {
            // 系统: 仅草稿状态可发布
            throw new BizException(CommonCode.FAIL_CODE, "error.system.protocol.draftOnlyCanPublish");
        }
        // 分配版本号
        Integer versionNo = userProtocolVersionManager.nextVersionNo(version.getProtocolId(), version.getLanguage());
        // 旧生效版本归档
        userProtocolVersionManager.archivePublished(version.getProtocolId(), version.getLanguage());
        // 发布当前版本
        userProtocolVersionManager.publish(id, versionNo);
    }

    /// 归档版本(已发布 -> 归档)
    public void archive(Long id){
        var version = userProtocolVersionManager.findById(id)
                // 系统: 协议版本不存在
                .orElseThrow(() -> new DataNotExistException("error.system.protocol.versionNotExist"));
        if (!UserProtocolVersionStatusEnum.PUBLISHED.getCode().equals(version.getStatus())) {
            // 系统: 仅已发布状态可归档
            throw new BizException(CommonCode.FAIL_CODE, "error.system.protocol.publishedOnlyCanArchive");
        }
        userProtocolVersionManager.archive(id);
    }

    /// 根据协议ID和语言查询当前生效版本
    public UserProtocolVersionResult findPublished(Long protocolId, String language){
        return userProtocolVersionManager.findPublished(protocolId, language)
                .map(UserProtocolVersion::toResult)
                .orElse(null);
    }

    /// 复制版本到目标协议(用于复制到其他端, 直接以已发布状态写入)
    @Transactional(rollbackFor = Exception.class)
    public void copyVersion(UserProtocolVersion source, Long targetProtocolId){
        var copy = new UserProtocolVersion();
        copy.setProtocolId(targetProtocolId);
        copy.setLanguage(source.getLanguage());
        copy.setVersionNo(userProtocolVersionManager.nextVersionNo(targetProtocolId, source.getLanguage()));
        copy.setVersionLabel(source.getVersionLabel());
        copy.setTitle(source.getTitle());
        copy.setContent(source.getContent());
        copy.setContentHtml(source.getContentHtml());
        copy.setContentFormat(source.getContentFormat());
        copy.setStatus(UserProtocolVersionStatusEnum.PUBLISHED.getCode());
        copy.setSummary(source.getSummary());
        userProtocolVersionManager.save(copy);
    }

    /// 根据协议ID查询全部版本(复制时用)
    public List<UserProtocolVersion> findAllByProtocol(Long protocolId){
        return userProtocolVersionManager.findAllByProtocol(protocolId);
    }

    private String getContentFormat(String contentFormat) {
        if (StrUtil.isBlank(contentFormat)) {
            return UserProtocolContentFormatEnum.MARKDOWN.getCode();
        }
        return UserProtocolContentFormatEnum.findByCode(contentFormat).getCode();
    }
}
