package com.billyewing.mschat.network.packet;

import com.billyewing.mschat.client.Server;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.logging.Logger;

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
        //    String    - Authentication Token (optional)
        
        String serverName = _input.readUTF();
        String key = _server.getServer()._config.getProperty("authkey");
        
        if(!key.equals("changeme"))
        {
            String authKey = _input.readUTF();
            if(!authKey.equals(key))
                _server.killSelf();
        }
        
        String previousName = _server.getName();
        
        for(Server s : _server.getServer()._clientMan.getClients())
        {
            if(serverName.equalsIgnoreCase(s.getName()))
                _server.killSelf();
        }
        
        _server._isAuthed = true;
        _server.setName(serverName);
        
        _server._output.writeInt(0x07);
        _server._output.flush();
        
        Logger.getLogger("Minecraft").info(serverName + " has linked.");
        
        for(Server s : _server.getServer()._clientMan.getClients())
        {
            if(s.equals(_server))
                continue;
            
            DataOutputStream o = s._output;
            
            o.write(0x10);
            o.writeUTF(_server.getName());
            o.flush();
        }
    }
}
