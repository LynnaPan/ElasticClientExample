import org.apache.http.HttpHost;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;

import java.io.IOException;

public class EsClient {
    public static RestHighLevelClient createClient() {
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("localhost", 9200, "http"),
                        new HttpHost("localhost", 9201, "http")));

        return client;
    }

    public static void createIndex(RestHighLevelClient client, String index) {
        try {
            CreateIndexRequest request = new CreateIndexRequest(index);
            CreateIndexResponse createIndexResponse = client.indices().create(request);
            boolean acknowledged = createIndexResponse.isAcknowledged();
            if (acknowledged ) {
                System.out.println("The index" + index + "is been created successfully!");
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getById(RestHighLevelClient client, String index, String type, String id) throws IOException {
        try {
            GetRequest getRequest = new GetRequest(
                    index,
                    type,
                    id);

            GetResponse getResponse = client.get(getRequest);
            return getResponse.getSourceAsString();

        } catch (ElasticsearchException exception){
            System.out.println(exception);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            client.close();
        }

        return null;
    }

    public static void main(String[] args) throws IOException {
        RestHighLevelClient client = createClient();

        String top1 = getById(client, "production", "doc", "1");
        System.out.println(top1);
    }
}


