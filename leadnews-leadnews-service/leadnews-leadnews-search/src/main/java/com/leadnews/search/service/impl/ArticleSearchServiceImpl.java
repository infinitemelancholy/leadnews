package com.leadnews.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.leadnews.model.common.dtos.ResponseResult;
import com.leadnews.model.common.enums.AppHttpCodeEnum;
import com.leadnews.model.search.dtos.UserSearchDto;
import com.leadnews.model.user.pojos.ApUser;
import com.leadnews.search.service.ApUserSearchService;
import com.leadnews.search.service.ArticleSearchService;
import com.leadnews.utils.thread.AppThreadLocalUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class ArticleSearchServiceImpl implements ArticleSearchService {

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Autowired
    private ApUserSearchService apUserSearchService;

    /**
     * es鏂囩珷鍒嗛〉妫€绱?
     *
     * @param dto
     * @return
     */
    @Override
    public ResponseResult search(UserSearchDto dto) throws IOException {

        //1.妫€鏌ュ弬鏁?
        if(dto == null || StringUtils.isBlank(dto.getSearchWords())){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }

        ApUser user = AppThreadLocalUtil.getUser();

        //寮傛璋冪敤 淇濆瓨鎼滅储璁板綍
        if(user != null && dto.getFromIndex() == 0){
            apUserSearchService.insert(dto.getSearchWords(), user.getId());
        }


        //2.璁剧疆鏌ヨ鏉′欢
        SearchRequest searchRequest = new SearchRequest("app_info_article");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        //甯冨皵鏌ヨ
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        //鍏抽敭瀛楃殑鍒嗚瘝涔嬪悗鏌ヨ
        QueryStringQueryBuilder queryStringQueryBuilder = QueryBuilders.queryStringQuery(dto.getSearchWords()).field("title").field("content").defaultOperator(Operator.OR);
        boolQueryBuilder.must(queryStringQueryBuilder);

        //鏌ヨ灏忎簬mindate鐨勬暟鎹?
        RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("publishTime").lt(dto.getMinBehotTime().getTime());
        boolQueryBuilder.filter(rangeQueryBuilder);

        //鍒嗛〉鏌ヨ
        searchSourceBuilder.from(0);
        searchSourceBuilder.size(dto.getPageSize());

        //鎸夌収鍙戝竷鏃堕棿鍊掑簭鏌ヨ
        searchSourceBuilder.sort("publishTime", SortOrder.DESC);

        //璁剧疆楂樹寒  title
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.field("title");
        highlightBuilder.preTags("<font style='color: red; font-size: inherit;'>");
        highlightBuilder.postTags("</font>");
        searchSourceBuilder.highlighter(highlightBuilder);


        searchSourceBuilder.query(boolQueryBuilder);
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);


        //3.缁撴灉灏佽杩斿洖

        List<Map> list = new ArrayList<>();

        SearchHit[] hits = searchResponse.getHits().getHits();
        for (SearchHit hit : hits) {
            String json = hit.getSourceAsString();
            Map map = JSON.parseObject(json, Map.class);
            //澶勭悊楂樹寒
            if(hit.getHighlightFields() != null && hit.getHighlightFields().size() > 0){
                Text[] titles = hit.getHighlightFields().get("title").getFragments();
                String title = StringUtils.join(titles);
                //楂樹寒鏍囬
                map.put("h_title",title);
            }else {
                //鍘熷鏍囬
                map.put("h_title",map.get("title"));
            }
            list.add(map);
        }

        return ResponseResult.okResult(list);

    }
}

