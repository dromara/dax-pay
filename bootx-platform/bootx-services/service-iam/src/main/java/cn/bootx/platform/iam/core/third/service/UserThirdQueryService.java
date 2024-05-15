package cn.bootx.platform.iam.core.third.service;

import cn.bootx.platform.common.core.exception.DataNotExistException;
import cn.bootx.platform.common.core.rest.PageResult;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.starter.auth.code.AuthLoginTypeCode;
import cn.bootx.platform.starter.auth.util.SecurityUtil;
import cn.bootx.platform.iam.core.third.dao.UserThirdInfoManager;
import cn.bootx.platform.iam.core.third.dao.UserThirdManager;
import cn.bootx.platform.iam.core.third.entity.UserThird;
import cn.bootx.platform.iam.core.third.entity.UserThirdInfo;
import cn.bootx.platform.iam.dto.user.UserThirdBindInfo;
import cn.bootx.platform.iam.dto.user.UserThirdBindInfo.BindInfo;
import cn.bootx.platform.iam.dto.user.UserThirdDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 用户三方账号查询
 *
 * @author xxm
 * @since 2022/4/2
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserThirdQueryService {

    private final UserThirdManager userThirdManager;

    private final UserThirdInfoManager userThirdInfoManager;

    /**
     * 分页
     */
    public PageResult<UserThirdDto> page(PageParam pageParam) {
        return MpUtil.convert2DtoPageResult(userThirdManager.page(pageParam));
    }

    /**
     * 获取详情
     */
    public UserThirdDto findById(Long id) {
        return userThirdManager.findById(id).map(UserThird::toDto).orElseThrow(DataNotExistException::new);
    }

    /**
     * 获取用户绑定的详细信息
     */
    public UserThirdBindInfo getThirdBindInfo() {
        Long userId = SecurityUtil.getUserId();
        UserThirdBindInfo userThirdBindInfo = new UserThirdBindInfo();
        Map<String, UserThirdInfo> thirdInfoMap = userThirdInfoManager.findAllByUser(userId)
            .stream()
            .collect(Collectors.toMap(UserThirdInfo::getClientCode, Function.identity()));
        userThirdBindInfo.setWeChat(getBindInfo(thirdInfoMap, AuthLoginTypeCode.WE_CHAT));
        userThirdBindInfo.setWeChatOpen(getBindInfo(thirdInfoMap, AuthLoginTypeCode.WE_CHAT_OPEN));
        userThirdBindInfo.setWeCom(getBindInfo(thirdInfoMap, AuthLoginTypeCode.WE_COM));
        userThirdBindInfo.setDingTalk(getBindInfo(thirdInfoMap, AuthLoginTypeCode.DING_TALK));

        return userThirdBindInfo;
    }

    /**
     * 获取微信绑定信息
     */
    private BindInfo getBindInfo(Map<String, UserThirdInfo> thirdInfoMap, String clientCode) {
        UserThirdInfo userThirdInfo = thirdInfoMap.get(clientCode);
        BindInfo bindInfo = new BindInfo();
        if (Objects.nonNull(userThirdInfo)) {
            bindInfo.setBind(true);
            bindInfo.setUsername(userThirdInfo.getUsername());
            bindInfo.setThirdUserId(userThirdInfo.getThirdUserId());
        }
        return bindInfo;
    }

}
