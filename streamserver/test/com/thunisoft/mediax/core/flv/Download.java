package com.thunisoft.mediax.core.flv;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.io.IOUtils;



// This class downloads a file from a URL.
class Download {
    public static void main(String[] args) throws IOException {
        HttpClient c = new HttpClient();
        
        GetMethod method = new GetMethod("http://chenxh.thunisoft.com:8080/streamer/flv/kuiba-0001.flv");
        method.setQueryString("start=4190");
        c.executeMethod(method);
        InputStream inStream = method.getResponseBodyAsStream();
        IOUtils.copy(inStream, new FileOutputStream("test3.flv"));
        
        method = new GetMethod("http://chenxh.thunisoft.com:80/flv/kuiba-0001.flv");
        method.setQueryString("start=4190");
        c.executeMethod(method);
        inStream = method.getResponseBodyAsStream();
        IOUtils.copy(inStream, new FileOutputStream("test4.flv"));
        
        System.out.println("aa");
    }
}
