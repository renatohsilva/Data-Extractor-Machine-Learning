/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Util;

import static CriacaoArquivos.Token.TokenizarLinha;
import static Util.OperacoesUteis.RemoverTAGs;
import com.mongodb.BasicDBList;
import java.util.Map;
import org.apache.commons.lang3.StringEscapeUtils;
import org.jsoup.nodes.Node;

/**
 *
 * @author renato
 */
public class ArquivoFrasesUtil 
{

    /**
     * Cria um arquivo mongo com cada elemento sendo composto por 
     * etiqueta e o valor
     * @param childNode
     * @param nodeName
     * @param dicLetrasTokenMap
     * @return
     */
    public static BasicDBList GerarMapObjeto(Node childNode, String nodeName,
            Map<String, String> dicLetrasTokenMap)
    {
        BasicDBList dbolFrase = new BasicDBList();
        String legenda;
        Integer codigoAlvo;
        
        try 
        {
            if ((nodeName != null) && (!nodeName.equals("")))
            {
                if (nodeName.equals("alvo"))
                {
                    codigoAlvo = 0;
                    
                    String textoAlvosSemTags = RemoverTAGs(childNode.toString());
                    String textoAlvos = StringEscapeUtils.
                            unescapeHtml4(textoAlvosSemTags.trim());
                    
                    String textoAlvosTokenizado = TokenizarLinha(textoAlvos, 
                            dicLetrasTokenMap);
                    
                    String[] textoAlvosArray = textoAlvosTokenizado.trim().split(" ");
                    
                    for (String palavraAlvo : textoAlvosArray) 
                    {                       
                        if (!palavraAlvo.equals("")) 
                        {
                            codigoAlvo++;
                            if (codigoAlvo == 1) 
                                legenda = "B";
                            else 
                                legenda = "I";
                            
                            String valor = legenda + "_" + palavraAlvo;
                            
                            dbolFrase.add(valor);
                        }
                    }
                }
                else        
                {
                    String textoNaoAlvos = StringEscapeUtils.
                            unescapeHtml4(childNode.toString().trim());
                    
                    String textoNaoAlvosTokenizado = TokenizarLinha(textoNaoAlvos, 
                            dicLetrasTokenMap);
                    
                    String[] textoNaoAlvosArray = textoNaoAlvosTokenizado.trim().split(" ");

                    for (String palavraNaoAlvo : textoNaoAlvosArray) 
                    {
                        if (!palavraNaoAlvo.equals("")) 
                        {
                            legenda = "O";
                            
                            String valor = legenda + "_" + palavraNaoAlvo;
                            
                            dbolFrase.add(valor);    
                        } 
                    }
                }
            }
        } 
        catch (Exception e) 
        {
            throw new Error(e.getMessage());
        }
        
        return dbolFrase;
    }
   
}
