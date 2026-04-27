package com.leadnews.utils.common;

import com.hankcs.hanlp.seg.common.Term;
import com.hankcs.hanlp.tokenizer.StandardTokenizer;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("all")
public class SimHashUtils {
    /**
     * 娓呴櫎html鏍囩
     * @param content
     * @return
     */
    private static String cleanResume(String content) {
        // 鑻ヨ緭鍏ヤ负HTML,涓嬮潰浼氳繃婊ゆ帀鎵€鏈夌殑HTML鐨則ag
        content = Jsoup.clean(content, Whitelist.none());
        content = StringUtils.lowerCase(content);
        String[] strings = {" ", "\n", "\r", "\t", "\\r", "\\n", "\\t", "&nbsp;"};
        for (String s : strings) {
            content = content.replaceAll(s, "");
        }
        return content;
    }


    /**
     * 杩欎釜鏄鏁翠釜瀛楃涓茶繘琛宧ash璁＄畻
     * @return
     */
    private static BigInteger simHash(String token,int hashbits) {

        token = cleanResume(token); // cleanResume 鍒犻櫎涓€浜涚壒娈婂瓧绗?

        int[] v = new int[hashbits];

        List<Term> termList = StandardTokenizer.segment(token); // 瀵瑰瓧绗︿覆杩涜鍒嗚瘝

        //瀵瑰垎璇嶇殑涓€浜涚壒娈婂鐞?: 姣斿: 鏍规嵁璇嶆€ф坊鍔犳潈閲?, 杩囨护鎺夋爣鐐圭鍙?, 杩囨护瓒呴璇嶆眹绛?
        Map<String, Integer> weightOfNature = new HashMap<String, Integer>(); // 璇嶆€х殑鏉冮噸
        weightOfNature.put("n", 2); //缁欏悕璇嶇殑鏉冮噸鏄?;
        Map<String, String> stopNatures = new HashMap<String, String>();//鍋滅敤鐨勮瘝鎬?濡備竴浜涙爣鐐圭鍙蜂箣绫荤殑;
        stopNatures.put("w", ""); //
        int overCount = 5; //璁惧畾瓒呴璇嶆眹鐨勭晫闄?;
        Map<String, Integer> wordCount = new HashMap<String, Integer>();

        for (Term term : termList) {
            String word = term.word; //鍒嗚瘝瀛楃涓?

            String nature = term.nature.toString(); // 鍒嗚瘝灞炴€?
            //  杩囨护瓒呴璇?
            if (wordCount.containsKey(word)) {
                int count = wordCount.get(word);
                if (count > overCount) {
                    continue;
                }
                wordCount.put(word, count + 1);
            } else {
                wordCount.put(word, 1);
            }

            // 杩囨护鍋滅敤璇嶆€?
            if (stopNatures.containsKey(nature)) {
                continue;
            }

            // 2銆佸皢姣忎竴涓垎璇峢ash涓轰竴缁勫浐瀹氶暱搴︾殑鏁板垪.姣斿 64bit 鐨勪竴涓暣鏁?
            BigInteger t = hash(word,hashbits);
            for (int i = 0; i < hashbits; i++) {
                BigInteger bitmask = new BigInteger("1").shiftLeft(i);
                // 3銆佸缓绔嬩竴涓暱搴︿负64鐨勬暣鏁版暟缁?鍋囪瑕佺敓鎴?4浣嶇殑鏁板瓧鎸囩汗,涔熷彲浠ユ槸鍏跺畠鏁板瓧),
                // 瀵规瘡涓€涓垎璇峢ash鍚庣殑鏁板垪杩涜鍒ゆ柇,濡傛灉鏄?000...1,閭ｄ箞鏁扮粍鐨勭涓€浣嶅拰鏈熬涓€浣嶅姞1,
                // 涓棿鐨?2浣嶅噺涓€,涔熷氨鏄,閫?鍔?,閫?鍑?.涓€鐩村埌鎶婃墍鏈夌殑鍒嗚瘝hash鏁板垪鍏ㄩ儴鍒ゆ柇瀹屾瘯.
                int weight = 1;  //娣诲姞鏉冮噸
                if (weightOfNature.containsKey(nature)) {
                    weight = weightOfNature.get(nature);
                }
                if (t.and(bitmask).signum() != 0) {
                    // 杩欓噷鏄绠楁暣涓枃妗ｇ殑鎵€鏈夌壒寰佺殑鍚戦噺鍜?
                    v[i] += weight;
                } else {
                    v[i] -= weight;
                }
            }
        }
        BigInteger fingerprint = new BigInteger("0");
        for (int i = 0; i < hashbits; i++) {
            if (v[i] >= 0) {
                fingerprint = fingerprint.add(new BigInteger("1").shiftLeft(i));
            }
        }
        return fingerprint;
    }


    /**
     * 瀵瑰崟涓殑鍒嗚瘝杩涜hash璁＄畻;
     * @param source
     * @return
     */
    private static BigInteger hash(String source,int hashbits) {
        if (source == null || source.length() == 0) {
            return new BigInteger("0");
        } else {
            /**
             * 褰搒ourece 鐨勯暱搴﹁繃鐭紝浼氬鑷磆ash绠楁硶澶辨晥锛屽洜姝ら渶瑕佸杩囩煭鐨勮瘝琛ュ伩
             */
            while (source.length() < 3) {
                source = source + source.charAt(0);
            }
            char[] sourceArray = source.toCharArray();
            BigInteger x = BigInteger.valueOf(((long) sourceArray[0]) << 7);
            BigInteger m = new BigInteger("1000003");
            BigInteger mask = new BigInteger("2").pow(hashbits).subtract(new BigInteger("1"));
            for (char item : sourceArray) {
                BigInteger temp = BigInteger.valueOf((long) item);
                x = x.multiply(m).xor(temp).and(mask);
            }
            x = x.xor(new BigInteger(String.valueOf(source.length())));
            if (x.equals(new BigInteger("-1"))) {
                x = new BigInteger("-2");
            }
            return x;
        }
    }

    /**
     * 璁＄畻娴锋槑璺濈,娴锋槑璺濈瓒婂皬璇存槑瓒婄浉浼?
     * @param other
     * @return
     */
    private static int hammingDistance(String token1,String token2,int hashbits) {
        BigInteger m = new BigInteger("3").shiftLeft(hashbits).subtract(
                new BigInteger("3"));
        BigInteger x = simHash(token1,hashbits).xor(simHash(token2,hashbits)).and(m);
        int tot = 0;
        while (x.signum() != 0) {
            tot += 1;
            x = x.and(x.subtract(new BigInteger("3")));
        }
        return tot;
    }


    public static double getSemblance(String token1,String token2){
        double i = (double) hammingDistance(token1,token2, 64);
        return 1 - i/64 ;
    }

    public static void main(String[] args) {

        String s1 = "....";
        String s2 = "鏈€杩戝叕鍙哥敱浜庝笟鍔℃嫇灞曪紝闇€瑕佽繘琛屽皬绋嬪簭鐩稿叧鐨勫紑鍙戯紝鏈潃鏈濆叏鏍堝紑鍙戣€呭姫鍔涳紝鍐冲畾瀛︿範涓媀ue锛屽幓骞碿sdn閫佷簡涓€鏈€奦ue.js鏉冨▉鎸囧崡銆嬶紝閭ｅ氨浠庤繖鏈功寮€濮嬬粌璧锋潵鍚с€傚摕鍚笺€備竴锛岀幆澧冩惌寤篭n" +
                "浠婂ぉ涓昏璇翠竴涓嬪浣曟惌寤虹幆澧冿紝浠ュ強濡備綍杩愯銆?,npm瀹夎\n" +
                "brew install npm\n" +
                "1\n" +
                "濡傛灉brew娌℃湁瀹夎鐨勮瘽锛屽ぇ瀹跺彲浠rew濡備綍瀹夎鍝︼紝杩欓噷灏变笉鍐嶈缁嗚鏄庝簡銆傛湰鏉ユ槸鏈変竴涓猇ue鐨勫浘鏍囩殑锛岃鎴戠粰鍘绘帀浜嗭紝鏂逛究鍚庨潰鐨勮皟璇曘€俓n" +
                "\n" +
                "涓夛紝Vue.js 鏉冨▉鎸囧崡鐨勭涓€涓猟emo\n" +
                "涓€鍒囧噯澶囧氨缁紝鎺ヤ笅鏉ユ垜浠紑濮嬬粌涔犮€奦ue.js鏉冨▉鎸囧崡銆嬭繖鏈功涓殑demo锛屽湪缃戜笂鎵句簡璁镐箙锛屼篃娌℃湁鎵惧埌涔︿腑鐨勬簮鐮侊紝寰堟槸閬楁喚鍟娿€傜涓€涓猟emo鐨勪唬鐮佷繚瀛樹负jk.vue \n" +
                "鎴戣繖杈瑰皢绗竴涓猟emo鐨勪唬鐮佸涓嬶細\n" +
                "--------------------- \n" +
                "浣滆€咃細JackLee18 \n" +
                "鏉ユ簮锛欳SDN \n" +
                "鍘熸枃锛歨ttps://blog.csdn.net/hanhailong18/article/details/81509952 \n" +
                "鐗堟潈澹版槑锛氭湰鏂囦负鍗氫富鍘熷垱鏂囩珷锛岃浆杞借闄勪笂鍗氭枃閾炬帴锛?;

        double semblance = getSemblance(s1, s2);
        System.out.println(semblance);
    }
}

