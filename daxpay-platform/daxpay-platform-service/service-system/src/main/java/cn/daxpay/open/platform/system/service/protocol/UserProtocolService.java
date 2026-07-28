package cn.daxpay.open.platform.system.service.protocol;

import cn.daxpay.open.platform.common.mybatisplus.util.MpUtil;
import cn.daxpay.open.platform.core.code.CommonCode;
import cn.daxpay.open.platform.core.exception.BizException;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import cn.daxpay.open.platform.system.convert.protocol.UserProtocolConvert;
import cn.daxpay.open.platform.system.dao.protocol.UserProtocolManager;
import cn.daxpay.open.platform.system.dao.protocol.UserProtocolVersionManager;
import cn.daxpay.open.platform.system.entity.protocol.UserProtocol;
import cn.daxpay.open.platform.system.entity.protocol.UserProtocolVersion;
import cn.daxpay.open.platform.system.enums.UserProtocolClientTypeEnum;
import cn.daxpay.open.platform.system.enums.UserProtocolTypeEnum;
import cn.daxpay.open.platform.system.enums.UserProtocolVersionStatusEnum;
import cn.daxpay.open.platform.system.param.protocol.UserProtocolParam;
import cn.daxpay.open.platform.system.param.protocol.UserProtocolQuery;
import cn.daxpay.open.platform.system.result.protocol.UserProtocolContentResult;
import cn.daxpay.open.platform.system.result.protocol.UserProtocolResult;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

/// # 用户协议管理服务
///
@Slf4j
@Service
@RequiredArgsConstructor
public class UserProtocolService {
    private final UserProtocolManager userProtocolManager;
    private final UserProtocolVersionManager userProtocolVersionManager;
    private final UserProtocolVersionService userProtocolVersionService;

    /// 默认语言
    private static final String DEFAULT_LANGUAGE = "zh-CN";

    /// 分页
    public PageResult<UserProtocolResult> page(PageParam pageParam, UserProtocolQuery query){
        return MpUtil.toPageResult(userProtocolManager.page(pageParam,query));

    }

    /// 创建协议
    public void add(UserProtocolParam param){
        this.validateParam(param);
        var userProtocol = UserProtocol.init(param);
        userProtocol.setDefaultLanguage(this.getLanguage(param.getDefaultLanguage()));
        userProtocolManager.save(userProtocol);
    }

    /// 更新协议
    public void update(UserProtocolParam param){
        this.validateParam(param);
        var userProtocol = userProtocolManager.findById(param.getId())
                // 系统: 协议不存在
                .orElseThrow(() -> new DataNotExistException("error.system.protocol.notExist"));
        UserProtocolConvert.CONVERT.copy(param, userProtocol);
        userProtocol.setDefaultLanguage(this.getLanguage(param.getDefaultLanguage()));
        userProtocolManager.updateById(userProtocol);
    }

    /// 删除(级联删除版本)
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id){
        // 默认不可被删除
        var userProtocol = userProtocolManager.findById(id)
                // 系统: 协议不存在
                .orElseThrow(() -> new DataNotExistException("error.system.protocol.notExist"));
        if (userProtocol.getDefaultProtocol()){
            // 系统: 默认协议不可删除
            throw new BizException(CommonCode.FAIL_CODE, "error.system.protocol.defaultCannotDelete");
        }
        // 级联删除版本
        userProtocolVersionManager.deleteByProtocolId(id);
        userProtocolManager.deleteById(id);
    }

    /// 根据ID查询
    public UserProtocolResult findById(Long id){
        return userProtocolManager.findById(id)
                .map(UserProtocol::toResult)
                // 系统: 协议不存在
                .orElseThrow(() -> new DataNotExistException("error.system.protocol.notExist"));
    }

    /// 根据分类查询默认协议内容(对外展示), language 为空时回退到协议默认语言
    public UserProtocolContentResult findDefault(String type, String clientType, String language){
        UserProtocolTypeEnum.findByCode(type);
        UserProtocolClientTypeEnum.findByCode(clientType);
        var protocol = userProtocolManager.findDefault(type, clientType)
                // 系统: 协议不存在
                .orElseThrow(() -> new DataNotExistException("error.system.protocol.notExist"));
        String lang = StrUtil.isBlank(language) ? protocol.getDefaultLanguage() : language;
        var version = userProtocolVersionManager.findPublished(protocol.getId(), lang).orElse(null);
        // 组装对外内容结果
        var result = new UserProtocolContentResult();
        result.setId(protocol.getId());
        result.setName(protocol.getName());
        result.setShowName(protocol.getShowName());
        result.setType(protocol.getType());
        result.setClientType(protocol.getClientType());
        result.setLanguage(lang);
        if (version != null){
            result.setVersionNo(version.getVersionNo());
            result.setVersionLabel(version.getVersionLabel());
            result.setTitle(version.getTitle());
            result.setContent(version.getContent());
            result.setContentHtml(version.getContentHtml());
            result.setContentFormat(version.getContentFormat());
            result.setEffectiveTime(version.getEffectiveTime());
        }
        return result;
    }

    /// 设置默认协议
    @Transactional(rollbackFor = Exception.class)
    public void setDefault(Long id){
        var userProtocol = userProtocolManager.findById(id)
                // 系统: 协议不存在
                .orElseThrow(() -> new DataNotExistException("error.system.protocol.notExist"));
        userProtocolManager.clearDefault(userProtocol.getType(), userProtocol.getClientType());
        userProtocolManager.setDefault(id);
    }

    /// 取消默认协议
    public void cancelDefault(Long id){
        var userProtocol = userProtocolManager.findById(id)
                // 系统: 协议不存在
                .orElseThrow(() -> new DataNotExistException("error.system.protocol.notExist"));
        userProtocolManager.cancelDefault(id);
    }

    /// 复制协议到目标端(含每种语言的当前生效版本)
    @Transactional(rollbackFor = Exception.class)
    public Long copyToClient(Long sourceProtocolId, String targetClientType){
        var source = userProtocolManager.findById(sourceProtocolId)
                // 系统: 协议不存在
                .orElseThrow(() -> new DataNotExistException("error.system.protocol.notExist"));
        UserProtocolClientTypeEnum.findByCode(targetClientType);
        if (targetClientType.equalsIgnoreCase(source.getClientType())){
            // 系统: 目标端类型与源端一致
            throw new BizException(CommonCode.FAIL_CODE, "error.system.protocol.sameClientType");
        }
        // 创建目标协议
        var target = new UserProtocol();
        target.setName(source.getName());
        target.setShowName(source.getShowName());
        target.setType(source.getType());
        target.setClientType(targetClientType);
        target.setDefaultProtocol(false);
        target.setDefaultLanguage(source.getDefaultLanguage());
        userProtocolManager.save(target);
        // 复制每种语言的已发布版本(同语言仅一条已发布)
        var versions = userProtocolVersionService.findAllByProtocol(source.getId());
        Map<String, UserProtocolVersion> publishedByLang = new HashMap<>();
        for (var v : versions){
            if (UserProtocolVersionStatusEnum.PUBLISHED.getCode().equals(v.getStatus())){
                publishedByLang.put(v.getLanguage(), v);
            }
        }
        for (var v : publishedByLang.values()){
            userProtocolVersionService.copyVersion(v, target.getId());
        }
        return target.getId();
    }

    private void validateParam(UserProtocolParam param) {
        UserProtocolTypeEnum.findByCode(param.getType());
        UserProtocolClientTypeEnum.findByCode(param.getClientType());
        this.getLanguage(param.getDefaultLanguage());
    }

    private String getLanguage(String language) {
        return StrUtil.isBlank(language) ? DEFAULT_LANGUAGE : language;
    }
}
