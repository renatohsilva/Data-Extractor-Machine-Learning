/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CriacaoArquivos;

import com.google.common.base.Joiner;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import tagger.TestTagger;

/**
 *
 * @author renato
 */
public class PosTagger 
{

    /**
     * Chama o pos tagger e recupera a informação extraída da execução.
     * @param listaArquivoTokenizado
     * @param caminhoTagSet
     * @param nomeBanco
     * @param nomeColecaoFrase
     * @param nomeChaveObjFrase
     * @param nomeChaveObjPOSTag
     * @throws IOException
     */
    public static void GerarArquivoFrasesPosTag(ArrayList<String> listaArquivoTokenizado, 
            String caminhoTagSet, String nomeBanco, String nomeColecaoFrase, 
            String nomeChaveObjFrase, String nomeChaveObjPOSTag) 
            throws IOException 
    {
        String conteudoArquivoPosTag = "";
        String frases = Joiner.on("\n").join(listaArquivoTokenizado);
        
        try 
        (InputStream inputPosTagger = 
                new ByteArrayInputStream(frases.getBytes("UTF-8"))) 
        {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PrintStream printsTream = new PrintStream(baos);
            try
            {
                
                System.setIn(inputPosTagger);
                System.setOut(printsTream);
                System.setErr(new PrintStream(new File("MXPOST_console.txt")));
                String tagset[] = { caminhoTagSet };
                TestTagger.main(tagset);
            }
            catch (FileNotFoundException e)
            {
                if (inputPosTagger != null)
                    inputPosTagger.close();
                
                throw new Error(e.getMessage());
            }
            conteudoArquivoPosTag = baos.toString("UTF-8");
        }
        AtualizarDadosSalvosComEtiquetasPOS(conteudoArquivoPosTag,
                nomeBanco, nomeColecaoFrase, nomeChaveObjFrase, nomeChaveObjPOSTag);
    }
    
    /**
     * Atualiza os dados, salvos no arquivo mongo, com a informação 
     * extraída do pos tagger. 
     * @param conteudoArquivo
     * @param nomeBanco
     * @param nomeColecaoFrase
     * @param nomeChaveObjFrase
     * @param nomeChaveObjPOSTag
     */
    public static void AtualizarDadosSalvosComEtiquetasPOS(String conteudoArquivo, 
            String nomeBanco, String nomeColecaoFrase, String nomeChaveObjFrase,
            String nomeChaveObjPOSTag) 
    {
        try 
        {
            Integer contador = 0;
            
            MongoClient mongoClient = new MongoClient();
            DB db = mongoClient.getDB( nomeBanco );
            DBCollection collectionFrase = db.getCollection( nomeColecaoFrase );
            
            BasicDBObject objSalvo = (BasicDBObject)collectionFrase.findOne();
            if(objSalvo.size() <= 6) 
            {                
                ArrayList<String> listaFrasesEtiquetadas = 
                        new ArrayList<>(Arrays.asList(conteudoArquivo.split("\n")));
                
                DBCursor cursor = collectionFrase.find().
                        sort(new BasicDBObject("_id", 1));
                
                while(cursor.hasNext()) 
                {
                    DBObject objIteracao = cursor.next();
                    objIteracao.put(nomeChaveObjPOSTag, 
                            listaFrasesEtiquetadas.get(contador));
                    contador++;

                    collectionFrase.save(objIteracao);
                }
            }
            mongoClient.close();
            
        } 
        catch (Exception e) 
        {
            throw new Error(e.getMessage());   
        }
    }
}
