package lyr.testbot.enums;

import discord4j.core.object.util.Permission;
import discord4j.core.object.util.PermissionSet;

public enum CommandType {
    GENERAL(Permission.SEND_MESSAGES),
    ADMIN(Permission.ADMINISTRATOR),
    MODERATOR(Permission.ADMINISTRATOR,Permission.MANAGE_MESSAGES,Permission.MANAGE_CHANNELS,Permission.MANAGE_GUILD),
    OWNER(),
    CUSTOM();

    private PermissionSet perms;

    CommandType(Permission... perms){  // OR
        this.perms = PermissionSet.of(perms);
    }

    public CommandType edit(Permission... perms){
        this.perms = PermissionSet.of(perms);
        return this;
    }

    public PermissionSet get(){
        return perms;
    }

    public boolean isAllowed(PermissionSet userPerms){
        return (userPerms.getRawValue() & perms.getRawValue()) > 0L;
    }
}
