package com.ocellusvoicer;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;
import net.runelite.client.config.Range;

@ConfigGroup("ocellusvoicer")
public interface OcellusVoicerConfig extends Config
{
    @Range(
            min = 0,
            max = 100
    )
    @ConfigItem(
            keyName = "volume",
            name = "Voice Volume",
            description = "Adjust the volume of Ocellus voice lines (0 to mute)",
            position = 1
    )
    default int volume()
    {
        return 80;
    }

    @ConfigSection(
            name = "Donation",
            description = "Support the development of this plugin",
            position = 2
    )
    String donationSection = "donationSection";

    @ConfigItem(
            keyName = "paypalEmail",
            name = "PayPal",
            description = "Select and copy this email (Ctrl+C) to donate via PayPal",
            section = donationSection,
            position = 3
    )
    default String paypalEmail()
    {
        return "Alexanderdrope1@gmail.com";
    }
}