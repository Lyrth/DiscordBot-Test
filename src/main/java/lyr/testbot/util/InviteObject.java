package lyr.testbot.util;

import java.util.Collection;

public final class InviteObject {

    public int approximate_presence_count;
    public int approximate_member_count;
    public String code;    // Invite code or 10006
    public String message; // Unknown Invite or null
    public InviteGuildObject guild;
    public InviteChannelObject channel;

    public static class InviteGuildObject {
        public int verification_level;
        public Collection<String> features; // ["VIP_REGIONS", "VANITY_URL", "INVITE_SPLASH"] etc
        public String name;
        public String id;
        public String splash,icon;
    }

    public static class InviteChannelObject {
        public int type;
        public String name;
        public String id;
    }
}
