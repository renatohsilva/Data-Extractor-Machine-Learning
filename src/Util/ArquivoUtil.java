/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 *
 * @author renato
 */
public class ArquivoUtil 
{

    /**
     * Grava um arquivo de texto, considerando o caminho e conteúdo
     * @param caminhoArquivo
     * @param conteudoArquivo
     */
   public static void GravarArquivo(String caminhoArquivo, 
            ArrayList<String> conteudoArquivo)
    {
        try 
        {
            try (FileWriter fw = new FileWriter(caminhoArquivo); 
                    BufferedWriter bw = new BufferedWriter(fw)) 
            {
                for (String item : conteudoArquivo)
                {
                    bw.write(item);
                    bw.newLine();
                }
            }
        } 
        catch (Exception e) 
        {
            throw new Error(e.getMessage());
        }
    }
    
    /**
     * Abre um arquivo texto considerando um determinado caminho
     * @param caminhoArquivo
     * @return
     */
    public static BufferedReader AbrirAquivo(String caminhoArquivo)
    {
        String corpus;
        FileInputStream stream;
        InputStreamReader streamReader;
        BufferedReader readerCorpus = null;
        
        try 
        {
            corpus = caminhoArquivo;
            
            stream = new FileInputStream(corpus);
            streamReader = new InputStreamReader(stream);
            readerCorpus = new BufferedReader(streamReader);
        } 
        catch (Exception e) 
        {
            throw new Error(e.getMessage());
        }
        return readerCorpus;      
    }
}
