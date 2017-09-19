/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CriacaoArquivos;

import ObjetoExtracao.Atributos;
import Log.Log;
import static Util.ArquivoAtributosUtil.ConvertFraseListaFrase;
import static Util.ArquivoAtributosUtil.VerificaTamanhoListas;
import Util.ArquivoUtil;
import static Util.LogUtil.CriaNovoObjLog;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 *
 * @author renato
 */
public class ArquivoAtributos 
{
    private static Atributos objAtributos;  
    private static ArrayList<Atributos> listaObjAtributos;  
    
    private static ArrayList<String> listaObjFraseStr;
    private static Integer tamanholistaObjFraseStr;
    
    private static ArrayList<String> listaFrase;
    private static Integer tamanholistaFrase;
    
    private static ArrayList<String> listaFraseRadical;
    private static Integer tamanholistaFraseRadical;
    
    private static ArrayList<String> listaFrasePosTag;
    private static Integer tamanholistaFrasePosTag;
    
    private static Integer posFrase;
        
    private static ArrayList<String> listaAlvos;
    private static ArrayList<Log> listaLog;
    
    /**
     * Gera a ListaFrase, ListaPOSTag, ListaRaizes, ListaObjFraseStr, 
     * ListaAlvos(objIteracao, pNomeChaveObjAlvo);
     * 
     * 
     * 
     * @param caminhoArquivoAtributos
     * @param pNomeBanco
     * @param pNomeColecaoFrase
     * @param pNomeChaveObjFrase
     * @param pNomeChaveObjAlvo
     * @param pNomeChaveObjRadical
     * @param pNomeChaveObjPOSTag
     * @param pNomeChaveObjToken
     * @param pNomeChaveObjIndice
     * @param caminhoArquivoLog
     */
    public static void GerarArquivos(String caminhoArquivoAtributos, 
            String pNomeBanco, String pNomeColecaoFrase, String pNomeChaveObjFrase, 
            String pNomeChaveObjAlvo, String pNomeChaveObjRadical, 
            String pNomeChaveObjPOSTag, String pNomeChaveObjToken, 
            String pNomeChaveObjIndice, String caminhoArquivoLog) 
    {
        try 
        {
            listaLog = new ArrayList<>();
            listaObjAtributos = new ArrayList<>();
                    
            MongoClient mongoClient = new MongoClient();
            DB db = mongoClient.getDB( pNomeBanco );
            DBCollection collectionFrase = db.getCollection( pNomeColecaoFrase );
            
            DBCursor cursor = collectionFrase.find().
                    sort(new BasicDBObject("_id", 1));
         
            posFrase = 0;
            while(cursor.hasNext()) 
            {
                DBObject objIteracao = cursor.next();
                
                CriaListaFrase(objIteracao, pNomeChaveObjToken);
                CriaListaPOSTag(objIteracao, pNomeChaveObjPOSTag);
                CriaListaRaizes(objIteracao, pNomeChaveObjRadical);
                CriaListaObjFraseStr(objIteracao, pNomeChaveObjFrase);
                CriaListaAlvos(objIteracao, pNomeChaveObjAlvo);
            
                tamanholistaFrase = listaFrase.size();
                tamanholistaFraseRadical = listaFraseRadical.size();
                tamanholistaFrasePosTag = listaFrasePosTag.size();
                tamanholistaObjFraseStr = listaObjFraseStr.size();
                
                if (VerificaTamanhoListas(tamanholistaFrase, 
                        tamanholistaFraseRadical, tamanholistaFrasePosTag, 
                        tamanholistaObjFraseStr)) 
                {
                    for (int posPalavra = 0; posPalavra < tamanholistaFrase; posPalavra++) 
                    {
                        String marcadorPalavraFrase = "";
                        
                        if (posPalavra == 0) 
                        {
                            marcadorPalavraFrase = "__BOF__";
                        }
                        else if (posPalavra == (tamanholistaFrase - 1)) 
                        {
                            marcadorPalavraFrase = "__EOF__";
                        }
                        
                        objAtributos = new Atributos(objIteracao, posPalavra, 
                                posFrase, listaObjFraseStr, listaFrase, listaFraseRadical, 
                                listaFrasePosTag, tamanholistaFrase, marcadorPalavraFrase);
                        
                        objAtributos = objAtributos.MontaObjeto();
                        
                        listaObjAtributos.add(objAtributos);
                    }
                }
                else
                {
                    throw new Error("Frase com ID" + posFrase + 
                            " com problemas de formatação");
                }
                
                listaObjAtributos.add(new Atributos());
                
                listaLog.add(CriaNovoObjLog(posFrase, listaAlvos, 
                        tamanholistaFrase));
                
                posFrase++;
            }    
        } 
        catch (UnknownHostException e) 
        {
            throw new Error(e.getMessage());
        }

        ArrayList<String> listaObjAtributosStr = listaObjAtributos.stream().
                map(s -> s.toString()).collect(Collectors.toCollection(ArrayList::new));

        ArquivoUtil.GravarArquivo(caminhoArquivoAtributos, listaObjAtributosStr);
        
        ArquivoLog.MontaArquivoLog(caminhoArquivoLog, listaLog);
    }
    
    private static void CriaListaFrase(DBObject objIteracao, 
            String nomeChaveObjToken)
    {
        try 
        {
            String strFraseTokenizada;
                        
            strFraseTokenizada = (String)objIteracao.get(nomeChaveObjToken);
            
            listaFrase = ConvertFraseListaFrase(strFraseTokenizada);
        } 
        catch (Exception e) 
        {
            throw new Error(e.getMessage());
        }
    }
    
    private static void CriaListaPOSTag(DBObject objIteracao, 
            String nomeChaveObjPOSTag)
    {
        try 
        {
            listaFrasePosTag = new ArrayList<>();
            listaFrasePosTag = ConvertFraseListaFrase((String)objIteracao.
                        get(nomeChaveObjPOSTag));
        } 
        catch (Exception e) 
        {
            throw new Error(e.getMessage());
        }
    }
    
    private static void CriaListaRaizes(DBObject objIteracao, 
            String nomeChaveObjRadical)
    {
        try 
        {
            listaFraseRadical = new ArrayList<>();
            listaFraseRadical = 
                        (ArrayList<String>)objIteracao.get(nomeChaveObjRadical);
        } 
        catch (Exception e) 
        {
            throw new Error(e.getMessage());
        }
    }
    
    private static void CriaListaObjFraseStr(DBObject objIteracao, 
            String nomeChaveObjFrase)
    {
        try 
        {           
            listaObjFraseStr = 
                    (ArrayList<String>)objIteracao.get(nomeChaveObjFrase);   
        } 
        catch (Exception e) 
        {
            throw new Error(e.getMessage());
        }
    }

    private static void CriaListaAlvos(DBObject objIteracao, 
            String pNomeChaveObjAlvo)
    {
        try 
        {
            listaAlvos = new ArrayList<>();
            listaAlvos = 
                    (ArrayList<String>)objIteracao.get(pNomeChaveObjAlvo);
        } 
        catch (Exception e) 
        {
            throw new Error(e.getMessage());
        }
    }

}
