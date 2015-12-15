package net.ilexiconn.hipster.item.grade;

public class ItemGrade {
    public final String subject;
    public final String lastGrade;
    public final String averageGrade;

    public ItemGrade(String subject, String lastGrade, String averageGrade) {
        this.subject = subject;
        this.lastGrade = lastGrade;
        this.averageGrade = averageGrade;
    }
}
