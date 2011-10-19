package com.billyewing.mschat.client;

import com.billyewing.mschat.MSChat;
import com.billyewing.mschat.network.packet.PlayerChatEvent;
import com.billyewing.mschat.network.packet.PlayerJoinEvent;
import com.billyewing.mschat.network.packet.PlayerQuitEvent;
import com.billyewing.mschat.network.packet.ServerJoinEvent;
import com.billyewing.mschat.network.packet.ServerQuitEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.logging.Logger;

public class Server implements Runnable
{
    private static final Logger log = Logger.getLogger("Minecraft");
    public String _name;
    public Socket _socket;
    private MSChat _server;
    public InetAddress _address;
    public DataInputStream _input;
    public DataOutputStream _output;
    public Thread _thread;
    public boolean _isKilling = false;
    
    public Server(MSChat s, String n, Socket ss) throws IOException
    {
        _server = s;
        _name = n;
        _socket = ss;
        _address = _socket.getInetAddress();
        
        _input = new DataInputStream(_socket.getInputStream());
        _output = new DataOutputStream(_socket.getOutputStream());
    }
    
    public void setThread(Thread t)
    {
        _thread = t;
    }
    
    public Thread getThread()
    {
        return _thread;
    }
    
    public MSChat getServer()
    {
        return _server;
    }
    
    public String getName()
    {
        return _name;
    }
    
    public void killSelf() throws IOException
    {
        if(_isKilling)
            return;
        _isKilling = true;
        log.info("Server " + getName() + " (" + _address.toString().substring(1) + ") disconnected");
        new ServerQuitEvent(this, _input).execute();
        try
        {
            _input.close();
            _output.close();
            _socket.close();
            _thread.interrupt();
        } catch(Exception e)
        {
            if(!_thread.isAlive())
                log.info("Closed thread successfully");
            else
                log.severe("Could not close thread!");
        }
    }

    public void setName(String serverName)
    {
        _server._clientMan.renameServer(_name, serverName);
        
        _name = serverName;
    }
    
    @Override
    public void run()
    {
        try
        {
            int packetId = 0x00;
            
            packetId = _input.read();
            
            if(packetId != 0x05)
            {
                killSelf();
            } else {
                new ServerJoinEvent(this, _input).execute();
            }
            
            while((packetId = _input.read()) != 0x00)
            {
                switch(packetId)
                {
                    case 0x05: // ServerIndetificationEvent
                        // Do Nothing - should not receive packet beyond first authentication.
                        break;
                    case 0x10: // ServerJoinEvent
                        // Do Nothing - should not receive packet, only send.
                        break;
                    case 0x11: // ServerQuitEvent
                        // Do Nothing - should not receive packet, only send.
                        break;
                    case 0x12: // ServerChatEvent
                        new ServerChatEvent(this, _input).execute();
                        break;
                    case 0x20: // PlayerJoinEvent
                        new PlayerJoinEvent(this, _input).execute();
                        break;
                    case 0x21: // PlayerQuitEvent
                        new PlayerQuitEvent(this, _input).execute();
                        break;
                    case 0x22: // PlayerChatEvent
                        new PlayerChatEvent(this, _input).execute();
                        break;
                    case 0x23: // PlayerActionEvent
                        new PlayerActionEvent(this, _input).execute();
                        break;
                }
            }
        } catch(Exception e)
        {
            log.severe(e.getMessage());
            e.printStackTrace();
            try
            {
                killSelf();
            } catch (IOException ex)
            {
                log.severe(ex.getLocalizedMessage());
            }
        }
    }
}
