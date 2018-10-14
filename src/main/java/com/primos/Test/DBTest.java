/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.primos.Test;

import com.primos.core.persistence.PA.Correo;
import com.primos.core.persistence.PersistenceManager;

/**
 *
 * @author gusta
 */
public class DBTest {
    public static void testDB(){
        Correo correo = new Correo(0, true, true, 0, "gustavoovalle470@gmail.com");
        PersistenceManager.getInstance().save(correo);
    }
}
