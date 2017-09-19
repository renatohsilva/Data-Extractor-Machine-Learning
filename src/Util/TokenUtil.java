/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Util;

import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author renato
 */
public class TokenUtil 
{

    /**
     * Cria um dicionário de tokens considerando todos os simbolos que não 
     * devem ser separados no processo de tokenização de um texto
     * @return
     */
    public static ArrayList<String> CriarTokenDicionarioPalavras() 
    {
        ArrayList<String> listaDicionarioInicial = new ArrayList<>();
        
        listaDicionarioInicial.addAll(SepararCaracteres("@ \' ª º -"));
        listaDicionarioInicial.addAll(SepararCaracteres("A a À à Á á Â â Ã ã Ä ä"));
        listaDicionarioInicial.addAll(SepararCaracteres("B b"));
        listaDicionarioInicial.addAll(SepararCaracteres("C c Ç ç"));
        listaDicionarioInicial.addAll(SepararCaracteres("D d"));
        listaDicionarioInicial.addAll(SepararCaracteres("E e È è É é Ê ê Ë"));
        listaDicionarioInicial.addAll(SepararCaracteres("F f"));
        listaDicionarioInicial.addAll(SepararCaracteres("G g"));
        listaDicionarioInicial.addAll(SepararCaracteres("H h"));
        listaDicionarioInicial.addAll(SepararCaracteres("I i Ì ì Í í Î î Ï ï"));
        listaDicionarioInicial.addAll(SepararCaracteres("J j"));
        listaDicionarioInicial.addAll(SepararCaracteres("K k"));
        listaDicionarioInicial.addAll(SepararCaracteres("L l"));
        listaDicionarioInicial.addAll(SepararCaracteres("M m"));
        listaDicionarioInicial.addAll(SepararCaracteres("N Ñ n ñ"));
        listaDicionarioInicial.addAll(SepararCaracteres("O o Ò ò Ó ó Ô ô Õ õ Ö ö"));
        listaDicionarioInicial.addAll(SepararCaracteres("P p"));
        listaDicionarioInicial.addAll(SepararCaracteres("Q q"));
        listaDicionarioInicial.addAll(SepararCaracteres("R r"));
        listaDicionarioInicial.addAll(SepararCaracteres("S s"));
        listaDicionarioInicial.addAll(SepararCaracteres("T t"));
        listaDicionarioInicial.addAll(SepararCaracteres("U u Ù ù Ú ú Û û Ü ü"));
        listaDicionarioInicial.addAll(SepararCaracteres("V v"));
        listaDicionarioInicial.addAll(SepararCaracteres("W w"));
        listaDicionarioInicial.addAll(SepararCaracteres("X x"));
        listaDicionarioInicial.addAll(SepararCaracteres("Y y"));
        listaDicionarioInicial.addAll(SepararCaracteres("Z z"));
        listaDicionarioInicial.addAll(SepararCaracteres("0 1 2 3 4 5 6 7 8 9"));
        
        return listaDicionarioInicial;
    }
    
    private static ArrayList<String> SepararCaracteres(String caracteres)
    {
        try 
        {
            ArrayList<String> listaDicionario = new ArrayList<>();
            if (caracteres != null) 
            {
                String[] listaDicArrays = caracteres.split(" "); 
                listaDicionario = new ArrayList(Arrays.asList(listaDicArrays));
            }
            return listaDicionario;   
        } 
        catch (Exception e) 
        {
            throw new Error(e.getMessage());  
        }
       
    }
        
}
