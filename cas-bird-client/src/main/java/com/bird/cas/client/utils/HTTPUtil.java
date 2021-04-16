package com.bird.cas.client.utils;

import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import javax.net.ssl.*;
import java.io.IOException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

/**
 * @program: cas-bird
 * @description:
 * @author: JuFeng(ZhaoJun)
 * @create: 2021-04-15 19:46
 **/
@Slf4j
public class HTTPUtil {

    private static final OkHttpClient clientInstance;

    private static final Integer connectTimeout_time = 5;
    private static final Integer writeTimeout_time = 5;
    private static final Integer readTimeout_time = 5;
    private static final int maxIdleConnections = 35;
    private static final int keepAliveDuration = 60;
    private static final TimeUnit timeUnit = TimeUnit.SECONDS;

    static {
        clientInstance = new OkHttpClient.Builder()
                .sslSocketFactory(createSSLSocketFactory())
                .hostnameVerifier(new TrustAllHostnameVerifier())
                .connectTimeout(connectTimeout_time, timeUnit)
                .writeTimeout(writeTimeout_time, timeUnit)
                .readTimeout(readTimeout_time, timeUnit)
                .connectionPool( new ConnectionPool(maxIdleConnections,keepAliveDuration, timeUnit))
                .retryOnConnectionFailure(true)
                .build();
    }


    public static String doPostHttpRequest(String url, String json)
            {
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, json);
        Request request = new Request.Builder().url(url).post(body)
                .addHeader("content-type", "application/json").build();

        Response response = null;
        try {
            response = clientInstance.newCall(request).execute();
            if (response ==null || !response.isSuccessful()) {
                return null;
            }
            return response.body().string();
        } catch (IOException e) {
            log.error("check st error {}" ,e.getMessage());
        }

        return null;

    }

    private static SSLSocketFactory createSSLSocketFactory() {
        SSLSocketFactory ssfFactory = null;

        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null,  new TrustManager[] { new TrustAllCerts() }, new SecureRandom());
            ssfFactory = sc.getSocketFactory();
        } catch (Exception e) {
        }

        return ssfFactory;
    }

    private static class TrustAllCerts implements javax.net.ssl.X509TrustManager {

        @Override
        public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
            return;
        }

        @Override
        public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
            return;
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }

    private static class TrustAllHostnameVerifier implements HostnameVerifier {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }

}
