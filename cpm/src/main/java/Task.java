public class Task {
    private String name;
    private int importance;
    private boolean isStart = false;
    private boolean isEnd = false;

    Task(String name, int importance) {
        this.name = name;
        this.importance = importance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImportance() {
        return importance;
    }

    public void setImportance(int importance) {
        this.importance = importance;
    }

    public boolean isStart() {
        return isStart;
    }

    public void setStart(boolean start) {
        isStart = start;
    }

    public boolean isEnd() {
        return isEnd;
    }

    public void setEnd(boolean end) {
        isEnd = end;
    }

    @Override
    public String toString() {
        String str = this.name + "(" + this.importance + ")";
//        if(this.isEnd) {
//            str = str.concat(" END");
//        }
//        if(this.isStart) {
//            str = str.concat(" START");
//        }
        return str;
    }
}
