/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Util;

import static CriacaoArquivos.Token.CriarDicionarioSimbolos;
import static CriacaoArquivos.Token.TokenizarLinha;
import Log.Alvo;
import Log.Log;
import java.util.ArrayList;
import java.util.Map;

/**
 *
 * @author re_hs
 */
public class LogUtil 
{

    /**
     * Cria um objeto de Log da extração
     * @param posFrase
     * @param listaAlvos
     * @param tamanholistaFrase
     * @return
     */
    public static Log CriaNovoObjLog(Integer posFrase, 
            ArrayList<String> listaAlvos, Integer tamanholistaFrase) 
    {
        Log retorno = null;
        try 
        {
            retorno = new Log(posFrase, listaAlvos, tamanholistaFrase);
        }
        catch (Exception e) 
        {
            throw new Error(e.getMessage());
        }
        
        return retorno;
    }
    
    /**
     * Considerando uma lista de palavras de entrada é montado uma lista de alvos
     * @param pListaAlvosStr
     * @return
     */
    public static ArrayList<Alvo> CriaListaAlvos(ArrayList<String> pListaAlvosStr) 
    {
        ArrayList<Alvo> listaAlvos = new ArrayList<>();
        try
        {
            Alvo objAlvo;
            for (String alvo : pListaAlvosStr) 
            {
                objAlvo = new Alvo();
                objAlvo.setPalavras(alvo);
                objAlvo.setNumPalavras(PegaNumPalavrasAlvo(alvo));
                listaAlvos.add(objAlvo);
            }
        } 
        catch (Exception e) 
        {
            throw new Error(e.getMessage());
        }
        
        return listaAlvos;
    }
    
    /**
     * Pega a posição da palavra do alvo na frase
     * @param alvo
     * @return
     */
    public static Integer PegaNumPalavrasAlvo(String alvo) 
    {
        Integer retorno = 0;
        try
        {
            Map<String, String> dicLetrasToken = CriarDicionarioSimbolos();
            String alvoTokenizado = TokenizarLinha(alvo.trim(), dicLetrasToken);
            String[] arrayAlvo = alvoTokenizado.trim().split(" ");
            retorno = arrayAlvo.length;
        } 
        catch (Exception e) 
        {
            throw new Error(e.getMessage());
        }
        
        return retorno;
    }
}
