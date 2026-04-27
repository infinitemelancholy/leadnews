package com.leadnews.model.wemedia.dtos;

import com.leadnews.model.common.dtos.PageRequestDto;
import lombok.Data;

@Data
public class WmMaterialDto extends PageRequestDto {

    /**
     * 1 鏀惰棌
     * 0 鏈敹钘?
     */
    private Short isCollection;
}

