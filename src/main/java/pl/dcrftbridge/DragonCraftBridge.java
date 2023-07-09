package pl.dcrftbridge;

import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;

import java.io.*;

public class DragonCraftBridge extends Plugin implements Listener {
        public void onEnable() {
            getLogger().info("Loaded.");
            this.getProxy().registerChannel("BungeeCord");
            this.getProxy().getPluginManager().registerListener(this, this);
        }

        public void onDisable() {

        }

        public void sendMessage(String message, ServerInfo server) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(stream);
            try {
                out.writeUTF("DragonCraftBridge");
                out.writeUTF(message);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            server.sendData("BungeeCord", stream.toByteArray());
        }

        @EventHandler
        public void onPluginMessage(PluginMessageEvent ev) {
            if (!ev.getTag().equalsIgnoreCase("BungeeCord")) {
                return;
            }

            if (!(ev.getSender() instanceof Server)) {
                return;
            }

            ByteArrayInputStream stream = new ByteArrayInputStream(ev.getData());
            DataInputStream in = new DataInputStream(stream);

            try {
                if(in.readUTF().equalsIgnoreCase("DragonCraftBridge")){
                    String utf = in.readUTF();
                    if(((Server) ev.getSender()).getInfo().getName().equalsIgnoreCase("skyblock")) {
                        sendMessage(utf, getProxy().getServerInfo("survival"));
                    } else if(((Server) ev.getSender()).getInfo().getName().equalsIgnoreCase("survival")) {
                        sendMessage(utf, getProxy().getServerInfo("skyblock"));
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            } {

            }
        }

}
