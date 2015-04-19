import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;

import java.util.Collection;

public class ChooseApplicationWindow {

    private Shell shell;
    private List list;
    private String result = "unknown";

    ChooseApplicationWindow(Display display, Collection<String> applications) {
        shell = new Shell(display);
        if (applications.size() == 0)
            shell.dispose();

        initializeLayout();
        createListOf(applications);
    }

    private void initializeLayout() {
        shell.setSize(250, 200);
        shell.setMinimumSize(250, 200);
        GridLayout layout = new GridLayout();
        layout.numColumns = 1;
        shell.setLayout(layout);
        shell.setText("Choose Application");
    }

    private void createListOf(Collection<String> applications) {
        list = new List(shell, SWT.BORDER | SWT.SINGLE | SWT.V_SCROLL);
        GridData gridData = new GridData(GridData.FILL, GridData.FILL, true, true);
        list.setLayoutData(gridData);

        list.addSelectionListener(onSelectApplicationName());

        for (String app : applications) {
            list.add(app);
        }
    }

    private SelectionListener onSelectApplicationName() {
        return new SelectionListener() {
            public void widgetSelected(SelectionEvent event) {
                result = list.getSelection()[0];
                shell.dispose();
            }

            public void widgetDefaultSelected(SelectionEvent event) {
                widgetSelected(event);
            }
        };
    }

    public String getChosenApplication() {
        return result;
    }

    public void open() {
        shell.open();
    }

    public boolean isDisposed() {
        return shell.isDisposed();
    }
}

