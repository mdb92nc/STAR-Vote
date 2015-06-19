package sexpression;

/**
 * Created by Matthew Kindy II on 6/10/2015.
 */
public class KeyValuePair<K,V> {

    K key;
    V value;

    public KeyValuePair(){
        this(null, null);
    }

    public KeyValuePair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public void setValue(V value) {
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }

    public String toString(){
       return "Key: " + key + ", Value: " + value;
    }
}
