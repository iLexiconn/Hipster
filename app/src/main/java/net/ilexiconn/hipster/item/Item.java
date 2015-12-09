package net.ilexiconn.hipster.item;

public class Item {
    public String string1;
    public String string2;
    public String string3;
    public String string4;
    public String special;

    public Item(String string1, String string2, String string3, String string4) {
        this.string1 = string1;
        this.string2 = string2;
        this.string3 = string3;
        this.string4 = string4;
        this.special = "";
    }

    public Item(String special) {
        this("", "", "", "");
        this.special = special;
    }
}
