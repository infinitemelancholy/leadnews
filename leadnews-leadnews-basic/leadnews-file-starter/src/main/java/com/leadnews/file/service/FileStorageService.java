package com.leadnews.file.service;

import java.io.InputStream;

/**
 * @author leadnews
 */
public interface FileStorageService {


    /**
     *  涓婁紶鍥剧墖鏂囦欢
     * @param prefix  鏂囦欢鍓嶇紑
     * @param filename  鏂囦欢鍚?
     * @param inputStream 鏂囦欢娴?
     * @return  鏂囦欢鍏ㄨ矾寰?
     */
    public String uploadImgFile(String prefix, String filename,InputStream inputStream);

    /**
     *  涓婁紶html鏂囦欢
     * @param prefix  鏂囦欢鍓嶇紑
     * @param filename   鏂囦欢鍚?
     * @param inputStream  鏂囦欢娴?
     * @return  鏂囦欢鍏ㄨ矾寰?
     */
    public String uploadHtmlFile(String prefix, String filename,InputStream inputStream);

    /**
     * 鍒犻櫎鏂囦欢
     * @param pathUrl  鏂囦欢鍏ㄨ矾寰?
     */
    public void delete(String pathUrl);

    /**
     * 涓嬭浇鏂囦欢
     * @param pathUrl  鏂囦欢鍏ㄨ矾寰?
     * @return
     *
     */
    public byte[]  downLoadFile(String pathUrl);

}

