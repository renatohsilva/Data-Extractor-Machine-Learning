/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ObjetoExtracao;

import Log.Alvo;
import java.util.ArrayList;

/**
 *
 * @author renato
 */
public abstract class AtributosAbstrato 
{    
    public String indiceAlvo;
    
    public String etiquetaPOSPalavra;
    public String raizPalavra;
    
    public ArrayList<String> etiquetasPOS3Posteriores;
    public ArrayList<String> etiquetasPOS3Anteriores;
    
    public ArrayList<String> raizes3Posteriores;
    public ArrayList<String> raizes3Anteriores;
    
    public Boolean letraMaiuscula;
    public Boolean palavraMaiuscula;
    
    public ArrayList<Alvo> listaAlvos;
    public Integer tamanholistaFrase;
}
