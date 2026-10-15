
package com.tls13.simulator;

import javax.swing.*;
import java.awt.*;

public class TLSHandshakeGUI extends JFrame {

    private int currentStep = -1;
    private Timer timer;

    private final String[] messages = {
            "ClientHello",
            "ServerHello",
            "EncryptedExtensions",
            "Certificate",
            "CertificateVerify",
            "Server Finished",
            "Client Finished",
            "Secure Connection Established",
            "Client Sends Encrypted Message",
            "Server Sends Encrypted Response"
    };

    private final String[] descriptions = {
            "Client proposes TLS 1.3 and supported algorithms.",
            "Server selects connection parameters and key share.",
            "Server sends negotiated connection extensions.",
            "Server provides its certificate when required.",
            "Server proves possession of its private key.",
            "Server confirms handshake integrity.",
            "Client confirms handshake integrity.",
            "The handshake is complete. Application data can now be exchanged.",
            "Client sends application data protected using TLS traffic keys.",
            "Server processes the request and returns protected application data."
    };

    private final JLabel stepLabel =
            new JLabel("Press Start to begin the handshake",
                    SwingConstants.CENTER);

    private final JTextArea details = new JTextArea();
    private final HandshakePanel handshakePanel = new HandshakePanel();

    private final JButton startButton = new JButton("Start Animation");
    private final JButton nextButton = new JButton("Next Step");
    private final JButton resetButton = new JButton("Reset");

    public TLSHandshakeGUI() {
        setTitle("TLS 1.3 Handshake Simulator");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JLabel heading = new JLabel(
                "TLS 1.3 HANDSHAKE VISUALIZATION",
                SwingConstants.CENTER);
        heading.setFont(new Font("Arial", Font.BOLD, 23));
        heading.setBorder(
                BorderFactory.createEmptyBorder(15, 5, 10, 5));

        add(heading, BorderLayout.NORTH);
        add(handshakePanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout(8, 8));
        bottomPanel.setBorder(
                BorderFactory.createEmptyBorder(5, 15, 15, 15));

        stepLabel.setFont(new Font("Arial", Font.BOLD, 16));
        bottomPanel.add(stepLabel, BorderLayout.NORTH);

        details.setEditable(false);
        details.setLineWrap(true);
        details.setWrapStyleWord(true);
        details.setFont(new Font("Arial", Font.PLAIN, 14));
        details.setText(
                "Explore the TLS 1.3 handshake, followed by "
                        + "encrypted application-data exchange.");

        bottomPanel.add(new JScrollPane(details), BorderLayout.CENTER);

        JPanel buttons = new JPanel(new FlowLayout());
        buttons.add(startButton);
        buttons.add(nextButton);
        buttons.add(resetButton);
        bottomPanel.add(buttons, BorderLayout.SOUTH);

        add(bottomPanel, BorderLayout.SOUTH);

        nextButton.setEnabled(true);

        startButton.addActionListener(e -> startAnimation());
        nextButton.addActionListener(e -> showNextStep());
        resetButton.addActionListener(e -> resetSimulation());
    }

    private void startAnimation() {
        if (timer != null && timer.isRunning()) {
            timer.stop();
        }

        if (currentStep >= messages.length - 1) {
            resetSimulation();
        }

        startButton.setEnabled(false);
        showNextStep();

        if (currentStep < messages.length - 1) {
            timer = new Timer(1800, e -> showNextStep());
            timer.start();
        } else {
            startButton.setEnabled(true);
        }
    }

    private void showNextStep() {
        if (currentStep >= messages.length - 1) {
            finishAnimation();
            return;
        }

        currentStep++;

        stepLabel.setText(
                "Step " + (currentStep + 1) + " of " + messages.length
                        + ": " + messages[currentStep]);

        details.setText(
                messages[currentStep] + "\n\n"
                        + descriptions[currentStep] + "\n\n"
                        + (currentStep >= 8
                        ? "Direction: " + (currentStep == 8
                        ? "CLIENT  →  SERVER"
                        : "SERVER  →  CLIENT")
                        : "Handshake stage"));

        details.setCaretPosition(0);
        handshakePanel.repaint();

        if (currentStep == messages.length - 1) {
            finishAnimation();
        }
    }

    private void finishAnimation() {
        if (timer != null) {
            timer.stop();
        }

        startButton.setEnabled(true);
        nextButton.setEnabled(false);
    }

    private void resetSimulation() {
        if (timer != null) {
            timer.stop();
        }

        currentStep = -1;

        stepLabel.setText("Press Start to begin the handshake");
        details.setText(
                "TLS 1.3 establishes a secure connection between a client "
                        + "and a server.\n\n"
                        + "Use Start Animation or Next Step to explore all 10 stages.");

        handshakePanel.repaint();
        nextButton.setEnabled(true);
        startButton.setEnabled(true);
    }

    private class HandshakePanel extends JPanel {

        HandshakePanel() {
            setBackground(Color.WHITE);
            setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            int width = getWidth();
            int leftX = 40;
            int boxWidth = 165;
            int boxHeight = 75;
            int rightX = width - boxWidth - 40;
            int boxY = 45;

            g2.setColor(new Color(220, 235, 255));
            g2.fillRoundRect(leftX, boxY, boxWidth, boxHeight, 20, 20);

            g2.setColor(new Color(210, 240, 220));
            g2.fillRoundRect(rightX, boxY, boxWidth, boxHeight, 20, 20);

            g2.setColor(Color.DARK_GRAY);
            g2.setFont(new Font("Arial", Font.BOLD, 18));
            g2.drawString("CLIENT", leftX + 48, boxY + 43);
            g2.drawString("SERVER", rightX + 48, boxY + 43);

            if (currentStep >= 0) {
                boolean clientToServer =
                        currentStep == 0
                                || currentStep == 6
                                || currentStep == 8;

                boolean applicationData = currentStep >= 8;

                if (currentStep == 7) {
                    g2.setColor(new Color(20, 130, 65));
                    g2.setFont(new Font("Arial", Font.BOLD, 17));
                    drawCentered(g2, "SECURE SESSION READY",
                            width / 2, 200);

                } else {
                    int startX = clientToServer
                            ? leftX + boxWidth : rightX;
                    int endX = clientToServer
                            ? rightX : leftX + boxWidth;
                    int y = 150;

                    g2.setStroke(new BasicStroke(3));
                    g2.setColor(applicationData
                            ? new Color(20, 150, 75)
                            : new Color(45, 105, 200));

                    g2.drawLine(startX, y, endX, y);

                    int direction = clientToServer ? 1 : -1;
                    g2.fillPolygon(
                            new int[]{
                                    endX,
                                    endX - 12 * direction,
                                    endX - 12 * direction
                            },
                            new int[]{y, y - 7, y + 7},
                            3);

                    g2.setFont(new Font("Arial", Font.BOLD, 13));
                    drawCentered(g2, messages[currentStep],
                            width / 2, y - 18);

                    if (applicationData) {
                        g2.setFont(new Font("Arial", Font.BOLD, 12));
                        drawCentered(g2, "ENCRYPTED APPLICATION DATA",
                                width / 2, y + 28);
                    }
                }
            } else {
                g2.setColor(Color.GRAY);
                g2.setFont(new Font("Arial", Font.PLAIN, 15));
                drawCentered(g2, "Handshake messages appear here",
                        width / 2, 150);
            }

            g2.setColor(Color.GRAY);
            g2.setFont(new Font("Arial", Font.ITALIC, 12));
            drawCentered(g2,
                    "Illustrative TLS 1.3 message flow",
                    width / 2, 235);

            g2.dispose();
        }

        private void drawCentered(
                Graphics2D g2, String text, int centerX, int y) {
            int textWidth = g2.getFontMetrics().stringWidth(text);
            g2.drawString(text, centerX - textWidth / 2, y);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TLSHandshakeGUI gui = new TLSHandshakeGUI();
            gui.setVisible(true);
        });
    }
}
