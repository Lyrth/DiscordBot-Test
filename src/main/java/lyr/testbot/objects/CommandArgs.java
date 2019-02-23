package lyr.testbot.objects;

public class CommandArgs {

    String raw;
    String[] split;

    CommandArgs(String raw){
        this.raw = raw;
        this.split = raw.split(" ");
    }

    public String getRaw() {
        return raw;
    }

    public String get(int index) {
        if (index < 0) throw new IllegalArgumentException("Position cannot be less than 0.");
        return (index < split.length) ? split[index] : "";
    }

    public boolean matchesAt(String regex, int index){
        return (index < split.length) && split[index].matches(regex);
    }

    public boolean equalsAt(String str, int index){
        return (index < split.length) && split[index].equals(str);
    }

    public boolean isEmpty(){
        return split.length == 1 && split[0].isEmpty();
    }

    public int getCount(){
        return split.length;
    }
}
