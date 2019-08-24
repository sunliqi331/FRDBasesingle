import java.io.IOException;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

public class sendAlarmTest {
    public static void main(String[] args) throws IOException {
        HttpClient httpCilent = new DefaultHttpClient();// Creates CloseableHttpClient instance with
                                                                     // default configuration.
        HttpGet httpGet = new HttpGet("http://192.168.217.34:8309/FRDBasesingle/sendAlarm/sendInfos?productLineid=1");
        try {
            httpCilent.execute(httpGet);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        }

    }
}
