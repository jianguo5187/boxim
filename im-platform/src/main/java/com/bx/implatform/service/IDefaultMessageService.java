package com.bx.implatform.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bx.implatform.dto.DefaultMessageDTO;
import com.bx.implatform.entity.DefaultMessage;
import com.bx.implatform.vo.DefaultMessageVO;

import java.util.List;

public interface IDefaultMessageService extends IService<DefaultMessage> {
    List<DefaultMessageVO> findWelcomeMessage();

    List<DefaultMessageVO> findAllActivedDefaultMessage();

    List<DefaultMessageVO> findAutoMessage(String sendMessage);

    void saveOrUpdateDefaultMessage(DefaultMessageDTO dto);

    void deleteDefaultMessage(Long id);
}
