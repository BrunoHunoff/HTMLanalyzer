public class HtmlContent {
    private String content;
    private int lvl;

    public String getContent() {
        return content;
    }

    public int getLvl() {
        return lvl;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setLvl(int lvl) {
        this.lvl = lvl;
    }

    public HtmlContent() {
    }

    public HtmlContent(String content, int lvl) {
        this.content = content;
        this.lvl = lvl;
    }

    public void showContent() {
        System.out.println(content);
        System.out.println(lvl);
    }

}
