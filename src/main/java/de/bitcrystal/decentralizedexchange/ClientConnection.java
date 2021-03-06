/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.bitcrystal.decentralizedexchange;

import com.nitinsurana.bitcoinlitecoin.rpcconnector.RPCApp;
import de.bitcrystal.decentralizedexchange.security.BitCrystalKeyGenerator;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ABC
 */
public class ClientConnection implements Runnable {

    private static Map<String, String> traders = new ConcurrentHashMap<String, String>();
    private static Map<String, String> traderspw = new ConcurrentHashMap<String, String>();
    private TCPClient server;
    private String command;

    public ClientConnection(TCPClient server, String command) {
        this.server = server;
        this.command = command;
    }

    public void run() {
        try {
            RPCApp bitcrystalrpc = RPCApp.getAppOutRPCconf("bitcrystalrpc.conf");
            RPCApp bitcoinrpc = RPCApp.getAppOutRPCconf("bitcoinrpc.conf");
            if (!command.contains(" ")) {
                this.server.close();
                return;
            }
            String[] split = command.split(" ");
            if (split.length < 1) {
                this.server.close();
                return;
            }
            switch (split.length) {
                case 2: {
                    if (split[0].equalsIgnoreCase("sniffersex")) {
                        int count = 1;
                        try {
                            count = Integer.parseInt(split[1]);
                            if (count <= 0) {
                                count = 1;
                            }
                        } catch (Throwable ex) {
                            count = 1;
                        }
                        String recv = "";
                        for (int i = 0; i < count; i++) {
                            this.server.send("0");
                            recv = this.server.recv();
                            if (recv.equals("SNIFFER_IS_FUCKED")) {
                                BitCrystalKeyGenerator.generateNewHash();
                                BitCrystalKeyGenerator.generateNewKey();
                                BitCrystalKeyGenerator.getByteSizeKey();
                                System.out.println("Fuck the sniffer!");
                            }
                        }
                        this.server.close();
                    }
                }
                break;
            }
        } catch (Exception ex) {
            Logger.getLogger(ClientConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
