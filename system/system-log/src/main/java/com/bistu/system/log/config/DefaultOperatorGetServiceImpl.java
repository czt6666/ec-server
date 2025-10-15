package com.bistu.system.log.config;

import com.mzt.logapi.beans.Operator;
import com.mzt.logapi.service.IOperatorGetService;
import com.bistu.common.dto.session.SessionUserInfo;
import com.bistu.common.util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DefaultOperatorGetServiceImpl implements IOperatorGetService {
    @Autowired
    private TokenUtil tokenService;

    @Override
    public Operator getUser() {
        //UserUtils 是获取用户上下文的方法
        /*return Optional.ofNullable(UserUtils.getUser())
                .map(a -> new Operator(a.getName(), a.getLogin()))
                .orElseThrow(()->new IllegalArgumentException("user is null"));*/

        Operator operator = new Operator();
        try{
            SessionUserInfo userInfo = tokenService.getUserInfo();
            operator.setOperatorId(userInfo.getNickname());
        }catch (Exception e){
            operator.setOperatorId("未登录");
        }
        return operator;

    }
}