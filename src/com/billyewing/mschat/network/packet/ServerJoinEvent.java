package com.billyewing.mschat.network.packet;

import com.billyewing.mschat.client.Server;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ServerJoinEvent implements Event
{
    private DataInputStream _input;
    private Server _server;
    
    public ServerJoinEvent(Server s, DataInputStream dis)
    {
        _server = s;
        _input = dis;
    }
    
    public void execute() throws IOException
    {
        // Packet Data:
        //    byte      - Packet ID
        //    String    - Server Name
        
        String serverName = _input.readUTF();
        String previousName = _server.getName();
        
        _server.setName(serverName);
        
        for(Server s : _server.getServer()._clientMan.getClients())
        {
            if(s.getName().equals(_server.getName()))
                continue;
            
            DataOutputStream o = s._output;
            
            o.write(0x10);
            o.writeUTF(_server.getName());
            o.flush();
        }
    }
}
