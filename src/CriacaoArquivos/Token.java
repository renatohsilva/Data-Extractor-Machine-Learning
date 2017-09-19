/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CriacaoArquivos;

import Util.TokenUtil;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author renato
 */
public class Token 
{
 
    /**
     * Tokeniza uma linha de acordo com um dicionario de simbolos.
     * @param linha
     * @param dicLetrasToken
     * @return
     */
    public static String TokenizarLinha(String linha, 
            Map<String, String> dicLetrasToken)
    {
        String palavras, tokens, valorChar;
        
        palavras = "";
        tokens = "";
        char[] linhaArray = linha.toCharArray(); 
        for (int contador = 0; contador < linhaArray.length; contador++)
        {
            valorChar = String.valueOf(linhaArray[contador]);
            
            if (VerificarNoDicionario(dicLetrasToken, valorChar))
            {
                palavras += valorChar;
                if (contador == (linhaArray.length - 1))
                    tokens += palavras.trim() + " ";
            }
            else
            {
                if (!palavras.trim().equals(""))
                    tokens += palavras.trim() + " ";
                
                if (!valorChar.trim().equals(""))
                    tokens += valorChar + " ";
                
                palavras = "";
            }
        }
        return tokens.trim();  
    }
    
    private static Boolean VerificarNoDicionario(Map dicionario, Object chave)
    {
        return dicionario.get(chave) != null;
    }
    
    /**
     * Cria um dicionário de dados que será utilizado na rotina de tokenização.
     * @return
     */
    public static Map<String, String> CriarDicionarioSimbolos() 
    {
        Map<String, String> dicionarioToken = new TreeMap<>();
        ArrayList<String> listaDicionario;
        try 
        {
            listaDicionario = TokenUtil.CriarTokenDicionarioPalavras();
    
            listaDicionario.stream().filter((letra) -> 
                    (!letra.trim().equals(""))).map((letra) -> 
                            letra.trim()).forEach((letra) -> {
                                dicionarioToken.put(letra, letra);
                            });
        } 
        catch (Exception e) 
        {
            throw new Error(e.getMessage());  
        }
        return dicionarioToken;
    }
}
