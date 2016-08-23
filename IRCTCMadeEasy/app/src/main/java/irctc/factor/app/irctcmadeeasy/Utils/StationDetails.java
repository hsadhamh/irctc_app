package irctc.factor.app.irctcmadeeasy.Utils;

/**
 * Created by hassanhussain on 8/23/2016.
 */
public class StationDetails implements Comparable<StationDetails>{
    public String StationCode, StationName, StationFullName;

    @Override
    public int compareTo(StationDetails another) {
        return this.StationCode.compareToIgnoreCase(another.StationName.toLowerCase());
    }

    @Override
    public String toString(){ return StationFullName; }
}
