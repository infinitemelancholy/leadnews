package com.leadnews.wemedia.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.leadnews.model.common.dtos.ResponseResult;
import com.leadnews.model.common.enums.AppHttpCodeEnum;
import com.leadnews.model.wemedia.dtos.WmLoginDto;
import com.leadnews.model.wemedia.pojos.WmUser;
import com.leadnews.utils.common.AppJwtUtil;
import com.leadnews.wemedia.mapper.WmUserMapper;
import com.leadnews.wemedia.service.WmUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.HashMap;
import java.util.Map;

@Service
public class WmUserServiceImpl extends ServiceImpl<WmUserMapper, WmUser> implements WmUserService {

    @Override
    public ResponseResult login(WmLoginDto dto) {
        //1.妫€鏌ュ弬鏁?
        if(StringUtils.isBlank(dto.getName()) || StringUtils.isBlank(dto.getPassword())){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID,"鐢ㄦ埛鍚嶆垨瀵嗙爜涓虹┖");
        }

        //2.鏌ヨ鐢ㄦ埛
        WmUser wmUser = getOne(Wrappers.<WmUser>lambdaQuery().eq(WmUser::getName, dto.getName()));
        if(wmUser == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST);
        }

        //3.姣斿瀵嗙爜
        String salt = wmUser.getSalt();
        String pswd = dto.getPassword();
        pswd = DigestUtils.md5DigestAsHex((pswd + salt).getBytes());
        if(pswd.equals(wmUser.getPassword())){
            //4.杩斿洖鏁版嵁  jwt
            Map<String,Object> map  = new HashMap<>();
            map.put("token", AppJwtUtil.getToken(wmUser.getId().longValue()));
            wmUser.setSalt("");
            wmUser.setPassword("");
            map.put("user",wmUser);
            return ResponseResult.okResult(map);

        }else {
            return ResponseResult.errorResult(AppHttpCodeEnum.LOGIN_PASSWORD_ERROR);
        }
    }
}
