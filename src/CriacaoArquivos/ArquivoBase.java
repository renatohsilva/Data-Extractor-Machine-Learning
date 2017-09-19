/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CriacaoArquivos;

import Util.ArquivoUtil;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 *
 * @author renato
 */
public class ArquivoBase 
{

    /**
     * Gera um arquivo mongo com as informações iniciais
     * @param caminhoArquivo
     * @param nomeBanco
     * @param nomeColecaoBase
     * @param nomeChaveObjBase
     */
    public static void GerarArquivoBase(String caminhoArquivo, 
            String nomeBanco, String nomeColecaoBase, String nomeChaveObjBase) 
    {
        try 
        {
            BufferedReader reader = ArquivoUtil.AbrirAquivo(caminhoArquivo);
            
            ArrayList<String> lista = reader.lines().collect(
                    Collectors.toCollection(ArrayList::new));
            
            MongoClient mongoClient = new MongoClient();
            DB db = mongoClient.getDB( nomeBanco );
            
            DBCollection collectionBase = db.getCollection( nomeColecaoBase );
            
            BasicDBList dbl = new BasicDBList();
            DBObject myDoc = collectionBase.findOne();
            
            if (myDoc == null) 
            {
                lista.stream().forEach((linha) -> 
                {
                    dbl.add(CorrigeProblemasDeFormatacaoArquivo(linha));
                });
                
                BasicDBObject dbo = new BasicDBObject(nomeChaveObjBase, dbl);
                
                collectionBase.save(dbo);
            }
            mongoClient.close(); 
        } 
        catch (Exception e) 
        {
            throw new Error(e.getMessage());
        }
    }
    
    /**
     * Corrige possíveis problemas de formatação das tags dos arquivos iniciais
     * @param pLinha
     * @return
     */
    public static String CorrigeProblemasDeFormatacaoArquivo(String pLinha)
     {
         String linha = pLinha;
         
         try 
         {             
             linha = CorrigirStringEsquerdaAlvo(linha);
             linha = CorrigirStringDireitaAlvo(linha);
         } 
         catch (Exception e) 
         {
              throw new Error(e.getMessage());
         }
         
         return linha;
     }

    private static String CorrigirStringEsquerdaAlvo(String linha) 
    {
        try 
        {
            Pattern pattern = Pattern.compile("([^\\s+])(<ALVO TIPO)");
            Matcher matcher = pattern.matcher(linha);
            
            if (matcher.find()) 
            {   
                String grupoMatch = matcher.group();
                String palavraMatch = String.valueOf(grupoMatch.substring(1));
                String caracterMatch = String.valueOf(grupoMatch.charAt(0));
                String espaco = " ";
                
                linha = matcher.replaceFirst(espaco + caracterMatch+
                        espaco + palavraMatch);
                
                return CorrigirStringEsquerdaAlvo(linha);
            }
        } 
        catch (Exception e) 
        {
            throw new Error(e.getMessage());
        }
        return linha;
    }
    
    private static String CorrigirStringDireitaAlvo(String linha) 
    {
        try 
        {
            Pattern pattern = Pattern.compile("(</ALVO>)([^\\s+])");
            Matcher matcher = pattern.matcher(linha);
            
            if (matcher.find()) 
            {   
                String grupoMatch = matcher.group();
                Integer intGrupoMatch = grupoMatch.length();

                String palavraMatch = String.valueOf(matcher.group().
                        substring(0, intGrupoMatch - 1));
                String caracterMatch = String.valueOf(matcher.group().
                        charAt(intGrupoMatch - 1));
                String espaco = " ";
                
                linha = matcher.replaceFirst(palavraMatch + espaco +
                        caracterMatch + espaco);
                
                return CorrigirStringDireitaAlvo(linha);
            }
        } 
        catch (Exception e) 
        {
            throw new Error(e.getMessage());
        }
        return linha;
    }
}
