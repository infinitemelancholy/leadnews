package com.leadnews.wemedia.test;

import com.alibaba.fastjson.JSON;
import com.leadnews.file.service.FileStorageService;
import com.leadnews.model.wemedia.dtos.WmNewsDto;
import com.leadnews.model.wemedia.pojos.WmMaterial;
import com.leadnews.model.wemedia.pojos.WmUser;
import com.leadnews.utils.thread.WmThreadLocalUtil;
import com.leadnews.wemedia.WemediaApplication;
import com.leadnews.wemedia.mapper.WmMaterialMapper;
import com.leadnews.wemedia.service.WmNewsService;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
/**
 * 鐖彇鏂伴椈鏁版嵁  鐢ㄤ簬app绔紨绀?
 * @浣滆€?itcast
 * @鍒涘缓鏃ユ湡 2021/4/19 21:41
 **/
@SpringBootTest(classes = WemediaApplication.class)
@RunWith(SpringRunner.class)
public class ReptilesArticleData {
    @Autowired
    WmNewsService wmNewsService;
    @Autowired
    WmMaterialMapper wmMaterialMapper;
    @Autowired
    FileStorageService fileStorageService;

    @Test
    public void reptilesData() throws IOException {
        // 妯℃嫙褰撳墠鑷獟浣撶櫥褰曠敤鎴?
        WmUser wmUser = new WmUser();
        wmUser.setId(4);
        WmThreadLocalUtil.setUser(wmUser);

        // 鑾峰彇缃戞槗鏂伴椈鏁版嵁
//        String url = "https://3g.163.com/touch/news/sub/history/?ver=c&clickfrom=index2018_header_main";
//        String url = "https://3g.163.com/touch/news/?ver=c&clickfrom=index2018_header_main";
        String url = "https://3g.163.com/touch/ent/sub/television/?ver=c&clickfrom=index2018_header_main";

        // 鑾峰彇璇ョ綉椤礵ocument鏂囨。鏁版嵁
        Document document = Jsoup.connect(url)
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.183 Safari/537.36")
                .get();

        // 鎵惧埌鎸囧畾class鐨勬墍鏈夊厓绱?
        Elements elementsByClass = document.getElementsByClass("tab-content");
        System.out.println("div鍏冪礌涓暟:  " + elementsByClass.size());
        Element element = elementsByClass.get(0);

        // 鎵惧埌鎵€鏈塧rticle鏍囩椤甸潰鍏冪礌
        Elements articleList = element.getElementsByTag("article");
        for (Element article : articleList) {
            Element aElement = article.getElementsByTag("a").get(0);
            String href = aElement.attr("href");
            System.out.println("鏂伴椈鐨剈rl璺緞: "+href);
            Element titleEle = aElement.getElementsByClass("title").get(0);
            String title = titleEle.text();
            System.out.println("鏂伴椈鐨勬枃绔犳爣棰?"+title);
            Element newsPic = aElement.getElementsByClass("news-pic").get(0);
            // 鑾峰彇灏侀潰鍥剧墖鍏冪礌闆嗗悎
            Elements imgList = newsPic.getElementsByTag("img");
            // 灏佽WmNewsDto瀵硅薄
            WmNewsDto wmNewsDto = new WmNewsDto();
            // 瑙ｆ瀽鍗曚釜鏂囩珷璇︽儏
            List<Map> contentMap = parseContent(href);
            wmNewsDto.setContent(JSON.toJSONString(contentMap));
            // 灏侀潰鍥剧墖闆嗗悎
            List<String> urlList = new ArrayList<>();
            for (Element imgEle : imgList) {
                String src = imgEle.attr("src");
                System.out.println("灏侀潰鍥剧墖url==>"+src);
                String fileName = uploadPic(src);
                if(StringUtils.isNotBlank(fileName)){
                    // 濡傛灉涓婁紶鍥剧墖璺緞涓嶄负绌? 鍒涘缓绱犳潗淇℃伅
                    WmMaterial wmMaterial = new WmMaterial();
                    wmMaterial.setUserId(WmThreadLocalUtil.getUser().getId());
                    wmMaterial.setUrl(fileName);
                    wmMaterial.setType((short)0);
                    wmMaterial.setIsCollection((short)0);
                    wmMaterial.setCreatedTime(new Date());
                    wmMaterialMapper.insert(wmMaterial);
                    urlList.add(fileName);
                }
            }
            wmNewsDto.setTitle(title);
            wmNewsDto.setType((short) urlList.size());
            if(urlList.size()>0){
                wmNewsDto.setImages(urlList);
            }
            wmNewsDto.setChannelId(6);
            try {
                Thread.sleep(1000); // 鐫＄湢1绉?璁╁彂甯冩椂闂翠笉涓€鑷?
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            wmNewsDto.setPublishTime(new Date());
            wmNewsDto.setStatus((short)1); // 寰呭鏍哥姸鎬?
            wmNewsDto.setLabels("閲囬泦");
            wmNewsService.submitNews(wmNewsDto);
        }
    }
    public String uploadPic(String imgUrl){
        String url = imgUrl.split("\\?")[0];
        String uuid = UUID.randomUUID().toString().replace("-", "");
        String suffix = url.substring(url.lastIndexOf("."));
        InputStream in = getInputStreamByUrl("http:"+url);
        if(in!=null){
            String fileName = fileStorageService.uploadImgFile("", uuid+suffix, in);
            System.out.println("涓婁紶鏂囦欢鍚嶇О: "+fileName);
            return fileName;
        }
        return null;
    }
    /**
     * 宸ュ叿鏂规硶:  鏍规嵁url璺緞 鑾峰彇杈撳叆娴?
     * @param strUrl
     * @return
     */
    public static InputStream getInputStreamByUrl(String strUrl){
        HttpURLConnection conn = null;
        try {
            URL url = new URL(strUrl);
            conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(20 * 1000);
            final ByteArrayOutputStream output = new ByteArrayOutputStream();
            IOUtils.copy(conn.getInputStream(),output);
            return  new ByteArrayInputStream(output.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try{
                if (conn != null) {
                    conn.disconnect();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 瑙ｆ瀽鏂囩珷璇︽儏鍐呭
     * @param href
     * @return
     */
    private List<Map> parseContent(String href)  {
        String url="http:"+href;
        List<Map> contentMap = new ArrayList<>();
        Document document = null;
        try {
            document = Jsoup.connect(url)
                    .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.183 Safari/537.36")
                    .get();
                Element contentDiv = document.getElementsByClass("content").get(0);
                Element pageDiv = contentDiv.getElementsByTag("div").get(0);
                Elements allElements = pageDiv.getAllElements();

                for (Element subElement : allElements) {
                    String tagName = subElement.tagName();
                    String className = subElement.className();
                    if("p".equalsIgnoreCase(tagName)){
                        Map map = new HashMap();
                        map.put("type","text");
                        map.put("value",subElement.text());
                        contentMap.add(map);
                        System.out.println("鏂囨湰鍐呭: " + subElement.text());
                    }
                    if("div".equalsIgnoreCase(tagName)&&"photo".equalsIgnoreCase(className)){
                        System.out.println("鍥剧墖鍐呭: " + subElement.text());
                    }
                }
        } catch (Exception e) {
            e.printStackTrace();
            Map map = new HashMap();
            map.put("type","text");
            map.put("value","娴嬭瘯鏂囩珷鍐呭");
            contentMap.add(map);
        }
        return contentMap;
    }
}
