package com.billyewing.mschat.network.packet;

import com.billyewing.mschat.client.Server;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ServerChatEvent implements Event
{
    private DataInputStream _input;
    private Server _server;
    
    public ServerChatEvent(Server s, DataInputStream dis)
    {
        _server = s;
        _input = dis;
    }
    
    public void execute() throws IOException
    {
        // Packet Data:
        //    byte      - Packet ID
        //    String    - Chat mesage
        
        String chatMsg = _input.readUTF();
        
        for(Server s : _server.getServer()._clientMan.getClients())
        {
            if(s.equals(_server))
                continue;
            
            DataOutputStream o = s._output;
            
            o.write(0x12);
            o.writeUTF(chatMsg);
            o.flush();
        }
    }
}
