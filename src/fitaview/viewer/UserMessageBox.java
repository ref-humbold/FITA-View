package fitaview.viewer;

import javax.swing.JOptionPane;

public final class UserMessageBox
{
    public static void showInfo(String title, String info)
    {
        JOptionPane.showMessageDialog(null, info, title, JOptionPane.INFORMATION_MESSAGE);
    }

    public static void showWarning(String title, String info)
    {
        JOptionPane.showMessageDialog(null, info, title, JOptionPane.WARNING_MESSAGE);
    }

    public static void showException(Exception exception)
    {
        JOptionPane.showMessageDialog(null, String.format("Error: %s", exception.getMessage()),
                                      exception.getClass().getSimpleName(),
                                      JOptionPane.ERROR_MESSAGE);
    }
}
