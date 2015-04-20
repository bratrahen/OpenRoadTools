import org.apache.commons.cli.*;
import org.eclipse.swt.widgets.Display;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

class PrettyHardLinkApp {

    public static void main(String[] args) {
        OpenRoadGateway gateway = new OpenRoadStub();

        try {
            CommandLine cmdline = parseCommandLine(args);
            if (cmdline.hasOption('h')) {
                printHelp();
                return;
            }

            String url = cmdline.getOptionValue('u');
            String login = cmdline.getOptionValue('l');
            String password = cmdline.getOptionValue('p');
            String workspace = cmdline.getOptionValue('w');
            String version = cmdline.getOptionValue('v');
            String component = cmdline.getOptionValue('c');
            Path openRoadFile = Paths.get(cmdline.getOptionValue('f'));

            if (Files.notExists(openRoadFile))
                throw new RuntimeException("File <" + openRoadFile + "> does not exist");

            gateway.connect(url, login, password);
            List<String> applications = gateway.selectApplicationsWhereComponent(component);

            String application = "unknown";
            if (applications.size() == 1)
                application = applications.get(0);
            else if (applications.size() > 1)
                application = chooseApplicationFrom(applications);

            String filePath = generateFilePath(workspace, version, application);
            String fullFilePath = filePath + File.separator + component + ".or";

            createAllDirectoriesInPath(filePath);
            Files.createLink(Paths.get(fullFilePath), openRoadFile);
        } catch (ParseException e) {
            System.err.println("Parsing parameters failed.  Reason: " + e.getMessage());
            printHelp();
        } catch (RuntimeException e) {
            System.err.println(e.getMessage());
        } catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        } finally {
            gateway.disconnect();
        }
    }

    private static void createAllDirectoriesInPath(String path) {
        boolean isPathCreated = new File(path).mkdirs();
        if (!isPathCreated) {
            throw new RuntimeException("Failed to create all directories in path:" + path);
        }
    }

    private static String generateFilePath(String workspace, String version, String application) {
        return workspace + File.separator + version + File.separator + application;
    }

    private static String chooseApplicationFrom(List<String> applications) {
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

    private static void printHelp() {
        HelpFormatter helpFormatter = new HelpFormatter();
        helpFormatter.printHelp(PrettyHardLinkApp.class.getSimpleName(), getCmdLineOptions());
    }


    private static CommandLine parseCommandLine(String[] args) throws ParseException {
        CommandLineParser parser = new BasicParser();
        return parser.parse(getCmdLineOptions(), args);
    }

    @SuppressWarnings("AccessStaticViaInstance")
    private static Options getCmdLineOptions() {
        Option help = new Option("h", "print this message");
        Option url = OptionBuilder.withArgName("url").hasArg().isRequired(true).withDescription("sourcecode database url").create("u");
        Option login = OptionBuilder.withArgName("login").hasArg().isRequired(true).withDescription("login used to connect to database").create("l");
        Option password = OptionBuilder.withArgName("password").isRequired(true).hasArg().withDescription("password used to connect to database").create("p");
        Option workspace = OptionBuilder.withArgName("path").hasArg().isRequired(true).withDescription("path to workspace directory").create("w");
        Option version = OptionBuilder.withArgName("lido version").hasArg().isRequired(true).withDescription("eg. DEV000").create("v");
        Option component = OptionBuilder.withArgName("component").hasArg().isRequired(true).withDescription("component for which we search parent application").create('c');
        Option file = OptionBuilder.withArgName("path").hasArg().isRequired(true).withDescription("full file path to temporary file created by OpenRoad Workbench").create('f');

        Options cmdOptions = new Options();
        cmdOptions.addOption(help);
        cmdOptions.addOption(url);
        cmdOptions.addOption(login);
        cmdOptions.addOption(password);
        cmdOptions.addOption(workspace);
        cmdOptions.addOption(version);
        cmdOptions.addOption(component);
        cmdOptions.addOption(file);

        return cmdOptions;
    }
}
