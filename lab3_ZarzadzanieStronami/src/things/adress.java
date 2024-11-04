package things;

public class adress {
    int page;
    int distance;
    boolean done;

    public adress(int page, int distance) {
        this.page = page;
        this.distance = distance;
        done = false;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }
}
