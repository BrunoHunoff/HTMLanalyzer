import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class HtmlAnalyzer {

    public static void main(String[] args)throws InterruptedException, URISyntaxException, IOException  {

        String url = args[0];

        //HTTP REQUEST 
        String htmlContent = getURL(url);

        String[] str = htmlContent.split("\n");

        ArrayList<HtmlContent> contentList = tratarHtml(str);

        //ordena arrayList por lvl decrescente
        Collections.sort(contentList, Comparator.comparingInt(HtmlContent::getLvl).reversed());

        //printa primeiro item da lista - maior lvl, primeiro a aparecer
        System.out.println(contentList.get(0).getContent());
    }

    public static String getURL(String url)throws InterruptedException, URISyntaxException, IOException {
        //definindo Client
        HttpClient client = HttpClient.newHttpClient();

        //definindo HTTP request - GET
        HttpRequest request = HttpRequest.newBuilder()
            .uri(new URI(url))
            .GET()
            .build();

        //HTTP REQUEST
        HttpResponse<String> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());

        //Tratamento de erro - Perguntar professor
        if (httpResponse.statusCode() != 200) {
            return "URL connection error";
        }

        return httpResponse.body();
    }

    public static ArrayList<HtmlContent> tratarHtml(String[] html){
        ArrayList<HtmlContent> list = new ArrayList<>();

        int cont = 0;

        //verificar conteudo de cada linha
        for (int i = 0; i < html.length; i++) {

            //linha vazia - passar para próxima interação 
            if (html[i] == "") {
                continue;
            }

            //tag encontrada - aumenta nível
            if (html[i].contains("<")) {
                cont++;

                //fechamento de tag encontrado - diminui nivel
                if (html[i].contains("</")) {
                    cont-= 2;
                }
            }

            //conteudo encontrado - cria objeto e salva em arrayList
            if (!html[i].contains("<")) {
                HtmlContent element = new HtmlContent(html[i], cont);
            
                list.add(element);
            }
        }

        return list;
    }

}