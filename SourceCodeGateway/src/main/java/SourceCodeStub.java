import java.util.ArrayList;
import java.util.List;

public class SourceCodeStub implements SourceCodeGateway {

    @Override
    public void connect(String url, String user, String password){

    }

    @Override
    public void disconnect() {

    }

    @Override
    public List<String> selectApplicationsWhereComponent(String component){
        List<String> result = new ArrayList<String>();
        result.add("CP4Basic");
        result.add("Waypoint");
        result.add("_kucmat1");

        return result;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        disconnect();
    }
}
