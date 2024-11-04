package theUglySideOfFile;

import finallySomethingUsefull.LRU;

import java.util.ArrayList;

public class MyProcess {
    private ArrayList<Adress> requests;
    private int pageNumber;
    private int pageErrors;
    private int asksDone = 0;
    ArrayList<Integer> pages = new ArrayList<>();

    public MyProcess(int numberOfPages){
        pageNumber = numberOfPages;
        pageErrors = 0;
    }

    public void generateRequests(int numberOfRequests, int MEMORY_SIZE){
        requests = new Generating().generateAsksList(numberOfRequests, pageNumber, MEMORY_SIZE);
    }

    public void addRequest(Adress request){
        requests.add(request);
    }

    public int getNumberOfRequests(){
        return requests.size();
    }

    public Adress getRequestAt(int index) {
        return requests.get(index);
    }

    public void increasePageErrors() {
        pageErrors++;
    }

    public int getPageErrors() {
        return pageErrors;
    }

    public int getNumber() {
        return pageNumber;
    }

    public int getAsksDone(){
        return asksDone;
    }

    public void addAsksDone(int a){
        asksDone += a;
    }

    public boolean ifDone(){
        if (asksDone >= requests.size()) return true;
        else return false;
    }
    public ArrayList<Adress> getRequests() {
        return requests;
    }

    public MyProcess cloneProcess() {
        MyProcess clone = new MyProcess(pageNumber);
        clone.pageErrors = pageErrors;
        clone.requests.addAll(requests);
        return clone;
    }

    public int doProcessStrefowoPart(MyProcess process, int FRAMES_NUMBER, ArrayList<Integer> whichPage, int processName){
        LRU lru = new LRU();
        while (FRAMES_NUMBER <= pages.size())
            pages.remove(pages.size()-1);
        if (asksDone < requests.size()) {
            int pageName = getRequestAt(asksDone).getPage();
            if (!pages.contains(pageName)) {
                lru.doLRU(asksDone, pages, pageName, FRAMES_NUMBER, process);
                pageErrors++;
            }
        }
        asksDone++;
        return pageErrors;
    }
}