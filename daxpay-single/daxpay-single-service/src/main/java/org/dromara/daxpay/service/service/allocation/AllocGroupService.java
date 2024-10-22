package org.dromara.daxpay.service.service.allocation;

import cn.bootx.platform.common.mybatisplus.function.CollectorsFunction;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.core.exception.BizException;
import cn.bootx.platform.core.exception.DataNotExistException;
import cn.bootx.platform.core.rest.param.PageParam;
import cn.bootx.platform.core.rest.result.PageResult;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.daxpay.service.bo.allocation.AllocGroupReceiverResultBo;
import org.dromara.daxpay.service.convert.allocation.AllocGroupConvert;
import org.dromara.daxpay.service.dao.allocation.receiver.AllocGroupManager;
import org.dromara.daxpay.service.dao.allocation.receiver.AllocGroupReceiverManager;
import org.dromara.daxpay.service.dao.allocation.receiver.AllocReceiverManager;
import org.dromara.daxpay.service.entity.allocation.receiver.AllocGroup;
import org.dromara.daxpay.service.entity.allocation.receiver.AllocGroupReceiver;
import org.dromara.daxpay.service.entity.allocation.receiver.AllocReceiver;
import org.dromara.daxpay.service.param.allocation.group.*;
import org.dromara.daxpay.service.bo.allocation.AllocGroupResultBo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 分账组服务
 * @author xxm
 * @since 2024/4/1
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AllocGroupService {
    private final AllocGroupManager groupManager;
    private final AllocGroupReceiverManager groupReceiverManager;
    private final AllocReceiverManager receiverManager;

    /**
     * 分页
     */
    public PageResult<AllocGroupResultBo> page(PageParam pageParam, AllocGroupQuery query){
        return MpUtil.toPageResult(groupManager.page(pageParam, query));
    }

    /**
     * 查询详情
     */
    public AllocGroupResultBo findById(Long id){
        return groupManager.findById(id).map(AllocGroup::toResult).orElseThrow(()->new DataNotExistException("分账组不存在"));
    }

    /**
     * 查询分账接收方
     */
    public List<AllocGroupReceiverResultBo> findReceiversByGroups(Long groupId){
        List<AllocGroupReceiver> groupReceivers = groupReceiverManager.findByGroupId(groupId);
        List<Long> receiverIds = groupReceivers.stream()
                .map(AllocGroupReceiver::getReceiverId)
                .collect(Collectors.toList());
        // 查询关联接收方信息
        Map<Long, AllocReceiver> receiverMap = receiverManager.findAllByIds(receiverIds)
                .stream()
                .collect(Collectors.toMap(AllocReceiver::getId, Function.identity(), CollectorsFunction::retainLatest));
        // 组装信息
        return groupReceivers.stream()
                .map(o -> {
                    AllocReceiver receiver = receiverMap.get(o.getReceiverId());
                    AllocGroupReceiverResultBo result = new AllocGroupReceiverResultBo()
                            .setReceiverId(receiver.getId())
                            .setReceiverNo(receiver.getReceiverNo())
                            .setReceiverAccount(receiver.getReceiverAccount())
                            .setReceiverName(receiver.getReceiverName())
                            .setRate(o.getRate())
                            .setReceiverType(receiver.getReceiverType())
                            .setRelationName(receiver.getRelationName())
                            .setRelationType(receiver.getRelationType());
                    result.setId(o.getId());
                    return result;
                })
                .collect(Collectors.toList());
    }

    /**
     * 创建分账组
     */
    public void create(AllocGroupParam param){
        AllocGroup group = AllocGroupConvert.CONVERT.convert(param);
        group.setTotalRate(BigDecimal.ZERO);
        groupManager.save(group);
    }

    /**
     * 设置默认分账组
     */
    @Transactional(rollbackFor = Exception.class)
    public void setUpDefault(Long id){
        // 分账组
        AllocGroup group = groupManager.findById(id).orElseThrow(() -> new DataNotExistException("未找到分账组"));
        groupManager.clearDefault(group.getChannel(), group.getAppId());
        group.setDefaultGroup(true);
        groupManager.updateById(group);
    }

    /**
     * 清除默认分账组
     */
    @Transactional(rollbackFor = Exception.class)
    public void clearDefault(Long id){
        AllocGroup group = groupManager.findById(id).orElseThrow(() -> new DataNotExistException("未找到分账组"));
        group.setDefaultGroup(false);
        groupManager.updateById(group);
    }

    /**
     * 更新分账组
     */
    public void update(AllocGroupParam param){
        AllocGroup group = groupManager.findById(param.getId()).orElseThrow(() -> new DataNotExistException("未找到分账组"));
        BeanUtil.copyProperties(param,group, CopyOptions.create().ignoreNullValue());
        group.setTotalRate(null);
        groupManager.updateById(group);
    }

    /**
     * 删除分账组
     */
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id){
        groupManager.deleteById(id);
        groupReceiverManager.deleteByGroupId(id);
    }

    /**
     * 绑定分账接收方
     */
    @Transactional(rollbackFor = Exception.class)
    public void bindReceivers(AllocGroupBindParam param) {
        // 分账组
        AllocGroup group = groupManager.findById(param.getGroupId()).orElseThrow(() -> new DataNotExistException("未找到分账组"));
        // 查询接收方
        List<AllocGroupReceiverParam> receiverParams = param.getReceivers();
        List<Long> receiverIds =  receiverParams.stream()
                .map(AllocGroupReceiverParam::getReceiverId)
                .collect(Collectors.toList());
        List<AllocReceiver> receivers = receiverManager.findAllByIds(receiverIds);
        if (receivers.size() != receiverIds.size()){
            throw new DataNotExistException("传入的分账接收房数量与查询到的不一致");
        }
        // 接收方只能为一个支付通道
        long count = receivers.stream()
                .map(AllocReceiver::getChannel)
                .distinct()
                .count();
        if (count > 1){
            throw new BizException("接收方只能为一个支付通道");
        }
        // 检查接收方和传入的通道是否是不一致
        if (!Objects.equals(group.getChannel(), receivers.getFirst().getChannel())){
            throw new BizException("接收方和传入的通道不一致");
        }

        // 保存分账接收者
        List<AllocGroupReceiver> groupReceivers = receivers.stream()
                .map(receiver -> {
                    var groupReceiver = new AllocGroupReceiver().setGroupId(group.getId())
                            .setReceiverId(receiver.getId())
                            .setRate(receiverParams.get(receivers.indexOf(receiver))
                                    .getRate());
                    groupReceiver.setAppId(group.getAppId());
                    return groupReceiver;
                })
                .collect(Collectors.toList());
        groupReceiverManager.saveAll(groupReceivers);
        // 计算分账比例
        var sum = receiverParams.stream()
                .map(AllocGroupReceiverParam::getRate)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        group.setTotalRate(group.getTotalRate().add(sum));
        groupManager.updateById(group);
    }

    /**
     * 批量删除分账接收方
     */
    @Transactional(rollbackFor = Exception.class)
    public void unbindReceivers(AllocGroupUnbindParam param){
        // 分账组
        AllocGroup group = groupManager.findById(param.getGroupId())
                .orElseThrow(() -> new DataNotExistException("未找到分账组"));
        // 删除接收方
        List<AllocGroupReceiver> receivers = groupReceiverManager.findAllByIds(param.getReceiverIds());
        if (receivers.size() != param.getReceiverIds().size()){
            throw new DataNotExistException("传入的分账接收房数量与查询到的不一致");
        }
        groupReceiverManager.deleteByIds(param.getReceiverIds());
        // 计算分账比例
        var sum = receivers.stream()
                .map(AllocGroupReceiver::getRate)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        group.setTotalRate(group.getTotalRate().subtract(sum));
        groupManager.updateById(group);
    }

    /**
     * 删除单个分账接收方
     */
    @Transactional
    public void unbindReceiver(Long receiverId){
        AllocGroupReceiver groupReceiver = groupReceiverManager.findById(receiverId)
                .orElseThrow(() -> new DataNotExistException("未找到分账接收方"));
        AllocGroup group = groupManager.findById(groupReceiver.getGroupId())
                .orElseThrow(() -> new DataNotExistException("未找到分账组"));
        // 更新分账比例
        group.setTotalRate(group.getTotalRate().subtract(groupReceiver.getRate()));
        // 更新接收比例
        groupReceiverManager.deleteById(receiverId);
        groupManager.updateById(group);
    }

    /**
     * 修改分账比例
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateRate(Long receiverId, BigDecimal rate){
        AllocGroupReceiver groupReceiver = groupReceiverManager.findById(receiverId)
                .orElseThrow(() -> new DataNotExistException("未找到分账接收方"));
        AllocGroup group = groupManager.findById(groupReceiver.getGroupId())
                .orElseThrow(() -> new DataNotExistException("未找到分账组"));
        // 更新分账比例
        group.setTotalRate(group.getTotalRate().subtract(groupReceiver.getRate()).add(rate));
        // 更新接收比例
        groupReceiver.setRate(rate);
        groupReceiverManager.updateById(groupReceiver);
        groupManager.updateById(group);
    }

    /**
     * 判断分账组编号是否存在
     */
    public boolean existsByGroupNo(String groupNo, String appId) {
        return groupManager.existedByGroupNo(groupNo, appId);
    }
}
