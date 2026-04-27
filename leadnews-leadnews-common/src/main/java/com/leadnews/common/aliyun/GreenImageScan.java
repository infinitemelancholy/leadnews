package com.leadnews.common.aliyun;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.green.model.v20180509.ImageSyncScanRequest;
import com.aliyuncs.http.FormatType;
import com.aliyuncs.http.HttpResponse;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.http.ProtocolType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.leadnews.common.aliyun.util.ClientUploader;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.*;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "aliyun")
public class GreenImageScan {

    private String accessKeyId;
    private String secret;
    private String scenes;

    public Map imageScan(List<byte[]> imageList) throws Exception {
        IClientProfile profile = DefaultProfile
            .getProfile("cn-shanghai", accessKeyId, secret);
        DefaultProfile
            .addEndpoint("cn-shanghai", "cn-shanghai", "Green", "green.cn-shanghai.aliyuncs.com");
        IAcsClient client = new DefaultAcsClient(profile);
        ImageSyncScanRequest imageSyncScanRequest = new ImageSyncScanRequest();
        // 鎸囧畾api杩斿洖鏍煎紡
        imageSyncScanRequest.setAcceptFormat(FormatType.JSON);
        // 鎸囧畾璇锋眰鏂规硶
        imageSyncScanRequest.setMethod(MethodType.POST);
        imageSyncScanRequest.setEncoding("utf-8");
        //鏀寔http鍜宧ttps
        imageSyncScanRequest.setProtocol(ProtocolType.HTTP);
        JSONObject httpBody = new JSONObject();
        /**
         * 璁剧疆瑕佹娴嬬殑鍦烘櫙, 璁¤垂鏄寜鐓ц澶勪紶閫掔殑鍦烘櫙杩涜
         * 涓€娆¤姹備腑鍙互鍚屾椂妫€娴嬪寮犲浘鐗囷紝姣忓紶鍥剧墖鍙互鍚屾椂妫€娴嬪涓闄╁満鏅紝璁¤垂鎸夌収鍦烘櫙璁＄畻
         * 渚嬪锛氭娴?寮犲浘鐗囷紝鍦烘櫙浼犻€抪orn銆乼errorism锛岃璐逛細鎸夌収2寮犲浘鐗囬壌榛勶紝2寮犲浘鐗囨毚鎭愭娴嬭绠?
         * porn: porn琛ㄧず鑹叉儏鍦烘櫙妫€娴?
         */

        httpBody.put("scenes", Arrays.asList(scenes.split(",")));

        /**
         * 濡傛灉鎮ㄨ妫€娴嬬殑鏂囦欢瀛樹簬鏈湴鏈嶅姟鍣ㄤ笂锛屽彲浠ラ€氳繃涓嬭堪浠ｇ爜鐗囩敓鎴恥rl
         * 鍐嶅皢杩斿洖鐨剈rl浣滀负鍥剧墖鍦板潃浼犻€掑埌鏈嶅姟绔繘琛屾娴?
         */
        /**
         * 璁剧疆寰呮娴嬪浘鐗囷紝 涓€寮犲浘鐗囦竴涓猼ask
         * 澶氬紶鍥剧墖鍚屾椂妫€娴嬫椂锛屽鐞嗙殑鏃堕棿鐢辨渶鍚庝竴涓鐞嗗畬鐨勫浘鐗囧喅瀹?
         * 閫氬父鎯呭喌涓嬫壒閲忔娴嬬殑骞冲潎rt姣斿崟寮犳娴嬬殑瑕侀暱, 涓€娆℃壒閲忔彁浜ょ殑鍥剧墖鏁拌秺澶氾紝rt琚媺闀跨殑姒傜巼瓒婇珮
         * 杩欓噷浠ュ崟寮犲浘鐗囨娴嬩綔涓虹ず渚? 濡傛灉鏄壒閲忓浘鐗囨娴嬶紝璇疯嚜琛屾瀯寤哄涓猼ask
         */
        ClientUploader clientUploader = ClientUploader.getImageClientUploader(profile, false);
        String url = null;
        List<JSONObject> urlList = new ArrayList<JSONObject>();
        for (byte[] bytes : imageList) {
            url = clientUploader.uploadBytes(bytes);
            JSONObject task = new JSONObject();
            task.put("dataId", UUID.randomUUID().toString());
            //璁剧疆鍥剧墖閾炬帴涓轰笂浼犲悗鐨剈rl
            task.put("url", url);
            task.put("time", new Date());
            urlList.add(task);
        }
        httpBody.put("tasks", urlList);
        imageSyncScanRequest.setHttpContent(org.apache.commons.codec.binary.StringUtils.getBytesUtf8(httpBody.toJSONString()),
            "UTF-8", FormatType.JSON);
        /**
         * 璇疯缃秴鏃舵椂闂? 鏈嶅姟绔叏閾捐矾澶勭悊瓒呮椂鏃堕棿涓?0绉掞紝璇峰仛鐩稿簲璁剧疆
         * 濡傛灉鎮ㄨ缃殑ReadTimeout灏忎簬鏈嶅姟绔鐞嗙殑鏃堕棿锛岀▼搴忎腑浼氳幏寰椾竴涓猺ead timeout寮傚父
         */
        imageSyncScanRequest.setConnectTimeout(3000);
        imageSyncScanRequest.setReadTimeout(10000);
        HttpResponse httpResponse = null;
        try {
            httpResponse = client.doAction(imageSyncScanRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Map<String, String> resultMap = new HashMap<>();

        //鏈嶅姟绔帴鏀跺埌璇锋眰锛屽苟瀹屾垚澶勭悊杩斿洖鐨勭粨鏋?
        if (httpResponse != null && httpResponse.isSuccess()) {
            JSONObject scrResponse = JSON.parseObject(org.apache.commons.codec.binary.StringUtils.newStringUtf8(httpResponse.getHttpContent()));
            System.out.println(JSON.toJSONString(scrResponse, true));
            int requestCode = scrResponse.getIntValue("code");
            //姣忎竴寮犲浘鐗囩殑妫€娴嬬粨鏋?
            JSONArray taskResults = scrResponse.getJSONArray("data");
            if (200 == requestCode) {
                for (Object taskResult : taskResults) {
                    //鍗曞紶鍥剧墖鐨勫鐞嗙粨鏋?
                    int taskCode = ((JSONObject) taskResult).getIntValue("code");
                    //鍥剧墖瑕佹娴嬬殑鍦烘櫙鐨勫鐞嗙粨鏋? 濡傛灉鏄涓満鏅紝鍒欎細鏈夋瘡涓満鏅殑缁撴灉
                    JSONArray sceneResults = ((JSONObject) taskResult).getJSONArray("results");
                    if (200 == taskCode) {
                        for (Object sceneResult : sceneResults) {
                            String scene = ((JSONObject) sceneResult).getString("scene");
                            String label = ((JSONObject) sceneResult).getString("label");
                            String suggestion = ((JSONObject) sceneResult).getString("suggestion");
                            //鏍规嵁scene鍜宻uggetion鍋氱浉鍏冲鐞?
                            //do something
                            System.out.println("scene = [" + scene + "]");
                            System.out.println("suggestion = [" + suggestion + "]");
                            System.out.println("suggestion = [" + label + "]");
                            if (!suggestion.equals("pass")) {
                                resultMap.put("suggestion", suggestion);
                                resultMap.put("label", label);
                                return resultMap;
                            }
                        }

                    } else {
                        //鍗曞紶鍥剧墖澶勭悊澶辫触, 鍘熷洜瑙嗗叿浣撶殑鎯呭喌璇︾粏鍒嗘瀽
                        System.out.println("task process fail. task response:" + JSON.toJSONString(taskResult));
                        return null;
                    }
                }
                resultMap.put("suggestion","pass");
                return resultMap;
            } else {
                /**
                 * 琛ㄦ槑璇锋眰鏁翠綋澶勭悊澶辫触锛屽師鍥犺鍏蜂綋鐨勬儏鍐佃缁嗗垎鏋?
                 */
                System.out.println("the whole image scan request failed. response:" + JSON.toJSONString(scrResponse));
                return null;
            }
        }
        return null;
    }
}
