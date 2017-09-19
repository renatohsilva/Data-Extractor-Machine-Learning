/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Util;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 *
 * @author renato
 */
public class OperacoesUteis 
{   
    /**
     * Remove vazios de um Arraylist de entrada passada como parâmetro
     * @param arrayStrings
     * @return
     */
    public static ArrayList<String> RemoveElementosVaziosLista(
            String[] arrayStrings)
    {
        ArrayList<String> arrayListInicial = 
                new ArrayList<>(Arrays.asList(arrayStrings));
        
        ArrayList<String> arrayListRetorno = arrayListInicial.stream().
                filter((palavra) -> (palavra != null)).
                filter((palavra) -> (!palavra.trim().equals(""))).
                map((palavra) -> palavra.trim()).
                collect(Collectors.toCollection(ArrayList::new));

        return arrayListRetorno;
    }      

    /**
     * Remove as Tags de xml de um string de entrada passado como parâmetro
     * @param html
     * @return
     */
    public static String RemoverTAGs(String html)
    {  
        String retorno = "";
        try 
        {
            String noTagRegex = "<[^>]+>";  
            retorno = html.replaceAll(noTagRegex, "").replaceAll("\n", "").trim();     
        }
        catch (Exception e) 
        {
            throw new Error(e.getMessage());
        }
        return retorno;
    } 
    
    /**
     * Apaga um arquivo gravado considerando um caminho passado como parâmetro
     * @param caminhoArquivoLog
     */
    public static void ApagaArquivoLog(String caminhoArquivoLog)
    {  
        try 
        {
            File arqLog = new File(caminhoArquivoLog);
            if(arqLog.exists())
                arqLog.delete();     
        }
        catch (Exception e) 
        {
            throw new Error(e.getMessage());
        }
    } 
}
