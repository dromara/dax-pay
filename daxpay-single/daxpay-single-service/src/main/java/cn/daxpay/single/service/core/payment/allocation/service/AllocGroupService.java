package cn.daxpay.single.service.core.payment.allocation.service;

import cn.bootx.platform.common.core.exception.BizException;
import cn.bootx.platform.common.core.exception.DataNotExistException;
import cn.bootx.platform.common.core.function.CollectorsFunction;
import cn.bootx.platform.common.core.rest.PageResult;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.daxpay.single.service.core.payment.allocation.convert.AllocGroupConvert;
import cn.daxpay.single.service.core.payment.allocation.dao.AllocGroupManager;
import cn.daxpay.single.service.core.payment.allocation.dao.AllocGroupReceiverManager;
import cn.daxpay.single.service.core.payment.allocation.dao.AllocReceiverManager;
import cn.daxpay.single.service.core.payment.allocation.entity.AllocGroup;
import cn.daxpay.single.service.core.payment.allocation.entity.AllocGroupReceiver;
import cn.daxpay.single.service.core.payment.allocation.entity.AllocReceiver;
import cn.daxpay.single.service.dto.allocation.AllocGroupDto;
import cn.daxpay.single.service.dto.allocation.AllocGroupReceiverResult;
import cn.daxpay.single.service.param.allocation.group.AllocGroupBindParam;
import cn.daxpay.single.service.param.allocation.group.AllocGroupParam;
import cn.daxpay.single.service.param.allocation.group.AllocGroupReceiverParam;
import cn.daxpay.single.service.param.allocation.group.AllocGroupUnbindParam;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public PageResult<AllocGroupDto> page(PageParam pageParam, AllocGroupParam query){
        return MpUtil.convert2DtoPageResult(groupManager.page(pageParam,query));
    }

    /**
     * 查询详情
     */
    public AllocGroupDto findById(Long id){
        return groupManager.findById(id).map(AllocGroup::toDto).orElseThrow(()->new DataNotExistException("分账组不存在"));
    }

    /**
     * 查询分账接收方
     */
    public List<AllocGroupReceiverResult> findReceiversByGroups(Long groupId){
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
                    return new AllocGroupReceiverResult()
                            .setId(o.getId())
                            .setReceiverId(receiver.getId())
                            .setReceiverNo(receiver.getReceiverNo())
                            .setReceiverAccount(receiver.getReceiverAccount())
                            .setReceiverName(receiver.getReceiverName())
                            .setRate(o.getRate())
                            .setReceiverType(receiver.getReceiverType())
                            .setRelationName(receiver.getRelationName())
                            .setRelationType(receiver.getRelationType());
                })
                .collect(Collectors.toList());
    }

    /**
     * 创建分账组
     */
    public void create(AllocGroupParam param){
        AllocGroup group = AllocGroupConvert.CONVERT.convert(param);
        group.setTotalRate(0);
        groupManager.save(group);
    }

    /**
     * 设置默认分账组
     */
    @Transactional(rollbackFor = Exception.class)
    public void setUpDefault(Long id){
        // 分账组
        AllocGroup group = groupManager.findById(id)
                .orElseThrow(() -> new DataNotExistException("未找到分账组"));
        groupManager.clearDefault(group.getChannel());
        group.setDefaultGroup(true);
        groupManager.updateById(group);
    }

    /**
     * 清除默认分账组
     */
    @Transactional(rollbackFor = Exception.class)
    public void clearDefault(Long id){
        AllocGroup group = groupManager.findById(id)
                .orElseThrow(() -> new DataNotExistException("未找到分账组"));
        group.setDefaultGroup(false);
        groupManager.updateById(group);
    }

    /**
     * 更新分账组
     */
    public void update(AllocGroupParam param){
        AllocGroup group = groupManager.findById(param.getId())
                .orElseThrow(() -> new DataNotExistException("未找到分账组"));
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
        AllocGroup group = groupManager.findById(param.getGroupId())
                .orElseThrow(() -> new DataNotExistException("未找到分账组"));
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
        if (!Objects.equals(group.getChannel(), receivers.get(0).getChannel())){
            throw new BizException("接收方和传入的通道不一致");
        }

        // 保存分账接收者
        List<AllocGroupReceiver> groupReceivers = receivers.stream()
                .map(receiver -> new AllocGroupReceiver().setGroupId(group.getId())
                        .setReceiverId(receiver.getId())
                        .setRate(receiverParams.get(receivers.indexOf(receiver))
                                .getRate()))
                .collect(Collectors.toList());
        groupReceiverManager.saveAll(groupReceivers);
        // 计算分账比例
        int sum = receiverParams.stream()
                .mapToInt(AllocGroupReceiverParam::getRate)
                .sum();
        group.setTotalRate(group.getTotalRate() + sum);
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
        int sum = receivers.stream()
                .mapToInt(AllocGroupReceiver::getRate)
                .sum();
        group.setTotalRate(group.getTotalRate() - sum);
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
        group.setTotalRate(group.getTotalRate() - groupReceiver.getRate());
        // 更新接收比例
        groupReceiverManager.deleteById(receiverId);
        groupManager.updateById(group);
    }

    /**
     * 修改分账比例
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateRate(Long receiverId, Integer rate){
        AllocGroupReceiver groupReceiver = groupReceiverManager.findById(receiverId)
                .orElseThrow(() -> new DataNotExistException("未找到分账接收方"));
        AllocGroup group = groupManager.findById(groupReceiver.getGroupId())
                .orElseThrow(() -> new DataNotExistException("未找到分账组"));
        // 更新分账比例
        group.setTotalRate(group.getTotalRate() - groupReceiver.getRate() + rate);
        // 更新接收比例
        groupReceiver.setRate(rate);
        groupReceiverManager.updateById(groupReceiver);
        groupManager.updateById(group);
    }

    /**
     * 判断分账组编号是否存在
     */
    public boolean existsByGroupNo(String groupNo) {
        return groupManager.existedByGroupNo(groupNo);
    }
}
