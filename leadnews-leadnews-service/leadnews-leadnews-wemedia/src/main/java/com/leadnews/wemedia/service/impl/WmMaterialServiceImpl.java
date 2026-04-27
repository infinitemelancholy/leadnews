package com.leadnews.wemedia.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.leadnews.file.service.FileStorageService;
import com.leadnews.model.common.dtos.PageResponseResult;
import com.leadnews.model.common.dtos.ResponseResult;
import com.leadnews.model.common.enums.AppHttpCodeEnum;
import com.leadnews.model.wemedia.dtos.WmMaterialDto;
import com.leadnews.model.wemedia.pojos.WmMaterial;
import com.leadnews.utils.thread.WmThreadLocalUtil;
import com.leadnews.wemedia.mapper.WmMaterialMapper;
import com.leadnews.wemedia.service.WmMaterialService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.UUID;


@Slf4j
@Service
@Transactional
public class WmMaterialServiceImpl extends ServiceImpl<WmMaterialMapper, WmMaterial> implements WmMaterialService {

    @Autowired
    private FileStorageService fileStorageService;


    /**
     * 鍥剧墖涓婁紶
     * @param multipartFile
     * @return
     */
    @Override
    public ResponseResult uploadPicture(MultipartFile multipartFile) {

        //1.妫€鏌ュ弬鏁?
        if(multipartFile == null || multipartFile.getSize() == 0){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }

        //2.涓婁紶鍥剧墖鍒癿inIO涓?
        String fileName = UUID.randomUUID().toString().replace("-", "");
        //aa.jpg
        String originalFilename = multipartFile.getOriginalFilename();
        String postfix = originalFilename.substring(originalFilename.lastIndexOf("."));
        String fileId = null;
        try {
            fileId = fileStorageService.uploadImgFile("", fileName + postfix, multipartFile.getInputStream());
            log.info("涓婁紶鍥剧墖鍒癕inIO涓紝fileId:{}",fileId);
        } catch (IOException e) {
            e.printStackTrace();
            log.error("WmMaterialServiceImpl-涓婁紶鏂囦欢澶辫触");
        }

        //3.淇濆瓨鍒版暟鎹簱涓?
        WmMaterial wmMaterial = new WmMaterial();
        wmMaterial.setUserId(WmThreadLocalUtil.getUser().getId());
        wmMaterial.setUrl(fileId);
        wmMaterial.setIsCollection((short)0);
        wmMaterial.setType((short)0);
        wmMaterial.setCreatedTime(new Date());
        save(wmMaterial);

        //4.杩斿洖缁撴灉

        return ResponseResult.okResult(wmMaterial);
    }

    /**
     * 绱犳潗鍒楄〃鏌ヨ
     * @param dto
     * @return
     */
    @Override
    public ResponseResult findList(WmMaterialDto dto) {

        //1.妫€鏌ュ弬鏁?
        dto.checkParam();

        //2.鍒嗛〉鏌ヨ
        IPage page = new Page(dto.getPage(),dto.getSize());
        LambdaQueryWrapper<WmMaterial> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        //鏄惁鏀惰棌
        if(dto.getIsCollection() != null && dto.getIsCollection() == 1){
            lambdaQueryWrapper.eq(WmMaterial::getIsCollection,dto.getIsCollection());
        }

        //鎸夌収鐢ㄦ埛鏌ヨ
        lambdaQueryWrapper.eq(WmMaterial::getUserId,WmThreadLocalUtil.getUser().getId());

        //鎸夌収鏃堕棿鍊掑簭
        lambdaQueryWrapper.orderByDesc(WmMaterial::getCreatedTime);


        page = page(page,lambdaQueryWrapper);

        //3.缁撴灉杩斿洖
        ResponseResult responseResult = new PageResponseResult(dto.getPage(),dto.getSize(),(int)page.getTotal());
        responseResult.setData(page.getRecords());
        return responseResult;
    }
}

