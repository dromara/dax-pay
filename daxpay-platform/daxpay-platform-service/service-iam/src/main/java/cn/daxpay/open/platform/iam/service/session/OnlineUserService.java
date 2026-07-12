package cn.daxpay.open.platform.iam.service.session;

import cn.daxpay.open.platform.core.code.CommonCode;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.code.DaxPayErrorCode;
import cn.daxpay.open.platform.core.entity.UserDetail;
import cn.daxpay.open.platform.core.exception.BizException;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import cn.daxpay.open.platform.iam.param.session.OnlineUserQuery;
import cn.daxpay.open.platform.iam.result.session.OnlineUserResult;
import cn.dev33.satoken.SaManager;
import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/// # 在线用户服务
///
@Slf4j
@Service
@RequiredArgsConstructor
public class OnlineUserService {

    private final StringRedisTemplate stringRedisTemplate;

    /// 分页查询在线用户
    /// 优化：使用 Sa-Token API 获取会话列表，批量获取会话详情
    public PageResult<OnlineUserResult> page(PageParam pageParam, OnlineUserQuery query) {
        // 第一步：使用 Sa-Token API 获取所有会话 ID
        List<String> sessionIds = StpUtil.searchSessionId("", 0, -1, false);
        if (sessionIds.isEmpty()) {
            return new PageResult<OnlineUserResult>()
                    .setRecords(new ArrayList<>())
                    .setTotal(0)
                    .setCurrent(pageParam.getCurrent())
                    .setSize(pageParam.getSize());
        }

        // 第二步：批量获取会话详情
        // searchSessionId 返回的就是完整的 Redis key 格式
        List<String> keys = new ArrayList<>(sessionIds);

        // 批量获取原始 JSON 字符串
        List<String> jsonList = stringRedisTemplate.opsForValue().multiGet(keys);
        if (CollUtil.isEmpty(jsonList)) {
            return new PageResult<OnlineUserResult>()
                    .setRecords(new ArrayList<>())
                    .setTotal(0)
                    .setCurrent(pageParam.getCurrent())
                    .setSize(pageParam.getSize());
        }

        List<OnlineUserResult> allResults = new ArrayList<>();

        for (String json : jsonList) {
            if (json == null) {
                continue;
            }
            try {
                // 使用 Sa-Token 自身的 JSON 模板反序列化，与写入时完全对称（自动保留类型信息）
                SaSession session = SaManager.getSaJsonTemplate().jsonToObject(json, SaSession.class);
                if (session == null) {
                    continue;
                }

                UserDetail userDetail = session.getModel(CommonCode.USER, UserDetail.class);
                if (userDetail == null) {
                    continue;
                }

                // 过滤条件
                if (StrUtil.isNotBlank(query.getUsername()) && !userDetail.getName().contains(query.getUsername())) {
                    continue;
                }
                if (StrUtil.isNotBlank(query.getAccount()) && !userDetail.getAccount().contains(query.getAccount())) {
                    continue;
                }
                if (StrUtil.isNotBlank(query.getClientCode()) && !query.getClientCode().equals(userDetail.getClientCode())) {
                    continue;
                }

                OnlineUserResult result = new OnlineUserResult()
                        .setSessionId(session.getId())
                        .setUserId(userDetail.getId())
                        .setUsername(userDetail.getName())
                        .setAccount(userDetail.getAccount())
                        .setClientCode(userDetail.getClientCode())
                        .setLoginTime(toLocalDateTime(session.getCreateTime()));

                allResults.add(result);
            } catch (Exception e) {
                log.warn("获取会话信息失败: json={}", json, e);
            }
        }

        // 内存分页
        int total = allResults.size();
        int start = (pageParam.getCurrent() - 1) * pageParam.getSize();
        int end = Math.min(start + pageParam.getSize(), total);

        List<OnlineUserResult> pageResults = start < total
                ? allResults.subList(start, end)
                : new ArrayList<>();

        return new PageResult<OnlineUserResult>()
                .setRecords(pageResults)
                .setTotal((long) total)
                .setCurrent(pageParam.getCurrent())
                .setSize(pageParam.getSize());
    }

    /// 强制用户下线
    public void kickout(String sessionId) {
        try {
            // sessionId 是完整的 Redis key，格式: {tokenName}:{loginType}:session:{loginId}
            // 使用 Sa-Token API 获取 session，然后获取 loginId
            SaSession session = StpUtil.getSessionBySessionId(sessionId);
            if (session == null) {
                throw new BizInfoException(CommonErrorCode.AUTHENTICATION_FAIL, "error.iam.session.sessionNotExistOrExpired");
            }

            Object loginId = session.getLoginId();
            if (loginId == null) {
                throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "error.iam.session.invalidSessionId");
            }

            Long userId = Long.parseLong(loginId.toString());

            // 不允许踢掉超级管理员
            UserDetail userDetail = session.getModel(CommonCode.USER, UserDetail.class);
            if (userDetail != null && userDetail.isAdmin()) {
                throw new BizInfoException(CommonErrorCode.UN_SUPPORTED_OPERATE, "error.iam.session.cannotKickoutAdmin");
            }

            StpUtil.kickout(userId);
        } catch (NumberFormatException e) {
            log.error("解析用户ID失败: sessionId={}", sessionId, e);
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "error.iam.session.invalidSessionId");
        } catch (Exception e) {
            // 业务异常直接透传, 不兜底为系统错误
            if (e instanceof BizException) throw e;
            log.error("强制用户下线失败: sessionId={}", sessionId, e);
            throw new BizInfoException(CommonErrorCode.SYSTEM_ERROR, "error.iam.session.kickoutFailed", e.getMessage());
        }
    }

    /// 批量强制用户下线
    public void kickoutBatch(List<String> sessionIds) {
        for (String sessionId : sessionIds) {
            try {
                kickout(sessionId);
            } catch (Exception e) {
                log.warn("批量强制用户下线失败: sessionId={}, error={}", sessionId, e.getMessage());
            }
        }
    }

    /// 修改密码后: 强制该用户除当前 token 外的所有会话下线(保留当前设备)
    /// 用于个人修改密码场景, 避免自己被踢下线
    public void kickoutOtherSessions(Long userId) {
        String currentToken = StpUtil.getTokenValue();
        List<String> tokenList = StpUtil.getTokenValueListByLoginId(userId);
        for (String token : tokenList) {
            if (!Objects.equals(token, currentToken)) {
                // 标记为踢下线, 前端下次请求会收到 KICK_OUT 类型的未登录异常
                StpUtil.kickoutByTokenValue(token);
            }
        }
    }

    /// 强制用户全部会话下线(不保留任何 token)
    /// 用于管理员重置密码场景
    public void kickoutAllSessions(Long userId) {
        StpUtil.kickout(userId);
    }

    /// 时间戳转 OffsetDateTime (UTC)
    private OffsetDateTime toLocalDateTime(long timestamp) {
        return Instant.ofEpochMilli(timestamp).atOffset(ZoneOffset.UTC);
    }
}

