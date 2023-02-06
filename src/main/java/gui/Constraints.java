package gui;

import java.awt.*;

/**
 * Custom constraints class used in the GUI panels.
 */
class Constraints extends GridBagConstraints {

    /**
     * Constructor for Constraints class.
     *
     * @param aligned boolean indicating whether to align vertically the elements.
     */
    Constraints(boolean aligned){
        super();

        gridwidth = GridBagConstraints.REMAINDER;
        insets = GameGUI.INSETS;

        if (aligned) fill = GridBagConstraints.HORIZONTAL;
    }
}
