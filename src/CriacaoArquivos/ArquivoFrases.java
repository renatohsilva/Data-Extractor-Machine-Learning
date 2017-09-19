/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CriacaoArquivos;

import static CriacaoArquivos.PosTagger.GerarArquivoFrasesPosTag;
import static CriacaoArquivos.Token.CriarDicionarioSimbolos;
import static CriacaoArquivos.Token.TokenizarLinha;
import Util.ArquivoFrasesUtil;
import static Util.OperacoesUteis.RemoverTAGs;
import static Util.Stemmer.PegarRadicaisFrase;
import com.google.common.base.Joiner;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

/**
 *
 * @author renato
 */
public class ArquivoFrases 
{

    /**
     * Gera os arquivos que serão utilizados para montar os atributos 
     * que serão usados no treinamento
     * @param caminhoTagSet
     * @param nomeBanco
     * @param nomeColecaoBase
     * @param nomeChaveObjBase
     * @param nomeColecaoFrase
     * @param nomeChaveObjAlvo
     * @param nomeChaveObjFrase
     * @param nomeChaveObjToken
     * @param nomeChaveObjIndice
     * @param nomeChaveObjRadical
     * @param nomeChaveObjPOSTag
     */
    public static void GerarArquivos(String caminhoTagSet, String nomeBanco, 
            String nomeColecaoBase, String nomeChaveObjBase, 
            String nomeColecaoFrase, String nomeChaveObjAlvo, 
            String nomeChaveObjFrase, String nomeChaveObjToken, 
            String nomeChaveObjIndice, String nomeChaveObjRadical, 
            String nomeChaveObjPOSTag) 
    {
        try 
        {
            String textoFiltrado = BuscaTextoBaseFiltrado(nomeBanco, 
                    nomeColecaoBase, nomeChaveObjBase);

            ArrayList<Element> elementosTodasFrases = SerializaTextoHTML(textoFiltrado);

            Map<String, String> dicLetrasToken = CriarDicionarioSimbolos();
                  
            GeracaoDadosFrase(nomeBanco, nomeColecaoFrase, elementosTodasFrases, 
                    nomeChaveObjFrase, nomeChaveObjAlvo, nomeChaveObjRadical, 
                    nomeChaveObjToken, nomeChaveObjIndice, dicLetrasToken); 
            
            GerarArquivoFrase(nomeBanco, nomeColecaoFrase, nomeChaveObjToken, 
                    caminhoTagSet, nomeChaveObjFrase, nomeChaveObjPOSTag);
        } 
        catch (Exception e) 
        {
            throw new Error(e.getMessage());
        }
    }
    
    private static String BuscaTextoBaseFiltrado(String nomeBanco, 
            String nomeColecaoBase, String nomeChaveObjBase)
    {
        String textoFiltrado = "";
           
        try
        {
            MongoClient mongoClient = new MongoClient();
            DB db = mongoClient.getDB(nomeBanco);
            
            DBCollection collBase = db.getCollection(nomeColecaoBase);
            
            DBObject objFrase = collBase.findOne();
            
            ArrayList<String> frasesTextoBase = 
                    (ArrayList<String>)objFrase.get(nomeChaveObjBase);
            
            ArrayList<String> frasesFiltradas = 
                    frasesTextoBase.stream().filter((String s) -> 
            {
                return !s.contains("<?xml")
                        && !s.contains("<ROOT>")
                        && !s.contains("</ROOT>");
            }).collect(Collectors.toCollection(ArrayList::new));
            
            textoFiltrado = Joiner.on("\n").skipNulls().join(frasesFiltradas);
            
            mongoClient.close();
        } 
        catch (Exception e) 
        {
            throw new Error(e.getMessage());
        }
        return textoFiltrado;
    }
    
    private static ArrayList<Element> SerializaTextoHTML(String textoFiltrado)
    {
        ArrayList<Element> elementsDistintc = new ArrayList<>();
        try
        {
            Document documentosAlvos = Jsoup.parse(textoFiltrado, "UTF-8");
            Elements textoSerializado = documentosAlvos.getElementsByTag("f");

            elementsDistintc = 
                    (ArrayList<Element>)textoSerializado.stream().
                            collect(Collectors.toList());
            
        } 
        catch (Exception e) 
        {
            throw new Error(e.getMessage());
        }
        
        return elementsDistintc;
    }
    
    private static void GeracaoDadosFrase(String nomeBanco, 
            String nomeColecaoFrase, ArrayList<Element> elementosTodasFrases, 
            String nomeChaveObjFrase, String nomeChaveObjAlvo, 
            String nomeChaveObjRadical, String nomeChaveObjToken, 
            String nomeChaveObjIndice, Map<String, String> dicLetrasToken) 
    {
        try 
        {
            MongoClient mongoClient = new MongoClient();
            DB db = mongoClient.getDB(nomeBanco);
            
            BasicDBList dbolAlvo;
            BasicDBList dbolFrase;
            BasicDBObject dboFrase;
                                  
            DBCollection collectionFrase = db.getCollection( nomeColecaoFrase );
            DBCursor findFrase = collectionFrase.find();
            
            if (findFrase.size() == 0)
            {
                for (Element elementoFrase : elementosTodasFrases) 
                {
                    Integer codigoFrase = elementoFrase.elementSiblingIndex() + 1;

                    dbolFrase = new BasicDBList();
                    dbolAlvo = new BasicDBList();
                            
                    String fraseLimpa = elementoFrase.text();
                    String fraseTokenizada = TokenizarLinha(fraseLimpa, 
                            dicLetrasToken);
                    
                    dboFrase = new BasicDBObject(nomeChaveObjIndice, 
                            codigoFrase);
                    
                    dboFrase.append(nomeChaveObjToken, fraseTokenizada);
                            
                    int tamanhoNoFrase = elementoFrase.childNodeSize();
                    for (int contador = 0; contador < tamanhoNoFrase; contador++) 
                    {
                        Node childNode = elementoFrase.childNode(contador);
                        String nodeName = childNode.nodeName();
                        if (nodeName.equals("alvo")) 
                        {
                            String alvo = StringEscapeUtils.
                                    unescapeHtml4(childNode.toString().trim());
                            dbolAlvo.add(RemoverTAGs(alvo));
                        }
                        dbolFrase.addAll(ArquivoFrasesUtil.
                                GerarMapObjeto(childNode, nodeName, dicLetrasToken));
                    }
                    
                    dboFrase.append(nomeChaveObjAlvo, dbolAlvo);
                    dboFrase.append(nomeChaveObjFrase, dbolFrase);
                    
                    String[] radicaisFrase = PegarRadicaisFrase(fraseTokenizada);
                    dboFrase.append(nomeChaveObjRadical, radicaisFrase);
                    
                    collectionFrase.save(dboFrase);
                }
            }
            
            mongoClient.close();
        } 
        catch (Exception e) 
        {            
            throw new Error(e.getMessage());
        }
    }
    
    private static void GerarArquivoFrase(String nomeBanco, 
            String nomeColecaoFrase, String nomeChaveObjToken, 
            String caminhoTagSet, String nomeChaveObjFrase, 
            String nomeChaveObjPOSTag) 
    {
        try 
        {
            MongoClient mongoClient = new MongoClient();
            DB db = mongoClient.getDB( nomeBanco );
            DBCollection collectionFrase = db.getCollection( nomeColecaoFrase );
            
            DBCursor cursor = collectionFrase.find().
                    sort(new BasicDBObject("_id", 1));
            
            ArrayList<String> listaArquivoTokenizado = new ArrayList<>(); 
            while(cursor.hasNext()) 
            {
                DBObject objIteracao = cursor.next();
                String fraseSalva = (String)objIteracao.get(nomeChaveObjToken);
                
                listaArquivoTokenizado.add(fraseSalva);
            }
            
            GerarArquivoFrasesPosTag(listaArquivoTokenizado, caminhoTagSet, 
                    nomeBanco, nomeColecaoFrase, nomeChaveObjFrase, 
                    nomeChaveObjPOSTag);
            
            mongoClient.close();
        } 
        catch (Exception e) 
        {
            throw new Error(e.getMessage());
        }
    }
}
