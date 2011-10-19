package com.billyewing.mschat.client;

import com.billyewing.mschat.MSChat;
import com.billyewing.mschat.network.NetworkManager;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.logging.Logger;

public class ClientManager implements Runnable
{
    private static final Logger log = Logger.getLogger("Minecraft");
    private NetworkManager _netMan;
    private MSChat _server;
    private HashMap<String, Server> _servers;
    
    public ClientManager(MSChat msc, NetworkManager nm)
    {
        _server = msc;
        _netMan = nm;
        _servers = new HashMap<String, Server>();
    }
    
    public MSChat getServer()
    {
        return _server;
    }
    
    public NetworkManager getNetworkManager()
    {
        return _netMan;
    }

    @Override
    public void run()
    {
        try
        {
            Socket c = null;
            while((c = _netMan.newConnection()) != null)
            {
                Server s = new Server(_server, "Server" + ((int)(Math.random() * 9999)), c);
                _servers.put(s.getName(), s);
                s.setThread(new Thread(s));
                s.getThread().start();
            }
        } catch(Exception e)
        {
            log.severe(e.getLocalizedMessage());
            e.printStackTrace();
        }
    }

    public ArrayList<Server> getClients()
    {
        ArrayList<Server> servers = new ArrayList<Server>();
        
        for(Entry<String, Server> s : _servers.entrySet())
        {
            servers.add(s.getValue());
        }
        
        return servers;
    }

    public void renameServer(String _name, String serverName)
    {
        if(_servers.containsKey(_name))
        {
            Server s = _servers.get(_name);
            _servers.remove(s.getName());
            _servers.put(serverName, s);
        }
    }

    public void disconnect(Server _server)
    {
        if(_servers.containsKey(_server.getName()))
        {
            _servers.remove(_server.getName());
        }
    }
}
