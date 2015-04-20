import java.util.ArrayList;
import java.util.List;

public class OpenRoadStub implements OpenRoadGateway {

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
    public List<OpenRoadComponent> selectComponentsWhere(String application) {
        List<OpenRoadComponent> result = new ArrayList<OpenRoadComponent>();
        result.add(new OpenRoadComponent("ots", "#include ots.h"));
        result.add(new OpenRoadComponent("aircraft", "#include aircraft.h"));
        result.add(new OpenRoadComponent("airport", "#include airport.h"));
        result.add(new OpenRoadComponent("UserClasses", "#include UserClasses.h"));

        return result;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        disconnect();
    }
}
