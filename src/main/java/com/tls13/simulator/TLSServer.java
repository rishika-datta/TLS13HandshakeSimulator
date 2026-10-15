
package com.tls13.simulator;

import javax.net.ssl.*;
import java.io.*;
import java.security.KeyStore;

public class TLSServer {

    public static void main(String[] args) throws Exception {
        char[] password = "changeit".toCharArray();

        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        try (FileInputStream file =
                     new FileInputStream("server-keystore.p12")) {
            keyStore.load(file, password);
        }

        KeyManagerFactory kmf = KeyManagerFactory.getInstance(
                KeyManagerFactory.getDefaultAlgorithm());
        kmf.init(keyStore, password);

        SSLContext context = SSLContext.getInstance("TLSv1.3");
        context.init(kmf.getKeyManagers(), null, null);

        SSLServerSocketFactory factory =
                context.getServerSocketFactory();

        try (SSLServerSocket server =
                     (SSLServerSocket) factory.createServerSocket(8443)) {

            server.setEnabledProtocols(new String[]{"TLSv1.3"});
            System.out.println("TLS 1.3 server listening on port 8443");

            try (SSLSocket socket = (SSLSocket) server.accept()) {
                socket.startHandshake();

                System.out.println("Handshake successful!");
                System.out.println("Protocol: "
                        + socket.getSession().getProtocol());
                System.out.println("Cipher suite: "
                        + socket.getSession().getCipherSuite());

                BufferedReader in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(
                        socket.getOutputStream(), true);

                String message = in.readLine();
                System.out.println("Client: " + message);
                out.println("Secure response from TLS server");
            }
        }
    }
}
