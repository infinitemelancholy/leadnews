package com.leadnews.user.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.leadnews.model.common.dtos.ResponseResult;
import com.leadnews.model.common.enums.AppHttpCodeEnum;
import com.leadnews.model.user.dtos.LoginDto;
import com.leadnews.model.user.pojos.ApUser;
import com.leadnews.user.mapper.ApUserMapper;
import com.leadnews.user.service.ApUserService;
import com.leadnews.utils.common.AppJwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.HashMap;
import java.util.Map;


@Service
@Transactional
@Slf4j
public class ApUserServiceImpl extends ServiceImpl<ApUserMapper, ApUser> implements ApUserService {
    /**
     * app绔櫥褰曞姛鑳?
     * @param dto
     * @return
     */
    @Override
    public ResponseResult login(LoginDto dto) {
        //1.姝ｅ父鐧诲綍 鐢ㄦ埛鍚嶅拰瀵嗙爜
        if(StringUtils.isNotBlank(dto.getPhone()) && StringUtils.isNotBlank(dto.getPassword())){
            //1.1 鏍规嵁鎵嬫満鍙锋煡璇㈢敤鎴蜂俊鎭?
            ApUser dbUser = getOne(Wrappers.<ApUser>lambdaQuery().eq(ApUser::getPhone, dto.getPhone()));
            if(dbUser == null){
                return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST,"鐢ㄦ埛淇℃伅涓嶅瓨鍦?);
            }

            //1.2 姣斿瀵嗙爜
            String salt = dbUser.getSalt();
            String password = dto.getPassword();
            String pswd = DigestUtils.md5DigestAsHex((password + salt).getBytes());
            if(!pswd.equals(dbUser.getPassword())){
                return ResponseResult.errorResult(AppHttpCodeEnum.LOGIN_PASSWORD_ERROR);
            }

            //1.3 杩斿洖鏁版嵁  jwt  user
            String token = AppJwtUtil.getToken(dbUser.getId().longValue());
            Map<String,Object> map = new HashMap<>();
            map.put("token",token);
            dbUser.setSalt("");
            dbUser.setPassword("");
            map.put("user",dbUser);

            return ResponseResult.okResult(map);
        }else {
            //2.娓稿鐧诲綍
            Map<String,Object> map = new HashMap<>();
            map.put("token",AppJwtUtil.getToken(0L));
            return ResponseResult.okResult(map);
        }


    }
}

