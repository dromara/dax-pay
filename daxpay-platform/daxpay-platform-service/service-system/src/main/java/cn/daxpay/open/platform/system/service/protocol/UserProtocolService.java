package cn.daxpay.open.platform.system.service.protocol;

import cn.daxpay.open.platform.system.convert.protocol.UserProtocolConvert;
import cn.daxpay.open.platform.system.dao.protocol.UserProtocolManager;
import cn.daxpay.open.platform.system.entity.protocol.UserProtocol;
import cn.daxpay.open.platform.system.enums.UserProtocolClientTypeEnum;
import cn.daxpay.open.platform.system.enums.UserProtocolContentFormatEnum;
import cn.daxpay.open.platform.system.enums.UserProtocolTypeEnum;
import cn.daxpay.open.platform.system.param.protocol.UserProtocolParam;
import cn.daxpay.open.platform.system.param.protocol.UserProtocolQuery;
import cn.daxpay.open.platform.system.result.protocol.UserProtocolResult;
import cn.daxpay.open.platform.common.mybatisplus.util.MpUtil;
import cn.daxpay.open.platform.core.exception.BizException;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cn.daxpay.open.platform.core.code.CommonCode;

/// # 用户协议管理服务
///
@Slf4j
@Service
@RequiredArgsConstructor
public class UserProtocolService {
    private final UserProtocolManager userProtocolManager;

    /// 分页
    public PageResult<UserProtocolResult> page(PageParam pageParam, UserProtocolQuery query){
        return MpUtil.toPageResult(userProtocolManager.page(pageParam,query));

    }

    /// 创建协议
    public void add(UserProtocolParam param){
        this.validateParam(param);
        var userProtocol = UserProtocol.init(param);
        userProtocol.setContentFormat(this.getContentFormat(param.getContentFormat()));
        userProtocolManager.save(userProtocol);
    }

    /// 更新协议
    public void update(UserProtocolParam param){
        this.validateParam(param);
        var userProtocol = userProtocolManager.findById(param.getId())
                // 系统: 协议不存在
                .orElseThrow(() -> new DataNotExistException("error.system.protocol.notExist"));
        UserProtocolConvert.CONVERT.copy(param, userProtocol);
        userProtocol.setContentFormat(this.getContentFormat(param.getContentFormat()));
        userProtocolManager.updateById(userProtocol);
    }

    /// 删除
    public void delete(Long id){
        // 默认不可被删除
        var userProtocol = userProtocolManager.findById(id)
                // 系统: 协议不存在
                .orElseThrow(() -> new DataNotExistException("error.system.protocol.notExist"));
        if (userProtocol.getDefaultProtocol()){
            // 默认协议不可删除
            throw new BizException(CommonCode.FAIL_CODE, "error.system.protocol.defaultCannotDelete");
        }
        userProtocolManager.deleteById(id);
    }

    /// 根据ID查询
    public UserProtocolResult findById(Long id){
        return userProtocolManager.findById(id)
                .map(UserProtocol::toResult)
                // 系统: 协议不存在
                .orElseThrow(() -> new DataNotExistException("error.system.protocol.notExist"));
    }

    /// 根据分类查询默认协议
    public UserProtocolResult findDefault(String type, String clientType){
        UserProtocolTypeEnum.findByCode(type);
        UserProtocolClientTypeEnum.findByCode(clientType);
        return userProtocolManager.findDefault(type, clientType)
                .map(UserProtocol::toResult)
                // 系统: 协议不存在
                .orElseThrow(() -> new DataNotExistException("error.system.protocol.notExist"));
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

    private void validateParam(UserProtocolParam param) {
        UserProtocolTypeEnum.findByCode(param.getType());
        UserProtocolClientTypeEnum.findByCode(param.getClientType());
        this.getContentFormat(param.getContentFormat());
    }

    private String getContentFormat(String contentFormat) {
        if (StrUtil.isBlank(contentFormat)) {
            return UserProtocolContentFormatEnum.MARKDOWN.getCode();
        }
        return UserProtocolContentFormatEnum.findByCode(contentFormat).getCode();
    }
}
