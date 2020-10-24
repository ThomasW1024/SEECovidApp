package com.example.covidapp;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.work.Configuration;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Objects;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

public class MyApplication extends Application implements Configuration.Provider {

    @Override
    public void onCreate() {
        super.onCreate();
        //Parse SDK stuff goes here
        try {
            HttpsURLConnection.setDefaultSSLSocketFactory(getSocketFactory(this));
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    if (Objects.equals("10.0.2.2", hostname)) {
                        return true;
                    }
                    return false;
                }
            });
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Configuration getWorkManagerConfiguration() {
        Configuration myConfig = new Configuration.Builder()
                .setMinimumLoggingLevel(Log.INFO)
                .build();
//        //initialize WorkManager
//        WorkManager.initialize(this, myConfig);
        return myConfig;
    }

    // From MaxMxx at https://stackoverflow.com/questions/17045795/making-a-https-request-using-android-volley
    public SSLSocketFactory getSocketFactory(Context context)
            throws CertificateException, IOException, KeyStoreException, NoSuchAlgorithmException, KeyManagementException {

        // Load CAs from an InputStream (could be from a resource or ByteArrayInputStream or ...)
        CertificateFactory cf = CertificateFactory.getInstance("X.509");

        InputStream caInput = new BufferedInputStream(context.getResources().openRawResource(R.raw.server));
        // I paste my myFile.crt in raw folder under res.
        Certificate ca = null;

        //noinspection TryFinallyCanBeTryWithResources
        try {
            ca = cf.generateCertificate(caInput);
            System.out.println("ca=" + ((X509Certificate) ca).getSubjectDN());
        } catch (CertificateException e) {
            e.printStackTrace();
        } finally {
            caInput.close();
        }

        // Create a KeyStore containing our trusted CAs
        String keyStoreType = KeyStore.getDefaultType();
        KeyStore keyStore = KeyStore.getInstance(keyStoreType);
        keyStore.load(null, null);
        keyStore.setCertificateEntry("ca", ca);

        // Create a TrustManager that trusts the CAs in our KeyStore
        String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
        tmf.init(keyStore);

        // Create an SSLContext that uses our TrustManager
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, tmf.getTrustManagers(), null);

        return sslContext.getSocketFactory();
    }
}