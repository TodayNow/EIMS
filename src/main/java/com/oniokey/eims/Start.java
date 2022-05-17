package com.oniokey.eims;

import com.formdev.flatlaf.FlatIntelliJLaf;

import javax.swing.*;
import java.awt.*;

/**
 * @author iokey
 */
public class Start
{
    public static void main(String[] args)
    {
        FlatIntelliJLaf.setup();
        UIManager.put("Button.arc",15);
        UIManager.put("Component.arc",15);
        UIManager.put("TextComponent.arc",15);
        UIManager.put("ScrollBar.thumbArc",999);
        UIManager.put("ScrollBar.thumbInsets",new Insets(2,2,2,2));
        UIManager.put("Panel.border.arc",15);
        new LoginUI();
    }
}
