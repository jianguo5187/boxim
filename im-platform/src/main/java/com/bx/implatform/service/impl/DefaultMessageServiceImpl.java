package com.bx.implatform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bx.implatform.dto.DefaultMessageDTO;
import com.bx.implatform.entity.DefaultMessage;
import com.bx.implatform.mapper.DefaultMessageMapper;
import com.bx.implatform.service.IDefaultMessageService;
import com.bx.implatform.util.BeanUtils;
import com.bx.implatform.vo.DefaultMessageVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultMessageServiceImpl extends ServiceImpl<DefaultMessageMapper, DefaultMessage> implements IDefaultMessageService {

    @Override
    public List<DefaultMessageVO> findWelcomeMessage() {
        LambdaQueryWrapper<DefaultMessage> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(DefaultMessage::getStatus,0)
                .eq(DefaultMessage::getWelcomeShowFlag,1)
                .orderByAsc(DefaultMessage::getId);
        List<DefaultMessage> defaultMessages = this.list(queryWrapper);
        List<DefaultMessageVO> defaultMessageInfos = defaultMessages.stream().map(m -> BeanUtils.copyProperties(m, DefaultMessageVO.class)).collect(Collectors.toList());

        return defaultMessageInfos;
    }

    @Override
    public List<DefaultMessageVO> findAllActivedDefaultMessage() {
        LambdaQueryWrapper<DefaultMessage> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(DefaultMessage::getStatus,0).orderByAsc(DefaultMessage::getId);
        List<DefaultMessage> defaultMessages = this.list(queryWrapper);
        List<DefaultMessageVO> defaultMessageInfos = defaultMessages.stream().map(m -> BeanUtils.copyProperties(m, DefaultMessageVO.class)).collect(Collectors.toList());

        return defaultMessageInfos;
    }

    @Override
    public List<DefaultMessageVO> findAutoMessage(String sendMessage) {
        LambdaQueryWrapper<DefaultMessage> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(DefaultMessage::getStatus,0)
                .eq(DefaultMessage::getContent,sendMessage)
                .orderByAsc(DefaultMessage::getId);
        List<DefaultMessage> defaultMessages = this.list(queryWrapper);
        List<DefaultMessageVO> defaultMessageInfos = defaultMessages.stream().map(m -> BeanUtils.copyProperties(m, DefaultMessageVO.class)).collect(Collectors.toList());

        return defaultMessageInfos;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveOrUpdateDefaultMessage(DefaultMessageDTO dto) {
//        DefaultMessage defaultMessage = BeanUtils.copyProperties(dto,DefaultMessage.class);
        if(dto.getId().compareTo(0l) > 0){
            DefaultMessage defaultMessage = this.getById(dto.getId());
            defaultMessage.setContent(dto.getContent());
            defaultMessage.setAnswerImgContent(dto.getAnswerImgContent());
            defaultMessage.setAnswerContent(dto.getAnswerContent());
            defaultMessage.setWelcomeShowFlag(dto.getWelcomeShowFlag());
            this.updateById(defaultMessage);
        }else {
            DefaultMessage defaultMessage = new DefaultMessage();
            defaultMessage.setContent(dto.getContent());
            defaultMessage.setAnswerImgContent(dto.getAnswerImgContent());
            defaultMessage.setAnswerContent(dto.getAnswerContent());
            defaultMessage.setWelcomeShowFlag(dto.getWelcomeShowFlag());
            defaultMessage.setStatus(0);
            defaultMessage.setCreatedTime(new Date());
            this.save(defaultMessage);
        }
    }

    @Override
    public void deleteDefaultMessage(Long id) {
        this.removeById(id);
    }
}
