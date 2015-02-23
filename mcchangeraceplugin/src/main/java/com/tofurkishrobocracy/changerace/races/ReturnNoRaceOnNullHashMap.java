/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tofurkishrobocracy.changerace.races;

import java.util.HashMap;

/**
 *
 * @author Cody
 */
public class ReturnNoRaceOnNullHashMap<K, V> extends HashMap<K, V> {

    protected V defaultValue;

    public ReturnNoRaceOnNullHashMap(V defaultValue) {
        this.defaultValue = defaultValue;
    }

    @Override
    public V get(Object k) {
        V v = super.get(k);
        return ((v == null) && !this.containsKey(k)) ? this.defaultValue : v;
    }
}
