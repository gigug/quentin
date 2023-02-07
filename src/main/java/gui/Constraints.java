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
        insets = new Insets(5, 0, 5, 0);

        if (aligned) fill = GridBagConstraints.HORIZONTAL;
    }
}
