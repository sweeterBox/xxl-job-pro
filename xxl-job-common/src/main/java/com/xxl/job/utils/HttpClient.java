package com.xxl.job.utils;


import com.xxl.job.model.R;
import lombok.extern.slf4j.Slf4j;
import javax.net.ssl.*;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * @author xuxueli 2018-11-25 00:55:31
 */
@Slf4j
public class HttpClient {
    public static final String XXL_JOB_ACCESS_TOKEN = "XXL-JOB-ACCESS-TOKEN";


    // trust-https start
    private static void trustAllHosts(HttpsURLConnection connection) {
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            SSLSocketFactory newFactory = sc.getSocketFactory();

            connection.setSSLSocketFactory(newFactory);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        connection.setHostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });
    }
    private static final TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[]{};
        }
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }
        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }
    }};
    // trust-https end


    /**
     * post
     *
     * @param url
     * @param accessToken
     * @param timeout
     * @param requestObj
     * @param returnTargClassOfT
     * @return
     */
    public static R postBody(String url, String accessToken, int timeout, Object requestObj, Class returnTargClassOfT) {
        HttpURLConnection connection = null;
        BufferedReader bufferedReader = null;
        try {
            // connection
            URL realUrl = new URL(url);
            connection = (HttpURLConnection) realUrl.openConnection();

            // trust-https
            boolean useHttps = url.startsWith("https");
            if (useHttps) {
                HttpsURLConnection https = (HttpsURLConnection) connection;
                trustAllHosts(https);
            }

            // connection setting
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setUseCaches(false);
            connection.setReadTimeout(timeout * 1000);
            connection.setConnectTimeout(3 * 1000);
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            connection.setRequestProperty("Accept-Charset", "application/json;charset=UTF-8");

            if(accessToken!=null && accessToken.trim().length()>0){
                connection.setRequestProperty(XXL_JOB_ACCESS_TOKEN, accessToken);
            }

            // do connection
            connection.connect();

            // write requestBody
            if (requestObj != null) {
                String requestBody = JsonUtils.obj2Json(requestObj);

                DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream());
                dataOutputStream.write(requestBody.getBytes("UTF-8"));
                dataOutputStream.flush();
                dataOutputStream.close();
            }
            // valid StatusCode
            int statusCode = connection.getResponseCode();
            if (statusCode != 200) {
                return new R<String>(R.FAIL_CODE, "xxl-job remoting fail, StatusCode("+ statusCode +") invalid. for url : " + url);
            }

            // result
            bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
            StringBuilder result = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                result.append(line);
            }
            String resultJson = result.toString();

            // parse returnT
            try {
                return JsonUtils.json2Obj(resultJson, R.class, returnTargClassOfT);
            } catch (Exception e) {
                log.error("xxl-job remoting (url="+url+") response content invalid("+ resultJson +").", e);
                return new R<String>(R.FAIL_CODE, "connection xxl-job-admin (url="+url+") response content invalid("+ resultJson +").");
            }

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new R<String>(R.FAIL_CODE, "connection xxl-job-admin error("+ e.getMessage() +"), for url : " + url);
        } finally {
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
                if (connection != null) {
                    connection.disconnect();
                }
            } catch (Exception e2) {
               log.error(e2.getMessage(), e2);
            }
        }
    }

}
