/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

/**
 *
 * @author renato
 */
public class ArquivoAtributosUtil 
{
    
    /**
     * Verifica se todas as listas geradas na extração possuem o mesmo tamanho,
     * se houver diferença de tamanho existe problema na rotina
     * @param tamanholistaFrase
     * @param tamanholistaFraseRadical
     * @param tamanholistaFrasePosTag
     * @param tamanholistaObjFraseStr
     * @return
     */
    public static boolean VerificaTamanhoListas(Integer tamanholistaFrase, 
            Integer tamanholistaFraseRadical, Integer tamanholistaFrasePosTag, 
            Integer tamanholistaObjFraseStr) 
    {
        return (Objects.equals(tamanholistaFrase, 
                tamanholistaFraseRadical)) &&
                (Objects.equals(tamanholistaFraseRadical,
                        tamanholistaFrasePosTag)) &&
                (Objects.equals(tamanholistaFrasePosTag,
                        tamanholistaObjFraseStr));
    }
    
    /**
     * Converte uma string frase de entrada em um arraylist de palavras
     * @param strFrase
     * @return
     */
    public static ArrayList<String> ConvertFraseListaFrase(String strFrase) 
    {  
        ArrayList<String> retorno = new ArrayList<>(Arrays.asList(strFrase.
                trim().split(" ")));
        return retorno;
    }
    
}
