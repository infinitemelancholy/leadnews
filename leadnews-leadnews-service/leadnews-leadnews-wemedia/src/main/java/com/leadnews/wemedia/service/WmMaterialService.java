package com.leadnews.wemedia.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.leadnews.model.common.dtos.ResponseResult;
import com.leadnews.model.wemedia.dtos.WmMaterialDto;
import com.leadnews.model.wemedia.pojos.WmMaterial;
import org.springframework.web.multipart.MultipartFile;

public interface WmMaterialService extends IService<WmMaterial> {

    /**
     * 鍥剧墖涓婁紶
     * @param multipartFile
     * @return
     */
    public ResponseResult uploadPicture(MultipartFile multipartFile);

    /**
     * 绱犳潗鍒楄〃鏌ヨ
     * @param dto
     * @return
     */
    public ResponseResult findList( WmMaterialDto dto);


}
