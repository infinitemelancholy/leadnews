package com.leadnews.utils.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class FileUtils {

    /**
     * 閲嶈祫婧愭祦涓鍙栫涓€琛屽唴瀹?
     * @param in
     * @return
     * @throws IOException
     */
    public static String readFristLineFormResource(InputStream in) throws IOException{
        BufferedReader br=new BufferedReader(new InputStreamReader(in));
        return br.readLine();
    }


}

