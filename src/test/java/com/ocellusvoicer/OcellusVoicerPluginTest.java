package com.ocellusvoicer;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class OcellusVoicerPluginTest
{
    public static void main(String[] args) throws Exception
    {
        ExternalPluginManager.loadBuiltin(OcellusVoicerPlugin.class);
        RuneLite.main(args);
    }
}
