package com.leadnews.tess4j;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import java.io.File;

public class Application {

    /**
     * 璇嗗埆鍥剧墖涓殑鏂囧瓧
     * @param args
     */
    public static void main(String[] args) throws TesseractException {

        //鍒涘缓瀹炰緥
        ITesseract tesseract = new Tesseract();

        //璁剧疆瀛椾綋搴撹矾寰?
        tesseract.setDatapath("D:\\workspace\\tessdata");

        //璁剧疆璇█ -->绠€浣撲腑鏂?
        tesseract.setLanguage("chi_sim");

        File file = new File("D:\\143.png");

        //璇嗗埆鍥剧墖
        String result = tesseract.doOCR(file);

        System.out.println("璇嗗埆鐨勭粨鏋滀负锛?+result.replaceAll("\\r|\\n","-"));

    }
}

