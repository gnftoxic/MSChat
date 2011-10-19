package com.billyewing.mschat;

import com.billyewing.mschat.client.ClientManager;
import com.billyewing.mschat.logging.LogFormatter;
import com.billyewing.mschat.logging.LogHandle;
import com.billyewing.mschat.network.NetworkManager;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;

public class MSChat
{
    public static void main(String[] args) throws IOException
    {
        new MSChat();
    }
    
    public static final Logger _log = Logger.getLogger("Minecraft");
    public NetworkManager _netMan;
    public ClientManager _clientMan;
    public Properties _config;
    
    public MSChat() throws IOException
    {
        config();
        
        LogHandle lh = new LogHandle("server.log");
        lh.setFormatter(new LogFormatter());
        _log.addHandler(lh);
        _log.setUseParentHandlers(false);
        
        _log.info("Chopping some wood...");
        _netMan = new NetworkManager(this);
        
        _log.info("Building our base...");
        _clientMan = new ClientManager(this, _netMan);
        
        _log.info("Crafting our tools...");
        new Thread(_clientMan).start();
    }
    
    public void config() throws IOException
    {
        _config = new Properties();
        File f = new File("server.properties");
        if(!f.exists())
        {
            f.createNewFile();
            _config.load(new FileReader(f));
            
            _config.setProperty("server-address", "0.0.0.0");
            _config.setProperty("server-port", "60000");
            _config.setProperty("authkey", "changeme");
            saveConfig();
        }
        
        _config.load(new FileReader(f));
    }
    
    public void saveConfig() throws IOException
    {
        _config.store(new FileWriter(new File("server.properties")), "Cross-Server Chat Service");
    }
}