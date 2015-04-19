import org.eclipse.swt.widgets.Display;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class ApplicationOfComponentApp {

    public static void main(String[] args) {
        if (args.length < 4)
            return;

        Path tmpFile = Paths.get(args[0]);
        String root = args[1];
        String version = args[2];
        String component = args[3];

        try {
            if (Files.notExists(tmpFile))
                return;

            SourceCodeGateway sourceCode = new SourceCodeStub();
            sourceCode.connect("url", "user", "password");
            List<String> applications = sourceCode.selectApplicationsWhereComponent(component);

            String application = "unknown";
            if (applications.size() == 1)
                application = applications.get(0);
            else if (applications.size() > 1)
                application = chooseApplicationFrom(applications);

            String filePath = generateFilePath(root, version, application);
            String fullFilePath = filePath + File.separator + component + ".or";

            createAllDirectoriesInPath(filePath);
            Files.createLink(Paths.get(fullFilePath), tmpFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void createAllDirectoriesInPath(String path) {
        boolean isPathCreated = new File(path).mkdirs();
        if (!isPathCreated) {
            throw new RuntimeException("Failed to create all directories in path:" + path);
        }
    }

    private static String generateFilePath(String root, String version, String application) {
        return root + File.separator + version + File.separator + application;
    }

//    private static String chooseApplicationFrom(List<String> applications) throws IOException {
//        System.out.println("Choose application:");
//
//        for (int i = 0; i < applications.size(); i++){
//            System.out.println("[" + i + "]: " + applications.get(i));
//        }
//
//        int chosenOption = scanIntegerInRange(0, applications.size()-1);
//        return applications.get(chosenOption);
//    }
//
//    private static int scanIntegerInRange(int min, int max){
//        Scanner scanner = new Scanner(System.in);
//        boolean tryAgain;
//        int result = 0;
//
//        do{
//            System.out.print("> ");
//            if (scanner.hasNextInt()){
//                result = scanner.nextInt();
//                tryAgain = result < min || result > max;
//            }else{
//                scanner.nextLine();
//                tryAgain = true;
//            }
//        }while(tryAgain);
//
//        return result;
//    }

    private static String chooseApplicationFrom(List<String> applications) throws IOException {
        Display display = new Display();
        ChooseApplicationWindow window = new ChooseApplicationWindow(display, applications);
        window.open();

        while (!window.isDisposed()) {
            if (!display.readAndDispatch())
                display.sleep();
        }
        display.dispose();

        return window.getChosenApplication();
    }
}
