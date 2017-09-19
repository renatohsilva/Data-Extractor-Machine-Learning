/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Util;

import ptstemmer.implementations.OrengoStemmer;

/**
 *
 * @author renato
 */
public class Stemmer 
{

    /**
     * Usa o Stemmer para retornar o radical da palavra
     * @param palavra
     * @return
     */
    public static String PegarRadicalPalavra(String palavra)
    {
        String radical;
        try 
        {
            ptstemmer.Stemmer stemmer = new OrengoStemmer();
            radical = stemmer.getWordStem(palavra);
        } 
        catch (Exception e) 
        {
            throw new Error(e.getMessage());
        }
        return radical;
    }
    
    /**
     * Usa o Stemmer para retornar o radical de cada palavra que faz parte
     * da frase
     * @param frase
     * @return
     */
    public static String[] PegarRadicaisFrase(String frase)
    {
        String[] radicais;
        try 
        {
            ptstemmer.Stemmer stemmer = new OrengoStemmer();
            radicais = stemmer.getPhraseStems(frase);
        } 
        catch (Exception e) 
        {
            throw new Error(e.getMessage());
        }
        return radicais;
    } 
}
