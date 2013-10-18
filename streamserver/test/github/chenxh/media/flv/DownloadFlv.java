package github.chenxh.media.flv;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.io.IOUtils;

public class DownloadFlv {
    public static void main(String[] args) throws HttpException, IOException {
        String apacheUrl = "http://172.16.2.5:8103/vod/2013/06/18/B59A9A10E5264516B59E426D1DFCE77C/SD_U2Lbx5.flv?start=682942309";
        String myUrl = "http://localhost:9090/flvx/demo/SD_U2Lbx5.flv?start=682942309";

        String url = myUrl;
        HttpClient client = new HttpClient();
        HttpMethod method = new GetMethod(url);
        client.executeMethod(method);

        InputStream inStream = method.getResponseBodyAsStream();
        FileOutputStream outStream = null;

        File file = new File("demo4.flv");
        System.out.println(file.getAbsolutePath());
        try {
            outStream = new FileOutputStream(file);
            IOUtils.copy(inStream, outStream);
            outStream.close();
            inStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(outStream);
            IOUtils.closeQuietly(inStream);
        }
        method.abort();
    }
}
