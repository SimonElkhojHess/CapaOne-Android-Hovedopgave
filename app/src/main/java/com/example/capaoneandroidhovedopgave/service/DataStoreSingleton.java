package com.example.capaoneandroidhovedopgave.service;

import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.preferences.core.PreferencesKeys;
import androidx.datastore.rxjava2.RxDataStore;

import io.reactivex.Single;

public class DataStoreSingleton {
    RxDataStore<Preferences> datastore;
    private static final DataStoreSingleton dataStoreInstance = new DataStoreSingleton();
    public static DataStoreSingleton getInstance() {
        return dataStoreInstance;
    }
    private DataStoreSingleton() {}
    public void setDataStore(RxDataStore<Preferences> datastore) {
        this.datastore = datastore;
    }
    public RxDataStore<Preferences> getDataStore() {
        return datastore;
    }

    public boolean putStringValue(String Key, String value){
        boolean returnValue;
        Preferences.Key<String> PREF_KEY = PreferencesKeys.stringKey(Key);
        Single<Preferences> updateResult = dataStoreRX.updateDataStoreAsync(prefsIn -> {

        })
    }

}
