package com.leadnews.utils.common;


import java.util.*;

public class SensitiveWordUtil {

    public static Map<String, Object> dictionaryMap = new HashMap<>();


    /**
     * 鐢熸垚鍏抽敭璇嶅瓧鍏稿簱
     * @param words
     * @return
     */
    public static void initMap(Collection<String> words) {
        if (words == null) {
            System.out.println("鏁忔劅璇嶅垪琛ㄤ笉鑳戒负绌?);
            return ;
        }

        // map鍒濆闀垮害words.size()锛屾暣涓瓧鍏稿簱鐨勫叆鍙ｅ瓧鏁?灏忎簬words.size()锛屽洜涓轰笉鍚岀殑璇嶅彲鑳戒細鏈夌浉鍚岀殑棣栧瓧)
        Map<String, Object> map = new HashMap<>(words.size());
        // 閬嶅巻杩囩▼涓綋鍓嶅眰娆＄殑鏁版嵁
        Map<String, Object> curMap = null;
        Iterator<String> iterator = words.iterator();

        while (iterator.hasNext()) {
            String word = iterator.next();
            curMap = map;
            int len = word.length();
            for (int i =0; i < len; i++) {
                // 閬嶅巻姣忎釜璇嶇殑瀛?
                String key = String.valueOf(word.charAt(i));
                // 褰撳墠瀛楀湪褰撳墠灞傛槸鍚﹀瓨鍦? 涓嶅瓨鍦ㄥ垯鏂板缓, 褰撳墠灞傛暟鎹寚鍚戜笅涓€涓妭鐐? 缁х画鍒ゆ柇鏄惁瀛樺湪鏁版嵁
                Map<String, Object> wordMap = (Map<String, Object>) curMap.get(key);
                if (wordMap == null) {
                    // 姣忎釜鑺傜偣瀛樺湪涓や釜鏁版嵁: 涓嬩竴涓妭鐐瑰拰isEnd(鏄惁缁撴潫鏍囧織)
                    wordMap = new HashMap<>(2);
                    wordMap.put("isEnd", "0");
                    curMap.put(key, wordMap);
                }
                curMap = wordMap;
                // 濡傛灉褰撳墠瀛楁槸璇嶇殑鏈€鍚庝竴涓瓧锛屽垯灏唅sEnd鏍囧織缃?
                if (i == len -1) {
                    curMap.put("isEnd", "1");
                }
            }
        }

        dictionaryMap = map;
    }

    /**
     * 鎼滅储鏂囨湰涓煇涓枃瀛楁槸鍚﹀尮閰嶅叧閿瘝
     * @param text
     * @param beginIndex
     * @return
     */
    private static int checkWord(String text, int beginIndex) {
        if (dictionaryMap == null) {
            throw new RuntimeException("瀛楀吀涓嶈兘涓虹┖");
        }
        boolean isEnd = false;
        int wordLength = 0;
        Map<String, Object> curMap = dictionaryMap;
        int len = text.length();
        // 浠庢枃鏈殑绗琤eginIndex寮€濮嬪尮閰?
        for (int i = beginIndex; i < len; i++) {
            String key = String.valueOf(text.charAt(i));
            // 鑾峰彇褰撳墠key鐨勪笅涓€涓妭鐐?
            curMap = (Map<String, Object>) curMap.get(key);
            if (curMap == null) {
                break;
            } else {
                wordLength ++;
                if ("1".equals(curMap.get("isEnd"))) {
                    isEnd = true;
                }
            }
        }
        if (!isEnd) {
            wordLength = 0;
        }
        return wordLength;
    }

    /**
     * 鑾峰彇鍖归厤鐨勫叧閿瘝鍜屽懡涓鏁?
     * @param text
     * @return
     */
    public static Map<String, Integer> matchWords(String text) {
        Map<String, Integer> wordMap = new HashMap<>();
        int len = text.length();
        for (int i = 0; i < len; i++) {
            int wordLength = checkWord(text, i);
            if (wordLength > 0) {
                String word = text.substring(i, i + wordLength);
                // 娣诲姞鍏抽敭璇嶅尮閰嶆鏁?
                if (wordMap.containsKey(word)) {
                    wordMap.put(word, wordMap.get(word) + 1);
                } else {
                    wordMap.put(word, 1);
                }

                i += wordLength - 1;
            }
        }
        return wordMap;
    }

    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        list.add("娉曡疆");
        list.add("娉曡疆鍔?);
        list.add("鍐版瘨");
        initMap(list);
        String content="鎴戞槸涓€涓ソ浜猴紝骞朵笉浼氬崠鍐版瘨锛屼篃涓嶆搷缁冩硶杞姛,鎴戠湡鐨勪笉鍗栧啺姣?;
        Map<String, Integer> map = matchWords(content);
        System.out.println(map);
    }
}

