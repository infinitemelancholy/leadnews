package com.leadnews.article.controller.v1;

import com.leadnews.article.service.ApArticleService;
import com.leadnews.common.constants.ArticleConstants;
import com.leadnews.model.article.dtos.ArticleHomeDto;
import com.leadnews.model.common.dtos.ResponseResult;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/article")
public class ArticleHomeController {

//    @Autowired
    private ApArticleService apArticleService;

    /**
     * 鍔犺浇棣栭〉
     * @param dto
     * @return
     */
    @PostMapping("/load")
    public ResponseResult load(@RequestBody ArticleHomeDto dto){
//        return apArticleService.load(dto, ArticleConstants.LOADTYPE_LOAD_MORE);
        return apArticleService.load2(dto, ArticleConstants.LOADTYPE_LOAD_MORE,true);
    }

    /**
     * 鍔犺浇鏇村
     * @param dto
     * @return
     */
    @PostMapping("/loadmore")
    public ResponseResult loadmore(@RequestBody ArticleHomeDto dto){
        return apArticleService.load(dto, ArticleConstants.LOADTYPE_LOAD_MORE);
    }

    /**
     * 鍔犺浇鏈€鏂?
     * @param dto
     * @return
     */
    @PostMapping("/loadnew")
    public ResponseResult loadnew(@RequestBody ArticleHomeDto dto){
        return apArticleService.load(dto, ArticleConstants.LOADTYPE_LOAD_NEW);
    }
}

