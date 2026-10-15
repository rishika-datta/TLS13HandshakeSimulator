
package com.tls13.simulator;

import javax.net.ssl.*;
import java.io.*;
import java.security.KeyStore;

public class TLSClient {

    public static void main(String[] args) throws Exception {
        char[] password = "changeit".toCharArray();

        KeyStore trustStore = KeyStore.getInstance("PKCS12");
        try (FileInputStream file =
                     new FileInputStream("server-keystore.p12")) {
            trustStore.load(file, password);
        }

        TrustManagerFactory tmf = TrustManagerFactory.getInstance(
                TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(trustStore);

        SSLContext context = SSLContext.getInstance("TLSv1.3");
        context.init(null, tmf.getTrustManagers(), null);

        SSLSocketFactory factory = context.getSocketFactory();

        try (SSLSocket socket =
                     (SSLSocket) factory.createSocket("localhost", 8443)) {

            socket.setEnabledProtocols(new String[]{"TLSv1.3"});
            socket.startHandshake();

            System.out.println("Handshake successful!");
            System.out.println("Protocol: "
                    + socket.getSession().getProtocol());
            System.out.println("Cipher suite: "
                    + socket.getSession().getCipherSuite());

            PrintWriter out = new PrintWriter(
                    socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));

            out.println("Hello from TLS client");
            System.out.println("Server: " + in.readLine());
        }
    }
}
