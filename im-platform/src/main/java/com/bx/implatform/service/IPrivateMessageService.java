package com.bx.implatform.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bx.implatform.dto.NoAuthNoReadCntDto;
import com.bx.implatform.dto.PrivateMessageDTO;
import com.bx.implatform.dto.PrivateReadedMessageDTO;
import com.bx.implatform.entity.PrivateMessage;
import com.bx.implatform.vo.PrivateMessageVO;
import com.bx.implatform.vo.PrivateReadedMessageVO;

import java.util.List;

public interface IPrivateMessageService extends IService<PrivateMessage> {

    /**
     * 发送私聊消息(高并发接口，查询mysql接口都要进行缓存)
     *
     * @param dto 私聊消息
     * @return 消息id
     */
    Long sendMessage(PrivateMessageDTO dto);

    PrivateReadedMessageVO sendReadedMessage(PrivateReadedMessageDTO dto);


    /**
     * 撤回消息
     *
     * @param id 消息id
     */
    void recallMessage(Long id);

    /**
     * 拉取历史聊天记录
     *
     * @param friendId 好友id
     * @param page     页码
     * @param size     页码大小
     * @return 聊天记录列表
     */
    List<PrivateMessageVO> findHistoryMessage(Long friendId, Long page, Long size);


    /**
     * 拉取消息，只能拉取最近1个月的消息，一次拉取100条
     *
     * @param minId 消息起始id
     * @return 聊天消息列表
     */
    List<PrivateMessageVO> loadMessage(Long minId);

    /**
     * 拉取离线消息，只能拉取最近1个月的消息，最多拉取1000条
     *
     * @param minId 消息起始id
     */
    void pullOfflineMessage(Long minId);

    /**
     * 消息已读,将整个会话的消息都置为已读状态
     *
     * @param friendId 好友id
     */
    void readedMessage(Long friendId);

    /**
     *  获取某个会话中已读消息的最大id
     *
     * @param friendId 好友id
     */
    Long getMaxReadedId(Long friendId);

    Integer noAuthNoReadCnt(NoAuthNoReadCntDto vo);

    Integer noAuthKefuNoReadCnt(NoAuthNoReadCntDto vo);

    Integer getNoReadCnt();

    void deleteOneDayBeforeMessage();

    void deleteThreeDayBeforeMessage();
}
