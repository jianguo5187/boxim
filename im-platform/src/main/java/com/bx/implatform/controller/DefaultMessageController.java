package com.bx.implatform.controller;

import com.bx.implatform.dto.DefaultMessageDTO;
import com.bx.implatform.result.Result;
import com.bx.implatform.result.ResultUtils;
import com.bx.implatform.service.IDefaultMessageService;
import com.bx.implatform.vo.DefaultMessageVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Api(tags = "设置自动回复内容")
@RestController
@RequestMapping("/defaultMessage")
@RequiredArgsConstructor
public class DefaultMessageController {
    private final IDefaultMessageService defaultMessageService;


    @PostMapping("/saveDefaultMessage")
    @ApiOperation(value = "保存自动回复设置信息")
    public Result<Long>  saveDefaultMessage(@Valid @RequestBody DefaultMessageDTO dto){
        defaultMessageService.saveOrUpdateDefaultMessage(dto);
        return ResultUtils.success();
    }

    @DeleteMapping("/deleteDefaultMessage/{id}")
    @ApiOperation(value = "删除自动回复信息")
    public Result deleteDefaultMessage(@NotEmpty(message = "信息id不可为空") @PathVariable("id") Long id) {
        defaultMessageService.deleteDefaultMessage(id);
        return ResultUtils.success();
    }

    @GetMapping("/loadAllDefaultMessage")
    @ApiOperation(value = "拉取自动回复设置消息")
    public Result<List<DefaultMessageVO>>  loadAllDefaultMessage(){
        return ResultUtils.success(defaultMessageService.findAllActivedDefaultMessage());
    }
}
