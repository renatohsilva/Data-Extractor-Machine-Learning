/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Log;

import Util.LogUtil;
import java.util.ArrayList;

/**
 *
 * @author re_hs
 */
public class Log 
{
    private final Integer posFrase;
    private final Integer tamanholistaFrase;
    private final ArrayList<Alvo> listaAlvos;
    private final Integer numAlvos;
    
    public Log(Integer pPosFrase, ArrayList<String> pListaAlvosStr, 
            Integer pTamanholistaFrase)
    {
        posFrase = pPosFrase;
        tamanholistaFrase = pTamanholistaFrase;
        listaAlvos = LogUtil.CriaListaAlvos(pListaAlvosStr); 
        numAlvos = listaAlvos.size();
    }
    

    /**
     * @return the listaAlvos
     */
    public ArrayList<Alvo> getListaAlvos() {
        return listaAlvos;
    }

    /**
     * @return the tamanholistaFrase
     */
    public Integer getTamanholistaFrase() {
        return tamanholistaFrase;
    }

    /**
     * @return the numAlvos
     */
    public Integer getNumAlvos() {
        return numAlvos;
    }

    /**
     * @return the posFrase
     */
    public Integer getPosFrase() {
        return posFrase;
    }
}
