package com.billyewing.mschat.network;

import com.billyewing.mschat.MSChat;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class NetworkManager
{
    private MSChat _server;
    private ServerSocket _socket;
    
    public NetworkManager(MSChat msc) throws IOException
    {
        _server = msc;
        _socket = new ServerSocket(Integer.parseInt(_server._config.getProperty("server-port")), 1000, InetAddress.getByName(_server._config.getProperty("server-address")));
    }
    
    public MSChat getServer()
    {
        return _server;
    }

    public Socket newConnection() throws IOException
    {
        return _socket.accept();
    }
}
