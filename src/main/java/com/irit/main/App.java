package com.irit.main;

import com.irit.upnp.DefilementServer;

/**
 * Created by mkostiuk on 11/07/2017.
 */
public class App {

    public static void main(String[] args) {
        new Thread(new DefilementServer()).run();
    }
}
