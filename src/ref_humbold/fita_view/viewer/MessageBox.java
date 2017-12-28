package ref_humbold.fita_view.viewer;

import javax.swing.JOptionPane;

public class MessageBox
{
    public static void showInfoBox(String title, String info)
    {
        JOptionPane.showMessageDialog(null, info, title, JOptionPane.INFORMATION_MESSAGE);
    }

    public static void showExceptionBox(Exception e)
    {
        JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(),
                                      e.getClass().getSimpleName(), JOptionPane.ERROR_MESSAGE);
    }
}
