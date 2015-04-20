import org.apache.commons.cli.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;

class FetchSourceCodeApp {

    public static void main(String[] args) {
        OpenRoadGateway gateway = new OpenRoadStub();

        try {
            CommandLine cmdline = parseCommandLine(args);
            if (cmdline.hasOption("h")) {
                printHelp();
                return;
            }

            String url = cmdline.getOptionValue('u');
            String login = cmdline.getOptionValue('l');
            String password = cmdline.getOptionValue('p');
            String workspace = cmdline.getOptionValue('w');
            String version = cmdline.getOptionValue('v');
            String application = cmdline.getOptionValue('a');

            String filePath = generateFilePath(workspace, version, application);
            createAllDirectoriesInPath(filePath);

            gateway.connect(url, login, password);
            List<OpenRoadComponent> components = gateway.selectComponentsWhere(application);

            for (OpenRoadComponent component : components) {
                String fullFilePath = filePath + File.separator + component.name + ".or";
                writeToFile(component, fullFilePath);
            }
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

    private static void writeToFile(OpenRoadComponent component, String fullFilePath) throws FileNotFoundException, UnsupportedEncodingException {
        PrintWriter writer = new PrintWriter(fullFilePath, "UTF-8");
        writer.print(component.sourceCode);
        writer.close();
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

    private static void printHelp() {
        HelpFormatter helpFormatter = new HelpFormatter();
        helpFormatter.printHelp(FetchSourceCodeApp.class.getSimpleName(), getCmdLineOptions());
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
        Option password = OptionBuilder.withArgName("password").hasArg().isRequired(true).withDescription("password used to connect to database").create("p");
        Option workspace = OptionBuilder.withArgName("path").hasArg().isRequired(true).withDescription("path to workspace directory").create("w");
        Option version = OptionBuilder.withArgName("lido version").hasArg().isRequired(true).withDescription("eg. DEV000").create("v");
        Option application = OptionBuilder.withArgName("application").hasArg().isRequired(true).withDescription("name of OpenRoad application to be checked out").create("a");

        Options cmdOptions = new Options();
        cmdOptions.addOption(help);
        cmdOptions.addOption(url);
        cmdOptions.addOption(login);
        cmdOptions.addOption(password);
        cmdOptions.addOption(workspace);
        cmdOptions.addOption(version);
        cmdOptions.addOption(application);

        return cmdOptions;
    }
}