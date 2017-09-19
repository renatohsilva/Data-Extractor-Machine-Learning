/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Extrator;

import CriacaoArquivos.ArquivoAtributos;
import CriacaoArquivos.ArquivoBase;
import CriacaoArquivos.ArquivoFrases;
import Util.MongoDbUtil;
import Util.OperacoesUteis;
import java.io.File;
import javax.swing.JOptionPane;

/**
 * @author renato
 */
public class Extrator
{
    private static String caminhoPastaArquivos;
    private static String caminhoCorpus;
    private static String caminhoArquivoAtributos;    
    private static String caminhoTagSet;
    private static String caminhoArquivoLog;
    
    private static String nomeBanco;    
    
    private static String nomeColecaoBase;
    private static String nomeChaveObjBase;
    
    private static String nomeColecaoFrase;
    private static String nomeChaveObjFrase;
    private static String nomeChaveObjAlvo;
    private static String nomeChaveObjRadical;
    private static String nomeChaveObjPOSTag;
    private static String nomeChaveObjToken;
    private static String nomeChaveObjIndice;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) 
    {       
        try 
        {
            ExtrairAtributos();   
        } 
        catch (Exception e) 
        {
            throw new Error(e.getMessage());
        }
        finally
        {
            JOptionPane.showMessageDialog(null, "A Extração terminou",
                    "Atenção", JOptionPane.INFORMATION_MESSAGE); 
        }
    }

    private static void ExtrairAtributos() 
    {          
        GerarCaminhos();
        GerarNomes();
        CriarArquivos();
    }

    private static void GerarCaminhos() 
    {
        caminhoPastaArquivos = System.getProperty("user.dir") + "/src/Arquivos";
        
        caminhoCorpus = caminhoPastaArquivos + "/01-SentiCorpus-PT.txt";
        caminhoArquivoAtributos = caminhoPastaArquivos + "/02-Atributos.txt";
        caminhoArquivoLog = caminhoPastaArquivos + "/LogExtracao.txt";
        
        caminhoTagSet = caminhoPastaArquivos + "/MXPOST/port";
    }
    
    
    private static void GerarNomes() 
    {
        nomeBanco = "bancoExtrator";
        
        nomeColecaoBase = "collectionBase";
        nomeChaveObjBase = "textoBase";
        
        nomeColecaoFrase = "collectionFrase";
        nomeChaveObjFrase = "frase";
        
        nomeChaveObjAlvo = "alvos";
        nomeChaveObjToken = "token";
        
        nomeChaveObjRadical = "radicais";
        nomeChaveObjPOSTag = "POSTag";
        
        nomeChaveObjIndice = "indiceFrase";
    }
    
    private static void CriarArquivos()
    {
        try 
        {   
            MongoDbUtil.ApagarBanco(nomeBanco);

            File arqAtributo = new File(caminhoArquivoAtributos);
            if (!arqAtributo.exists()) 
            {
                OperacoesUteis.ApagaArquivoLog(caminhoArquivoLog);
                
                ArquivoBase.GerarArquivoBase(caminhoCorpus, nomeBanco, nomeColecaoBase,
                        nomeChaveObjBase);
                
                ArquivoFrases.GerarArquivos(caminhoTagSet, nomeBanco, nomeColecaoBase, 
                        nomeChaveObjBase, nomeColecaoFrase, nomeChaveObjAlvo,
                        nomeChaveObjFrase, nomeChaveObjToken, nomeChaveObjIndice, 
                        nomeChaveObjRadical, nomeChaveObjPOSTag); 
                
                ArquivoAtributos.GerarArquivos(caminhoArquivoAtributos, nomeBanco, 
                        nomeColecaoFrase, nomeChaveObjFrase, nomeChaveObjAlvo, 
                        nomeChaveObjRadical, nomeChaveObjPOSTag, nomeChaveObjToken, 
                        nomeChaveObjIndice, caminhoArquivoLog);
            }
        }
        catch (Exception e) 
        {
            throw new Error(e.getMessage());
        }
    }
}
