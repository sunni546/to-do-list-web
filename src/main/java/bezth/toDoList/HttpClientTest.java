package bezth.toDoList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class HttpClientTest {

    public static void main(String[] args) throws URISyntaxException, ClientProtocolException, IOException {

        URI uri = new URI("http://localhost:8080/todos");
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpResponse httpResponse = httpClient.execute(new HttpGet(uri));   // Post 요청은 HttpPost() 사용
        HttpEntity httpEntity = httpResponse.getEntity();
        String content = EntityUtils.toString(httpEntity);  // httpEntity.getContent()는 InputStream을 반환, 바로 String으로 받기 위해 EntityUtils를 사용
        System.out.println(content);
    }
}
