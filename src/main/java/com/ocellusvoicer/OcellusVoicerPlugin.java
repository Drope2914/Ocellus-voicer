package com.ocellusvoicer;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Actor;
import net.runelite.api.Client;
import net.runelite.api.NPC;
import net.runelite.api.events.OverheadTextChanged;
import net.runelite.client.audio.AudioPlayer;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.util.Text;

@Slf4j
@PluginDescriptor(
        name = "Ocellus Voicer",
        description = "Plays custom voice lines for Ocellus in The Fractured Archive. PayPal Donations: Alexanderdrope1@gmail.com",
        tags = {"ocellus", "raids 4", "tfa", "voice", "boss"}
)
public class OcellusVoicerPlugin extends Plugin
{
    private static final String PAYPAL_EMAIL = "Alexanderdrope1@gmail.com";

    @Inject
    private Client client;

    @Inject
    private AudioPlayer audioPlayer;

    @Inject
    private OcellusVoicerConfig config;

    @Inject
    private ConfigManager configManager;

    private int lastPlayedTick = -1;

    @Override
    protected void startUp()
    {
        configManager.setConfiguration("ocellusvoicer", "paypalEmail", PAYPAL_EMAIL);
        log.info("Ocellus Voicer started.");
    }

    @Subscribe
    public void onConfigChanged(ConfigChanged event)
    {
        if (event.getGroup().equals("ocellusvoicer") && event.getKey().equals("paypalEmail"))
        {
            if (!PAYPAL_EMAIL.equals(event.getNewValue()))
            {
                configManager.setConfiguration("ocellusvoicer", "paypalEmail", PAYPAL_EMAIL);
            }
        }
    }

    @Subscribe
    public void onOverheadTextChanged(OverheadTextChanged event)
    {
        Actor actor = event.getActor();

        if (!(actor instanceof NPC) || actor.getName() == null)
        {
            return;
        }

        String npcName = Text.removeTags(actor.getName()).trim();
        if (npcName.equalsIgnoreCase("Ocellus"))
        {
            checkAndPlay(event.getOverheadText());
        }
    }

    private void checkAndPlay(String rawText)
    {
        if (rawText == null)
        {
            return;
        }

        String cleanText = Text.removeTags(rawText).trim().toLowerCase();
        String soundFile = null;

        if (cleanText.contains("annihilation")) soundFile = "annihilation.wav";
        else if (cleanText.contains("death")) soundFile = "death.wav";
        else if (cleanText.contains("void")) soundFile = "void.wav";
        else if (cleanText.contains("insolence")) soundFile = "insolence.wav";
        else if (cleanText.contains("untether")) soundFile = "untether.wav";
        else if (cleanText.contains("obliteration")) soundFile = "obliteration.wav";
        else if (cleanText.contains("run")) soundFile = "run.wav";
        else if (cleanText.contains("end")) soundFile = "end.wav";

        if (soundFile != null && client.getTickCount() != lastPlayedTick)
        {
            lastPlayedTick = client.getTickCount();
            playVoiceLine(soundFile);
        }
    }

    private void playVoiceLine(String fileName)
    {
        if (config.volume() == 0)
        {
            return;
        }

        try
        {
            float gain = 20f * (float) Math.log10(config.volume() / 100f);
            audioPlayer.play(OcellusVoicerPlugin.class, fileName, gain);
            log.info("Playing Ocellus voice line: {}", fileName);
        }
        catch (Exception e)
        {
            log.error("Error playing audio file: {}", fileName, e);
        }
    }

    @Provides
    OcellusVoicerConfig provideConfig(ConfigManager configManager)
    {
        return configManager.getConfig(OcellusVoicerConfig.class);
    }
}