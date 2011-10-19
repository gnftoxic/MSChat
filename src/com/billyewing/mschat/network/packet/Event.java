package com.billyewing.mschat.network.packet;

import java.io.IOException;

public interface Event
{
    public void execute() throws IOException;
}
