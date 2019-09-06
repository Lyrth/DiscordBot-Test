package lyr.testbot.modules.guild;

import lyr.testbot.annotations.ModuleInfo;
import lyr.testbot.templates.GuildModule;

@ModuleInfo(
    desc = "Test for settings.",
    commands = {lyr.testbot.commands.module.settingtest.SettingTest.class}
)
public class SettingTest extends GuildModule {

}
