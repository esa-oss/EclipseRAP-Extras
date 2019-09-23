/*********************************************************************
 * Copyright (c) 2019 European Space Agency
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *     European Space Agency - initial API and implementation
 **********************************************************************/

package org.eclipse.jface.preference;

import org.eclipse.swt.widgets.Composite;

/**
 * A field editor for a directory path type preference. A standard directory
 * dialog appears when the user presses the change button.
 * <p>
 * Fake implementation for RAP where users are not intended to navigate server
 * directories.
 */
public class DirectoryFieldEditor extends StringFieldEditor
{

    /** The serialVersionUID of this DirectoryFieldEditor.java */
    private static final long serialVersionUID = -5559860376408812674L;


    /**
     * Creates a new directory field editor
     */
    protected DirectoryFieldEditor()
    {
        //Empty constructor
    }

    /**
     * Sets the text for the change button.
     * 
     * @param text
     *            the text to set in the change button
     */
    public void setChangeButtonText(String text)
    {
        // do nothing
    }

    /**
     * Opens the directory chooser dialog and returns the selected directory.
     * Method declared on StringButtonFieldEditor
     * <p>
     * Method included for compatibility RCP/RAP. In RAP the implementation it
     * is empty as users are not intended to navigate server directories.
     * 
     * @return
     */
    protected String changePressed()
    {
        return null;
    }

    /** {@inheritDoc} */
    @Override
    protected void createControl(Composite parent)
    {
        super.createControl(parent);
        // Make text field not editable in RAP
        getTextControl().setEditable(false);
    }

}
