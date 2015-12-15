package net.ilexiconn.hipster.item;

public class Item {
    public String string1;
    public String string2;
    public String string3;
    public String string4;
    public String special;
    public String color;

    public Item(String string1, String string2, String string3, String string4, String color) {
        this.string1 = string1;
        this.string2 = string2;
        this.string3 = string3;
        this.string4 = string4;
        this.special = "";
        this.color = color;
    }

    public Item(String string1, String string2, String string3, String string4) {
        this(string1, string2, string3, string4, "#86000000");
    }

    public Item(String special) {
        this("", "", "", "", "#86000000");
        this.special = special;
    }
}
