import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Stack;

//http://hiring.axreng.com/internship/example1.html

public class HtmlAnalyzer {

    public static void main(String[] args)throws InterruptedException, URISyntaxException, IOException  {
        String url = args[0];
        
        String result;

        //HTTP REQUEST 
        String htmlContent = getURL(url);

        String[] str = htmlContent.split("\n");

        if (malformedHtml(str)) {
            return;
        };

        ArrayList<HtmlContent> contentList = tratarHtml(str);

        //ordena arrayList por lvl decrescente
        Collections.sort(contentList, Comparator.comparingInt(HtmlContent::getLvl).reversed());

        //result = valor do primeiro item do arrayList (maior lvl e primeiro a aparecer)
        result = contentList.get(0).getContent();
        
        //retira espaços antes da primeira letra da String
        result = result.replaceFirst("^\\s+", "");

        System.out.println(result);
            
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

        //Tratamento de erro
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

            //linha vazia - passa para próxima interação 
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

    public static Boolean malformedHtml(String[] str) {

        //pilha para armazenar tags em aberto
        Stack<String> tagStack = new Stack<>();

        for (int i = 0; i < str.length; i++) {
            //tag encontrada
            if (str[i].contains("<")) {

                //fechamento de tag
                if (str[i].contains("</")) {

                    //tratamento de erro - remover tag de pilha vazia
                    if (tagStack.isEmpty()) {
                        System.out.println("malformed HTML");
                        return true;
                    }
                    
                    //tira "/" da tag de fechamento
                    String temp = str[i].replace("/", "");
                    
                    //compara tags
                    if (tagStack.lastElement().equals(temp)) {
                        //remove elemento da pilha e passa para proxima interação for
                        tagStack.pop();
                        continue;
                    }

                }
                //caso seja tag de abertura, adiciona ao topo da lista
                tagStack.add(str[i]);
            }

        }

        //se lista não está vazia, houve tag não fechada ou fechada no lugar errado
        if (!tagStack.isEmpty()) {
            System.out.println("malformed HTML");
            return true;
        }

        return false;
    }
}