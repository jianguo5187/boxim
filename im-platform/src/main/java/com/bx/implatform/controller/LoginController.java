package com.bx.implatform.controller;

import com.bx.implatform.dto.*;
import com.bx.implatform.result.Result;
import com.bx.implatform.result.ResultUtils;
import com.bx.implatform.service.IPrivateMessageService;
import com.bx.implatform.service.IUserService;
import com.bx.implatform.vo.LoginVO;
import com.bx.implatform.vo.ThirdLoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(tags = "用户登录和注册")
@RestController
@RequiredArgsConstructor
public class LoginController {

    private final IUserService userService;

    private final IPrivateMessageService privateMessageService;

    @PostMapping("/login")
    @ApiOperation(value = "用户注册", notes = "用户注册")
    public Result register(@Valid @RequestBody LoginDTO dto) {
        LoginVO vo = userService.login(dto);
        return ResultUtils.success(vo);
    }

    @PostMapping("/thirdLogin")
    @ApiOperation(value = "第三方用户登录", notes = "第三方用户登录")
    public Result thirdLogin(@Valid @RequestBody ThirdLoginDTO dto) {
        ThirdLoginVO vo = userService.thirdLogin(dto);
        return ResultUtils.success(vo);
    }

    @PostMapping("/kefuAutoThirdLogin")
    @ApiOperation(value = "客服自动登录", notes = "客服自动登录")
    public Result kefuAutoThirdLogin(@RequestBody LoginDTO dto) {
        LoginVO vo = userService.kefuAutoThirdLogin(dto);
        return ResultUtils.success(vo);
    }

    @PutMapping("/refreshToken")
    @ApiOperation(value = "刷新token", notes = "用refreshtoken换取新的token")
    public Result refreshToken(@RequestHeader("refreshToken") String refreshToken) {
        LoginVO vo = userService.refreshToken(refreshToken);
        return ResultUtils.success(vo);
    }


    @PostMapping("/register")
    @ApiOperation(value = "用户注册", notes = "用户注册")
    public Result register(@Valid @RequestBody RegisterDTO dto) {
        userService.register(dto);
        return ResultUtils.success();
    }

    @PutMapping("/modifyPwd")
    @ApiOperation(value = "修改密码", notes = "修改用户密码")
    public Result update(@Valid @RequestBody ModifyPwdDTO dto) {
        userService.modifyPassword(dto);
        return ResultUtils.success();
    }

    @PostMapping("/noAuthNoReadCnt")
    @ApiOperation(value = "未读消息件数", notes = "未读消息件数")
    public Result<Integer> noAuthNoReadCnt(@Valid @RequestBody NoAuthNoReadCntDto vo) {
        return ResultUtils.success(privateMessageService.noAuthNoReadCnt(vo));
    }

    @PostMapping("/noAuthKefuNoReadCnt")
    @ApiOperation(value = "客服未读消息件数", notes = "客服未读消息件数")
    public Result<Integer> noAuthKefuNoReadCnt(@Valid @RequestBody NoAuthNoReadCntDto vo) {
        return ResultUtils.success(privateMessageService.noAuthKefuNoReadCnt(vo));
    }

    @PostMapping("/appRegister")
    @ApiOperation(value = "app用户注册", notes = "app用户注册")
    public Result appRegister(@Valid @RequestBody AppRegisterDTO dto) {
        userService.appRegister(dto);
        return ResultUtils.success();
    }
}
