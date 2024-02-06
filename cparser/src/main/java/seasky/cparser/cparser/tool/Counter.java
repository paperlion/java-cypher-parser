package seasky.cparser.cparser.tool;

public class Counter {
    
    int count = 0;
    
    public Counter() {
        count = 0;
    }
    
    public Counter inc() {
        count ++;
        return this;
    }
    
    public Counter inc(int n) {
        count += n;
        return this;
    }
    
    public int get() {
        return count;
    }

    public static Counter build() {
        return new Counter();
    }
}
