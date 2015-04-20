import java.util.List;

@SuppressWarnings("ALL")
interface OpenRoadGateway {
    void connect(String url, String user, String password);

    void disconnect();

    List<String> selectApplicationsWhereComponent(String component);

    List<OpenRoadComponent> selectComponentsWhere(String application);
}
