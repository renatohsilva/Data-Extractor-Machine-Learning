/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ObjetoExtracao;

import Util.Stemmer;
import com.mongodb.DBObject;
import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author renato
 */
public class Atributos extends AtributosAbstrato
{
    private Integer posicaoPalavra;
    private Integer posicaoFrase;
    private ArrayList<String> listaObjFraseStr;
    private ArrayList<String> listaFrase;
    private ArrayList<String> listaFrasePosTag;    
    private ArrayList<String> listaFraseRadical;
    
    private String marcadorPalavraFrase;
    
    public Atributos()
    {
        indiceAlvo = "";
        etiquetaPOSPalavra = "";
        raizPalavra = "";
        letraMaiuscula = null;
        palavraMaiuscula = null;
        
        raizes3Anteriores = new ArrayList<>();
        raizes3Posteriores = new ArrayList<>();
        etiquetasPOS3Anteriores = new ArrayList<>();
        etiquetasPOS3Posteriores = new ArrayList<>();
        
        marcadorPalavraFrase = "";
    }
    
    public Atributos(DBObject pObjIteracao, Integer pPosicaoPalavra,
            Integer pPosicaoFrase, ArrayList<String> pListaObjFraseStr, 
            ArrayList<String> pListaFrase, ArrayList<String> pListaFraseRadical, 
            ArrayList<String> pListaFrasePosTag, Integer pTamanholistaFrase, 
            String pMarcadorPalavraFrase)
    {      
        posicaoPalavra = pPosicaoPalavra;
        posicaoFrase = pPosicaoFrase;
        listaObjFraseStr = pListaObjFraseStr;
        listaFrase = pListaFrase;
        listaFraseRadical = pListaFraseRadical;
        listaFrasePosTag = pListaFrasePosTag;
        tamanholistaFrase = pTamanholistaFrase;
        marcadorPalavraFrase = pMarcadorPalavraFrase;
    }
    
    /**
     * Monta um objeto contendo todos os atributos necessários nja extração.
     * @return
     */
    public Atributos MontaObjeto() 
    {
        try 
        {           
            String[] arrayObjFraseStr = listaObjFraseStr.get(posicaoPalavra)
                    .trim().split("_");
            
            String frase = listaFrase.get(posicaoPalavra);
            String fraseRadical = listaFraseRadical.get(posicaoPalavra);
            String frasePosTag = listaFrasePosTag.get(posicaoPalavra);
            
            String[] arrayPosTag = frasePosTag.trim().split("_");
            
            if ((frasePosTag.startsWith("_") && arrayPosTag[0].trim().equals(""))
               || (arrayPosTag[0].trim().equals(frase))) 
            {
                indiceAlvo = arrayObjFraseStr[0];
                etiquetaPOSPalavra = "pos[0]=" + arrayPosTag[1];
                raizPalavra = "raiz[0]=" + fraseRadical; 
                
                CriarListasAnteriores(posicaoPalavra, listaFrasePosTag);
                CriarListasPosteriores(posicaoPalavra, listaFrasePosTag, tamanholistaFrase);
                
                letraMaiuscula = VerificaPriLetraMaiuscula(frase);
                palavraMaiuscula = VerificaPalavraMaiuscula(frase);
            }
            else        
            {
                throw new Error("(Palavra x Frase) - " +
                        posicaoPalavra +", "+ posicaoFrase + 
                        " com problemas de formatação");
            }
        } 
        catch (Exception e) 
        {
            throw new Error(e.getMessage());
        }
       
        return this;
    }

   
    
    private void CriarListasAnteriores(Integer bPosicaoPalavra, 
            ArrayList<String> fraseEtiquetada)
    {
        etiquetasPOS3Anteriores = new ArrayList<>();
        raizes3Anteriores = new ArrayList<>();
        String palavraEtiquetadaAnalisada, palavraAnalisada, radical, etiqueta;
                    
        if (bPosicaoPalavra > 0) 
        {
            Integer contador = 0;
            Integer posicaoNaLista = bPosicaoPalavra - 1;
                       
            while ((contador < 3) && (posicaoNaLista >= 0)) 
            {       
                try 
                {
                    palavraEtiquetadaAnalisada = 
                            fraseEtiquetada.get(posicaoNaLista);
                    
                    palavraAnalisada = 
                            BuscarPalavraFromTAG(palavraEtiquetadaAnalisada);
                    
                    radical = 
                            Stemmer.PegarRadicalPalavra(palavraAnalisada);
                    
                    etiqueta = 
                            BuscarEtiquetaPalavraFromTAG(palavraEtiquetadaAnalisada);
                    
                    raizes3Anteriores.add("raiz[-" + (contador + 1) + "]=" + radical);
                    etiquetasPOS3Anteriores.add("pos[-" + (contador + 1) + "]=" + etiqueta);
                    
                    posicaoNaLista--;
                    contador++;
                } 
                catch (Exception e) 
                {
                    throw new Error(e.getMessage());
                }   
            }
            
            Collections.reverse(raizes3Anteriores);
            Collections.reverse(etiquetasPOS3Anteriores);
 
        }
        else
        {
            raizes3Anteriores = etiquetasPOS3Anteriores = 
                    new ArrayList<>();
        }
    }
    
    private void CriarListasPosteriores(Integer bPosicaoPalavra, 
            ArrayList<String> fraseEtiquetada, Integer tamanhoFrase)
    {
        etiquetasPOS3Posteriores = new ArrayList<>();
        raizes3Posteriores = new ArrayList<>();
        
        String palavraEtiquetadaAnalisada, palavraAnalisada, radical, etiqueta;
         
        Integer posicaoUltimoElemento = (tamanhoFrase - 1);
        if (bPosicaoPalavra < posicaoUltimoElemento) 
        {
            Integer contador = 0;
            Integer posicaoNaLista = bPosicaoPalavra + 1;
            while ((contador < 3) && (posicaoNaLista < tamanhoFrase)) 
            {    
                try 
                {
                    palavraEtiquetadaAnalisada = 
                            fraseEtiquetada.get(posicaoNaLista);
                    
                    palavraAnalisada = 
                            BuscarPalavraFromTAG(palavraEtiquetadaAnalisada);
                    
                    radical = 
                            Stemmer.PegarRadicalPalavra(palavraAnalisada);
                                        
                    etiqueta = 
                            BuscarEtiquetaPalavraFromTAG(palavraEtiquetadaAnalisada);
                    
                    raizes3Posteriores.add("raiz[" + (contador + 1) + "]=" + radical);
                    etiquetasPOS3Posteriores.add("pos[" + (contador + 1) + "]=" + etiqueta);
                    
                    posicaoNaLista++;
                    contador++;
                } 
                catch (Exception e) 
                {
                    throw new Error(e.getMessage());
                }   
            }
        }
        else
        {
            raizes3Posteriores = etiquetasPOS3Posteriores = 
                    new ArrayList<>();
        }
    }
    
    private static Boolean VerificaPriLetraMaiuscula(String palavra)
    {
        try 
        {
            if((palavra != null) && !(palavra.trim().equals("")))
            {
                char[] palavraToArray = palavra.toCharArray();
                if(palavraToArray != null)
                {
                    char letra = palavra.charAt(0);
                    if(Character.isLetter(letra))
                    {               
                        return (String.valueOf(letra).toUpperCase().
                                equals(String.valueOf(letra)));
                    }
                }
            }
        }
        catch (Exception e) 
        {
            return false;
        }
        return false;
    }

    private static Boolean VerificaPalavraMaiuscula(String palavra) 
    {
        try 
        {
            if ((palavra != null) && !(palavra.trim().equals("")))
            {
                if (VerificaPriLetraMaiuscula(palavra)) 
                {
                    return palavra.toUpperCase().equals(palavra);   
                }
            }
        }
        catch (Exception e) 
        {
            return false;
        }
        return false;
    }
    
    private static String BuscarPalavraFromTAG(String palavraEtiquetada)
    {
        String palavra = "";
        try
        {
            if ((palavraEtiquetada != null) && 
                    (palavraEtiquetada.split("_").length > 0))
            {
                char[] palavraEtiquetaArray = palavraEtiquetada.toCharArray();
                if (palavraEtiquetaArray[0] == '_') 
                {
                    palavra = String.valueOf(palavraEtiquetaArray[0]);
                }
                else
                {
                    palavra = palavraEtiquetada.split("_")[0];   
                }
            }
        } 
        catch (Exception e) 
        {
            throw new Error(e.getMessage());
        }
        
        return palavra.trim();
    }
    
    private static String BuscarEtiquetaPalavraFromTAG(String palavraEtiquetada)
    {
        String etiqueta = "";
        try
        {
            if ((palavraEtiquetada != null) && 
                    (palavraEtiquetada.split("_").length > 0)) 
            {
                int tamanhoArray = palavraEtiquetada.split("_").length;

                if (palavraEtiquetada.toCharArray()[0] == '_') 
                {
                    etiqueta = palavraEtiquetada.split("_")[tamanhoArray-1];
                }
                else
                {
                    etiqueta = palavraEtiquetada.split("_")[1];
                }
            }
        } 
        catch (Exception e) 
        {
            throw new Error(e.getMessage());
        }
        return etiqueta.trim();
    }

    @Override public String toString() 
    {
        StringBuilder result = new StringBuilder();
               
        result.append(this.indiceAlvo);
        result.append("\t");
               
        result.append(this.raizPalavra);
        result.append("\t");
        
        result.append(this.etiquetaPOSPalavra);
        result.append("\t");
        
        this.raizes3Anteriores.stream().filter((raiz) -> 
                ((raiz != null) && (!raiz.equals("")))).map((raiz) -> 
                {
                    result.append(raiz);
                    return raiz;
                }).forEach((_item) -> {
                    result.append("\t");
                });
        
        this.raizes3Posteriores.stream().filter((raiz) -> 
                ((raiz != null) && (!raiz.equals("")))).map((raiz) -> 
                {
                    result.append(raiz);
                    return raiz;
                }).forEach((_item) -> {
                    result.append("\t");
                });

        this.etiquetasPOS3Anteriores.stream().filter((etiqueta) -> 
                ((etiqueta != null) && (!etiqueta.equals("")))).map((etiqueta) -> 
                {
                    result.append(etiqueta);
                    return etiqueta;
                }).forEach((_item) -> {
                    result.append("\t");
                });
        
        this.etiquetasPOS3Posteriores.stream().filter((etiqueta) -> 
                ((etiqueta != null) && (!etiqueta.equals("")))).map((etiqueta) -> 
                {
                    result.append(etiqueta);
                    return etiqueta;
                }).forEach((_item) -> {
                    result.append("\t");
                });
        
        if (this.letraMaiuscula == null) 
            result.append("");    
        else
        {
            result.append("let[0]=");
            result.append(this.letraMaiuscula);
        }
        
        result.append("\t");
        
        if (this.palavraMaiuscula == null) 
            result.append(""); 
        else
        {
            result.append("pal[0]=");
            result.append(this.palavraMaiuscula);
        }
        
        result.append("\t");
        result.append(marcadorPalavraFrase);
        
        return result.toString().trim();
    }

    
}
