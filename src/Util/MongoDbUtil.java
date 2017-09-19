/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Util;

import com.mongodb.DB;
import com.mongodb.MongoClient;

/**
 *
 * @author renato
 */
public class MongoDbUtil 
{

    /**
     * Apaga o Banco de dados existe no cliente local
     * @param nomeBanco
     */
    public static void ApagarBanco(String nomeBanco)
    {
        try 
        {
            MongoClient mongoClient = new MongoClient();
            DB db = mongoClient.getDB( nomeBanco );
            
            db.dropDatabase();
            
            mongoClient.close();
        }
        catch (Exception e) 
        {
            throw new Error(e.getMessage());
        }
    }
}
