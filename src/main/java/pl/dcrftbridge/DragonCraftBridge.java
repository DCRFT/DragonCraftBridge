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
            getLogger().info("Yay! It loads!");
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
            // Alright quick note here guys : ProxiedPlayer.sendData() [b]WILL NOT WORK[/b]. It will send it to the client, and not to the server the client is connected. See the difference ? You need to send to Server or ServerInfo.
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
                    this.getLogger().warning("yess" + utf);
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
