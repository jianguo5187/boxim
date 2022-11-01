package com.lx.implatform.controller;


import com.lx.common.result.Result;
import com.lx.common.result.ResultUtils;
import com.lx.common.util.BeanUtils;
import com.lx.implatform.entity.Friend;
import com.lx.implatform.service.IFriendService;
import com.lx.implatform.session.SessionContext;
import com.lx.implatform.vo.FriendVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.stream.Collectors;

@Api(tags = "好友")
@RestController
@RequestMapping("/friend")
public class FriendController {

    @Autowired
    private IFriendService friendService;

    @GetMapping("/list")
    @ApiOperation(value = "好友列表",notes="获取好友列表")
    public Result< List<FriendVO>> findFriends(){
        List<Friend> friends = friendService.findFriendByUserId(SessionContext.getSession().getId());
        List<FriendVO> vos = friends.stream().map(f->{
            FriendVO vo = new FriendVO();
            vo.setId(f.getFriendId());
            vo.setHeadImage(f.getFriendHeadImage());
            vo.setNickName(f.getFriendNickName());
            return vo;
        }).collect(Collectors.toList());
        return ResultUtils.success(vos);
    }



    @PostMapping("/add")
    @ApiOperation(value = "添加好友",notes="双方建立好友关系")
    public Result addFriend(@NotEmpty(message = "好友id不可为空") @RequestParam("friendId") Long friendId){
         friendService.addFriend(friendId);
         return ResultUtils.success();
    }

    @DeleteMapping("/delete")
    @ApiOperation(value = "删除好友",notes="解除好友关系")
    public Result delFriend(@NotEmpty(message = "好友id不可为空") @RequestParam("friendId") Long friendId){
        friendService.delFriend(friendId);
        return ResultUtils.success();
    }

    @PutMapping("/update")
    @ApiOperation(value = "更新好友信息",notes="更新好友头像或昵称")
    public Result modifyFriend(@Valid @RequestBody FriendVO vo){
        friendService.update(vo);
        return ResultUtils.success();
    }


}
