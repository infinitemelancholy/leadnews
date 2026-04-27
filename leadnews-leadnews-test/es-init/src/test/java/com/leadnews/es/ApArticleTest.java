package com.leadnews.es;

import com.alibaba.fastjson.JSON;
import com.leadnews.es.mapper.ApArticleMapper;
import com.leadnews.es.pojo.SearchArticleVo;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;


@SpringBootTest
@RunWith(SpringRunner.class)
public class ApArticleTest {

    @Autowired
    private ApArticleMapper apArticleMapper;

    @Autowired
    private RestHighLevelClient restHighLevelClient;


    /**
     * 娉ㄦ剰锛氭暟鎹噺鐨勫鍏ワ紝濡傛灉鏁版嵁閲忚繃澶э紝闇€瑕佸垎椤靛鍏?
     * @throws Exception
     */
    @Test
    public void init() throws Exception {

        //1.鏌ヨ鎵€鏈夌鍚堟潯浠剁殑鏂囩珷鏁版嵁
        List<SearchArticleVo> searchArticleVos = apArticleMapper.loadArticleList();

        //2.鎵归噺瀵煎叆鍒癳s绱㈠紩搴?

        BulkRequest bulkRequest = new BulkRequest("app_info_article");

        for (SearchArticleVo searchArticleVo : searchArticleVos) {

            IndexRequest indexRequest = new IndexRequest().id(searchArticleVo.getId().toString())
                    .source(JSON.toJSONString(searchArticleVo), XContentType.JSON);

            //鎵归噺娣诲姞鏁版嵁
            bulkRequest.add(indexRequest);

        }
        restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);

    }

}
