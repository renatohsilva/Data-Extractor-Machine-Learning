/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CriacaoArquivos;

import Log.Alvo;
import Log.Log;
import Util.ArquivoUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * @author re_hs
 */
public class ArquivoLog 
{
    private static Integer totNumPalavras;
    private static Map<Integer, Long> mapNumAlvos;
    private static Map<Integer, Long> mapPalavrasAlvos;

    /**
     * Monta log de extração
     * @param caminhoArquivoLog
     * @param listaLogs
     */
    public static void MontaArquivoLog(String caminhoArquivoLog, 
            ArrayList<Log> listaLogs) 
    {
        try
        {
            ArrayList<String> arqLog = new ArrayList<>();
            MontaParametrosArquivo(listaLogs);
            
            arqLog.add("Número de palavras e símbolos analisados: " 
                    + totNumPalavras);
            arqLog.add(" ");
            
            arqLog.add("Número de ocorrência de Alvos:");
            mapNumAlvos.entrySet().stream().forEach((map) -> 
            {
                Integer key = map.getKey();
                Long value = map.getValue();
                String linha;
                
                if (value > 1) 
                    linha = value + " frases possuem ";
                else
                    linha = value + " frase possui ";

                if (key > 1) 
                    linha = linha + key + " alvos.";
                else
                    linha = linha + key + " alvo.";
                
                arqLog.add(linha);
            });
            arqLog.add(" ");
            
            arqLog.add("Número de palavras nos Alvos:");
            mapPalavrasAlvos.entrySet().stream().forEach((map) -> 
            {
                Integer key = map.getKey();
                Long value = map.getValue();
                String linha;
                               
                if (value > 1) 
                    linha = value + " alvos possuem ";
                else
                    linha = value + " alvo possui ";
                
                if (key > 1) 
                    linha = linha + key + " palavras.";
                else
                    linha = linha + key + " palavra.";
                
                arqLog.add(linha);
            });
            ArquivoUtil.GravarArquivo(caminhoArquivoLog, arqLog);
        } 
        catch (Exception e) 
        {
            throw new Error(e.getMessage());
        }
    }
    
    private static void MontaParametrosArquivo(ArrayList<Log> listaLogs)
    {
        try
        {
            totNumPalavras = listaLogs.stream().
                    mapToInt(s -> s.getTamanholistaFrase()).sum();
            
            mapNumAlvos = listaLogs.stream()
                    .collect(Collectors.groupingBy(o -> o.getNumAlvos(), 
                            Collectors.counting()));
            
            List<Alvo> listaAlvos = listaLogs.stream().map(x -> x.getListaAlvos()).
                    flatMap(x -> x.stream()).collect(Collectors.toList());
            
            mapPalavrasAlvos = listaAlvos.stream()
                    .collect(Collectors.groupingBy(o -> o.getNumPalavras(), 
                            Collectors.counting()));
            
        } 
        catch (Exception e) 
        {
            throw new Error(e.getMessage());
        }
    }
}
