package com.leadnews.utils.common;

import javax.swing.border.TitledBorder;
import java.text.NumberFormat;
import java.util.Locale;

public class Compute {

    public static void main(String[] args) {
        String content = "鏈€杩戝叕鍙哥敱浜庝笟鍔℃嫇灞曪紝闇€瑕佽繘琛屽皬绋嬪簭鐩稿叧鐨勫紑鍙戯紝鏈潃鏈濆叏鏍堝紑鍙戣€呭姫鍔涳紝鍐冲畾瀛︿範涓媀ue锛屽幓骞碿sdn閫佷簡涓€鏈€奦ue.js鏉冨▉鎸囧崡銆?;
        String title = "VueVueVue";
        double ss = SimilarDegree(content, title);
        System.out.println(ss);
    }
    /*
     * 璁＄畻鐩镐技搴?
     * */
    public static double SimilarDegree(String strA, String strB) {
        String newStrA = removeSign(strA);
        String newStrB = removeSign(strB);
        //鐢ㄨ緝澶х殑瀛楃涓查暱搴︿綔涓哄垎姣嶏紝鐩镐技瀛愪覆浣滀负鍒嗗瓙璁＄畻鍑哄瓧涓茬浉浼煎害
        int temp = Math.max(newStrA.length(), newStrB.length());
        int temp2 = longestCommonSubstring(newStrA, newStrB).length();
        return temp2 * 1.0 / temp;
    }


    /*
     * 灏嗗瓧绗︿覆鐨勬墍鏈夋暟鎹緷娆″啓鎴愪竴琛?
     * */
    public static String removeSign(String str) {
        StringBuffer sb = new StringBuffer();
        //閬嶅巻瀛楃涓瞫tr,濡傛灉鏄眽瀛楁暟瀛楁垨瀛楁瘝锛屽垯杩藉姞鍒癮b涓婇潰
        for (char item : str.toCharArray()) {
            if (charReg(item)) {
                sb.append(item);
            }
        }
        return sb.toString();
    }


    /*
     * 鍒ゆ柇瀛楃鏄惁涓烘眽瀛楋紝鏁板瓧鍜屽瓧姣嶏紝
     * 鍥犱负瀵圭鍙疯繘琛岀浉浼煎害姣旇緝娌℃湁瀹為檯鎰忎箟锛屾晠绗﹀彿涓嶅姞鍏ヨ€冭檻鑼冨洿銆?
     * */
    public static boolean charReg(char charValue) {
        return (charValue >= 0x4E00 && charValue <= 0X9FA5) || (charValue >= 'a' && charValue <= 'z')
                || (charValue >= 'A' && charValue <= 'Z') || (charValue >= '0' && charValue <= '9');
    }


    /*
     * 姹傚叕鍏卞瓙涓诧紝閲囩敤鍔ㄦ€佽鍒掔畻娉曘€?
     * 鍏朵笉瑕佹眰鎵€姹傚緱鐨勫瓧绗﹀湪鎵€缁欑殑瀛楃涓蹭腑鏄繛缁殑銆?
     *
     * */
    public static String longestCommonSubstring(String strA, String strB) {
        char[] chars_strA = strA.toCharArray();
        char[] chars_strB = strB.toCharArray();
        int m = chars_strA.length;
        int n = chars_strB.length;

        /*
         * 鍒濆鍖栫煩闃垫暟鎹?matrix[0][0]鐨勫€间负0锛?
         * 濡傛灉瀛楃鏁扮粍chars_strA鍜宑hars_strB鐨勫搴斾綅鐩稿悓锛屽垯matrix[i][j]鐨勫€间负宸︿笂瑙掔殑鍊煎姞1锛?
         * 鍚﹀垯锛宮atrix[i][j]鐨勫€肩瓑浜庡乏涓婃柟鏈€杩戜袱涓綅缃殑杈冨ぇ鍊硷紝
         * 鐭╅樀涓叾浣欏悇鐐圭殑鍊间负0.
         */
        int[][] matrix = new int[m + 1][n + 1];
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (chars_strA[i - 1] == chars_strB[j - 1]) {
                    matrix[i][j] = matrix[i - 1][j - 1] + 1;
                } else {
                    matrix[i][j] = Math.max(matrix[i][j - 1], matrix[i - 1][j]);
                }

            }
        }
        /*
         * 鐭╅樀涓紝濡傛灉matrix[m][n]鐨勫€间笉绛変簬matrix[m-1][n]鐨勫€间篃涓嶇瓑浜巑atrix[m][n-1]鐨勫€硷紝
         * 鍒檓atrix[m][n]瀵瑰簲鐨勫瓧绗︿负鐩镐技瀛楃鍏冿紝骞跺皢鍏跺瓨鍏esult鏁扮粍涓€?
         *
         */
        char[] result = new char[matrix[m][n]];
        int currentIndex = result.length - 1;
        while (matrix[m][n] != 0) {
            if (matrix[n] == matrix[n - 1]){
                n--;
            } else if (matrix[m][n] == matrix[m - 1][n]){
                m--;
            }else {
                result[currentIndex] = chars_strA[m - 1];
                currentIndex--;
                n--;
                m--;
            }
        }
        return new String(result);
    }


    /*
     * 缁撴灉杞崲鎴愮櫨鍒嗘瘮褰㈠紡
     * */
    public static String similarityResult(double resule) {
        return NumberFormat.getPercentInstance(new Locale("en ", "US ")).format(resule);
    }
}
